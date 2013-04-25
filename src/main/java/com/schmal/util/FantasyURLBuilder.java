package com.schmal.util;

import com.schmal.domain.League;
import com.schmal.domain.Team;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FantasyURLBuilder
{
    private static final Map<String,String> fantasyServiceMap = new HashMap<String,String>();

    static
    {
        fantasyServiceMap.put("espn", "http://games.espn.go.com/flb/leagueoffice?leagueId=%s&seasonId=%s");
        fantasyServiceMap.put("espn:schedule", "http://games.espn.go.com/flb/schedule?leagueId=%s&seasonId=%s");
        fantasyServiceMap.put("espn:settings", "http://games.espn.go.com/flb/leaguesetup/settings?leagueId=%s&seasonId=%s");
        fantasyServiceMap.put("espn:scoringperiod",
                              "http://games.espn.go.com/flb/clubhouse?leagueId=%s&seasonId=%s&teamId=%s&scoringPeriodId=%s");
    }

    public static URL getLeagueURL(League league) throws Exception
    {
        return getLeagueURL(league.getFantasyID(), league.getYear(), league.getService());
    }

    public static URL getLeagueURL(
        long fantasyLeagueID,
        int year,
        String fantasyService)
        throws Exception
    {
        String leagueURL = String.format(
            fantasyServiceMap.get(fantasyService.toLowerCase()),
            String.valueOf(fantasyLeagueID),
            String.valueOf(year));
        return new URL(leagueURL);
    }

    public static URL getScheduleURL(League league) throws Exception
    {
        String scheduleURL = String.format(
            fantasyServiceMap.get(league.getService().toLowerCase() + ":schedule"),
            league.getFantasyID(),
            String.valueOf(league.getYear()));
        return new URL(scheduleURL);
    }

    public static URL getSettingsURL(League league) throws Exception
    {
        String settingsURL = String.format(
            fantasyServiceMap.get(league.getService().toLowerCase() + ":settings"),
            league.getFantasyID(),
            String.valueOf(league.getYear()));
        return new URL(settingsURL);
    }

    public static URL getScoringPeriodURL(Team team, int scoringPeriod) throws Exception
    {
        String scoringPeriodURL = String.format(
            fantasyServiceMap.get(team.getLeague().getService().toLowerCase() + ":scoringperiod"),
            team.getLeague().getFantasyID(),
            String.valueOf(team.getLeague().getYear()),
            String.valueOf(team.getFantasyTeamID()),
            String.valueOf(scoringPeriod));
        return new URL(scoringPeriodURL);
    }
}