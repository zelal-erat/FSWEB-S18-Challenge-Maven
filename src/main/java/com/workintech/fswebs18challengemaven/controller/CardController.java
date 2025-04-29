package com.workintech.fswebs18challengemaven.controller;

import com.workintech.fswebs18challengemaven.entity.Card;
import com.workintech.fswebs18challengemaven.entity.Color;
import com.workintech.fswebs18challengemaven.entity.Type;
import com.workintech.fswebs18challengemaven.exceptions.CardException;
import com.workintech.fswebs18challengemaven.repository.CardRepository;
import com.workintech.fswebs18challengemaven.util.CardValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// controller/CardController.java
@RestController
@RequestMapping("/workintech/cards")
@Slf4j
public class CardController {
    private final CardRepository cardRepo;

    @Autowired
    public CardController(CardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    @GetMapping
    public List<Card> getAllCards() {
        return cardRepo.findAll();
    }

    @GetMapping("/byColor/{color}")
    public List<Card> getCardsByColor(@PathVariable("color") String color) {
        Color colorEnum;

        // String parametreyi Color enum'ına dönüştürme
        try {
            colorEnum = Color.valueOf(color.toUpperCase());  // Color enum'ına dönüştürme
        } catch (IllegalArgumentException e) {
            throw new CardException("Invalid color: " + color, HttpStatus.BAD_REQUEST); // Geçersiz renk hatası
        }

        return cardRepo.findByColor(colorEnum); // Enum kullanılarak veritabanı sorgusu yapılır
    }



    @PostMapping
    public Card createCard(@RequestBody Card card) {
        CardValidation.validate(card);
        return cardRepo.save(card);
    }

    @PutMapping
    public Card updateCard(@RequestBody Card card) {
        CardValidation.validate(card);
        return cardRepo.update(card);
    }

    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable Long id) {
        cardRepo.remove(id);
    }

    @GetMapping("/byValue/{value}")
    public List<Card> getCardsByValue(@PathVariable Integer value) {
        return cardRepo.findByValue(value);
    }

    @GetMapping("/byType/{type}")
    public List<Card> getCardsByType(@PathVariable("type") String type) {
        Type typeEnum;
        try {
            typeEnum = Type.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CardException("Invalid type: " + type, HttpStatus.BAD_REQUEST);
        }
        return cardRepo.findByType(typeEnum);
    }



}
