package com.schmal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import lombok.Data;

@Data
public class MLBFantasyStatsConfig extends Configuration
{
    @JsonProperty
    private String leagueURL;
}