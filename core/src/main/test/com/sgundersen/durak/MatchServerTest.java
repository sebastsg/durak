package com.sgundersen.durak;

import com.sgundersen.durak.core.match.MatchServer;
import com.sgundersen.durak.core.net.match.Action;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchServerTest {

    @Test
    void attackerCannotTakeCards() {
        MatchServer server = new MatchServer();
        assertFalse(server.onAction(server.getAttackingPlayerId(), Action.takeCards()));
    }

    @Test
    void defenderCannotEndTurn() {
        MatchServer server = new MatchServer();
        assertFalse(server.onAction(server.getDefendingPlayerId(), Action.endTurn()));
    }

    @Test
    void attackerCannotUseInvalidCardIndex() {
        MatchServer server = new MatchServer();
        assertFalse(server.onAction(server.getAttackingPlayerId(), Action.useCard(100)));
    }

    @Test
    void attackerCanUseValidCardIndex() {
        MatchServer server = new MatchServer();
        assertTrue(server.onAction(server.getAttackingPlayerId(), Action.useCard(3)));
    }

    @Test
    void defenderCannotDefendBeforeAnAttack() {
        MatchServer server = new MatchServer();
        assertFalse(server.onAction(server.getDefendingPlayerId(), Action.useCard(1)));
    }

}
