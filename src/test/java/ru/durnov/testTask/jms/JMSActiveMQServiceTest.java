package ru.durnov.testTask.jms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class JMSActiveMQServiceTest {


    private final JMSService jmsService;

    @Autowired
    JMSActiveMQServiceTest(JMSService jmsService) {
        this.jmsService = jmsService;
    }

    @Test
    void send() {
        JMSMessage JMSMessage = new JMSMessage(4, "jms.message.mq", "Привет!");
        jmsService.send(JMSMessage);
    }
}