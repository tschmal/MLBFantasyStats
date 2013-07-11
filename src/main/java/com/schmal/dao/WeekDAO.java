package com.schmal.dao;

import com.schmal.domain.League;
import com.schmal.domain.Week;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.Date;
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

    public List<Week> getDateRange(League league, Date startDate, Date endDate)
    {
        return list(namedQuery("byDateRange")
            .setParameter("league", league)
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate));
    }

    public Week getByID(long id)
    {
        return get(id);
    }
}