package com.schmal.entity;

import java.util.List;
import lombok.Data;

@Data
public class League
{
    private final long ID;

    private final long fantasyID;

    private final int year;

    private final String name;

    private List<Team> teams;

    private List<Week> weeks;

    private List<Category> categories;

    private List<Scoring> scoring;

    private List<Player> players;
}