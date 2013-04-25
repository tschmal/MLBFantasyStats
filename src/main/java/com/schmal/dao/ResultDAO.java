package com.schmal.dao;

import com.schmal.domain.Result;
import com.schmal.domain.Team;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
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

    public Result getLatestResult(Team team)
    {
        return uniqueResult(namedQuery("latestResult").setParameter("team", team));
    }

    public List<Result> getLatestTwoResults(Team team, int scoringPeriod)
    {
        int secondScoringPeriod = (scoringPeriod > 1) ? scoringPeriod - 1 : scoringPeriod;
        return list(namedQuery("latestTwoResults")
            .setParameter("team", team)
            .setParameter("firstScoringPeriod", scoringPeriod)
            .setParameter("secondScoringPeriod", secondScoringPeriod));
    }

    public List<Result> getAllResults(Team team)
    {
        return list(namedQuery("allResults").setParameter("team", team));
    }
}