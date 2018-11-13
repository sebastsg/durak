package com.sgundersen.durak.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class Player {

    private final String name;
    private final String email;
    private final String id;

    @Setter
    private float rating = 0.0f;

    @Setter
    private int activeMatchId = -1;

    @Setter
    private int activeMatchPlayerId = -1;

}
