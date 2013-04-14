package com.schmal.dao;

import com.schmal.domain.Team;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class TeamDAO extends AbstractDAO<Team>
{
    public TeamDAO(SessionFactory factory)
    {
        super(factory);
    }

    public List<Team> save(List<Team> teams)
    {
        for (Team team : teams)
        {
            persist(team);
        }

        return teams;
    }
}