package com.schmal.service;

import com.schmal.dao.TeamDAO;
import com.schmal.domain.League;
import com.schmal.domain.Team;
import com.schmal.util.FantasyURLBuilder;
import com.schmal.util.LinkUtil;
import com.yammer.dropwizard.hibernate.HibernateBundle;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class TeamService
{
    private final TeamDAO dao;

    private final LeagueService leagueService;

    public TeamService(HibernateBundle hibernateBundle)
    {
        dao = new TeamDAO(hibernateBundle.getSessionFactory());
        leagueService = new LeagueService(hibernateBundle);
    }

    public List<Team> createTeams(long leagueID) throws Exception
    {
        League league = leagueService.getLeagueByID(leagueID);
        URL leagueURL = FantasyURLBuilder.getLeagueURL(league);

        Map<Integer,Team> teamIDMap = buildTeamIDMap(league);

        List<Team> teams;
        switch (league.getService().toLowerCase())
        {
            case "espn":
                teams = createESPNTeams(leagueURL, league, teamIDMap);
                break;
            default:
                teams = new ArrayList<Team>();
                break;
        }

        if (teams.size() > 0)
        {
            dao.save(teams);
        }

        return teams;
    }

    public Map<Integer,Team> buildTeamIDMap(League league) throws Exception
    {
        Map<Integer,Team> teamIDMap = new HashMap<Integer,Team>();

        List<Team> teams = dao.getTeamsByLeagueID(league);
        for (Team team : teams)
        {
            teamIDMap.put(team.getFantasyTeamID(), team);
        }

        return teamIDMap;
    }

    private List<Team> createESPNTeams(
        URL leagueURL,
        League league,
        Map<Integer,Team> teamIDMap)
        throws Exception
    {
        List<Team> teams = new ArrayList<Team>();

        Document leagueDoc = Jsoup.connect(leagueURL.toString()).get();

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

                Team newTeam = null;
                Team existingTeam = teamIDMap.get(teamID);
                if (existingTeam == null)
                {
                    newTeam = new Team(league, teamID, name, owner);
                }
                else
                {
                    newTeam = existingTeam;
                    newTeam.setName(name);
                    newTeam.setOwner(owner);
                }

                teams.add(newTeam);

                curTeamRow = curTeamRow.nextElementSibling();
                member = curTeamRow.select("a");
            }
        }

        return teams;
    }
}