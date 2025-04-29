package com.workintech.fswebs18challengemaven.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CardException.class)
    public ResponseEntity<CardErrorResponse> handleCardException(CardException ex) {
        log.error("Card Exception: {}", ex.getMessage());

        CardErrorResponse errorResponse = new CardErrorResponse(
                ex.getHttpStatus().value(),    // Status kodu (örneğin 400)
                ex.getMessage(),               // Hata mesajı
                System.currentTimeMillis()     // Şu anki zaman
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CardErrorResponse> handleGeneralException(Exception ex) {
        log.error("Unexpected error", ex);

        CardErrorResponse errorResponse = new CardErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),   // 500
                "Something went wrong",
                System.currentTimeMillis()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
