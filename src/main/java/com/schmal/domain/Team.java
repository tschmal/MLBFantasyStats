package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "team")
public class Team
{
    @EmbeddedId @JsonUnwrapped
    private final TeamKey key;

    @Column(name = "owner", nullable = false)
    private final String owner;

    @Column(name = "name", nullable = false)
    private final String name;
}