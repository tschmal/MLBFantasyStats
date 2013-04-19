package com.schmal.domain;

import java.util.Date;
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
@Table(name = "week")
public class Week
{
    @Id
    @GeneratedValue(generator = "week-id-gen")
    @GenericGenerator(name = "week-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private final long ID;

    @Column(name = "league_id", nullable = false)
    private final long leagueID;

    @Column(name = "start_date", nullable = false)
    private final Date startDate;

    @Column(name = "end_date", nullable = false)
    private final Date endDate;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "week_id")
    private List<Matchup> matchups;
}