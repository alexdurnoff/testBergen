package ru.durnov.testTask.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonMessages {
    private final List<JsonMessage> messageList;

    public JsonMessages(){
        this.messageList = new ArrayList<>();
    }

    public JsonMessages(List<JsonMessage> messageList) {
        this();
        this.messageList.addAll(messageList);
    }

    public JsonMessages(JsonMessage ... messages){
        this(Arrays.asList(messages));
    }

    @JsonProperty
    public List<JsonMessage> getMessageList() {
        return messageList;
    }
}
