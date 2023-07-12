package com.templateproject.api.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "topic")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "title")
    private String title;
    @Column(nullable = false, name = "content")
    @Lob
    private String content;

    @Column(nullable = false, name = "creation_date")
    private LocalDateTime creationDate;

    @Column(nullable = true, name = "upvote")
    private Float upvote;

   @ManyToOne(fetch = FetchType.EAGER)
   //@ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    //@OneToMany(mappedBy = "topic")
    @OneToMany(mappedBy = "topic", cascade = CascadeType.REMOVE)
    private List<Answer> answers;

    public Topic() {
    }

    public Topic(String title, String content, LocalDateTime creationDate, Float upvote, Promotion promotion,
            User author, List<Answer> answers) {
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.upvote = upvote;
        this.promotion = promotion;
        this.author = author;
        this.answers = answers;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Float getUpvote() {
        return upvote;
    }

    public void setUpvote(Float upvote) {
        this.upvote = Float.valueOf(upvote);
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", creationDate=" + creationDate +
                ", upvote=" + upvote +
                ", promotion=" + promotion +
                ", author=" + author +
                ", answers=" + answers +
                '}';
    }
}
