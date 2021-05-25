package ru.durnov.testTask.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import ru.durnov.testTask.dao.JMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.durnov.testTask.jms.JMSService;
import ru.durnov.testTask.requestbody.JsonDestination;
import ru.durnov.testTask.requestbody.JsonInterval;
import ru.durnov.testTask.jms.JMSMessage;
import ru.durnov.testTask.requestbody.JsonMessage;
import ru.durnov.testTask.requestbody.JsonMessages;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/", produces = "application/json")
public class JMSController {
    private final JMSRepository jmsRepository;
    private final JMSService jmsService;

    @Autowired
    public JMSController(JMSRepository jmsRepository, JMSService jmsService) {
        this.jmsRepository = jmsRepository;
        this.jmsService = jmsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> receiveMessage(@RequestBody JsonMessage... jsonMessages){
        Arrays.stream(jsonMessages).forEach(jsonMessage -> {
            JMSMessage jmsMessage = new JMSMessage(jsonMessage);
            saveMessage(jmsMessage);
            jmsService.send(jmsMessage);
        });
        return ResponseEntity.created(URI.create("/")).build();
    }

    @Async
    private void saveMessage(JMSMessage JMSMessage) {
        jmsRepository.save(JMSMessage);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public JMSMessage messageById(@PathVariable long id) {
        Optional<JMSMessage> optionalJsonMessage = jmsRepository.findById(id);
        if (optionalJsonMessage.isPresent()) return optionalJsonMessage.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
    }


    @GetMapping("/search_by_interval")
    @ResponseStatus(HttpStatus.FOUND)
    public List<JMSMessage> messagesByInterval(JsonInterval jsonInterval) {
        return jmsRepository.findByInterval(jsonInterval.getStart(), jsonInterval.getStop());
    }

    @GetMapping("/search_by_destination")
    @ResponseStatus(HttpStatus.FOUND)
    public List<JMSMessage> messageByDestination(JsonDestination jsonDestination) {
        return jmsRepository.findByDestination(jsonDestination.getDestination());
    }




}
