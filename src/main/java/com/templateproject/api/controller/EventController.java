package com.templateproject.api.controller;


import com.templateproject.api.entity.Event;
import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.EventRepository;
import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.UserRepository;
import com.templateproject.api.service.BeanUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
        newEvent.setDate(localDateTimeNow);
        newEvent.setPromotion(promotion);
        newEvent.setAuthor(author);

        return eventRepository.save(newEvent);
    }

    @PutMapping("/{id}/{promotionId}/{authorId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Event> updateEvent(@PathVariable UUID authorId,
                                             @PathVariable UUID promotionId,
                                             @PathVariable UUID id,
                                             @RequestBody @Validated Event updatedEvent) {
        return this.eventRepository.findById(id).map(event -> {
            BeanUtils.copyNonNullProperties(updatedEvent, event );
            return ResponseEntity.ok(eventRepository.save(event ));
        }).orElse(ResponseEntity.notFound().build());
    }
//Todo pas de many to one entre event et promotion sur promotion (verifier si la cascade fonctionne)

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        eventRepository.deleteById(id);
    }
}