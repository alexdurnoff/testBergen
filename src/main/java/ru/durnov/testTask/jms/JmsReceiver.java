package ru.durnov.testTask.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import ru.durnov.testTask.requestbody.JsonMessage;

import javax.jms.JMSException;
import javax.jms.Message;

@Service
@Slf4j
@EnableJms
public class JmsReceiver {

        @JmsListener(destination = "jms.message.mq")
        public void receiveMessage(JsonMessage jsonMessage){
            log.info("Received "
                    + jsonMessage.getId()
                    + ":" + jsonMessage.getDestination()
                    + ":" + jsonMessage.getBody());
        }


}
