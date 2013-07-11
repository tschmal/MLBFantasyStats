package com.schmal.util;

import com.schmal.domain.League;
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
                              "http://games.espn.go.com/flb/leaders?leagueId=%s&seasonId=%s&scoringPeriodId=%s" +
                                                                  "&slotCategoryGroup=%s&startIndex=%s&avail=-1");
        fantasyServiceMap.put("espn:boxscore",
                              "http://games.espn.go.com/flb/boxscorequick?leagueId=%s&seasonId=%s&scoringPeriodId=%s" +
                                                                  "&teamId=%s&view=scoringperiod");
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

    public static URL getScoringPeriodURL(
        League league,
        int scoringPeriod,
        int categoryGroup,
        int startIndex) throws Exception
    {
        String scoringPeriodURL = String.format(
            fantasyServiceMap.get(league.getService().toLowerCase() + ":scoringperiod"),
            String.valueOf(league.getFantasyID()),
            String.valueOf(league.getYear()),
            String.valueOf(scoringPeriod),
            String.valueOf(categoryGroup),
            String.valueOf(startIndex));
        return new URL(scoringPeriodURL);
    }

    public static URL getBoxScoreURL(
        League league,
        int scoringPeriod,
        int fantasyTeamID) throws Exception
    {
        String boxScoreURL = String.format(
            fantasyServiceMap.get(league.getService().toLowerCase() + ":boxscore"),
            String.valueOf(league.getFantasyID()),
            String.valueOf(league.getYear()),
            String.valueOf(scoringPeriod),
            String.valueOf(fantasyTeamID));
        return new URL(boxScoreURL);
    }
}