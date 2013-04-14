package com.schmal.service;

import com.schmal.dao.LeagueDAO;
import com.schmal.domain.League;
import com.schmal.domain.LeagueKey;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import org.jsoup.nodes.Document;

public class LeagueService
{
    private final LeagueDAO leagueDAO;

    public LeagueService(HibernateBundle hibernateBundle)
    {
        leagueDAO = new LeagueDAO(hibernateBundle.getSessionFactory());
    }

    public League saveLeague(Document leagueDoc, URL leagueURL, int year, long espnID) throws Exception
    {
        String leagueName = leagueDoc.select("#lo-league-header > .league-team-names > h1").first().ownText();

        League league = new League(new LeagueKey(espnID, year), leagueName, leagueURL.toString());
        return leagueDAO.save(league);
    }
}