package com.workintech.fswebs18challengemaven.util;

import com.workintech.fswebs18challengemaven.entity.Card;
import com.workintech.fswebs18challengemaven.entity.Type;
import com.workintech.fswebs18challengemaven.exceptions.CardException;
import org.springframework.http.HttpStatus;

// util/CardValidation.java
public class CardValidation {
    public static void validate(Card card) {
        if (card.getType() == Type.JOKER) {
            if (card.getValue() != null || card.getColor() != null) {
                throw new CardException("JOKER cards cannot have value or color.", HttpStatus.BAD_REQUEST);

            }
        } else {
            if ((card.getValue() != null && card.getType() != null)
                    || (card.getValue() == null && card.getType() == null)) {
                throw new CardException("Card must have either a type or a value, not both or neither.", HttpStatus.BAD_REQUEST);

            }
        }
    }
}
