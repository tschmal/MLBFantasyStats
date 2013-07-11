package com.schmal.dao;

import com.schmal.domain.Result;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

public class ResultDAO extends AbstractDAO<Result>
{
    public ResultDAO(SessionFactory factory)
    {
        super(factory);
    }

    public List<Result> save(List<Result> results)
    {
        for (Result result : results)
        {
            persist(result);
        }

        return results;
    }

    public int getMaxScoringPeriod(long leagueID)
    {
        Query query = namedQuery("maxScoringPeriod").setParameter("leagueID", leagueID);
        List<Integer> scoringPeriods = query.list();

        if (scoringPeriods.get(0) == null)
        {
            return 1;
        }

        return scoringPeriods.get(0);
    }

    public void clearStats(Result result)
    {
        result.getStats().clear();
        currentSession().flush();
    }
}