package com.schmal.dao;

import com.schmal.domain.League;
import com.schmal.domain.Week;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class WeekDAO extends AbstractDAO<Week>
{
    private final LeagueDAO leagueDAO;

    public WeekDAO(SessionFactory factory)
    {
        super(factory);

        leagueDAO = new LeagueDAO(factory);
    }

    public List<Week> save(League league, List<Week> weeks)
    {
        // Delete everything.
        league.getWeeks().clear();
        leagueDAO.save(league);
        currentSession().flush();

        // Insert all the new Weeks.
        league.getWeeks().addAll(weeks);
        leagueDAO.save(league);

        return league.getWeeks();
    }
}