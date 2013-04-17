package com.schmal.entity;

import java.util.List;
import lombok.Data;

@Data
public class Team
{
    private final long ID;

    private final long leagueID;

    private final long fantasyTeamID;

    private final String name;

    private final String owner;

    private List<Result> results;
}