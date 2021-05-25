package ru.durnov.testTask.jms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.durnov.testTask.requestbody.JsonMessage;
import ru.durnov.testTask.requestbody.JsonMessages;
import ru.durnov.testTask.requestbody.RequestBody;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class JMSMessage implements Serializable, RequestBody {
    @Id
    private long id;
    private String destination;
    private String body;
    private LocalDateTime createdAT;

    public JMSMessage(){

    }

    public JMSMessage(int id, String destination, String body) {
        this.id = id;
        this.destination = destination;
        this.body = body;
        this.createdAT = LocalDateTime.now();
    }

    public JMSMessage(long id, String destination, String body) {
        this.id = id;
        this.destination = destination;
        this.body = body;
        this.createdAT = LocalDateTime.now();
    }

    public JMSMessage(JsonMessage jsonMessage){
        this(jsonMessage.getId(), jsonMessage.getDestination(), jsonMessage.getBody());
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
        JMSMessage that = (JMSMessage) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
