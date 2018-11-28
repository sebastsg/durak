package com.sgundersen.durak.core.net.lobby;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LobbyState {

    private List<String> players = new ArrayList<>();
    private LobbyItemInfo info = new LobbyItemInfo();
    private boolean started;

}
