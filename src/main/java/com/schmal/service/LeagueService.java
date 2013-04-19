package com.schmal.service;

import com.schmal.dao.LeagueDAO;
import com.schmal.domain.League;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Slf4j
public class LeagueService
{
    private final Map<String,String> fantasyServiceMap;

    private final LeagueDAO dao;

    public LeagueService(HibernateBundle hibernateBundle)
    {
        fantasyServiceMap = new HashMap<String,String>();
        fantasyServiceMap.put("espn", "http://games.espn.go.com/flb/leagueoffice?leagueId=%s&seasonId=%s");

        dao = new LeagueDAO(hibernateBundle.getSessionFactory());
    }

    public List<League> getLeaguesByService(String fantasyService) throws Exception
    {
        return dao.getLeaguesByService(fantasyService);
    }

    public List<League> getLeaguesByFantasyID(long fantasyID) throws Exception
    {
        return dao.getLeaguesByFantasyID(fantasyID);
    }

    public League createNewLeague(long fantasyLeagueID, int year, String fantasyService) throws Exception
    {
        URL leagueURL = getLeagueURL(fantasyLeagueID, year, fantasyService);
        Document leagueDoc = Jsoup.connect(leagueURL.toString()).get();

        String leagueName = leagueDoc.select(".league-team-names > h1").first().attr("title");
        League league = getLeague(fantasyLeagueID, year, leagueName, fantasyService);

        return dao.save(league);
    }

    private URL getLeagueURL(long fantasyLeagueID, int year, String fantasyService) throws Exception
    {
        String leagueURL = String.format(
            fantasyServiceMap.get(fantasyService.toLowerCase()),
            String.valueOf(fantasyLeagueID),
            String.valueOf(year));
        return new URL(leagueURL);
    }

    private League getLeague(long fantasyLeagueID, int year, String leagueName, String fantasyService) throws Exception
    {
        League league = dao.getLeague(fantasyLeagueID, year);
        if (league == null)
        {
            league = new League(fantasyLeagueID, year, leagueName, fantasyService);
        }
        else
        {
            league.setName(leagueName);
        }

        return league;
    }
}