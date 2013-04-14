package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Data;

@Data
@Embeddable
public class ScoringCategoryKey implements Serializable
{
    @Embedded @JsonUnwrapped
    private final LeagueKey leagueKey;

    @Column(name = "category", nullable = false)
    private final String category;

    @Column(name = "category_type", nullable = false)
    private final char type;
}