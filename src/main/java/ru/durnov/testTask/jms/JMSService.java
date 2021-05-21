package ru.durnov.testTask.jms;

import org.springframework.stereotype.Service;
import ru.durnov.testTask.requestbody.JsonMessage;

import javax.jms.JMSConnectionFactory;


@Service
public interface JMSService {
    void send(JsonMessage message);
}
