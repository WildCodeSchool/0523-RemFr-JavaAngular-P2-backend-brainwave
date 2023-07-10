package com.templateproject.api.controller;

import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            throw new RuntimeException("Promotion not found" + id);
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

        System.out.println(newPromotion);
        return this.promotionRepository.save(newPromotion);
    }

    @PutMapping("/{id}/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Promotion updatePromotion(
            @PathVariable UUID id,
            @PathVariable UUID userId,
            @RequestBody Promotion updatedPromotion) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found!" + userId));

        Promotion updatePromotion = promotionRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND));

        LocalDateTime localDateTimeNow = LocalDateTime.now();
        updatePromotion.setCreationDate(localDateTimeNow);
        updatePromotion.setName(updatedPromotion.getName());
        updatePromotion.setDescription(updatedPromotion.getDescription());
        updatePromotion.setTag(updatedPromotion.getTag());
        updatePromotion.setRating(updatedPromotion.getRating());
        updatePromotion.setDifficulty(updatedPromotion.getDifficulty());
        updatePromotion.setType(updatedPromotion.getType());
        System.out.println(updatedPromotion);
        return this.promotionRepository.save(updatePromotion);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        this.promotionRepository.deleteById(id);
    }

    @PostMapping("/search")
    public List<Promotion> search(@RequestBody Map<String, String> body) {
        String searchTerm = body.get("content");
        return promotionRepository.findPromotionsByTagOrNameContaining(searchTerm ,searchTerm);
    }

}