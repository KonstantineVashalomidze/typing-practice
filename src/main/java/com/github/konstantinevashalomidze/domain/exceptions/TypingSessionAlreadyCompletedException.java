package com.github.konstantinevashalomidze.domain.exceptions;

public class TypingSessionAlreadyCompletedException extends RuntimeException {
    public TypingSessionAlreadyCompletedException(String message) {
        super(message);
    }
}
