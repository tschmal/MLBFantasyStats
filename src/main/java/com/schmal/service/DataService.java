package com.schmal.service;

import com.schmal.domain.Matchup;
import com.schmal.domain.League;
import com.schmal.domain.Luck;
import com.schmal.domain.TeamWeek;
import com.schmal.domain.Week;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataService
{
    private final LeagueService leagueService;
    private final WeekService weekService;

    public DataService(HibernateBundle hibernateBundle)
    {
        leagueService = new LeagueService(hibernateBundle);
        weekService = new WeekService(hibernateBundle);
    }

    public TeamWeek getTeamWeekScores(long leagueID) throws Exception
    {
        League league = leagueService.getLeagueByID(leagueID);

        TeamWeek teamWeek = new TeamWeek(
            league.getTeams(),
            league.getWeeks(),
            new HashMap<Long,Map<Long,Float>>());

        Date today = new Date();
        for (Week week : league.getWeeks())
        {
            if (today.compareTo(week.getStartPeriod().getDate()) < 0)
            {
                break;
            }

            weekService.computeScore(week, false);

            for (Matchup matchup : week.getMatchups())
            {
                teamWeek.addTeamScore(
                    league.getTeamMap().get(matchup.getHomeTeamID()), week, matchup.getHomeTeamScore());
                teamWeek.addTeamScore(
                    league.getTeamMap().get(matchup.getAwayTeamID()), week, matchup.getAwayTeamScore());
            }
        }

        return teamWeek;
    }

    public List<Luck> getLuck(long leagueID, Long maxWeekID) throws Exception
    {
        Map<Integer,Luck> luckMap = new HashMap<Integer,Luck>();

        League league = leagueService.getLeagueByID(leagueID);

        Date today = new Date();
        for (Week week : league.getWeeks())
        {
            if (today.compareTo(week.getEndPeriod().getDate()) < 0 ||
                (maxWeekID != null && week.getID() > maxWeekID))
            {
                break;
            }

            weekService.computeScore(week, false);

            for (Matchup matchup : week.getMatchups())
            {
                addLuckScores(luckMap, league, matchup, matchup.getHomeTeamID());
                addLuckScores(luckMap, league, matchup, matchup.getAwayTeamID());
            }
        }

        // Compute team averages.
        for (Luck luck : luckMap.values())
        {
            float totalScore = 0f;
            for (float score : luck.getScores())
            {
                totalScore += score;
            }

            luck.setAverage(totalScore / luck.getScores().size());
        }

        // Set matchup averages.
        for (Week week : league.getWeeks())
        {
            if (today.compareTo(week.getStartPeriod().getDate()) < 0 ||
                (maxWeekID != null && week.getID() > maxWeekID))
            {
                break;
            }

            for (Matchup matchup : week.getMatchups())
            {
                Luck luck = luckMap.get(matchup.getHomeTeamID());
                Luck opponentLuck = luckMap.get(matchup.getAwayTeamID());

                luck.getOpponentAverages().add(opponentLuck.getAverage());
                opponentLuck.getOpponentAverages().add(luck.getAverage());
            }
        }

        // Finally, calculate the luck and add it to the final list.
        List<Luck> luckList = new ArrayList<Luck>();
        for (Luck luck : luckMap.values())
        {
            float diffTotal = 0f;
            for (int i = 0; i < luck.getOpponentScores().size(); i++)
            {
                diffTotal += luck.getOpponentScores().get(i) - luck.getOpponentAverages().get(i);
            }

            luck.setLuck(diffTotal / luck.getOpponentScores().size());
            luckList.add(luck);
        }

        return luckList;
    }

    private void addLuckScores(
        Map<Integer,Luck> luckMap,
        League league,
        Matchup matchup,
        int teamID)
    {
        Luck luck = luckMap.get(teamID);
        if (luck == null)
        {
            luck = new Luck(league.getTeamMap().get(teamID));
            luckMap.put(teamID, luck);
        }

        luck.addScores(matchup);
    }
}