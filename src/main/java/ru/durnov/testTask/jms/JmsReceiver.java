package ru.durnov.testTask.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableJms
public class JmsReceiver {

    @JmsListener(destination = "DLQ")
    public void receiveMessage(JMSMessage JMSMessage){
        log.info("Received "
                + JMSMessage.getId()
                + ":" + JMSMessage.getDestination()
                + ":" + JMSMessage.getBody());
    }

    @JmsListener(destination = "jms.message.queue1")
    public void receiveMessageFromJmsQueue1(JMSMessage JMSMessage){
        log.info("Received "
                + JMSMessage.getId()
                + ":" + JMSMessage.getDestination()
                + ":" + JMSMessage.getBody());
    }

    @JmsListener(destination = "jms.message.queue2")
    public void receiveMessageFromJmsQueue2(JMSMessage JMSMessage){
        log.info("Received "
                + JMSMessage.getId()
                + ":" + JMSMessage.getDestination()
                + ":" + JMSMessage.getBody());
    }

}
