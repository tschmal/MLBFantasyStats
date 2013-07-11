package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@EqualsAndHashCode(of = {"league", "category", "categoryType"})
@Entity
@Table(name = "category")
@NamedQueries({
    @NamedQuery(name = "allCategories", query = "from Category"),
    @NamedQuery(name = "singleCategory", query = "from Category where league = :league and " +
                                                 "category = :category and categoryType = :categoryType")
})
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

    public String logicalKey()
    {
        return categoryType + ":" + category;
    }
}