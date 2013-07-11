package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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
    @NamedQuery(name = "byLeague", query = "from Team where league = :league"),
    @NamedQuery(name = "byID", query = "from Team where ID = :teamID")
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

    @Getter @Setter @JsonIgnore
    @OneToMany(mappedBy = "team", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Lineup> lineups;

    @Transient @JsonIgnore
    private Map<ScoringPeriod,Lineup> lineupMap;

    public void addLineup(Lineup lineup)
    {
        lineups.add(lineup);

        if (lineupMap == null)
        {
            createLineupMap();
        }

        lineupMap.put(lineup.getScoringPeriod(), lineup);
    }

    public Lineup getLineup(ScoringPeriod period)
    {
        if (lineupMap == null)
        {
            createLineupMap();
        }

        return lineupMap.get(period);
    }

    private void createLineupMap()
    {
        lineupMap = new HashMap<ScoringPeriod,Lineup>();

        for (Lineup lineup : getLineups())
        {
            lineupMap.put(lineup.getScoringPeriod(), lineup);
        }
    }
}