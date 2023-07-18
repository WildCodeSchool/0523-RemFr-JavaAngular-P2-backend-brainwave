package com.templateproject.api.DTO;


import java.time.LocalDateTime;
import java.util.UUID;

public record ResourceDTO(
        UUID id,
        String title,
        String content,
        String link,
        LocalDateTime creationDate,
        UUID promotionId,
        UUID authorId

) {
    public void setCreationDate(LocalDateTime localDateTimeNow) {
        LocalDateTime creationDate;
    }

}
