package com.schmal.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.NonNull;

@Data
@Embeddable
public class ScoringCategoryKey implements Serializable
{
    @NonNull @Column(name = "league_id", nullable = false)
    private long leagueID;

    @NonNull @Column(name = "category", nullable = false)
    private String category;

    @NonNull @Column(name = "category_type", nullable = false)
    private final char type;
}