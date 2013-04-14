package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "matchup")
public class Matchup
{
    @EmbeddedId @JsonUnwrapped
    private final MatchupKey key;

    @Column(name = "away_espn_team_id", nullable = false)
    private final int awayEspnTeamID;

    @Column(name = "end_date", nullable = false)
    private final Date endDate;
}