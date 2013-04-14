package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "league")
@NamedQueries({
    @NamedQuery(
        name = "checkExisting",
        query = "select L from League L where L.key = :key"
    )
})
public class League
{
    @NonNull @EmbeddedId @JsonUnwrapped
    private LeagueKey key;

    @NonNull @Column(name = "name", nullable = false)
    private String name;

    @NonNull @Column(name = "url", nullable = false)
    private String url;
}