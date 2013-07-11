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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"league", "fantasyID"})
@Entity
@Table(name = "player")
public class Player
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "player-id-gen")
    @GenericGenerator(name = "player-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private League league;

    @NonNull @Getter @Setter
    @Column(name = "fantasy_id", nullable = false)
    private long fantasyID;

    @NonNull @Getter @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @NonNull @Getter @Setter
    @Column(name = "eligibility", nullable = false)
    private String eligibility;

    @Getter @Setter
    @OneToMany(mappedBy = "player", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Result> results;

    @Getter @Setter
    @OneToMany(mappedBy = "player", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Projection> projections;

    @Transient @JsonIgnore
    private Map<Integer,Result> resultMap;

    public Result getResult(int scoringPeriod)
    {
        if (resultMap == null)
        {
            createResultMap();
        }

        return resultMap.get(scoringPeriod);
    }

    private void createResultMap()
    {
        resultMap = new HashMap<Integer,Result>();

        for (Result result : getResults())
        {
            resultMap.put(result.getScoringPeriod().getPeriodID(), result);
        }
    }

    public void addResult(Result result)
    {
        results.add(result);
        resultMap.put(result.getScoringPeriod().getPeriodID(), result);
    }
}