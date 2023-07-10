package com.templateproject.api.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name ="lastname")
    private String lastname;

    @Column(nullable = false, name="firstname")
    private String firstname;

    @Column(nullable = false, name="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, unique=true, name="email")
    private String email;

    @Column(nullable = false, name="password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(mappedBy = "author")
    private List<Promotion> promotions;

    @OneToMany(mappedBy = "author")
    private List<Answer> answers;

    @OneToMany(mappedBy = "author")
    private List<Resource> resources;

    @OneToMany(mappedBy = "author")
    private List<Topic> topics;

    @OneToMany(mappedBy = "author")
    private List<Event> eventsCreated;

    @ManyToMany(mappedBy = "participants")
    private List<Event> eventsParticipated;

    public User() {
    }

    public User(String lastname, String firstname, Role role, String email, String password, List<Promotion> promotions,
            List<Answer> answers, List<Resource> resources, List<Topic> topics, List<Event> eventsCreated,
            List<Event> eventsParticipated) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.role = role;
        this.email = email;
        this.password = password;
        this.promotions = promotions;
        this.answers = answers;
        this.resources = resources;
        this.topics = topics;
        this.eventsCreated = eventsCreated;
        this.eventsParticipated = eventsParticipated;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
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

    public List<Event> getEventsCreated() {
        return eventsCreated;
    }

    public void setEventsCreated(List<Event> eventsCreated) {
        this.eventsCreated = eventsCreated;
    }

    public List<Event> getEventsParticipated() {
        return eventsParticipated;
    }

    public void setEventsParticipated(List<Event> eventsParticipated) {
        this.eventsParticipated = eventsParticipated;
    }
}
