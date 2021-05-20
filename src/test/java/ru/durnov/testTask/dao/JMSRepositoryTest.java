package ru.durnov.testTask.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.durnov.testTask.requestbody.JsonDestination;
import ru.durnov.testTask.requestbody.JsonMessage;
import ru.durnov.testTask.requestbody.JsonMessages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

    @Test
    void testDestinationToString() throws IOException {
        JsonDestination jsonDestination = new JsonDestination("localhost");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = new String(objectMapper.writeValueAsBytes(jsonDestination));
        Files.newBufferedWriter(Path.of("jsondestionation1.txt")).write(json);
        System.out.println(json);
    }

}