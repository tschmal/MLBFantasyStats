package com.schmal.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class LeagueKey implements Serializable
{
    @Column(name = "espn_id", nullable = false)
    private final long espnID;

    @Column(name = "year", nullable = false)
    private final int year;
}