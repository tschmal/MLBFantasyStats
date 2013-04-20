package com.schmal.dao;

import com.schmal.domain.League;
import com.schmal.domain.Week;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class WeekDAO extends AbstractDAO<Week>
{
    public WeekDAO(SessionFactory factory)
    {
        super(factory);
    }

    public List<Week> save(List<Week> weeks)
    {
        if (weeks.size() > 0)
        {
            deleteWeeks(weeks.get(0).getLeague());
        }

        for (Week week : weeks)
        {
            persist(week);
        }

        return weeks;
    }

    public void deleteWeeks(League league)
    {
        namedQuery("deleteByLeagueID").setParameter("league", league).executeUpdate();
    }
}