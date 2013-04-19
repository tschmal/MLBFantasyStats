package com.schmal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "matchup")
public class Matchup
{
    @Id
    @GeneratedValue(generator = "matchup-id-gen")
    @GenericGenerator(name = "matchup-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private final long ID;

    @Column(name = "week_id", nullable = false)
    private final long weekID;

    @Column(name = "home_team_id", nullable = false)
    private final long homeTeamID;

    @Column(name = "away_team_id", nullable = false)
    private final long awayTeamID;
}