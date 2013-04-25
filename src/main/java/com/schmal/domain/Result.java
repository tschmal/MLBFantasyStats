package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "result")
@NamedQueries({
    @NamedQuery(
        name = "latestResult",
        query = "select R from Result R " +
                "where R.date = (select max(date) from Result) " +
                  "and R.team = :team"
    ),
    @NamedQuery(
        name = "latestTwoResults",
        query = "from Result where team = :team and " +
                    "(scoringPeriod = :firstScoringPeriod or scoringPeriod = :secondScoringPeriod)"
    ),
    @NamedQuery(
        name = "allResults",
        query = "from Result where team = :team"
    )
})
public class Result
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "result-id-gen")
    @GenericGenerator(name = "result-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private Team team;

    @NonNull @Getter @Setter
    @Column(name = "date", nullable = false)
    private Date date;

    @NonNull @Getter @Setter
    @Column(name = "scoring_period")
    private int scoringPeriod;

    @Getter @Setter
    @OneToMany(mappedBy = "result", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Stat> stats;
}