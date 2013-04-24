package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
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
@Table(name = "category")
public class Category
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "category-id-gen")
    @GenericGenerator(name = "category-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private League league;

    @NonNull @Getter @Setter
    @Column(name = "category", nullable = false)
    private String category;

    @NonNull @Getter @Setter
    @Column(name = "category_type", nullable = false)
    private char categoryType;

    @NonNull @Getter @Setter
    @Column(name = "points")
    private float points;
}