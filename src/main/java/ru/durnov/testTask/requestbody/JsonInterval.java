package ru.durnov.testTask.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class JsonInterval implements RequestBody{
    private String start;
    private String stop;

    public JsonInterval(String start, String stop){
        this.start = start;
        this.stop = stop;
    }

    public JsonInterval(LocalDateTime startTime, LocalDateTime stopTime){
        this.start = startTime.toString();
        this.stop = stopTime.toString();
    }

    public JsonInterval(){
        this("", "");
    }

    @JsonProperty
    public String getStart() {
        return start;
    }

    @JsonProperty
    public void setStart(String start) {
        this.start = start;
    }

    @JsonProperty
    public String getStop() {
        return stop;
    }

    @JsonProperty
    public void setStop(String stop) {
        this.stop = stop;
    }
}
