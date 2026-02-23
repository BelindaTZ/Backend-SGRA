package com.CLMTZ.Backend.controller.reinforcement.student;

import com.CLMTZ.Backend.dto.reinforcement.student.*;
import com.CLMTZ.Backend.service.reinforcement.student.StudentCatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student/catalogs")
public class StudentCatalogController {

    private final StudentCatalogService studentCatalogService;

    public StudentCatalogController(StudentCatalogService studentCatalogService) {
        this.studentCatalogService = studentCatalogService;
    }

    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjects() {
        try {
            List<SubjectItemDTO> subjects = studentCatalogService.getSubjects();
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error retrieving subjects: " + e.getMessage()));
        }
    }

    @GetMapping("/subjects/{subjectId}/syllabi")
    public ResponseEntity<?> getSyllabiBySubject(@PathVariable("subjectId") Integer subjectId) {
        try {
            if (subjectId == null || subjectId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid subjectId parameter"));
            }
            List<SyllabusItemDTO> syllabi = studentCatalogService.getSyllabiBySubject(subjectId);
            return ResponseEntity.ok(syllabi);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error retrieving syllabi: " + e.getMessage()));
        }
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getTeachers(@RequestParam(value = "modalityId", required = false) Integer modalityId) {
        try {
            List<TeacherItemDTO> teachers = studentCatalogService.getTeachers(modalityId);
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error retrieving teachers: " + e.getMessage()));
        }
    }

    @GetMapping("/modalities")
    public ResponseEntity<?> getModalities() {
        try {
            List<ModalityItemDTO> modalities = studentCatalogService.getModalities();
            return ResponseEntity.ok(modalities);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error retrieving modalities: " + e.getMessage()));
        }
    }

    @GetMapping("/sessionTypes")
    public ResponseEntity<?> getSessionTypes() {
        try {
            List<SessionTypeItemDTO> sessionTypes = studentCatalogService.getSessionTypes();
            return ResponseEntity.ok(sessionTypes);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Error retrieving session types: " + e.getMessage()));
        }
    }

    @GetMapping("/timeSlots")
    public ResponseEntity<?> getTimeSlots() {
        try {
            List<TimeSlotItemDTO> timeSlots = studentCatalogService.getTimeSlots();
            return ResponseEntity.ok(timeSlots);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error retrieving time slots: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener franjas horarias disponibles de un docente.
     * Filtra por disponibilidad registrada y excluye franjas ocupadas por solicitudes activas.
     *
     * @param teacherId ID del docente
     * @param dayOfWeek Día de la semana (1=Lunes, 7=Domingo)
     * @param periodId ID del periodo académico
     * @return Lista de franjas disponibles [{timeSlotId, label, timeSlotJson}]
     */
    @GetMapping("/timeSlots/available")
    public ResponseEntity<?> getAvailableTimeSlots(
            @RequestParam("teacherId") Integer teacherId,
            @RequestParam("dayOfWeek") Short dayOfWeek,
            @RequestParam("periodId") Integer periodId) {
        try {
            if (teacherId == null || teacherId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid teacherId parameter"));
            }
            if (dayOfWeek == null || dayOfWeek < 1 || dayOfWeek > 7) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid dayOfWeek parameter (must be 1-7)"));
            }
            if (periodId == null || periodId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid periodId parameter"));
            }

            List<AvailableTimeSlotDTO> availableSlots = studentCatalogService.getAvailableTimeSlots(teacherId, dayOfWeek, periodId);
            return ResponseEntity.ok(availableSlots);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error retrieving available time slots: " + e.getMessage()));
        }
    }
}