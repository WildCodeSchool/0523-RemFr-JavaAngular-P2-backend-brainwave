package com.templateproject.api.controller;

import com.templateproject.api.DTO.EventDTO;
import com.templateproject.api.DtoMapper.EventDTOMapper;
import com.templateproject.api.entity.Event;
import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.EventRepository;
import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.UserRepository;
import com.templateproject.api.service.BeanUtils;

import com.templateproject.api.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;
    private final EventDTOMapper eventDTOMapper;

    public EventController(EventRepository eventRepository, PromotionRepository promotionRepository,
                           UserRepository userRepository, EventDTOMapper eventDTOMapper) {
        this.eventRepository = eventRepository;
        this.promotionRepository = promotionRepository;
        this.userRepository = userRepository;
        this.eventDTOMapper = eventDTOMapper;
    }

    @GetMapping("")
    public List<EventDTO> getAllEvents() {
        EventService eventService = new EventService(
                eventRepository, eventDTOMapper);
        List<EventDTO> eventDTOs = eventService.findAllEvents();
        return eventDTOs;
    }

    @GetMapping("/{id}")
    public EventDTO show(@PathVariable UUID id) {
        Optional<EventDTO> optionalEventDTO = EventService.findById(id);
        if (optionalEventDTO.isPresent()) {
            return optionalEventDTO.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found : " + id);
        }
    }

    @PostMapping("/promotions/{promotionId}/authors/{authorId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(
            @PathVariable UUID promotionId,
            @PathVariable UUID authorId,
            @RequestBody Event newEvent) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Promotion not found: " + promotionId));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Author not found: " + authorId));

        LocalDateTime localDateTimeNow = LocalDateTime.now();
        newEvent.setDate(localDateTimeNow);
        newEvent.setPromotion(promotion);
        newEvent.setAuthor(author);

        return eventRepository.save(newEvent);
    }

    @PutMapping("/{id}/promotions/{promotionId}/authors/{authorId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<EventDTO> updateEvent(@PathVariable UUID id,
                                                @PathVariable UUID promotionId,
                                                @PathVariable UUID authorId,
                                                @RequestBody @Validated Event eventDTO) {
        Event updatedEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event not found :" + id + "for promotion " + promotionId + "author " + authorId));
        BeanUtils.copyNonNullProperties(eventDTO, updatedEvent);
        Event savedEvent = eventRepository.save(updatedEvent);
        EventDTO updatedEventDTO = eventDTOMapper.convertToDTO(savedEvent);
        return ResponseEntity.ok(updatedEventDTO);

    }


    @PutMapping("/{id}/users/{userId}/add-participants")
    public ResponseEntity<EventDTO> addParticipants(@PathVariable UUID id,
                                                    @PathVariable UUID userId,
                                                    @RequestBody @Validated Event addParticipants) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found: " + id));

        event.setParticipants(event.getParticipants() != null ? event.getParticipants() : new ArrayList<>());
        event.getParticipants().add(user);

        BeanUtils.copyNonNullProperties(addParticipants, addParticipants);

        Event updatedEvent = eventRepository.save(event);
        EventDTO addedParticipantsDTO = eventDTOMapper.convertToDTO(updatedEvent);
        return ResponseEntity.ok(addedParticipantsDTO);
    }

    @PutMapping("/{id}/users/{userId}/add-authors")
    public ResponseEntity<EventDTO> addAuthor(@PathVariable UUID id,
                                              @PathVariable UUID userId,
                                              @RequestBody @Validated Event addAuthor) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId));

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event not found: " + id));

        event.setAuthor(user);

        BeanUtils.copyNonNullProperties(addAuthor, event);

        Event updatedEvent = eventRepository.save(event);
        EventDTO addedAuthorDTO = eventDTOMapper.convertToDTO(updatedEvent);
        return ResponseEntity.ok(addedAuthorDTO);
    }

    @PutMapping("/{id}/promotions/{promoId}/add-promotions")
    public ResponseEntity<EventDTO> addPromotion(@PathVariable UUID id,
                                                 @PathVariable UUID promoId,
                                                 @RequestBody @Validated Event addPromotion) {

        Promotion promotion = promotionRepository.findById(promoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Promotion found: " + promoId));

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event not found: " + id));

        event.setPromotion(promotion);

        BeanUtils.copyNonNullProperties(addPromotion, event);

        Event updatedEvent = eventRepository.save(event);
        EventDTO addedPromotionDTO = eventDTOMapper.convertToDTO(updatedEvent);
        return ResponseEntity.ok(addedPromotionDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        eventRepository.deleteById(id);
    }
}