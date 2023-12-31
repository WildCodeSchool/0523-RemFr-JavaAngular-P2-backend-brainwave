package com.templateproject.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "promotion")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = true, name = "name")
    private String name;

    @Lob
    @Column(nullable = true, name = "description", length = 1000)
    private String description;

    @Column(nullable = true, name = "banner")
    private String banner;

    @Column(name = "tag")
    private String tag;

    @Column(name = "picture")
    private String picture;

    @Column(nullable = true, name = "rating")
    private Float rating;

    @Column(nullable = true, name = "difficulty")
    private String difficulty;

    @Column(nullable = true, name = "type")
    private String type;

    @Column(nullable = true, name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToMany
    @JoinTable(name = "promotion_participants",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.REMOVE)
    private List<Event> events;
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.REMOVE)
    private List<Resource> resources = new ArrayList<>();


    @OneToMany(mappedBy = "promotion", cascade = CascadeType.REMOVE)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Topic> topics = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User author;

    public Promotion() {
    }

    public Promotion(String name, String description, String tag, Float rating, String difficulty, String type,
                     LocalDateTime creationDate, Set<User> participants, List<Resource> resources, List<Topic> topics,
                     User author) {
        this.name = name;
        this.description = description;
        this.tag = tag;
        this.rating = rating;
        this.difficulty = difficulty;
        this.type = type;
        this.creationDate = creationDate;
        this.participants = participants;
        this.resources = resources;
        this.topics = topics;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tag='" + tag + '\'' +
                ", rating=" + rating +
                ", difficulty='" + difficulty + '\'' +
                ", type='" + type + '\'' +
                ", creationDate=" + creationDate +
                ", participants=" + participants +
                ", resources=" + resources +
                ", topics=" + topics +
                ", author=" + author +
                '}';
    }
}
