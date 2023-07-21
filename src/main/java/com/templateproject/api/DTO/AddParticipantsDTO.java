package com.templateproject.api.DTO;

import java.util.List;

public record AddParticipantsDTO(
        List<String> participants
) {
    @Override
    public List<String> participants() {
        return participants;
    }
}
