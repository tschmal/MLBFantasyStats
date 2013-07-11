package com.schmal.dao;

import com.schmal.domain.League;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
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

    public List<League> getLeaguesByService(String fantasyService)
    {
        return list(namedQuery("getByService").setParameter("service", fantasyService));
    }

    public List<League> getLeaguesByFantasyID(long fantasyID)
    {
        return list(namedQuery("getByFantasyID").setParameter("fantasyID", fantasyID));
    }

    public League getLeague(long leagueID)
    {
        return uniqueResult(namedQuery("getByID").setParameter("leagueID", leagueID));
    }

    public League getLeague(long fantasyID, int year)
    {
        return uniqueResult(namedQuery("get")
            .setParameter("fantasyID", fantasyID)
            .setParameter("year", year));
    }

    public void deleteLeague(long leagueID)
    {
        League league = getLeague(leagueID);
        currentSession().delete(league);
    }
}