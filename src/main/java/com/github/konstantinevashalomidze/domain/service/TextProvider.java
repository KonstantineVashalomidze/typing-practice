package com.github.konstantinevashalomidze.domain.service;

public interface TextProvider {
    /**
     * Responsible for providing sanitized text
     * @return returns text to be used in sessions
     */
    String getText();
}
