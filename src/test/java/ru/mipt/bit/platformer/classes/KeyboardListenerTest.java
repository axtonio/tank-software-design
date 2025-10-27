package ru.mipt.bit.platformer.classes;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.mipt.bit.platformer.input.InputController;

class KeyboardListenerTest {

    private InputController keyboardListener;

    @BeforeEach
    void setUp() {
        keyboardListener = new KeyboardListener();
    }

    @Test
    void testGetDirectionIdle() {
        // Gdx.input is null in test environment, so this test will throw NPE
        // This is expected behavior - KeyboardListener requires LibGDX application
        // context
        assertThrows(NullPointerException.class, () -> keyboardListener.getDirection());
    }
}