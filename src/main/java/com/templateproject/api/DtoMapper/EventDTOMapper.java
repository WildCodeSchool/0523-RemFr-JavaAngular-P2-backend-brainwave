package com.templateproject.api.DtoMapper;

import com.templateproject.api.DTO.EventDTO;
import com.templateproject.api.entity.Event;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EventDTOMapper implements Function<Event, EventDTO> {
    @Override
    public EventDTO apply(Event event) {
        return new EventDTO(event.getId(), event.getTitle(), event.getDuration(), event.getPromotion().getId(), event.getDate(), event.getAuthor().getId(), event.getParticipants());
    }

    public EventDTO convertToDTO(Event event) {
        return apply(event);
    }

}
