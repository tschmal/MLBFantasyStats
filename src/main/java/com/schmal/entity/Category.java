package com.schmal.entity;

import lombok.Data;

@Data
public class Category
{
    private final long ID;

    private final long leagueID;

    private final String category;

    private final char categoryType;
}