package ru.mipt.bit.platformer.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.input.InputController;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyboardListenerTest {
    
    private InputController keyboardListener;
    
    @BeforeEach
    void setUp() {
        keyboardListener = new KeyboardListener();
    }
    
    @Test
    void testGetDirectionIdle() {
        assertEquals(Direction.IDLE, keyboardListener.getDirection());
    }
}