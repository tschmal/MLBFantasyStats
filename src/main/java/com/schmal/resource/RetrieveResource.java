package com.schmal.resource;

import com.schmal.domain.FullLeague;
import com.schmal.domain.Matchup;
import com.schmal.domain.ScoringCategory;
import com.schmal.service.ESPNFantasyService;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.metrics.annotation.Timed;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/retrieve")
@Produces(MediaType.APPLICATION_JSON)
public class RetrieveResource
{
    private final String leagueURL;

    private final ESPNFantasyService service;

    public RetrieveResource(HibernateBundle hibernateBundle, String leagueURL)
    {
        this.leagueURL = leagueURL;
        this.service = new ESPNFantasyService(hibernateBundle);
    }

    @GET
    @Path("/todo")
    @Timed
    @UnitOfWork
    public List<Matchup> getMatchupsToParse() throws Exception
    {
        return service.getMatchupsToParse(this.leagueURL);
    }

    @GET
    @Path("/scoring")
    @Timed
    @UnitOfWork
    public List<ScoringCategory> getScoringCategories() throws Exception
    {
        return service.getScoringCategories(this.leagueURL);
    }

    @POST
    @Path("/new")
    @Timed
    @UnitOfWork
    public FullLeague createNewLeague() throws Exception
    {
        return service.saveLeague(this.leagueURL);
    }

    @POST
    @Path("/matchups/all")
    @Timed
    @UnitOfWork
    public List<Matchup> createAllMatchups() throws Exception
    {
        return service.saveAllMatchups(this.leagueURL);
    }
}