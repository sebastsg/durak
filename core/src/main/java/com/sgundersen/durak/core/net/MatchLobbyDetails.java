package com.sgundersen.durak.core.net;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchLobbyDetails {

    private List<String> players = new ArrayList<>();
    private boolean started;

}
