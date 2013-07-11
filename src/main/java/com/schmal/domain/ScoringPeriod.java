package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"league", "periodID"})
@Entity
@Table(name = "scoring_period")
public class ScoringPeriod
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "scoring-period-gen")
    @GenericGenerator(name = "scoring-period-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private League league;

    @NonNull @Getter @Setter
    @Column(name = "period_id", nullable = false)
    private int periodID;

    @NonNull @Getter @Setter
    @Column(name = "date", nullable = false)
    private Date date;
}