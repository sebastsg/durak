package com.sgundersen.durak.server;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class Starter {

    @Inject
    private MatchService matchService;

    @Inject
    private PlayerService playerService;

    @PostConstruct
    public void start() {

    }

}
