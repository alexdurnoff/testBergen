package ru.durnov.testTask.requestbody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * Message without created time incoming from request
 */
public class JsonMessage implements Serializable, RequestBody {

    private long id;
    private String destination;
    private String body;

    public JsonMessage(){

    }

    public JsonMessage(int id, String destination, String body) {
        this.id = id;
        this.destination = destination;
        this.body = body;
    }

    public JsonMessage(long id, String destination, String body) {
        this.id = id;
        this.destination = destination;
        this.body = body;
    }
    @JsonProperty
    public long getId() {
        return id;
    }
    @JsonProperty
    public String getDestination() {
        return destination;
    }
    @JsonProperty
    public String getBody() {
        return body;
    }


    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public void setDestination(String destination) {
        this.destination = destination;
    }

    @JsonProperty
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonMessage that = (JsonMessage) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
