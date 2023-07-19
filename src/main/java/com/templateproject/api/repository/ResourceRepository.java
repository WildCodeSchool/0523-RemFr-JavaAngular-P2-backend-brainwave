package com.templateproject.api.repository;

import com.templateproject.api.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, UUID> {
    List<Resource> findByPromotionId(UUID promoId);

    List<Resource> findByAuthorId(UUID userId);
}
