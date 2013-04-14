package com.schmal.dao;

import com.schmal.domain.League;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

public class LeagueDAO extends AbstractDAO<League>
{
    public LeagueDAO(SessionFactory factory)
    {
        super(factory);
    }

    public League save(League league)
    {
        return persist(league);
    }

    public League getLeague(long espnID, int year)
    {
        List<League> leagues = list(namedQuery("checkExisting")
            .setLong("espnID", espnID)
            .setInteger("year", year));

        return (leagues.size() > 0) ? leagues.get(0) : null;
    }
}