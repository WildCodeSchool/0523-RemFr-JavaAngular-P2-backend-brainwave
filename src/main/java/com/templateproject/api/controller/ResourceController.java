package com.templateproject.api.controller;

import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.Resource;

import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.ResourceRepository;
import com.templateproject.api.service.BeanUtils;
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
     public  ResourceController(ResourceRepository resourceRepository, PromotionRepository promotionRepository){
         this.resourceRepository = resourceRepository;
         this.promotionRepository = promotionRepository;
     }
     @GetMapping("")
    public List<Resource> index(){
         return this.resourceRepository.findAll();
     }
     @GetMapping("/promotions/{promoId}")
     public List<Resource> getResourceByPromoId (@PathVariable UUID promoId){
         return this.resourceRepository.findByPromotionId(promoId);
     }

     @PostMapping ("/promotions/{promoId}")
     @ResponseStatus(HttpStatus.CREATED)
    public Resource createResource(
            @PathVariable UUID promoId,
            @RequestBody Resource newResource
     ){

         Promotion promotion = promotionRepository
                 .findById(promoId)
                 .orElseThrow(() -> new ResponseStatusException(
                         HttpStatus.NOT_FOUND, "Not Found" + promoId));

         LocalDateTime localDateTimeNow = LocalDateTime.now();
         newResource.setCreationDate(localDateTimeNow);
         newResource.setPromotion(promotion);


         return this.resourceRepository.save(newResource);
     }
    @PutMapping("/{id}")
    public ResponseEntity<Resource> updateResource(@PathVariable UUID id, @RequestBody @Validated  Resource updatedResource) {
        return resourceRepository.findById(id).map(resource-> {
            BeanUtils.copyNonNullProperties(updatedResource, resource);
            return ResponseEntity.ok(resourceRepository.save(resource));
        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
         this.resourceRepository.deleteById(id);
    }

}
