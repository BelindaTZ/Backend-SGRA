package com.CLMTZ.Backend.controller.reinforcement.student;

import com.CLMTZ.Backend.dto.reinforcement.student.NotificationChannelDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentPreferenceDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentPreferenceUpsertRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentPreferenceUpsertResponseDTO;
import com.CLMTZ.Backend.dto.security.session.UserContext;
import com.CLMTZ.Backend.service.reinforcement.student.StudentPreferenceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student/preferences")
public class StudentPreferenceController {

    private final StudentPreferenceService studentPreferenceService;

    public StudentPreferenceController(StudentPreferenceService studentPreferenceService) {
        this.studentPreferenceService = studentPreferenceService;
    }

    @GetMapping("/channels")
    public ResponseEntity<?> getActiveChannels(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("CTX") == null) {
                return ResponseEntity.status(401).body(Map.of("message", "No active session"));
            }

            UserContext ctx = (UserContext) session.getAttribute("CTX");
            Integer userId = ctx.getUserId();

            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("message", "No active session"));
            }

            List<NotificationChannelDTO> channels = studentPreferenceService.getActiveChannels();
            return ResponseEntity.ok(channels);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error retrieving channels: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyPreference(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("CTX") == null) {
                return ResponseEntity.status(401).body(Map.of("message", "No active session"));
            }

            UserContext ctx = (UserContext) session.getAttribute("CTX");
            Integer userId = ctx.getUserId();

            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("message", "No active session"));
            }

            StudentPreferenceDTO preference = studentPreferenceService.getMyPreference(userId);

            if (preference == null) {
                return ResponseEntity.ok(Map.of("preference", (Object) null));
            }

            return ResponseEntity.ok(preference);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error retrieving preference: " + e.getMessage()));
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> saveMyPreference(
            @RequestBody StudentPreferenceUpsertRequestDTO req,
            HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("CTX") == null) {
                return ResponseEntity.status(401).body(Map.of("message", "No active session"));
            }

            UserContext ctx = (UserContext) session.getAttribute("CTX");
            Integer userId = ctx.getUserId();

            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("message", "No active session"));
            }

            if (req.getChannelId() == null || req.getChannelId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid channelId parameter"));
            }

            if (req.getReminderAnticipation() == null || req.getReminderAnticipation() < 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid reminderAnticipation parameter"));
            }

            StudentPreferenceUpsertResponseDTO response = studentPreferenceService.saveMyPreference(userId, req);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error saving preference: " + e.getMessage()));
        }
    }
}