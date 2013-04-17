package com.schmal.entity;

import java.util.List;
import lombok.Data;

@Data
public class Player
{
    private final long ID;

    private final long leagueID;

    private final String name;

    private List<Position> positions;

    private List<Projection> projections;
}