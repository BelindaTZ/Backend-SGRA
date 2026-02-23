package com.CLMTZ.Backend.repository.reinforcement.student;

import com.CLMTZ.Backend.dto.reinforcement.student.*;

import java.util.List;

public interface StudentCatalogRepository {
    List<SubjectItemDTO> listSubjects();
    List<SyllabusItemDTO> listSyllabiBySubject(Integer subjectId);
    List<TeacherItemDTO> listTeachers(Integer modalityId);
    List<ModalityItemDTO> listModalities();
    List<SessionTypeItemDTO> listSessionTypes();
    List<TimeSlotItemDTO> listTimeSlots();

    /**
     * Lista franjas horarias disponibles para un docente específico.
     * Filtra por disponibilidad del docente y excluye franjas ocupadas por solicitudes activas.
     *
     * @param teacherId ID del docente
     * @param dayOfWeek Día de la semana (1=Lunes, 7=Domingo)
     * @param periodId ID del periodo académico
     * @return Lista de franjas disponibles
     */
    List<AvailableTimeSlotDTO> listAvailableTimeSlots(Integer teacherId, Short dayOfWeek, Integer periodId);

    /**
     * Verifica si una franja horaria específica está disponible para un docente.
     *
     * @param teacherId ID del docente
     * @param dayOfWeek Día de la semana
     * @param periodId ID del periodo
     * @param timeSlotId ID de la franja a verificar
     * @return true si la franja está disponible, false en caso contrario
     */
    boolean isTimeSlotAvailable(Integer teacherId, Short dayOfWeek, Integer periodId, Integer timeSlotId);
}