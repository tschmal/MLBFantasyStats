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
@Table(name = "projection")
public class Projection
{
    @Id
    @GeneratedValue(generator = "projection-id-gen")
    @GenericGenerator(name = "projection-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private final long ID;

    @Column(name = "player_id", nullable = false)
    private final long playerID;

    @Column(name = "category_id", nullable = false)
    private final long categoryID;

    @Column(name = "value", nullable = false)
    private final float value;
}