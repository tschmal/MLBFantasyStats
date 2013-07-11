package com.schmal.resource;

import com.schmal.domain.Player;
import com.schmal.domain.Week;
import com.schmal.service.PlayerService;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Path("/player")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class PlayerResource
{
    private final PlayerService service;

    public PlayerResource(HibernateBundle hibernateBundle)
    {
        service = new PlayerService(hibernateBundle);
    }

    @POST
    @Path("/results")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    @UnitOfWork
    public String syncResults(@QueryParam("leagueID") long leagueID) throws Exception
    {
        service.syncResults(leagueID);

        return "Got it.";
    }

    @POST
    @Path("/results/week")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public Week syncResults(
        @QueryParam("weekID") long weekID,
        @QueryParam("maxPeriodID") int maxPeriodID) throws Exception
    {
        return service.syncResults(weekID, maxPeriodID);
    }

    @POST
    @Path("/projections")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public List<Player> createProjections(@QueryParam("leagueID") long leagueID) throws Exception
    {
        return service.createProjections(leagueID);
    }
}