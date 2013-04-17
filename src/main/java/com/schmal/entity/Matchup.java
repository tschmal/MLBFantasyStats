package com.schmal.entity;

import lombok.Data;

@Data
public class Matchup
{
    private final long ID;

    private final long weekID;

    private final long homeTeamID;

    private final long awayTeamID;
}