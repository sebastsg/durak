package com.sgundersen.durak;

import com.sgundersen.durak.core.match.Card;
import com.sgundersen.durak.core.match.Talon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TalonTest {

    @Test
    void minRank6() {
        Talon talon = new Talon(6);
        assertEquals(36, talon.count());
        assertEquals(6, talon.getLowestRank());
    }

    @Test
    void minRank1To2() {
        Talon talon = new Talon(1); // Should be set to 2 in constructor.
        assertEquals(52, talon.count());
        assertEquals(2, talon.getLowestRank());
    }

    @Test
    void takeCard() {
        Talon talon = new Talon(6);
        assertEquals(36, talon.count());
        assertNotNull(talon.take());
        assertEquals(35, talon.count());
    }

    @Test
    void checkBottom() {
        Talon talon = new Talon(6);
        Card bottom = talon.getBottom();
        for (int i = 0; i < 35; i++) {
            assertNotNull(talon.take());
            assertNotNull(talon.getBottom());
        }
        Card bottomNow = talon.getBottom();
        assertEquals(bottom, bottomNow);
        assertEquals(bottom, talon.take());
    }

}
