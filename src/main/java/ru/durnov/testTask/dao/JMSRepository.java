package ru.durnov.testTask.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.durnov.testTask.requestbody.JsonMessage;
import ru.durnov.testTask.requestbody.JsonMessages;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public interface JMSRepository extends CrudRepository<JsonMessage, Long> {

    default JsonMessages findByDestination(String destination){
        List<JsonMessage> jsonMessageList = new ArrayList<>();
        Iterable<JsonMessage> jsonMessages = findAll();
        jsonMessages.forEach(jsonMessage -> {
            if (jsonMessage.getDestination().equals(destination)){
                jsonMessageList.add(jsonMessage);
            }
        });
        return new JsonMessages(jsonMessageList);
    }

    default JsonMessages findByInterval(String start, String stop){
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
        return new JsonMessages(Collections.emptyList());
    }

    default JsonMessages findByInterval(LocalDate startDate, LocalDate stopDate){
        List<JsonMessage> jsonMessageList = new ArrayList<>();
        Iterable<JsonMessage> messages = findAll();
        messages.forEach(jsonMessage -> {
            LocalDateTime localDateTime = jsonMessage.getCreatedAT();
            LocalDate localDate =  LocalDate.of(
                    localDateTime.getYear(),
                    localDateTime.getMonth(),
                    localDateTime.getDayOfMonth()
            );
            if (localDate.compareTo(startDate) >= 0 && localDate.compareTo(stopDate) <=0){
                jsonMessageList.add(jsonMessage);
            }

        });
        return new JsonMessages(jsonMessageList);
    };

    default JsonMessages findByInterval(LocalDateTime startTime, LocalDateTime stopTime){
        List<JsonMessage> jsonMessageList = new ArrayList<>();
        Iterable<JsonMessage> jsonMessages = findAll();
        jsonMessages.forEach(jsonMessage -> {
            if (jsonMessage.getCreatedAT().compareTo(startTime) >= 0
                    && jsonMessage.getCreatedAT().compareTo(stopTime) <= 0){
                jsonMessageList.add(jsonMessage);
            }
        });
        return new JsonMessages(jsonMessageList);
    }
}
