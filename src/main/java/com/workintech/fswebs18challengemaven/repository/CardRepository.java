package com.workintech.fswebs18challengemaven.repository;

import com.workintech.fswebs18challengemaven.entity.Card;
import com.workintech.fswebs18challengemaven.entity.Color;
import com.workintech.fswebs18challengemaven.entity.Type;

import java.util.List;

public interface CardRepository {
    Card save(Card card);
    List<Card> findByColor(Color color);
    List<Card> findAll();
    List<Card> findByValue(Integer value);
    List<Card> findByType(Type type);
    Card update(Card card);
    void remove(Long id);
}
