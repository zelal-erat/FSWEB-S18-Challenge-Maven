package com.workintech.fswebs18challengemaven.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CardException extends RuntimeException {
    private HttpStatus httpStatus;
    public CardException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getStatus() {
        return this.httpStatus.value();
    }

}
