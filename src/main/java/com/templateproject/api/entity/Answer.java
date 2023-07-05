package com.templateproject.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, name ="content")
    @Lob
    private String content;
    @Column(nullable = true, name ="upvote")
    private Integer upvote;
    @Column(nullable = false, name ="creation_date")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name ="author_id")
    private User author;

    public Answer() {
    }

    public Answer(String content, Integer upvote, LocalDateTime creationDate, Topic topic, User author) {
        this.content = content;
        this.upvote = upvote;
        this.creationDate = creationDate;
        this.topic = topic;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUpvote() {
        return upvote;
    }

    public void setUpvote(Integer upvote) {
        this.upvote = upvote;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public User getUser() {
        return author;
    }

    public void setUser(User author) {
        this.author = author;
    }
}