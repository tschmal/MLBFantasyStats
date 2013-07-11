package com.schmal.resource;

import com.schmal.domain.Luck;
import com.schmal.domain.TeamWeek;
import com.schmal.service.DataService;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class DataResource
{
    private final DataService service;

    public DataResource(HibernateBundle hibernateBundle)
    {
        service = new DataService(hibernateBundle);
    }

    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public TeamWeek getTeamWeekScores(@QueryParam("leagueID") long leagueID) throws Exception
    {
        return service.getTeamWeekScores(leagueID);
    }

    @GET
    @Path("/luck")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public List<Luck> getLuck(
        @QueryParam("leagueID") long leagueID,
        @QueryParam("maxWeekID") Long maxWeekID)
        throws Exception
    {
        return service.getLuck(leagueID, maxWeekID);
    }
}