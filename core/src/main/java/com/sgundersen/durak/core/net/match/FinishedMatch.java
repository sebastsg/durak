package com.sgundersen.durak.core.net.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishedMatch {

    private List<MatchSnapshot> snapshots = new ArrayList<>();

    public MatchSnapshot getSnapshot(int index) {
        return snapshots.get(index);
    }

    public int getTotalSnapshots() {
        return snapshots.size();
    }

}
