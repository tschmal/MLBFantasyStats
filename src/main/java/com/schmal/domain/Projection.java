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
@Table(name = "projection")
public class Projection
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "projection-id-gen")
    @GenericGenerator(name = "projection-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private Player player;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private Category category;

    @NonNull @Getter @Setter
    @Column(name = "value", nullable = false)
    private float value;
}