package com.schmal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "league")
@NamedQueries({
    @NamedQuery(
        name = "checkExisting",
        query = "select L from League L where L.espnID = :espnID and L.year = :year"
    )
})
public class League
{
    @Id
    @GeneratedValue(generator = "league-id-gen")
    @GenericGenerator(name = "league-id-gen", strategy="increment")
    @Column(name = "id", nullable = false)
    @Getter @Setter
    private long ID;

    @Column(name = "espn_id", nullable = false)
    @Getter @Setter @NonNull
    private long espnID;

    @Column(name = "name", nullable = false)
    @Getter @Setter @NonNull
    private String name;

    @Column(name = "year", nullable = false)
    @Getter @Setter @NonNull
    private int year;

    @Column(name = "url", nullable = false)
    @Getter @Setter @NonNull
    private String url;
}