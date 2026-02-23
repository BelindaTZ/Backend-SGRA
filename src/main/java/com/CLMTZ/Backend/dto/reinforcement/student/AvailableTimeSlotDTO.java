package com.CLMTZ.Backend.dto.reinforcement.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar franjas horarias disponibles de un docente.
 * Usado por academico.fn_sl_franjas_docente_disponibles_ui().
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeSlotDTO {
    private Integer timeSlotId;
    private String label;
    private String timeSlotJson;
}

