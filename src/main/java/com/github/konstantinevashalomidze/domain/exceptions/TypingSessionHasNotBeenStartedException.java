package com.github.konstantinevashalomidze.domain.exceptions;

public class TypingSessionHasNotBeenStartedException extends RuntimeException {
    public TypingSessionHasNotBeenStartedException(String message) {
        super(message);
    }
}
