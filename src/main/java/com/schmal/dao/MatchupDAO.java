package com.schmal.dao;

import com.schmal.domain.Matchup;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class MatchupDAO extends AbstractDAO<Matchup>
{
    public MatchupDAO(SessionFactory factory)
    {
        super(factory);
    }

    public List<Matchup> save(List<Matchup> matchups)
    {
        for (Matchup matchup : matchups)
        {
            persist(matchup);
        }

        return matchups;
    }
}