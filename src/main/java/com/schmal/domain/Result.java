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
@Table(name = "result")
public class Result
{
    @Id
    @GeneratedValue(generator = "result-id-gen")
    @GenericGenerator(name = "result-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private final long ID;

    @Column(name = "team_id", nullable = false)
    private final long teamID;

    @Column(name = "date", nullable = false)
    private final Date date;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "result_id")
    private List<Stat> stats;
}