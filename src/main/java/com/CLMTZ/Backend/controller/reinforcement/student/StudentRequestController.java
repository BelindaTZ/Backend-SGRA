package com.CLMTZ.Backend.controller.reinforcement.student;

import com.CLMTZ.Backend.config.UserContextHolder;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestCreateRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestCreateResponseDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewResponseDTO;
import com.CLMTZ.Backend.dto.security.session.UserContext;
import com.CLMTZ.Backend.service.reinforcement.student.StudentRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student/requests")
public class StudentRequestController {

    private final StudentRequestService studentRequestService;

    public StudentRequestController(StudentRequestService studentRequestService) {
        this.studentRequestService = studentRequestService;
    }

    @PostMapping("/preview")
    public ResponseEntity<?> preview(@RequestBody StudentRequestPreviewRequestDTO req) {
        try {
            if (req.getSyllabusId() == null || req.getSyllabusId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid syllabusId parameter"));
            }
            if (req.getTeacherId() == null || req.getTeacherId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid teacherId parameter"));
            }
            if (req.getTimeSlotId() == null || req.getTimeSlotId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid timeSlotId parameter"));
            }
            if (req.getModalityId() == null || req.getModalityId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid modalityId parameter"));
            }
            if (req.getSessionTypeId() == null || req.getSessionTypeId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid sessionTypeId parameter"));
            }

            StudentRequestPreviewResponseDTO response = studentRequestService.preview(req);
            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error generating preview: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StudentRequestCreateRequestDTO req) {
        try {
            UserContext ctx = UserContextHolder.getContext();
            Integer userId = ctx.getUserId();


            if (req.getSyllabusId() == null || req.getSyllabusId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid syllabusId parameter"));
            }
            if (req.getTeacherId() == null || req.getTeacherId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid teacherId parameter"));
            }
            if (req.getTimeSlotId() == null || req.getTimeSlotId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid timeSlotId parameter"));
            }
            if (req.getModalityId() == null || req.getModalityId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid modalityId parameter"));
            }
            if (req.getSessionTypeId() == null || req.getSessionTypeId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid sessionTypeId parameter"));
            }
            if (req.getReason() == null || req.getReason().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Reason cannot be empty"));
            }
            if (req.getRequestedDay() == null || req.getRequestedDay() < 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid requestedDay parameter"));
            }

            StudentRequestCreateResponseDTO response = studentRequestService.create(req, userId);
            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error creating request: " + e.getMessage()));
        }
    }
}