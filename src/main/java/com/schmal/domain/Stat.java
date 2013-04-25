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
@Table(name = "stat")
public class Stat
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "stat-id-gen")
    @GenericGenerator(name = "stat-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private Result result;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private Player player;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private Category category;

    @NonNull @Getter @Setter
    @Column(name = "slot", nullable = false)
    private String slot;

    @NonNull @Getter @Setter
    @Column(name = "value", nullable = false)
    private float value;
}