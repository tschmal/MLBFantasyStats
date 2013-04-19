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
@Table(name = "stat")
public class Stat
{
    @Id
    @GeneratedValue(generator = "stat-id-gen")
    @GenericGenerator(name = "stat-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private final long ID;

    @Column(name = "result_id", nullable = false)
    private final long resultID;

    @Column(name = "player_id", nullable = false)
    private final long playerID;

    @Column(name = "category_id", nullable = false)
    private final long categoryID;

    @Column(name = "slot", nullable = false)
    private final String slot;

    @Column(name = "value", nullable = false)
    private final float value;
}