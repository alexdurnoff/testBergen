package ru.durnov.testTask.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonMessages implements Serializable, RequestBody {
    private final List<JsonMessage> messageList;

    public JsonMessages(){
        this.messageList = new ArrayList<>();
    }

    public JsonMessages(List<JsonMessage> messageList) {
        this();
        this.messageList.addAll(messageList);
    }

    public JsonMessages(JsonMessage... messages){
        this(Arrays.asList(messages));
    }

    @JsonProperty
    public JsonMessage[] messages() {
        return messageList.toArray(new JsonMessage[0]);
    }

    @Override
    public String json() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(messages());
    }
}
