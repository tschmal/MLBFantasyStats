package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "matchup")
public class Matchup
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "matchup-id-gen")
    @GenericGenerator(name = "matchup-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private Week week;

    @NonNull @Getter @Setter
    @Column(name = "home_team_id", nullable = false)
    private long homeTeamID;

    @NonNull @Getter @Setter
    @Column(name = "away_team_id", nullable = false)
    private long awayTeamID;
}