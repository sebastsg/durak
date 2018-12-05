package com.sgundersen.durak.server.service;

import com.sgundersen.durak.core.net.match.FinishedMatchItemInfo;
import com.sgundersen.durak.core.net.match.FinishedMatchItemInfoList;
import com.sgundersen.durak.server.db.MatchDao;
import com.sgundersen.durak.server.db.MatchEntity;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("history")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class HistoryService {

    private static final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    private MatchDao matchDao;

    @GET
    @Path("list")
    public String list() {
        FinishedMatchItemInfoList items = new FinishedMatchItemInfoList();
        for (MatchEntity match : matchDao.getAllMeta()) {
            items.add(new FinishedMatchItemInfo(match.getId(), match.getCreatedAt(), match.getName()));
        }
        return jsonb.toJson(items);
    }

    @GET
    @Path("match/{id}")
    public String match(@PathParam("id") long matchId) {
        return matchDao.find(matchId).getSnapshots();
    }

}
