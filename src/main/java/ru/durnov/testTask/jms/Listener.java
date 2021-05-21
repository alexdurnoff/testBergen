package ru.durnov.testTask.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Listener {

    @JmsListener(destination = "localhost")
    public void proceedMessage(String content){
        log.info("Message body is " + content);
    }
}
