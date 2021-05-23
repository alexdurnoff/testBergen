package ru.durnov.testTask.requestbody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class JsonMessage implements Serializable, RequestBody {
    @Id
    private long id;
    private String destination;
    private String body;
    private LocalDateTime createdAT;

    public JsonMessage(){

    }

    public JsonMessage(int id, String destination, String body) {
        this.id = id;
        this.destination = destination;
        this.body = body;
        this.createdAT = LocalDateTime.now();
    }

    public JsonMessage(long id, String destination, String body) {
        this.id = id;
        this.destination = destination;
        this.body = body;
        this.createdAT = LocalDateTime.now();
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

    @JsonIgnore
    public LocalDateTime getCreatedAT() {
        return createdAT;
    }

    @JsonProperty
    public String getCreatedDate() {
        return createdAT.toString();
    }

    @JsonProperty
    public void setCreatedDate(String createdDate) {
        try {
            this.createdAT = LocalDateTime.parse(createdDate);
        } catch (Exception ignored) {

        }
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


    public void setCreatedAT(LocalDateTime createdAT) {
        this.createdAT = createdAT;
    }

    public void setCreatedAt(String date){
        try {
            setCreatedAT(LocalDateTime.parse(date));
        } catch (Exception e) {
            setCreatedAT(createdAT);
        }
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
