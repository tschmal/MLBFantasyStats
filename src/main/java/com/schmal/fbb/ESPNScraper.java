package com.schmal.fbb;

import com.schmal.util.LinkUtil;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ESPNScraper
{
    private static final Pattern weekStartPattern = Pattern.compile("\\((.*?) -");
    private static final Pattern weekEndPattern = Pattern.compile(" - (.*?)\\)");

    private static URL leagueURL = null;

    public static void scrape(String urlString)
    {
        try
        {
            leagueURL = new URL(urlString);

            Document leagueDoc = Jsoup.connect(leagueURL.toString()).get();

            String year = leagueDoc.select("select#seasonHistoryMenu > option[selected]").first().val();

            Document scheduleDoc = getSchedule(leagueDoc);

            Elements weeks = scheduleDoc.select("tr.tableHead");
            for (Element week : weeks)
            {
                Date[] dates = getWeekDateRange(week, year);

                Calendar cal = Calendar.getInstance();
                if (dates != null && cal.getTime().compareTo(dates[0]) > 0)
                {
                    processWeek(week);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("You done fucked up, son. I don't care enough to say specifics, so have a stack trace:");
            e.printStackTrace();
        }
    }

    private static void processWeek(Element week) throws Exception
    {
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

            Document matchupDoc = getBoxScore(curRow);
            System.out.println(matchupDoc.title());

            curRow = curRow.nextElementSibling();
        }
    }

    private static Document getSchedule(Document leagueDoc) throws Exception
    {
        Elements leagueMenuLinks = leagueDoc.select("ul#games-subnav-links > li > a");
        return Jsoup.connect(getDomain() + LinkUtil.getLinkURL(leagueMenuLinks, "Schedule")).get();
    }

    private static Document getBoxScore(Element matchup) throws Exception
    {
        Element boxLink = matchup.select("a").get(4);
        return Jsoup.connect(getDomain() + boxLink.attr("href")).get();

    }

    private static String getDomain()
    {
        return leagueURL.getProtocol() + "://" + leagueURL.getHost();
    }

    private static Date[] getWeekDateRange(Element week, String year)
    {
        String weekString = week.select("td").first().ownText();

        String start, end;
        try
        {
            Matcher matcher = weekStartPattern.matcher(weekString);
            matcher.find();
            start = matcher.group(1);

            matcher = weekEndPattern.matcher(weekString);
            matcher.find();
            end = matcher.group(1);
        }
        catch (IllegalStateException ise)
        {
            return null;
        }

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

        try
        {
            dates[0] = formatter.parse(start);
            dates[1] = formatter.parse(end);
        }
        catch (ParseException pe)
        {
            System.out.println("Fucked a date parse.");
            pe.printStackTrace();
        }

        return dates;
    }
}