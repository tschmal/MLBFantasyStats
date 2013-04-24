package com.schmal.service;

import com.schmal.dao.WeekDAO;
import com.schmal.domain.League;
import com.schmal.domain.Matchup;
import com.schmal.domain.Team;
import com.schmal.domain.Week;
import com.schmal.util.FantasyURLBuilder;
import com.schmal.util.LinkUtil;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class WeekService
{
    private final WeekDAO dao;

    private final LeagueService leagueService;

    private final TeamService teamService;

    private final Pattern weekStartPattern = Pattern.compile("\\((.*?) -");

    private final Pattern weekEndPattern = Pattern.compile(" - (.*?)\\)");

    public WeekService(HibernateBundle hibernateBundle)
    {
        dao = new WeekDAO(hibernateBundle.getSessionFactory());
        teamService = new TeamService(hibernateBundle);
        leagueService = new LeagueService(hibernateBundle);
    }

    public List<Week> createWeeks(long leagueID) throws Exception
    {
        League league = leagueService.getLeagueByID(leagueID);
        URL scheduleURL = FantasyURLBuilder.getScheduleURL(league);

        List<Week> weeks;
        switch (league.getService().toLowerCase())
        {
            case "espn":
                weeks = createESPNWeeks(scheduleURL, league);
                break;
            default:
                weeks = new ArrayList<Week>();
                break;
        }

        return dao.save(league, weeks);
    }

    private List<Week> createESPNWeeks(
        URL scheduleURL,
        League league)
        throws Exception
    {
        List<Week> weeks = new ArrayList<Week>();

        Map<Integer,Team> teamMap = teamService.buildTeamIDMap(league);

        Document scheduleDoc = Jsoup.connect(scheduleURL.toString()).get();
        Elements weekElements = scheduleDoc.select("tr.tableHead");
        for (Element weekElement : weekElements)
        {
            Week week;
            try
            {
                week = buildESPNWeek(weekElement, league);
            }
            catch (IllegalStateException ise)
            {
                // We ran into a header that isn't a week...
                // ... meaning we're done with the weeks. Break!
                break;
            }

            buildESPNWeekMatchups(weekElement, scheduleURL, week, teamMap);
            weeks.add(week);
        }

        return weeks;
    }

    private Week buildESPNWeek(Element week, League league) throws Exception
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

        start = league.getYear() + "-" + startMonth + "-" + startDay;
        end = league.getYear() + "-" + endMonth + "-" + endDay;

        return new Week(league, formatter.parse(start), formatter.parse(end));
    }

    private void buildESPNWeekMatchups(Element weekElement, URL scheduleURL, Week week, Map<Integer,Team> teamMap)
        throws Exception
    {
        List<Matchup> matchups = new ArrayList<Matchup>();

        Element curRow = weekElement.nextElementSibling();
        while (curRow != null)
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

            int homeTeamID = getESPNTeamID(scheduleURL, curRow, true);
            int awayTeamID = getESPNTeamID(scheduleURL, curRow, false);

            matchups.add(new Matchup(week, homeTeamID, awayTeamID));

            curRow = curRow.nextElementSibling();
        }

        week.setMatchups(matchups);
    }

    private int getESPNTeamID(URL scheduleURL, Element week, boolean isHome) throws Exception
    {
        int childIdx = (isHome) ? 0 : 3;
        URL teamURL = new URL(LinkUtil.getDomain(scheduleURL) + week.child(childIdx).select("a").first().attr("href"));
        String teamIDString = LinkUtil.getLinkParameter(teamURL, "teamId");
        return (teamIDString != null) ? Integer.parseInt(teamIDString) : -1;
    }
}