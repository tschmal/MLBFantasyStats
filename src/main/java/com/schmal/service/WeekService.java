package com.schmal.service;

import com.schmal.dao.WeekDAO;
import com.schmal.domain.League;
import com.schmal.domain.Lineup;
import com.schmal.domain.Matchup;
import com.schmal.domain.Player;
import com.schmal.domain.ScoringPeriod;
import com.schmal.domain.Slot;
import com.schmal.domain.Team;
import com.schmal.domain.Week;
import com.schmal.util.DateUtil;
import com.schmal.util.FantasyURLBuilder;
import com.schmal.util.LinkUtil;
import com.schmal.util.PlayerUtil;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
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
public class WeekService
{
    private final WeekDAO dao;

    private final LeagueService leagueService;
    private final TeamService teamService;

    private final PlayerUtil playerUtil = new PlayerUtil();

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

        Document scheduleDoc = Jsoup.connect(scheduleURL.toString()).get();

        int lastPeriodID = 0;
        Elements weekElements = scheduleDoc.select("tr.tableHead");
        for (Element weekElement : weekElements)
        {
            Week week = buildESPNWeek(weekElement, league, lastPeriodID);

            if (week == null)
            {
                break;
            }

            buildESPNWeekMatchups(weekElement, scheduleURL, week);
            weeks.add(week);

            lastPeriodID = week.getEndPeriod().getPeriodID();
        }

        return weeks;
    }

    private Week buildESPNWeek(Element week, League league, int lastPeriodID) throws Exception
    {
        if (week.nextElementSibling().nextElementSibling().children().size() < 6)
        {
            return null;
        }

        String weekString = week.select("td").first().ownText();

        String start, end;

        Matcher matcher = weekStartPattern.matcher(weekString);
        matcher.find();
        start = matcher.group(1);

        matcher = weekEndPattern.matcher(weekString);
        matcher.find();
        end = matcher.group(1);

        Date[] dateRange = DateUtil.getDateRange(start, end, league.getYear());

        // Create scoring period objects (if necessary).

        int curPeriod = lastPeriodID + 1;
        ScoringPeriod startPeriod = null;
        ScoringPeriod endPeriod = null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateRange[0]);
        while (cal.getTime().compareTo(dateRange[1]) <= 0)
        {
            if (league.getScoringPeriodMap().get(curPeriod) == null)
            {
                ScoringPeriod period = new ScoringPeriod(league, curPeriod, cal.getTime());
                league.getScoringPeriods().add(period);
                league.getScoringPeriodMap().put(curPeriod, period);
            }

            if (startPeriod == null)
            {
                startPeriod = league.getScoringPeriodMap().get(curPeriod);
            }
            endPeriod = league.getScoringPeriodMap().get(curPeriod);

            cal.add(Calendar.DATE, 1);
            curPeriod++;
        }

        return new Week(league, startPeriod, endPeriod);
    }

    private int getScoringPeriodEnd(Element weekElement) throws Exception
    {
        Element firstMatchup = weekElement.nextElementSibling().nextElementSibling();
        Element boxLink = firstMatchup.children().last().select("a").first();

        return Integer.parseInt(LinkUtil.getLinkParameter(boxLink.attr("href"), "scoringPeriodId"));
    }

    private void buildESPNWeekMatchups(
        Element weekElement,
        URL scheduleURL,
        Week week)
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
        int childIdx = (isHome) ? 3 : 0;
        URL teamURL = new URL(LinkUtil.getDomain(scheduleURL) + week.child(childIdx).select("a").first().attr("href"));
        String teamIDString = LinkUtil.getLinkParameter(teamURL, "teamId");
        return (teamIDString != null) ? Integer.parseInt(teamIDString) : -1;
    }

    public Week computeScore(long weekID) throws Exception
    {
        Week week = dao.getByID(weekID);

        return computeScore(week, false);
    }

    public Week computeScore(Week week, boolean recalculate) throws Exception
    {
        for (Matchup matchup : week.getMatchups())
        {
            matchup.setHomeTeamScore(
                getScore(
                    week.getLeague().getTeamMap().get(matchup.getHomeTeamID()),
                    week.getStartPeriod(),
                    week.getEndPeriod(),
                    recalculate));

            matchup.setAwayTeamScore(
                getScore(
                    week.getLeague().getTeamMap().get(matchup.getAwayTeamID()),
                    week.getStartPeriod(),
                    week.getEndPeriod(),
                    recalculate));
        }

        return week;
    }

    private float getScore(
        Team team,
        ScoringPeriod startPeriod,
        ScoringPeriod endPeriod,
        boolean recalculate)
    {
        float score = 0f;

        for (int period = startPeriod.getPeriodID(); period <= endPeriod.getPeriodID(); period++)
        {
            ScoringPeriod scoringPeriod = team.getLeague().getScoringPeriodMap().get(period);
            Lineup lineup = team.getLineup(scoringPeriod);

            if (lineup == null)
            {
                continue;
            }

            if (recalculate)
            {
                lineup.calculateScore();
            }

            score += lineup.getScore();
        }

        return score;
    }

    public Week fetchRosters(long weekID, int maxPeriodID) throws Exception
    {
        Week week = dao.getByID(weekID);
        League league = week.getLeague();

        int curPeriod = week.getStartPeriod().getPeriodID();
        while (curPeriod <= week.getEndPeriod().getPeriodID() && curPeriod <= maxPeriodID)
        {
            ScoringPeriod scoringPeriod = league.getScoringPeriodMap().get(curPeriod);
            for (Matchup matchup : week.getMatchups())
            {
                clearLineup(league, scoringPeriod, matchup.getHomeTeamID());
                clearLineup(league, scoringPeriod, matchup.getAwayTeamID());

                URL url = FantasyURLBuilder.getBoxScoreURL(
                    league,
                    curPeriod,
                    league.getTeamMap().get(matchup.getHomeTeamID()).getFantasyTeamID());

                Document boxScoreDoc = Jsoup.connect(url.toString()).timeout(0).get();

                addLineups(league, matchup, scoringPeriod, boxScoreDoc);
            }

            curPeriod++;
        }

        return week;
    }

    private void clearLineup(League league, ScoringPeriod scoringPeriod, int teamID)
    {
        Team team = league.getTeamMap().get(teamID);
        getLineup(team, scoringPeriod).getSlots().clear();
    }

    private void addLineups(
        League league,
        Matchup matchup,
        ScoringPeriod scoringPeriod,
        Document boxScoreDoc) throws Exception
    {
        Team homeTeam = league.getTeamMap().get(matchup.getHomeTeamID());
        Team awayTeam = league.getTeamMap().get(matchup.getAwayTeamID());

        boolean isHome = true;

        Elements playerTables = boxScoreDoc.select(".playerTableTable");
        for (int i = 0; i < playerTables.size(); i++)
        {
            Element playerTable = playerTables.get(i);

            if (i > 0 && !playerTable.hasClass("hideableGroup"))
            {
                isHome = false;
            }

            Team team = (isHome) ? homeTeam : awayTeam;

            Elements playerRows = playerTable.select(".pncPlayerRow");

            for (Element playerRow : playerRows)
            {
                if (playerRow.child(1).select("a").isEmpty())
                {
                    continue;
                }

                Lineup lineup = team.getLineup(scoringPeriod);
                if (lineup == null)
                {
                    lineup = new Lineup(team, scoringPeriod);
                    team.addLineup(lineup);
                }

                Player player = playerUtil.getPlayer(playerRow.child(1), league);
                String position = playerRow.child(0).ownText();
                boolean eligible = !playerRow.hasClass("ineligibleSlot");
                lineup.getSlots().add(new Slot(lineup, player, position, eligible));
            }
        }
    }

    private Lineup getLineup(Team team, ScoringPeriod scoringPeriod)
    {
        Lineup lineup = team.getLineup(scoringPeriod);
        if (lineup == null)
        {
            lineup = new Lineup(team, scoringPeriod);
            lineup.setSlots(new ArrayList<Slot>());
            team.addLineup(lineup);
        }

        return lineup;
    }
}