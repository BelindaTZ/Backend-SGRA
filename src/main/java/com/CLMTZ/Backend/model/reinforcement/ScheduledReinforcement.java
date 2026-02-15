package com.CLMTZ.Backend.model.reinforcement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.CLMTZ.Backend.model.academic.Modality;
import com.CLMTZ.Backend.model.academic.TimeSlot;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tbrefuerzosprogramados", schema = "reforzamiento")
public class ScheduledReinforcement {
    @Id
    @Column(name = "idrefuerzoprogramado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer scheduledReinforcementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtiposesion", foreignKey = @ForeignKey(name = "fk_refuerzoprog_tiposesion"))
    private SessionTypes sessionTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmodalidad", foreignKey = @ForeignKey(name = "fk_refuerzoprog_modalidad"))
    private Modality modalityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idfranjahorario", foreignKey = @ForeignKey(name = "fk_refuerzoprog_franja"))
    private TimeSlot timeSlotId;

    @Column(name = "tiempoestimado", nullable = false, columnDefinition = "time")
    private LocalTime estimatedTime;
    
    @Column(name = "motivo", length = 200)
    private String reason;

    @Column (name = "fechacreacion",nullable = false, columnDefinition = "timestamp")
    private LocalDateTime newSchedule;

    @Column(name = "estado", nullable = false, columnDefinition = "char(2) default 'P' check(estado in ('P', 'RP', 'R'))")
    private String state;

    @OneToMany(mappedBy = "scheduledReinforcementId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledReinforcementDetail> scheduledReinforcementDetails;

    @OneToMany(mappedBy = "scheduledReinforcementId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReinforcementPerformed> reinforcementsPerformed;

    @OneToMany(mappedBy = "scheduledReinforcementId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OnSiteReinforcement> onSiteReinforcements;
}