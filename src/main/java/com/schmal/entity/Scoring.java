package com.schmal.entity;

import lombok.Data;

@Data
public class Scoring
{
    private final long ID;

    private final long leagueID;

    private final long categoryID;

    private final float value;
}