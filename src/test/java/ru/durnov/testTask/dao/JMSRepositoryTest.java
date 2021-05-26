package ru.durnov.testTask.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.durnov.testTask.jms.JMSMessage;

import java.util.List;

@Slf4j
@ExtendWith(SpringExtension.class)
@DataJpaTest
class JMSRepositoryTest {

    @Autowired
    private JMSRepository jmsRepository;

    @Test
    public void testSave(){
        JMSMessage JMSMessage = new JMSMessage(4, "DLQ", "Привет, сосед!");
        jmsRepository.save(JMSMessage);
        JMSMessage JMSMessage1 = jmsRepository.findById(4L).get();
        Assertions.assertEquals(JMSMessage, JMSMessage1);
        Assertions.assertEquals(JMSMessage1.getId(), 4);
        Assertions.assertEquals(JMSMessage1.getDestination(), "DLQ");
        Assertions.assertEquals(JMSMessage1.getBody(), "Привет, сосед!");
    }

    @Test
    public void testByDestination(){
        List<JMSMessage> messages = jmsRepository.findByDestination("jms.message.queue1");
        Assertions.assertEquals(messages.size(), 2);
        messages = jmsRepository.findByDestination("jms.message.queue2");
        Assertions.assertEquals(messages.size(), 1);
    }

    @Test
    public void testByInterval(){
        List<JMSMessage> messages = jmsRepository.findByInterval("2021-05-01T00:00:00", "2021-05-18T00:00:00");
        Assertions.assertEquals(messages.size(), 3);
        messages = jmsRepository.findByInterval("2021-05-10T00:00:00", "2021-05-18T00:00:00");
        Assertions.assertEquals(messages.size(), 2);
        messages = jmsRepository.findByInterval("2021-05-10", "2021-05-18");
        Assertions.assertEquals(messages.size(), 2);
        messages = jmsRepository.findByInterval("2021-05-11", "2021-05-18");
        Assertions.assertEquals(messages.size(), 1);
        messages = jmsRepository.findByInterval("2021-05-01", "2021-05-11");
        Assertions.assertEquals(messages.size(), 2);
        messages = jmsRepository.findByInterval("2021-05-01", "20ddd1-05-11");
        Assertions.assertEquals(messages.size(), 0);
    }

}