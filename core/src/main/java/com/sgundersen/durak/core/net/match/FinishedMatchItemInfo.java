package com.sgundersen.durak.core.net.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishedMatchItemInfo {

    private long id;
    private Date createdAt;
    private String name;

}
