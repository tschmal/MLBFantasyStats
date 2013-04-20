package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "team")
@NamedQueries({
    @NamedQuery(
        name = "getByLeagueID",
        query = "select T from Team T where T.league = :league"
    )
})
public class Team
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "team-id-gen")
    @GenericGenerator(name = "team-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private League league;

    @NonNull @Getter @Setter
    @Column(name = "fantasy_team_id", nullable = false)
    private int fantasyTeamID;

    @NonNull @Getter @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @NonNull @Getter @Setter
    @Column(name = "owner", nullable = false)
    private String owner;

    @Getter @Setter
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private List<Result> results;
}