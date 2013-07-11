package com.schmal.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class TeamWeek
{
    private final List<Team> teams;

    private final List<Week> weeks;

    private final Map<Long,Map<Long,Float>> teamScores;

    public void addTeamScore(Team team, Week week, float score)
    {
        Map<Long,Float> weekMap = teamScores.get(team.getID());
        if (weekMap == null)
        {
            weekMap = new HashMap<Long,Float>();
            teamScores.put(team.getID(), weekMap);
        }

        weekMap.put(week.getID(), score);
    }
}