package ru.durnov.testTask.jms;

import org.springframework.stereotype.Service;


@Service
public interface JMSService {
    void send(JMSMessage message);
}
