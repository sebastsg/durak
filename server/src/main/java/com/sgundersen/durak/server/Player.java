package com.sgundersen.durak.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Getter
public class Player {

    private final String name;
    private final String email;
    private final String id;
    private final AtomicInteger activeMatchId = new AtomicInteger(-1);
    private final AtomicInteger activeMatchPlayerId = new AtomicInteger(-1);

}
