package com.schmal.resource;

import com.schmal.domain.Result;
import com.schmal.service.ResultService;
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

@Path("/result")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class ResultResource
{
    private final ResultService service;

    public ResultResource(HibernateBundle hibernateBundle)
    {
        service = new ResultService(hibernateBundle);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public List<Result> createResults(@QueryParam("teamID") long teamID) throws Exception
    {
        return service.updateNewResults(teamID);
    }
}