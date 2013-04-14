package com.schmal.service;

import com.schmal.dao.MatchupDAO;
import com.schmal.domain.LeagueKey;
import com.schmal.domain.Matchup;
import com.schmal.domain.MatchupKey;
import com.schmal.util.LinkUtil;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class MatchupService
{
    private final MatchupDAO dao;
    private final Pattern weekStartPattern = Pattern.compile("\\((.*?) -");
    private final Pattern weekEndPattern = Pattern.compile(" - (.*?)\\)");

    public MatchupService(HibernateBundle hibernateBundle)
    {
        dao = new MatchupDAO(hibernateBundle.getSessionFactory());
    }

    public List<Matchup> saveMatchups(List<Matchup> matchups) throws Exception
    {
        return dao.save(matchups);
    }

    public List<Matchup> getAllMatchups(
        LeagueKey leagueKey,
        Document leagueDoc,
        URL leagueURL) throws Exception
    {
        List<Matchup> matchups = new ArrayList<Matchup>();

        Elements leagueMenuLinks = leagueDoc.select("ul#games-subnav-links > li > a");
        Document scheduleDoc = Jsoup.connect(LinkUtil.getLinkURL(leagueURL, leagueMenuLinks, "Schedule")).get();

        Elements weeks = scheduleDoc.select("tr.tableHead");
        for (Element week : weeks)
        {
            Date[] dates = getWeekDateRange(week, leagueKey.getYear());

            Calendar cal = Calendar.getInstance();
            if (cal.getTime().compareTo(dates[0]) > 0)
            {
                matchups.addAll(processWeek(leagueKey, leagueURL, week, dates[0], dates[1]));
            }
            else
            {
                break;
            }
        }

        return matchups;
    }

    private List<Matchup> processWeek(
        LeagueKey leagueKey,
        URL leagueURL,
        Element week,
        Date startDate,
        Date endDate) throws Exception
    {
        List<Matchup> matchups = new ArrayList<Matchup>();

        Element curRow = week.nextElementSibling();
        while (true)
        {
            if (curRow.hasClass("tableHead"))
            {
                break;
            }

            if (curRow.select("td").size() < 6)
            {
                curRow = curRow.nextElementSibling();
                continue;
            }

            int homeTeamID = getEspnTeamID(leagueURL, curRow, true);
            int awayTeamID = getEspnTeamID(leagueURL, curRow, false);

            matchups.add(new Matchup(new MatchupKey(leagueKey, homeTeamID, startDate), awayTeamID, endDate));

            curRow = curRow.nextElementSibling();
        }

        return matchups;
    }

    private int getEspnTeamID(URL leagueURL, Element week, boolean isHome) throws Exception
    {
        int childIdx = (isHome) ? 0 : 3;
        URL teamURL = new URL(LinkUtil.getDomain(leagueURL) + week.child(childIdx).select("a").first().attr("href"));
        String teamIDString = LinkUtil.getLinkParameter(teamURL, "teamId");
        return (teamIDString != null) ? Integer.parseInt(teamIDString) : -1;
    }

    private Date[] getWeekDateRange(Element week, int year) throws Exception
    {
        String weekString = week.select("td").first().ownText();

        String start, end;

        Matcher matcher = weekStartPattern.matcher(weekString);
        matcher.find();
        start = matcher.group(1);

        matcher = weekEndPattern.matcher(weekString);
        matcher.find();
        end = matcher.group(1);

        DateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");

        String[] startStuff = start.split(" ");
        String startMonth = startStuff[0];
        String startDay = startStuff[1];

        String[] endStuff = end.split(" ");
        String endMonth = (endStuff.length > 1) ? endStuff[0] : startMonth;
        String endDay = (endStuff.length > 1) ? endStuff[1] : endStuff[0];

        start = year + "-" + startMonth + "-" + startDay;
        end = year + "-" + endMonth + "-" + endDay;

        Date[] dates = new Date[2];

        dates[0] = formatter.parse(start);
        dates[1] = formatter.parse(end);

        return dates;
    }
}