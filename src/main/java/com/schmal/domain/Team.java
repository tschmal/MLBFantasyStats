package com.schmal.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "team")
public class Team
{
    @Id
    @GeneratedValue(generator = "team-id-gen")
    @GenericGenerator(name = "team-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private final long ID;

    @Column(name = "league_id", nullable = false)
    private final long leagueID;

    @Column(name = "fantasy_team_id", nullable = false)
    private final long fantasyTeamID;

    @Column(name = "name", nullable = false)
    private final String name;

    @Column(name = "owner", nullable = false)
    private final String owner;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private List<Result> results;
}