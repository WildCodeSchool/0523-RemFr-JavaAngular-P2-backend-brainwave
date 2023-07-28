package com.templateproject.api.controller;

import com.templateproject.api.DTO.ResourceDTO;
import com.templateproject.api.DTO.UserDTO;
import com.templateproject.api.DtoMapper.ResourceDTOMapper;
import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.Resource;

import com.templateproject.api.entity.User;
import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.ResourceRepository;
import com.templateproject.api.repository.UserRepository;
import com.templateproject.api.service.BeanUtils;
import com.templateproject.api.service.ResourceService;
import com.templateproject.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("resources")
public class ResourceController {
    private final ResourceRepository resourceRepository;
    private final PromotionRepository promotionRepository;
    private final ResourceDTOMapper resourceDTOMapper;
    private final UserRepository userRepository;

    public ResourceController(ResourceRepository resourceRepository,
                              PromotionRepository promotionRepository,
                              ResourceDTOMapper resourceDTOMapper,
                              UserRepository userRepository) {
        this.resourceRepository = resourceRepository;
        this.promotionRepository = promotionRepository;
        this.resourceDTOMapper = resourceDTOMapper;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public List<ResourceDTO> getAllResources() {
        ResourceService resourceService = new ResourceService(
                resourceRepository, resourceDTOMapper
        );
        List<ResourceDTO> resourceDTOS = resourceService.findAllResources();
        return resourceDTOS;
    }

    @GetMapping("/promotions/{promoId}")
    public List<Resource> getResourceByPromoId(@PathVariable UUID promoId) {
        return this.resourceRepository.findByPromotionId(promoId);
    }
    @GetMapping("/{resourceId}")
    public ResourceDTO getId(@PathVariable UUID resourceId) {
        Optional<ResourceDTO> optionalResourceDTO = ResourceService.findById(resourceId);
        if (optionalResourceDTO.isPresent()) {
            return optionalResourceDTO.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found " + resourceId);
        }
    }
    @PostMapping("/promotions/{promoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource createResource(
            @PathVariable UUID promoId,
            @RequestBody Resource newResource
    ) {

        Promotion promotion = promotionRepository
                .findById(promoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Not Found : " + promoId));

        LocalDateTime localDateTimeNow = LocalDateTime.now();
        newResource.setCreationDate(localDateTimeNow);
        newResource.setPromotion(promotion);


        return this.resourceRepository.save(newResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceDTO> updateResource(@PathVariable UUID id,
                                                      @RequestBody @Validated Resource resourceDTO) {
        Resource updatedResource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Resource not found : " + id
                ));
        BeanUtils.copyNonNullProperties(resourceDTO, updatedResource);
        Resource savedResource = resourceRepository.save(updatedResource);
        ResourceDTO updatedResourceDTO = resourceDTOMapper.convertToDTO(savedResource);
        return ResponseEntity.ok(updatedResourceDTO);

    }

    @PutMapping("/{id}/users/{userId}/add-authors")
    public ResponseEntity<ResourceDTO> addAuthor(@PathVariable UUID id,
                                                 @PathVariable UUID userId,
                                                 @RequestBody @Validated Resource addAuthor) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId));

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Resource not found: " + id));

        resource.setAuthor(user);
        BeanUtils.copyNonNullProperties(addAuthor, resource);
        Resource updatedResource = resourceRepository.save(resource);
        ResourceDTO addedAuthorDTO = resourceDTOMapper.convertToDTO(updatedResource);
        return ResponseEntity.ok(addedAuthorDTO);
    }

    @PutMapping("/{id}/promotions/{promoId}/add-promotions")
    public ResponseEntity<ResourceDTO> addPromotion(@PathVariable UUID id,
                                                    @PathVariable UUID promoId,
                                                    @RequestBody @Validated Resource addPromotion) {

        Promotion promotion = promotionRepository.findById(promoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Promotion not found: " + promoId));

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Resource not found: " + id));

        resource.setPromotion(promotion);
        BeanUtils.copyNonNullProperties(addPromotion, resource);
        Resource updatedResource = resourceRepository.save(resource);
        ResourceDTO addedAuthorDTO = resourceDTOMapper.convertToDTO(updatedResource);
        return ResponseEntity.ok(addedAuthorDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        this.resourceRepository.deleteById(id);
    }

}
