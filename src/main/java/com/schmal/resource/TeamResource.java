package com.schmal.resource;

import com.schmal.domain.Team;
import com.schmal.service.TeamService;
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

@Path("/team")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class TeamResource
{
    private final TeamService service;

    public TeamResource(HibernateBundle hibernateBundle)
    {
        service = new TeamService(hibernateBundle);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public List<Team> createNewLeague(@QueryParam("leagueID") long leagueID) throws Exception
    {
        return service.createTeams(leagueID);
    }
}