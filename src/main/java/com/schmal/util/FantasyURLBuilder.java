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
}