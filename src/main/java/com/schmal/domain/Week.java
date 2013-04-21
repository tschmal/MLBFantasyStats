package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"ID", "matchups"})
@Entity
@Table(name = "week")
public class Week
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "week-id-gen")
    @GenericGenerator(name = "week-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private League league;

    @NonNull @Getter @Setter
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @NonNull @Getter @Setter
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Getter @Setter
    @OneToMany(mappedBy = "week", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Matchup> matchups;
}