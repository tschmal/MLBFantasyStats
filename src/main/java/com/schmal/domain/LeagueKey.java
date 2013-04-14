package com.schmal.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Embeddable
public class LeagueKey implements Serializable
{
    @NonNull @Column(name = "espn_id", nullable = false)
    private long espnID;

    @NonNull @Column(name = "year", nullable = false)
    private int year;
}