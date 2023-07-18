package com.templateproject.api.service;

import com.templateproject.api.DTO.EventDTO;
import com.templateproject.api.DtoMapper.EventDTOMapper;
import com.templateproject.api.entity.Event;
import com.templateproject.api.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {
    private static EventRepository eventRepository;
    private static EventDTOMapper eventDTOMapper;

    public EventService(EventRepository eventRepository, EventDTOMapper eventDTOMapper) {
        this.eventRepository = eventRepository;
        this.eventDTOMapper = eventDTOMapper;
    }

    public static Optional<EventDTO> findById(UUID id) {
        return eventRepository.findById(id)
                .map(eventDTOMapper::convertToDTO);

    }

    public List<EventDTO> findAllEvents() {
        List<Event> events = eventRepository.findAll();

        return events.stream()
                .map(eventDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public static Optional<EventDTO> findTopicById(UUID id) {
        return eventRepository.findById(id)
                .map(eventDTOMapper::convertToDTO);

    }
    public List<EventDTO> findByPromotionId(UUID promoId) {
        List<Event> events = eventRepository.findByPromotionId(promoId);
        return events.stream()
                .map(eventDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<EventDTO> findByAuthorId(UUID userId) {
        List<Event> events = eventRepository.findByAuthorId(userId);
        return events.stream()
                .map(eventDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }
    public void setTopicRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void setTopicDTOMapper(EventDTOMapper eventDTOMapper) {
        this.eventDTOMapper = eventDTOMapper;
    }



}
