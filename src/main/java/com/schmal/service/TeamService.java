package com.schmal.service;

import com.schmal.dao.TeamDAO;
import com.schmal.domain.LeagueKey;
import com.schmal.domain.Team;
import com.schmal.domain.TeamKey;
import com.schmal.util.LinkUtil;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TeamService
{
    private final TeamDAO teamDAO;

    public TeamService(HibernateBundle hibernateBundle)
    {
        teamDAO = new TeamDAO(hibernateBundle.getSessionFactory());
    }

    public List<Team> saveTeams(
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

                URL teamURL = new URL(LinkUtil.getDomain(leagueURL) + member.first().attr("href"));
                String teamIDString = LinkUtil.getLinkParameter(teamURL, "teamId");
                int teamID = (teamIDString != null) ? Integer.parseInt(teamIDString) : -1;

                teams.add(new Team(new TeamKey(leagueKey, teamID), owner, name));

                curTeamRow = curTeamRow.nextElementSibling();
                member = curTeamRow.select("a");
            }
        }

        return teamDAO.save(teams);
    }
}