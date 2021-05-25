package ru.durnov.testTask.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.durnov.testTask.jms.JMSMessage;
import ru.durnov.testTask.requestbody.JsonMessages;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public interface JMSRepository extends CrudRepository<JMSMessage, Long> {

    default List<JMSMessage> findByDestination(String destination){
        List<JMSMessage> jmsMessages = new ArrayList<>();
        Iterable<JMSMessage> messages = findAll();
        messages.forEach(message -> {
            if (message.getDestination().equals(destination)){
                jmsMessages.add(message);
            }
        });
        return jmsMessages;
    }

    default List<JMSMessage> findByInterval(String start, String stop){
        try {
            LocalDateTime startTime = LocalDateTime.parse(start);
            LocalDateTime stopTime = LocalDateTime.parse(stop);
            return findByInterval(startTime, stopTime);
        } catch (Exception e) {
            try {
                LocalDate startDate = LocalDate.parse(start);
                LocalDate stopDate = LocalDate.parse(stop);
                return findByInterval(startDate, stopDate);
            } catch (Exception ignored) {
            }
        }
        return Collections.emptyList();
    }

    default List<JMSMessage> findByInterval(LocalDate startDate, LocalDate stopDate){
        List<JMSMessage> jmsMessages = new ArrayList<>();
        Iterable<JMSMessage> messages = findAll();
        messages.forEach(jsonMessage -> {
            LocalDateTime localDateTime = jsonMessage.getCreatedAT();
            LocalDate localDate =  LocalDate.of(
                    localDateTime.getYear(),
                    localDateTime.getMonth(),
                    localDateTime.getDayOfMonth()
            );
            if (localDate.compareTo(startDate) >= 0 && localDate.compareTo(stopDate) <=0){
                jmsMessages.add(jsonMessage);
            }

        });
        return jmsMessages;
    };

    default List<JMSMessage> findByInterval(LocalDateTime startTime, LocalDateTime stopTime){
        List<JMSMessage> messages = new ArrayList<>();
        Iterable<JMSMessage> jsonMessages = findAll();
        jsonMessages.forEach(jsonMessage -> {
            if (jsonMessage.getCreatedAT().compareTo(startTime) >= 0
                    && jsonMessage.getCreatedAT().compareTo(stopTime) <= 0){
                messages.add(jsonMessage);
            }
        });
        return messages;
    }
}
