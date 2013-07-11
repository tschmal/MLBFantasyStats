package com.schmal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
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
@Table(name = "lineup")
public class Lineup
{
    @Id
    @Getter @Setter
    @GeneratedValue(generator = "lineup-id-gen")
    @GenericGenerator(name = "lineup-id-gen", strategy = "increment")
    @Column(name = "id", nullable = false)
    private long ID;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private Team team;

    @NonNull @Getter @Setter
    @JsonIgnore
    @OneToOne
    private ScoringPeriod scoringPeriod;

    @Getter @Setter
    private float score;

    @Getter @Setter
    @OneToMany(mappedBy = "lineup", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Slot> slots;

    public void calculateScore()
    {
        float score = 0f;

        for (Slot slot : getSlots())
        {
            Result result = slot.getPlayer().getResult(scoringPeriod.getPeriodID());

            if (!slot.isEligible() ||
                "DL".equals(slot.getPosition()) ||
                "Bench".equals(slot.getPosition()))
            {
                continue;
            }

            if (result == null || result.getStats() == null)
            {
                continue;
            }

            score += result.getScore();
        }

        this.score = score;
    }
}