package com.CLMTZ.Backend.controller.reinforcement.student;

import com.CLMTZ.Backend.dto.reinforcement.student.StudentMyRequestsChipsDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentMyRequestsPageDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentMyRequestsStatusSummaryDTO;
import com.CLMTZ.Backend.dto.security.session.UserContext;
import com.CLMTZ.Backend.service.reinforcement.student.StudentMyRequestsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student/requests")
public class StudentMyRequestsController {

    private final StudentMyRequestsService studentMyRequestsService;

    public StudentMyRequestsController(StudentMyRequestsService studentMyRequestsService) {
        this.studentMyRequestsService = studentMyRequestsService;
    }

    @GetMapping
    public ResponseEntity<?> getMyRequests(
            @RequestParam(required = false) Integer periodId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer sessionTypeId,
            @RequestParam(required = false) Integer subjectId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
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

            if (page < 1) {
                return ResponseEntity.badRequest().body(Map.of("message", "Page must be >= 1"));
            }
            if (size < 1 || size > 100) {
                return ResponseEntity.badRequest().body(Map.of("message", "Size must be between 1 and 100"));
            }
            if (periodId != null && periodId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid periodId parameter"));
            }
            if (statusId != null && statusId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid statusId parameter"));
            }
            if (sessionTypeId != null && sessionTypeId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid sessionTypeId parameter"));
            }
            if (subjectId != null && subjectId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid subjectId parameter"));
            }

            StudentMyRequestsPageDTO response = studentMyRequestsService.getMyRequests(
                    userId, periodId, statusId, sessionTypeId, subjectId, search, page, size);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error retrieving requests: " + e.getMessage()));
        }
    }

    @GetMapping("/chips")
    public ResponseEntity<?> getMyRequestsChips(
            @RequestParam(required = false) Integer periodId,
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

            if (periodId != null && periodId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid periodId parameter"));
            }

            StudentMyRequestsChipsDTO response = studentMyRequestsService.getMyRequestsChips(userId, periodId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error retrieving chips: " + e.getMessage()));
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getMyRequestsSummary(
            @RequestParam(required = false) Integer periodId,
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

            if (periodId != null && periodId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid periodId parameter"));
            }

            List<StudentMyRequestsStatusSummaryDTO> response = studentMyRequestsService.getMyRequestsSummary(userId, periodId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error retrieving summary: " + e.getMessage()));
        }
    }
}