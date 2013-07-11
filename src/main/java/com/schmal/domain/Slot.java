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
@Table(name = "slot")
public class Slot
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "slot-id-gen")
    @GenericGenerator(name = "slot-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private Lineup lineup;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private Player player;

    @NonNull @Getter @Setter
    @Column(name = "position", nullable = false)
    private String position;

    @NonNull @Getter @Setter
    @Column(name = "eligible", nullable = false)
    private boolean eligible;
}