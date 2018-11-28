package com.sgundersen.durak.core.net.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttempt {

    private String name;
    private String email;
    private String accountId;
    private String currentTokenId;

}
