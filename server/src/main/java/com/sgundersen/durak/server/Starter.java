package com.sgundersen.durak.server;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class Starter {

    @PostConstruct
    public void start() {

    }

}
