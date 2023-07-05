package com.templateproject.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name ="title")
    private String title;

    @Column(nullable = false, name ="date")
    private LocalDateTime date;

    @Column(nullable = false, name ="duration")
    @Temporal(TemporalType.TIME)
    private LocalDateTime duration;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User author;

    @ManyToMany
    @JoinTable(name = "user_event",
    joinColumns = @JoinColumn(name = "user_id"))
    private List<User> participants;

    public Event(){}

    public Event(String title, LocalDateTime date, LocalDateTime duration, Promotion promotion, User author, List<User> participants) {
        this.title = title;
        this.date = date;
        this.duration = duration;
        this.promotion = promotion;
        this.author = author;
        this.participants = participants;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDuration() {
        return duration;
    }

    public void setDuration(LocalDateTime duration) {
        this.duration = duration;
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

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
}
