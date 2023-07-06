package com.templateproject.api.controller;

import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/promotions")
//@CrossOrigin("http://localhost:4200")
public class PromotionController {

    @Autowired
    PromotionRepository promotionRepository;

    @GetMapping("")
    public List<Promotion> index() {
        return promotionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Promotion show(@PathVariable UUID id) {
        Optional<Promotion> optionalPromotion = promotionRepository.findById(id);
        if (optionalPromotion.isPresent()) {
            return optionalPromotion.get();
        } else {
            throw new RuntimeException("Promotion not found");
        }
    }

    @Autowired
    UserRepository userRepository;

    @PostMapping("")
    public Promotion createPromotion(@RequestBody Promotion newPromotion) {

        LocalDateTime LocalDateTimeNow = LocalDateTime.now();
        DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String Time = LocalDateTimeNow.format(DateFormatter);
        User user = userRepository.findById(UUID.fromString("0147e0d2-7a96-44ef-8109-1c51d02475dd"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        newPromotion.setCreationDate(LocalDateTimeNow);

        newPromotion.setAuthor(user);
        System.out.println(newPromotion);
        return promotionRepository.save(newPromotion);
    }

    //@PutMapping("/promotions/{id}")
    //public Promotion updatePromotion(@RequestBody Promotion promotion,@PathVariable UUID id){
    //    Promotion updatedPromotion = promotionRepository.findById(id).get();
    //  updatedPromotion.setName(promotion.getName());
    // return promotionRepository.save(promotion);
    //}
    @PutMapping("/{id}")
    public Promotion update(@RequestBody Promotion updatePromotion, @PathVariable UUID id) {
        Promotion updatedPromotion = promotionRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return this.promotionRepository.save(updatePromotion);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        this.promotionRepository.deleteById(id);
    }
}
