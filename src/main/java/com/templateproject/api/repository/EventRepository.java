package com.templateproject.api.repository;

import com.templateproject.api.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByPromotionId(UUID promoId);

    List<Event> findByAuthorId(UUID userId);
}
