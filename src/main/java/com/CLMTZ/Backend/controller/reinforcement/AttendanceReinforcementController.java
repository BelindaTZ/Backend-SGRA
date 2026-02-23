package com.CLMTZ.Backend.controller.reinforcement;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.CLMTZ.Backend.dto.reinforcement.AttendanceReinforcementDTO;
import com.CLMTZ.Backend.service.reinforcement.IAttendanceReinforcementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reinforcement/attendance-reinforcements")
@RequiredArgsConstructor
public class AttendanceReinforcementController {

    private final IAttendanceReinforcementService service;

    @GetMapping
    public ResponseEntity<List<AttendanceReinforcementDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceReinforcementDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<AttendanceReinforcementDTO> save(@RequestBody AttendanceReinforcementDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceReinforcementDTO> update(@PathVariable("id") Integer id, @RequestBody AttendanceReinforcementDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }
}
