package com.sgundersen.durak.core.net;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchLobbyInfo {

    private int id;
    private String name;
    private int maxPlayers;
    private int currentPlayers;

}
