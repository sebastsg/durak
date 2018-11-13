package com.sgundersen.durak.server.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleAuthResponse {

    private String iss;
    private String sub; // Unique Google account ID
    private String azp;
    private String aud; // The client ID of the application
    private String iat;
    private String exp;

    private String email;
    private boolean emailVerified;
    private String name;
    private String picture;
    private String givenName;
    private String familyName;
    private String locale;

}
