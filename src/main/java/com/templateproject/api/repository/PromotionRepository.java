package com.templateproject.api.repository;

import com.templateproject.api.entity.Promotion;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
    List<Promotion> findPromotionsByTagOrNameContaining(String searchTagTerm,String searchNameTerm);

    void deleteUserById(UUID userId);

}