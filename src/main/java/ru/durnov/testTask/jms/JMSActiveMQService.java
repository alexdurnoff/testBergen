package ru.durnov.testTask.jms;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JMSActiveMQService implements JMSService {
    private final JmsTemplate jmsTemplate;

    @Autowired
    public JMSActiveMQService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void send(JMSMessage JMSMessage) {
        jmsTemplate.convertAndSend(JMSMessage.getDestination(), JMSMessage);
    }
}
