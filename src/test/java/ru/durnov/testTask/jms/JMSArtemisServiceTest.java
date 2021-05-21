package ru.durnov.testTask.jms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.durnov.testTask.requestbody.JsonMessage;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class JMSArtemisServiceTest {

    @Autowired
    JMSService jmsService;

    @Test
    void send() {
        JsonMessage jsonMessage = new JsonMessage(1, "localhost", "Hello");
        jmsService.send(jsonMessage);
    }
}