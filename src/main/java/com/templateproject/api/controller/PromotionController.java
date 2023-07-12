package com.templateproject.api.controller;

import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.Topic;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.UserRepository;
import com.templateproject.api.service.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/promotions")

public class PromotionController {

    private final PromotionRepository promotionRepository;

    public PromotionController(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @GetMapping("")
    public List<Promotion> index() {
        return this.promotionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Promotion show(@PathVariable UUID id) {
        Optional<Promotion> optionalPromotion = this.promotionRepository.findById(id);
        if (optionalPromotion.isPresent()) {
            return optionalPromotion.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion not found" + id);
        }
    }

    @Autowired
    UserRepository userRepository;

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Promotion createPromotion(
            @PathVariable UUID userId,
            @RequestBody Promotion newPromotion) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Not Found" + userId));

        LocalDateTime localDateTimeNow = LocalDateTime.now();
        newPromotion.setCreationDate(localDateTimeNow);
        newPromotion.setAuthor(user);

        return this.promotionRepository.save(newPromotion);
    }

    @PutMapping("/{id}/users/{userId}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable UUID userId, @PathVariable UUID id,
            @RequestBody @Validated Topic updatedPromotion) {
        return this.promotionRepository.findById(id).map(promotion -> {
            BeanUtils.copyNonNullProperties(updatedPromotion, promotion);
            return ResponseEntity.ok(promotionRepository.save(promotion));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        this.promotionRepository.deleteById(id);
    }

    @PostMapping("/search")
    public List<Promotion> search(@RequestBody Map<String, String> body) {
        String searchTerm = body.get("content");
        return promotionRepository.findPromotionsByTagOrNameContaining(searchTerm, searchTerm);
    }
//47f4d7e7-132f-4532-9147-c75a0331d387 userID:04895e78-4ef2-426d-b577-d3e7a1b9870b
}