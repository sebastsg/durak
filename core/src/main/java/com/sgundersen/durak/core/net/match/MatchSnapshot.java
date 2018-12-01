package com.sgundersen.durak.core.net.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchSnapshot {

    private List<MatchClientState> clientStates = new ArrayList<>();

    public MatchClientState getClientState(int index) {
        return clientStates.get(index);
    }

}
