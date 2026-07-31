package com.github.konstantinevashalomidze.domain.exceptions;

public class SessionNotStartedException extends RuntimeException {
    public SessionNotStartedException(String message) {
        super(message);
    }
}
