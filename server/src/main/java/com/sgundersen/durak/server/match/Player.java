package com.sgundersen.durak.server.match;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class Player {

    @Setter
    @Getter
    private String name;

    @Getter
    private final String email;

    @Getter
    private final String id;

    private final AtomicInteger matchId = new AtomicInteger(-1);
    private final AtomicInteger handId = new AtomicInteger(-1);

    public int getMatchId() {
        return matchId.get();
    }

    public int getHandId() {
        return handId.get();
    }

    public void set(int matchId, int handId) {
        this.matchId.set(matchId);
        this.handId.set(handId);
    }



}
