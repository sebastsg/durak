package com.sgundersen.durak.server;

import com.sgundersen.durak.server.service.LobbyService;
import com.sgundersen.durak.server.service.MatchService;
import com.sgundersen.durak.server.service.PlayerService;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@ApplicationPath("api")
public class ApplicationConfiguration extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(MatchService.class);
        resources.add(LobbyService.class);
        resources.add(PlayerService.class);
        return resources;
    }

}
