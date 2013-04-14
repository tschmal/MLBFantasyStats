package com.schmal.domain;

import java.util.List;
import lombok.Data;

@Data
public class FullLeague
{
    private League league;

    private List<ScoringCategory> categories;

    private List<Team> teams;
}