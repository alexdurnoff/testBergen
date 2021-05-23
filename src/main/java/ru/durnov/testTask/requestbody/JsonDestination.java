package ru.durnov.testTask.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonDestination implements RequestBody{
    private String destination;

    public JsonDestination(String destination){
        this.destination = destination;
    }

    public JsonDestination(){
        this("");
    }

    @JsonProperty
    public String getDestination() {
        return destination;
    }

    @JsonProperty
    public void setDestination(String destination) {
        this.destination = destination;
    }
}
