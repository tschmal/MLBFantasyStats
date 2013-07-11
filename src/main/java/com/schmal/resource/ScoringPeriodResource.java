package com.schmal.resource;

import com.schmal.domain.ScoringPeriod;
import com.schmal.service.ScoringPeriodService;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/period")
@Produces(MediaType.APPLICATION_JSON)
public class ScoringPeriodResource
{
    private final ScoringPeriodService service;

    public ScoringPeriodResource(HibernateBundle hibernateBundle)
    {
        service = new ScoringPeriodService(hibernateBundle);
    }

    @GET
    @Path("/max")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public ScoringPeriod getMaxScoringPeriod(@QueryParam("leagueID") long leagueID) throws Exception
    {
        return service.getMaxScoringPeriod(leagueID);
    }
}