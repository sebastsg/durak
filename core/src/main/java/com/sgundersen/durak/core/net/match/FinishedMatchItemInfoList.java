package com.sgundersen.durak.core.net.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishedMatchItemInfoList {

    private List<FinishedMatchItemInfo> matches = new ArrayList<>();

    public void add(FinishedMatchItemInfo info) {
        matches.add(info);
    }

}
