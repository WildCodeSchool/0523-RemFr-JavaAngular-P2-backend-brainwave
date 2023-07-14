package com.templateproject.api.controller;

import com.templateproject.api.DTO.PromotionDTO;
import com.templateproject.api.DtoMapper.PromotionDTOMapper;
import com.templateproject.api.entity.Promotion;
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
import com.templateproject.api.service.PromotionService;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/promotions")

public class PromotionController {

    private final PromotionRepository promotionRepository;
    private final PromotionDTOMapper promotionDTOMapper;

    public PromotionController(PromotionRepository promotionRepository,
                               PromotionDTOMapper promotionDTOMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionDTOMapper = promotionDTOMapper;
    }

    @GetMapping("")
    public List<PromotionDTO> getAllPromotions() {
        PromotionService promotionService = new PromotionService(
                promotionRepository, promotionDTOMapper, userRepository);
        List<PromotionDTO> promotionDTOs = promotionService.findAllPromotions();
        return promotionDTOs;
    }


    @GetMapping("/{id}")
    public PromotionDTO show(@PathVariable UUID id) {
        Optional<PromotionDTO> optionalPromotionDTO = PromotionService.findPromotionById(id);
        if (optionalPromotionDTO.isPresent()) {
            return optionalPromotionDTO.get();
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
    /*@PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PromotionDTO createPromotion(
            @PathVariable UUID userId,
            @RequestBody PromotionDTO newPromotion) {
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        PromotionDTO createdPromotion = PromotionService.createPromotion(userId, newPromotion, localDateTimeNow);

        return createdPromotion;
    }*/

    @PutMapping("/{id}/users/{userId}")
    public ResponseEntity<PromotionDTO> updatePromotion(
            @PathVariable UUID userId,
            @PathVariable UUID id,
            @RequestBody @Validated PromotionDTO promotionDTO) {

        Promotion updatedPromotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Promotion not found: " + id));

        BeanUtils.copyNonNullProperties(promotionDTO, updatedPromotion);

        Promotion savedPromotion = promotionRepository.save(updatedPromotion);

        PromotionDTO updatedPromotionDTO = promotionDTOMapper.convertToDTO(savedPromotion);
        return ResponseEntity.ok(updatedPromotionDTO);
    }


    /*@PutMapping("/{id}/users/{userId}/add-participants")
    public ResponseEntity<Promotion> addParticipants(@PathVariable UUID id,
                                                     @PathVariable UUID userId,
                                                     @RequestBody @Validated Promotion addParticipants) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Not Found" + userId));
        return this.promotionRepository.findById(id).map(promotion -> {
            addParticipants.getParticipants().add(user);
            BeanUtils.copyNonNullProperties(addParticipants, promotion);
            return ResponseEntity.ok(promotionRepository.save(promotion));
        }).orElse(ResponseEntity.notFound().build());
    }*/
    @PutMapping("/{id}/users/{userId}/add-participants")
    public ResponseEntity<PromotionDTO> addParticipants(@PathVariable UUID id,
                                                        @PathVariable UUID userId,
                                                        @RequestBody
                                                        @Validated Promotion addParticipants) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Not Found" + userId));
        Promotion addParticipantsPromotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Promotion not found: " + id));
        addParticipants.getParticipants().add(user);
        BeanUtils.copyNonNullProperties(addParticipants, addParticipantsPromotion);
        Promotion addedParticipants = promotionRepository.save(addParticipantsPromotion);
        PromotionDTO addParticipantsPromotionDTO = promotionDTOMapper
                .convertToDTO(addedParticipants);
        return ResponseEntity.ok(addParticipantsPromotionDTO);

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

}