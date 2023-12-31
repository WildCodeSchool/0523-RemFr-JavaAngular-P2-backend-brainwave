package com.templateproject.api.repository;

import com.templateproject.api.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {
    List<Topic> findByPromotionId(UUID promoId);

    List<Topic> findByAuthorId(UUID author);
}
