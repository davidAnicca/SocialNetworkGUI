package com.example.socialnetworkgui2.domain;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Message {
    String from;
    String to;
    String content;

    public Message(String from, String to, String content, LocalDateTime dateTime) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.dateTime = dateTime;
    }

    public Timestamp getDateTime() {
        return Timestamp.valueOf(dateTime);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    LocalDateTime dateTime;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
        dateTime = LocalDateTime.now();
    }
}
