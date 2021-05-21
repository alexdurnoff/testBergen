package ru.durnov.testTask.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.durnov.testTask.requestbody.JsonMessage;

import javax.jms.JMSException;
import javax.jms.Message;

@Service
@Slf4j
public class JmsReceiver {

    /*@Autowired
    private JmsTemplate jmsTemplate;

    public void receiveMessage1(){
        Message message = (Message) jmsTemplate.receiveAndConvert("test.queue1");
        assert message != null;
        log.info(message.toString());
    }

    public void receiveMessage2(){
        Message message = (Message) jmsTemplate.receiveAndConvert("test.queue2");
        assert message != null;
        log.info(message.toString());
    }*/

    @JmsListener(destination = "test.queue.1")
    public void receiveMessageForQueue1(Message message) throws JMSException {
        log.info("message for first queue is " + message.getBody(String.class));
    }

    @JmsListener(destination = "test.queue.2")
    public void receiveMessageForQueue2(Message message) throws JMSException {
        log.info("message for second queue is " + message.getBody(String.class));
    }
}
