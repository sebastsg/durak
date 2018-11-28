package com.sgundersen.durak.core.net.lobby;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LobbyItemInfoList {

    private List<LobbyItemInfo> lobbies = new ArrayList<>();

    public void add(LobbyItemInfo info) {
        lobbies.add(info);
    }

}
