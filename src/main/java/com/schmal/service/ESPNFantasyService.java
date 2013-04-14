package com.schmal.service;

import com.schmal.dao.LeagueDAO;
import com.schmal.dao.ScoringCategoryDAO;
import com.schmal.dao.TeamDAO;
import com.schmal.domain.FullLeague;
import com.schmal.domain.League;
import com.schmal.domain.LeagueKey;
import com.schmal.domain.ScoringCategory;
import com.schmal.domain.ScoringCategoryKey;
import com.schmal.domain.Team;
import com.schmal.domain.TeamKey;
import com.schmal.util.LinkUtil;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class ESPNFantasyService
{
    private final SessionFactory sessionFactory;

    private final LeagueDAO leagueDAO;

    private final ScoringCategoryDAO categoryDAO;

    private final TeamDAO teamDAO;

    public ESPNFantasyService(HibernateBundle hibernateBundle)
    {
        sessionFactory = hibernateBundle.getSessionFactory();
        leagueDAO = new LeagueDAO(sessionFactory);
        categoryDAO = new ScoringCategoryDAO(sessionFactory);
        teamDAO = new TeamDAO(sessionFactory);
    }

    public FullLeague saveLeague(String urlString) throws Exception
    {
        FullLeague fullLeague = new FullLeague();

        URL leagueURL = new URL(urlString);

        Document leagueDoc = Jsoup.connect(leagueURL.toString()).get();
        String leagueName = leagueDoc.select("#lo-league-header > .league-team-names > h1")
            .first()
            .ownText();
        int year = getLeagueYear(leagueDoc);
        long espnID = getEspnID(leagueURL);

        League league = new League(new LeagueKey(espnID, year), leagueName, urlString);
        leagueDAO.save(league);
        fullLeague.setLeague(league);

        List<ScoringCategory> categories = getScoringCategories(league.getKey(), leagueDoc, leagueURL);
        categoryDAO.save(categories);
        fullLeague.setCategories(categories);

        List<Team> teams = getTeams(league.getKey(), leagueDoc, leagueURL);
        teamDAO.save(teams);
        fullLeague.setTeams(teams);

        return fullLeague;
    }

    private List<ScoringCategory> getScoringCategories(
        LeagueKey leagueKey,
        Document leagueDoc,
        URL leagueURL) throws Exception
    {
        List<ScoringCategory> categories = new ArrayList<ScoringCategory>();
        Pattern categoryPattern = Pattern.compile("\\((.*?)\\)");

        Elements leagueMenuLinks = leagueDoc.select("ul#games-subnav-links > li > a");
        Document settingsDoc = Jsoup.connect(
            getDomain(leagueURL) + LinkUtil.getLinkURL(leagueMenuLinks, "Settings")).get();

        Elements categoryTypes = settingsDoc.select(".categoryName");
        for (Element categoryType : categoryTypes)
        {
            char type = categoryType.ownText().charAt(0);

            Elements statNames = categoryType.parent().select("td.statName");
            for (Element statName : statNames)
            {
                Matcher matcher = categoryPattern.matcher(statName.ownText());
                matcher.find();
                String categoryName = matcher.group(1);

                Element statPoints = statName.nextElementSibling();
                float categoryPoints = Float.parseFloat(statPoints.ownText());

                ScoringCategory newCategory = new ScoringCategory(
                    new ScoringCategoryKey(leagueKey, categoryName, type), categoryPoints);
                categories.add(newCategory);
            }
        }

        return categories;
    }

    public List<ScoringCategory> getScoringCategories(String urlString) throws Exception
    {
        URL leagueURL = new URL(urlString);
        Document leagueDoc = Jsoup.connect(leagueURL.toString()).get();

        long espnID = getEspnID(leagueURL);
        int year = getLeagueYear(leagueDoc);

        League league = leagueDAO.getLeague(espnID, year);

        return getScoringCategories(league.getKey(), leagueDoc, leagueURL);

    }

    private List<Team> getTeams(
        LeagueKey leagueKey,
        Document leagueDoc,
        URL leagueURL) throws Exception
    {
        List<Team> teams = new ArrayList<Team>();

        Elements divisionCells = leagueDoc.select(".division-name");
        for (Element divisionCell : divisionCells)
        {
            Element curTeamRow = divisionCell.parent().nextElementSibling();
            Elements member = curTeamRow.select("a");
            while (!member.isEmpty())
            {
                String teamFull = member.first().attr("title");
                int ownerStartIdx = teamFull.lastIndexOf('(');
                int ownerEndIdx = teamFull.lastIndexOf(')');

                String name = teamFull.substring(0, ownerStartIdx - 1);
                String owner = teamFull.substring(ownerStartIdx + 1, ownerEndIdx);

                URL teamURL = new URL(getDomain(leagueURL) + member.first().attr("href"));

                teams.add(new Team(
                    new TeamKey(leagueKey, getEspnTeamID(teamURL)), owner, name));

                curTeamRow = curTeamRow.nextElementSibling();
                member = curTeamRow.select("a");
            }
        }

        return teams;
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

    private int getEspnTeamID(URL teamURL) throws Exception
    {
        String teamID = LinkUtil.getLinkParameter(teamURL, "teamId");

        return (teamID != null) ? Integer.parseInt(teamID) : -1;
    }

    private String getDomain(URL url)
    {
        return url.getProtocol() + "://" + url.getHost();
    }
}