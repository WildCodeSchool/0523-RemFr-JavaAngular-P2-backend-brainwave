package com.templateproject.api.controller;

import com.templateproject.api.entity.Event;
import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.EventRepository;
import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventController(EventRepository eventRepository, PromotionRepository promotionRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.promotionRepository = promotionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public List<Event> index() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public Event show(@PathVariable UUID id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            return optionalEvent.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found: " + id);
        }
    }

    @PostMapping("/{promotionId}/{authorId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(
            @PathVariable UUID promotionId,
            @PathVariable UUID authorId,
            @RequestBody Event newEvent) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion not found: " + promotionId));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found: " + authorId));

        LocalDateTime localDateTimeNow = LocalDateTime.now();
        newEvent.setCreationDate(localDateTimeNow);
        newEvent.setPromotion(promotion);
        newEvent.setAuthor(author);

        return eventRepository.save(newEvent);
    }

    @PutMapping("/{id}/{promotionId}/{authorId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Event updateEvent(
            @PathVariable UUID id,
            @PathVariable UUID promotionId,
            @PathVariable UUID authorId,
            @RequestBody Event updatedEvent) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion not found: " + promotionId));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found: " + authorId));

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found: " + id));

        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDate(updatedEvent.getDate());
        existingEvent.setDuration(updatedEvent.getDuration());
        existingEvent.setPromotion(promotion);
        existingEvent.setAuthor(author);

        return eventRepository.save(existingEvent);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        eventRepository.deleteById(id);
    }
}
