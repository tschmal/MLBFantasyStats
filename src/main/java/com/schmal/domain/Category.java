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
@Table(name = "category")
public class Category
{
    @Id
    @GeneratedValue(generator = "category-id-gen")
    @GenericGenerator(name = "category-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private final long ID;

    @Column(name = "league_id", nullable = false)
    private final long leagueID;

    @Column(name = "category_id", nullable = false)
    private final String category;

    @Column(name = "category_type", nullable = false)
    private final char categoryType;
}