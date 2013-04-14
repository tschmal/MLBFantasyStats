package com.schmal.service;

import com.schmal.dao.LeagueDAO;
import com.schmal.dao.ScoringCategoryDAO;
import com.schmal.domain.FullLeague;
import com.schmal.domain.League;
import com.schmal.domain.LeagueKey;
import com.schmal.domain.ScoringCategory;
import com.schmal.domain.ScoringCategoryKey;
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

    public ESPNFantasyService(HibernateBundle hibernateBundle)
    {
        sessionFactory = hibernateBundle.getSessionFactory();
        leagueDAO = new LeagueDAO(sessionFactory);
        categoryDAO = new ScoringCategoryDAO(sessionFactory);
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

        List<ScoringCategory> categories = getScoringCategories(league, leagueDoc, leagueURL);
        categoryDAO.save(categories);
        fullLeague.setCategories(categories);

        return fullLeague;
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
        long espnID = -1l;
        for (String param : leagueURL.getQuery().split("&"))
        {
            String name = param.split("=")[0];
            if ("leagueId".equals(name))
            {
                espnID = Long.parseLong(param.split("=")[1]);
            }
        }

        return espnID;
    }

    private List<ScoringCategory> getScoringCategories(
        League league,
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
                    new ScoringCategoryKey(league.getKey(), categoryName, type), categoryPoints);
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

        return getScoringCategories(league, leagueDoc, leagueURL);

    }

    private String getDomain(URL url)
    {
        return url.getProtocol() + "://" + url.getHost();
    }
}