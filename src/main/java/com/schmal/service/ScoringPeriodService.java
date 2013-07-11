package com.schmal.service;

import com.schmal.dao.LeagueDAO;
import com.schmal.domain.League;
import com.schmal.domain.ScoringPeriod;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScoringPeriodService
{
    private final LeagueDAO leagueDAO;

    public ScoringPeriodService(HibernateBundle hibernateBundle)
    {
        leagueDAO = new LeagueDAO(hibernateBundle.getSessionFactory());
    }

    public ScoringPeriod getMaxScoringPeriod(long leagueID) throws Exception
    {
        League league = leagueDAO.getLeague(leagueID);

        List<ScoringPeriod> periods = league.getScoringPeriods();
        if (periods.isEmpty())
        {
            return null;
        }

        ScoringPeriod latestPeriod = periods.get(0);
        Date today = new Date();
        for (ScoringPeriod period : periods)
        {
            if (period.getDate().compareTo(today) > 0)
            {
                break;
            }

            latestPeriod = period;
        }

        return latestPeriod;
    }
}