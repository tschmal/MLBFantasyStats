package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "scoring")
public class ScoringCategory
{
    @EmbeddedId @JsonUnwrapped
    private final ScoringCategoryKey key;

    @Column(name = "points", nullable = false)
    private final float points;
}