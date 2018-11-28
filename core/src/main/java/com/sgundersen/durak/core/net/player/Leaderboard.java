package com.sgundersen.durak.core.net.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Leaderboard {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private String playerName;
        private int victories;
        private int defeats;
        private float ratio;
    }

    private List<Item> items = new ArrayList<>();

}
