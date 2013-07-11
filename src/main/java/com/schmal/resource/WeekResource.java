package com.schmal.resource;

import com.schmal.domain.Week;
import com.schmal.service.WeekService;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Path("/week")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class WeekResource
{
    private final WeekService service;

    public WeekResource(HibernateBundle hibernateBundle)
    {
        service = new WeekService(hibernateBundle);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public List<Week> createWeeks(@QueryParam("leagueID") long leagueID) throws Exception
    {
        return service.createWeeks(leagueID);
    }

    @POST
    @Path("/roster")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public Week fetchRosters(
        @QueryParam("weekID") long weekID,
        @QueryParam("maxPeriodID") int maxPeriodID)
        throws Exception
    {
        return service.fetchRosters(weekID, maxPeriodID);
    }

    @GET
    @Path("/score")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public Week computeScores(@QueryParam("weekID") long weekID) throws Exception
    {
        return service.computeScore(weekID);
    }
}