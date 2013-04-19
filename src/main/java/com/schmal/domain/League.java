package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
        query = "select L from League L where L.fantasyID = :fantasyID and L.year = :year"
    ),
    @NamedQuery(
        name = "getByService",
        query = "select L from League L where L.service = :service"
    ),
    @NamedQuery(
        name = "getByFantasyID",
        query = "select L from League L where L.fantasyID = :fantasyID"
    )
})
public class League
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "league-id-gen")
    @GenericGenerator(name = "league-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @Column(name = "fantasy_id", nullable = false)
    private long fantasyID;

    @NonNull @Getter @Setter
    @Column(name = "year", nullable = false)
    private int year;

    @NonNull @Getter @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @NonNull @Getter @Setter
    @Column(name = "service", nullable = false)
    private String service;

    @Getter @Setter
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "league_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Team> teams;

    @Getter @Setter
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "league_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Week> weeks;

    @Getter @Setter
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "league_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Category> categories;

    @Getter @Setter
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "league_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Scoring> scoring;

    @Getter @Setter
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "league_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Player> players;
}