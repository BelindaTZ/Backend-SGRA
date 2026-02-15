package com.CLMTZ.Backend.model.reinforcement;

import java.time.LocalDateTime;
import java.util.List;

import com.CLMTZ.Backend.model.academic.Modality;
import com.CLMTZ.Backend.model.academic.Period;
import com.CLMTZ.Backend.model.academic.Students;
import com.CLMTZ.Backend.model.academic.Syllabi;
import com.CLMTZ.Backend.model.academic.Teaching;
import com.CLMTZ.Backend.model.academic.TimeSlot;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tbsolicitudesrefuerzos", schema = "reforzamiento")
public class ReinforcementRequest {
    @Id
    @Column(name = "idsolicitudrefuerzo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer reinforcementRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idestudiante", foreignKey = @ForeignKey(name = "fk_solicitud_estudiante"))
    private Students studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddocente", foreignKey = @ForeignKey(name = "fk_solicitud_docente"))
    private Teaching teacherId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtemario", foreignKey = @ForeignKey(name = "fk_solicitud_temario"))
    private Syllabi topicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idfranjahoraria", foreignKey = @ForeignKey(name = "fk_solicitud_franja"))
    private TimeSlot timeSlotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idmodalidad", foreignKey = @ForeignKey(name = "fk_solicitud_modalidad"))
    private Modality modalityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtiposesion", foreignKey = @ForeignKey(name = "fk_solicitud_tiposesion"))
    private SessionTypes sessionTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idestadosolicitudrefuerzo", foreignKey = @ForeignKey(name = "fk_solicitud_estado"))
    private ReinforcementRequestStatus requestStatusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idperiodo", foreignKey = @ForeignKey(name = "fk_solicitud_periodo"))
    private Period periodId;

    @Column(name = "diasolicitado", nullable = false, columnDefinition = "smallint")
    private Short requestedDay;
    
    @Column(name = "motivo", length = 200, nullable = false)
    private String reason;

    @Column(name = "urlarchivo", nullable = false, columnDefinition = "TEXT")
    private String fileUrl;
    
    @Column(name = "fechahoracreacion", nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "reinforcementRequestId", fetch = FetchType.LAZY)
    private List<ScheduledReinforcementDetail> scheduledReinforcementDetails;

    @OneToMany(mappedBy = "reinforcementRequestId", fetch = FetchType.LAZY)
    private List<Participants> participants;
}
