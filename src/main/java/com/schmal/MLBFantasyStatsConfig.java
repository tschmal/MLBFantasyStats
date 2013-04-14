package com.schmal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = false)
public class MLBFantasyStatsConfig extends Configuration
{
    @JsonProperty @NotNull
    private String leagueURL;

    @JsonProperty @NotNull @Valid
    private DatabaseConfiguration database = new DatabaseConfiguration();
}