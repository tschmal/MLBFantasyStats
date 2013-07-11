package com.schmal.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Luck
{
    public Luck(Team team)
    {
        this.team = team;
        this.scores = new ArrayList<Float>();
        this.opponentScores = new ArrayList<Float>();
        this.opponentAverages = new ArrayList<Float>();
    }

    private final Team team;

    private final List<Float> scores;

    private final List<Float> opponentScores;

    private final List<Float> opponentAverages;

    private float average;

    private float luck;

    public void addScores(Matchup matchup)
    {
        if (matchup.getHomeTeamID() == team.getFantasyTeamID())
        {
            scores.add(matchup.getHomeTeamScore());
            opponentScores.add(matchup.getAwayTeamScore());
        }
        else
        {
            scores.add(matchup.getAwayTeamScore());
            opponentScores.add(matchup.getHomeTeamScore());
        }
    }
}