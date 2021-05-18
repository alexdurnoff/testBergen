package ru.durnov.testTask.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.durnov.testTask.requestbody.JsonMessage;
import ru.durnov.testTask.requestbody.JsonMessages;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
class JMSRepositoryTest {

    @Autowired
    private JMSRepository jmsRepository;

    @Test
    public void testSave(){
        JsonMessage jsonMessage = new JsonMessage(4, "localhost", "Привет, сосед!");
        jmsRepository.save(jsonMessage);
        JsonMessage jsonMessage1 = jmsRepository.findById(4L).get();
        Assertions.assertEquals(jsonMessage, jsonMessage1);
        Assertions.assertEquals(jsonMessage1.getId(), 4);
        Assertions.assertEquals(jsonMessage1.getDestination(), "localhost");
        Assertions.assertEquals(jsonMessage1.getBody(), "Привет, сосед!");
    }

    @Test
    public void testByDestination(){
        JsonMessages jsonMessages = jmsRepository.findByDestination("localhost.test.task");
        Assertions.assertEquals(jsonMessages.getMessageList().size(), 2);
        jsonMessages = jmsRepository.findByDestination("192.168.1.190.test.task");
        Assertions.assertEquals(jsonMessages.getMessageList().size(), 1);
    }

    @Test
    public void testByInterval(){
        JsonMessages jsonMessages = jmsRepository.findByInterval("2021-05-01T00:00:00", "2021-05-18T00:00:00");
        Assertions.assertEquals(jsonMessages.getMessageList().size(), 3);
        jsonMessages = jmsRepository.findByInterval("2021-05-10T00:00:00", "2021-05-18T00:00:00");
        Assertions.assertEquals(jsonMessages.getMessageList().size(), 2);
        jsonMessages = jmsRepository.findByInterval("2021-05-10", "2021-05-18");
        Assertions.assertEquals(jsonMessages.getMessageList().size(), 2);
        jsonMessages = jmsRepository.findByInterval("2021-05-11", "2021-05-18");
        Assertions.assertEquals(jsonMessages.getMessageList().size(), 1);
        jsonMessages = jmsRepository.findByInterval("2021-05-01", "2021-05-11");
        Assertions.assertEquals(jsonMessages.getMessageList().size(), 2);
        jsonMessages = jmsRepository.findByInterval("2021-05-01", "20ddd1-05-11");
        Assertions.assertEquals(jsonMessages.getMessageList().size(), 0);
    }

}