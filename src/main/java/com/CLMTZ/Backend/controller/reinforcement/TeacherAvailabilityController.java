package com.CLMTZ.Backend.controller.reinforcement;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.CLMTZ.Backend.dto.reinforcement.TeacherAvailabilityDTO;
import com.CLMTZ.Backend.service.reinforcement.ITeacherAvailabilityService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reinforcement/teacher-availabilities")
@RequiredArgsConstructor
public class TeacherAvailabilityController {

    private final ITeacherAvailabilityService service;

    @GetMapping
    public ResponseEntity<List<TeacherAvailabilityDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherAvailabilityDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<TeacherAvailabilityDTO> save(@RequestBody TeacherAvailabilityDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherAvailabilityDTO> update(@PathVariable("id") Integer id, @RequestBody TeacherAvailabilityDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }
}
