package com.schmal.service;

import com.schmal.dao.LeagueDAO;
import com.schmal.domain.League;
import com.schmal.util.FantasyURLBuilder;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Slf4j
public class LeagueService
{
    private final LeagueDAO dao;

    public LeagueService(HibernateBundle hibernateBundle)
    {
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

    public League getLeagueByID(long leagueID) throws Exception
    {
        return dao.getLeague(leagueID);
    }

    public League createNewLeague(long fantasyLeagueID, int year, String fantasyService) throws Exception
    {
        URL leagueURL = FantasyURLBuilder.getLeagueURL(fantasyLeagueID, year, fantasyService);

        League league;
        switch (fantasyService.toLowerCase())
        {
            case "espn":
                league = createESPNLeague(leagueURL, fantasyLeagueID, year, fantasyService);
                break;
            default:
                league = null;
                break;
        }

        if (league != null)
        {
            dao.save(league);
        }

        return league;
    }

    private League createESPNLeague(URL leagueURL, long fantasyLeagueID, int year, String fantasyService)
        throws Exception
    {
        Document leagueDoc = Jsoup.connect(leagueURL.toString()).get();

        String leagueName = leagueDoc.select(".league-team-names > h1").first().attr("title");

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