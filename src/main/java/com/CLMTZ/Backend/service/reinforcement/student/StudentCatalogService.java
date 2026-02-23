package com.CLMTZ.Backend.service.reinforcement.student;

import com.CLMTZ.Backend.dto.reinforcement.student.*;

import java.util.List;

public interface StudentCatalogService {
    List<SubjectItemDTO> getSubjects();
    List<SyllabusItemDTO> getSyllabiBySubject(Integer subjectId);
    List<TeacherItemDTO> getTeachers(Integer modalityId);
    List<ModalityItemDTO> getModalities();
    List<SessionTypeItemDTO> getSessionTypes();
    List<TimeSlotItemDTO> getTimeSlots();

    /**
     * Obtiene franjas horarias disponibles para un docente específico.
     *
     * @param teacherId ID del docente
     * @param dayOfWeek Día de la semana (1=Lunes, 7=Domingo)
     * @param periodId ID del periodo académico
     * @return Lista de franjas disponibles
     */
    List<AvailableTimeSlotDTO> getAvailableTimeSlots(Integer teacherId, Short dayOfWeek, Integer periodId);

    /**
     * Verifica si una franja horaria está disponible para un docente.
     *
     * @param teacherId ID del docente
     * @param dayOfWeek Día de la semana
     * @param periodId ID del periodo
     * @param timeSlotId ID de la franja a verificar
     * @return true si está disponible, false en caso contrario
     */
    boolean isTimeSlotAvailable(Integer teacherId, Short dayOfWeek, Integer periodId, Integer timeSlotId);
}