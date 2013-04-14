package com.schmal.service;

import com.schmal.domain.FullLeague;
import com.schmal.domain.League;
import com.schmal.domain.Matchup;
import com.schmal.domain.ScoringCategory;
import com.schmal.util.LinkUtil;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ESPNFantasyService
{
    private final LeagueService leagueService;

    private final ScoringCategoryService categoryService;

    private final TeamService teamService;

    private final MatchupService matchupService;

    public ESPNFantasyService(HibernateBundle hibernateBundle)
    {
        leagueService = new LeagueService(hibernateBundle);
        categoryService = new ScoringCategoryService(hibernateBundle);
        teamService = new TeamService(hibernateBundle);
        matchupService = new MatchupService(hibernateBundle);
    }

    public FullLeague saveLeague(String urlString) throws Exception
    {
        FullLeague fullLeague = new FullLeague();

        URL leagueURL = new URL(urlString);
        Document leagueDoc = Jsoup.connect(leagueURL.toString()).get();
        int year = getLeagueYear(leagueDoc);
        long espnID = getEspnID(leagueURL);

        League league = leagueService.saveLeague(leagueDoc, leagueURL, year, espnID);
        fullLeague.setLeague(league);
        fullLeague.setCategories(categoryService.saveScoringCategories(
            league.getKey(), leagueDoc, leagueURL));
        fullLeague.setTeams(teamService.saveTeams(league.getKey(), leagueDoc, leagueURL));

        return fullLeague;
    }

    public List<ScoringCategory> getScoringCategories(String urlString) throws Exception
    {
        URL leagueURL = new URL(urlString);

        Document leagueDoc = Jsoup.connect(leagueURL.toString()).get();

        long espnID = getEspnID(leagueURL);
        int year = getLeagueYear(leagueDoc);

        League league = leagueService.saveLeague(leagueDoc, leagueURL, year, espnID);

        return categoryService.saveScoringCategories(league.getKey(), leagueDoc, leagueURL);

    }

    public List<Matchup> saveAllMatchups(String urlString) throws Exception
    {
        List<Matchup> matchups = getMatchupsToParse(urlString);
        return matchupService.saveMatchups(matchups);
    }

    public List<Matchup> getMatchupsToParse(String urlString) throws Exception
    {
        URL leagueURL = new URL(urlString);

        Document leagueDoc = Jsoup.connect(leagueURL.toString()).get();

        long espnID = getEspnID(leagueURL);
        int year = getLeagueYear(leagueDoc);

        League league = leagueService.saveLeague(leagueDoc, leagueURL, year, espnID);

        return matchupService.getAllMatchups(league.getKey(), leagueDoc, leagueURL);
    }

    private int getLeagueYear(Document leagueDoc) throws Exception
    {
        return Integer.parseInt(
            leagueDoc.select("select#seasonHistoryMenu > option[selected]")
                .first()
                .val());
    }

    private long getEspnID(URL leagueURL) throws Exception
    {
        String espnID = LinkUtil.getLinkParameter(leagueURL, "leagueId");

        return (espnID != null) ? Long.parseLong(espnID) : -1l;
    }
}