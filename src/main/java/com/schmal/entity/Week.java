package com.schmal.entity;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class Week
{
    private final long ID;

    private final long leagueID;

    private final Date startDate;

    private final Date endDate;

    private List<Matchup> matchups;
}