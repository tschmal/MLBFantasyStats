package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "league")
public class League
{
    @EmbeddedId @JsonUnwrapped
    private final LeagueKey key;

    @Column(name = "name", nullable = false)
    private final String name;

    @Column(name = "url", nullable = false)
    private final String url;
}