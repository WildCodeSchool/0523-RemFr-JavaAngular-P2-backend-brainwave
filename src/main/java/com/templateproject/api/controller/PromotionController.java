package com.templateproject.api.controller;

import com.templateproject.api.DTO.AddParticipantsDTO;
import com.templateproject.api.DTO.PromotionDTO;
import com.templateproject.api.DtoMapper.PromotionDTOMapper;
import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.User;
import com.templateproject.api.entity.Resource;
import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.ResourceRepository;
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
import java.util.stream.Collectors;



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
        newPromotion.setDescription(newPromotion.getDescription());
        return this.promotionRepository.save(newPromotion);
    }

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
    @PutMapping("/{id}/add-participants")
    public ResponseEntity<PromotionDTO> addParticipants(@PathVariable UUID id,

                                                        @RequestBody AddParticipantsDTO participants) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Promotion not found: " + id));
        var updatedPromotion = promotion;
        for (String participantId : participants.participants()){
            User user = userRepository.findById(UUID.fromString(participantId)).orElseThrow(
                    () -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Not Found" + participants));
            updatedPromotion.getParticipants().add(user);
        }

        BeanUtils.copyNonNullProperties(updatedPromotion, promotion);
        Promotion addedParticipants = promotionRepository.save(promotion);
        PromotionDTO addParticipantsPromotionDTO = promotionDTOMapper
                .convertToDTO(addedParticipants);
        return ResponseEntity.ok(addParticipantsPromotionDTO);};



    @Autowired
    ResourceRepository resourceRepository;

    @PutMapping("/{id}/resources/{resourceId}/add-resources")
    public ResponseEntity<PromotionDTO> addResources(@PathVariable UUID id,
                                                     @PathVariable UUID resourceId,
                                                     @RequestBody
                                                     @Validated Promotion addResources) {

        Resource resource = resourceRepository.findById(resourceId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Not Found" + resourceId));
        Promotion addResourcesPromotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Promotion not found: " + id));
        addResources.getResources().add(resource);
        BeanUtils.copyNonNullProperties(addResources, addResourcesPromotion);
        Promotion addedResources = promotionRepository.save(addResourcesPromotion);
        PromotionDTO addParticipantsResourcesDTO = promotionDTOMapper
                .convertToDTO(addedResources);
        return ResponseEntity.ok(addParticipantsResourcesDTO);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        this.promotionRepository.deleteById(id);
    }

    @PostMapping("/search")
    public List<PromotionDTO> search(@RequestBody Map<String, String> body) {
        String searchTerm = body.get("content");
        List<Promotion> promotions = promotionRepository
                .findPromotionsByTagOrNameContaining(searchTerm, searchTerm);
        return promotions.stream()
                .map(promotionDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }


}