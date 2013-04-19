package com.schmal.resource;

import com.schmal.domain.League;
import com.schmal.service.LeagueService;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Path("/league")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class LeagueResource
{
    private final LeagueService service;

    public LeagueResource(HibernateBundle hibernateBundle)
    {
        service = new LeagueService(hibernateBundle);
    }

    @GET
    @Path("/service/{fantasyService}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public List<League> getLeaguesByService(
        @PathParam("fantasyService") String fantasyService)
        throws Exception
    {
        return service.getLeaguesByService(fantasyService);
    }

    @GET
    @Path("/fantasyID/{fantasyID}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public List<League> getLeaguesByFantasyID(
        @PathParam("fantasyID") long fantasyID)
        throws Exception
    {
        return service.getLeaguesByFantasyID(fantasyID);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public League createNewLeague(
        @QueryParam("fantasyLeagueID") long fantasyLeagueID,
        @QueryParam("year") int year,
        @QueryParam("fantasyService") String fantasyService)
        throws Exception
    {
        return service.createNewLeague(fantasyLeagueID, year, fantasyService);
    }
}