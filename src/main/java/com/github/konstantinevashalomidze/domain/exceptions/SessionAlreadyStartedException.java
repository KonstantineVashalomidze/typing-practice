package com.github.konstantinevashalomidze.domain.exceptions;

public class SessionAlreadyStartedException extends RuntimeException {
    public SessionAlreadyStartedException(String message) {
        super(message);
    }
}
