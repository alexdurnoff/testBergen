package ru.durnov.testTask.controller;

import lombok.extern.slf4j.Slf4j;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.durnov.testTask.dao.JMSRepository;
import ru.durnov.testTask.jms.JMSService;
import ru.durnov.testTask.requestbody.JsonDestination;
import ru.durnov.testTask.requestbody.JsonInterval;
import ru.durnov.testTask.requestbody.JsonMessage;
import ru.durnov.testTask.requestbody.JsonMessages;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(JMSController.class)
@Slf4j
public class JMSControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JMSRepository jmsRepository;

    @MockBean
    private JMSService jmsService;

    @Test
    public void saveNewMessagesTest() throws Exception {
        JsonMessage message1 = new JsonMessage(4, "jms.message.mq", "Превед");
        JsonMessage message2 = new JsonMessage(5, "jms.message.mq", "Пока");
        JsonMessages jsonMessages = new JsonMessages(message1, message2);
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMessages.json()))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void testFindBuId() throws Exception {
        when(jmsRepository.findById(2L))
                .thenReturn(java.util.Optional.of(
                        new JsonMessage(2L, "jms.message.mq", "Пока, сосед!")
                ));
        mockMvc.perform(get("/2"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value("Пока, сосед!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.destination").value("jms.message.mq"));
    }

    @Test
    public void testFindByDestination() throws Exception {
        when(jmsRepository.findByDestination("jms.message.mq"))
                .thenReturn(Optional.of(
                        new JsonMessages(
                                new JsonMessage(1L, "jms.message.mq", "Превед, сосед!"),
                                new JsonMessage(2L, "jms.message.mq", "Пока, сосед!")
                        )
                ).get());
        mockMvc.perform(get("/search_by_destination")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JsonDestination("jms.message.mq").json()))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FOUND.value()));
    }

    @Test
    public void testFindByInterval() throws Exception {
        JsonInterval jsonInterval = new JsonInterval(
                LocalDateTime.of(2021, 5, 1, 23, 15),
                LocalDateTime.of(2021, 5, 10, 19, 15)
        );
        when(jmsRepository.findByInterval(
                LocalDateTime.of(2021, 5, 1, 23, 15).toString(),
                LocalDateTime.of(2021, 5, 10, 19, 15).toString()
        )).thenReturn(
                new JsonMessages(
                        new JsonMessage(1, "jms.message.mq", "Превед!"),
                        new JsonMessage(2, "jms.message.mq", "Пока!")
                )
        );
        mockMvc.perform(get("/search_by_interval")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInterval.json()))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FOUND.value()));

    }



}