package com.schmal.service;

import com.schmal.dao.ResultDAO;
import com.schmal.domain.Result;
import com.schmal.domain.Team;
import com.schmal.util.FantasyURLBuilder;
import com.schmal.util.LinkUtil;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Slf4j
public class ResultService
{
    private final ResultDAO dao;

    private final TeamService teamService;

    public ResultService(HibernateBundle hibernateBundle)
    {
        dao = new ResultDAO(hibernateBundle.getSessionFactory());
        teamService = new TeamService(hibernateBundle);
    }

    public List<Result> updateNewResults(long teamID) throws Exception
    {
        List<Result> results = new ArrayList<Result>();

        Team team = teamService.getByTeamID(teamID);
        Result latestResult = dao.getLatestResult(team);

        int scoringPeriod = (latestResult == null) ? 1 : latestResult.getScoringPeriod();
        URL scoringPeriodURL = FantasyURLBuilder.getScoringPeriodURL(team, scoringPeriod);

        // Update/create the latest result just in case.
        if (latestResult == null)
        {
            latestResult = createResult(scoringPeriodURL, team);
            results.add(latestResult);
        }
        else
        {
            List<Result> latestTwoResults = dao.getLatestTwoResults(team, latestResult.getScoringPeriod());
            results.addAll(latestTwoResults);
        }

        // Now do the result of the incomplete days.
        Calendar curDate = Calendar.getInstance();
        curDate.setTime(latestResult.getDate());
        Date today = new Date();
        scoringPeriod++;
        curDate.add(Calendar.DATE, 1);
        while (curDate.getTime().compareTo(today) < 0)
        {
            scoringPeriodURL = FantasyURLBuilder.getScoringPeriodURL(team, scoringPeriod);
            results.add(createResult(scoringPeriodURL, team));
            scoringPeriod++;
            curDate.add(Calendar.DATE, 1);
        }

        return dao.save(results);
    }

    private Result createResult(URL scoringPeriodURL, Team team) throws Exception
    {
        int scoringPeriod = Integer.parseInt(LinkUtil.getLinkParameter(scoringPeriodURL, "scoringPeriodId"));

        Document scoringPeriodDoc = Jsoup.connect(scoringPeriodURL.toString()).get();

        return new Result(team, getScoringDate(scoringPeriodDoc, team), scoringPeriod);
    }

    private Date getScoringDate(Document scoringPeriodDoc, Team team) throws Exception
    {
        int year = team.getLeague().getYear();

        Element dateCell = scoringPeriodDoc.select(".playertableSectionHeader").first().child(2);
        String[] dateComponents = dateCell.ownText().split(" ");

        DateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        return formatter.parse(year + "-" + dateComponents[0] + "-" + dateComponents[1]);
    }
}