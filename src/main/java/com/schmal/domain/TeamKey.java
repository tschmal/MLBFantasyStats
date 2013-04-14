package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Data;

@Data
@Embeddable
public class TeamKey implements Serializable
{
    @Embedded @JsonUnwrapped
    private final LeagueKey leagueKey;

    @Column(name = "espn_team_id", nullable = false)
    private final int espnTeamID;
}