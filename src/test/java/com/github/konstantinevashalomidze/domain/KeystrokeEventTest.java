package com.github.konstantinevashalomidze.domain;

import com.github.konstantinevashalomidze.domain.exceptions.IllegalKeystrokeEventArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class KeystrokeEventTest {

    @Test
    void shouldReturnTrueIfCorrect() {
        var kse = new KeystrokeEvent(
                'c',
                'c',
                System.currentTimeMillis(),
                System.currentTimeMillis() + 10,
                false,
                0
        );

        assertTrue(kse.isCorrect());
    }

    @Test
    void shouldReturnFalseIfIncorrect() {
        var kse = new KeystrokeEvent(
                'c',
                'a',
                System.currentTimeMillis(),
                System.currentTimeMillis() + 10,
                false,
                0
        );
        assertFalse(kse.isCorrect());
    }


    @Test
    void shouldReturnCorrectDwellTime() {
        var kse = new KeystrokeEvent(
                'c',
                'a',
                System.currentTimeMillis(),
                System.currentTimeMillis() + 10,
                false,
                0
        );

        assertEquals(10, kse.dwellTime());
    }

    @Test
    void shouldThrowWhenInvalidEventArguments() {
        // Something that is not in allowed charset
        assertThrows(IllegalKeystrokeEventArgumentException.class, () -> new KeystrokeEvent(
                'ñ',
                'ñ',
                System.currentTimeMillis(),
                System.currentTimeMillis() + 10,
                false,
                0
        ));

        // Keydown is greater than keyup
        assertThrows(IllegalKeystrokeEventArgumentException.class, () -> new KeystrokeEvent(
                'i',
                'i',
                System.currentTimeMillis() + 10,
                System.currentTimeMillis(),
                false,
                0
        ));

        // Event is backspace and typed event at the same time
        assertThrows(IllegalKeystrokeEventArgumentException.class, () -> new KeystrokeEvent(
                'i',
                'i',
                System.currentTimeMillis(),
                System.currentTimeMillis() + 10,
                true,
                0
        ));

        assertThrows(IllegalKeystrokeEventArgumentException.class, () -> new KeystrokeEvent(
                'i',
                'i',
                System.currentTimeMillis(),
                System.currentTimeMillis() + 10,
                false,
                -1
        ));

    }


}
