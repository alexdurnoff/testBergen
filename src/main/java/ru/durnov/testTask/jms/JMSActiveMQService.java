package ru.durnov.testTask.jms;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.durnov.testTask.requestbody.JsonMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Service
public class JMSActiveMQService implements JMSService {
    private final JmsTemplate jmsTemplate;

    @Autowired
    public JMSActiveMQService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void send(JsonMessage jsonMessage) {
        jmsTemplate.convertAndSend(jsonMessage.getDestination(), jsonMessage);
    }
}
