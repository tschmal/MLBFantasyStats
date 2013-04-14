package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Data;

@Data
@Embeddable
public class MatchupKey implements Serializable
{
    @Embedded @JsonUnwrapped
    private final LeagueKey leagueKey;

    @Column(name = "home_espn_team_id", nullable = false)
    private final int homeEspnTeamID;

    @Column(name = "start_date", nullable = false)
    private final Date startDate;
}