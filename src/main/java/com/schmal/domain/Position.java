package com.schmal.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "position")
public class Position
{
    @Id
    @GeneratedValue(generator = "position-id-gen")
    @GenericGenerator(name = "position-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private final long ID;

    @Column(name = "player_id", nullable = false)
    private final long playerID;

    @Column(name = "slot", nullable = false)
    private final String slot;

    @Column(name = "eligible_start", nullable = false)
    private final Date eligibleStart;
}