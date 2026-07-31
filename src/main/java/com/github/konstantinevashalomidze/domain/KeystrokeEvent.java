package com.github.konstantinevashalomidze.domain;

import com.github.konstantinevashalomidze.domain.exceptions.IllegalKeystrokeEventArgumentException;

public record KeystrokeEvent(
        char typed,
        char expected,
        long keydownTimestamp,
        long keyupTimestamp,
        boolean isBackspace,
        int position
) {
    
    public KeystrokeEvent {
        if (isNotAscii(typed) || isNotAscii(expected)) {
            throw new IllegalKeystrokeEventArgumentException("Character should be from ASCII set");
        }

        if (keydownTimestamp > keyupTimestamp) {
            throw new IllegalKeystrokeEventArgumentException("Keydown timestamp should be less than keyup timestamp");
        }

        if (isBackspace && (typed != '\0' || expected != '\0')) {
            throw new IllegalKeystrokeEventArgumentException("Event can't be backspace and typed evet at the same time");
        }

        if (position < 0) {
            throw new IllegalKeystrokeEventArgumentException("Position should be greater than 0");
        }
    }
    
    private boolean isNotAscii(char c) {
        return !(c < 128);
    }
    
    /**
     * Weather what was typed equals what was expected
     * @return true if expected equals typed false otherwise
     */
    public boolean isCorrect() {
        return typed == expected;
    }

    /**
     * Key up timestamp minus key down timestamp
     * @return time holding press on key
     */
    public long dwellTime() {
        return keyupTimestamp - keydownTimestamp;
    }
}
