package ru.durnov.testTask.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import ru.durnov.testTask.requestbody.JsonMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Service
public class JMSArtemisService implements JMSService {
    private final JmsTemplate jmsTemplate;

    @Autowired
    public JMSArtemisService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void send(JsonMessage jsonMessage) {
        jmsTemplate.send(jsonMessage.getDestination(), new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message message = session.createObjectMessage(jsonMessage);
                message.setJMSMessageID(String.valueOf(jsonMessage.getId()));
                message.setStringProperty("body", jsonMessage.getBody());
                return message;
            }
        });
    }
}
