package com.CLMTZ.Backend.controller.reinforcement;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.CLMTZ.Backend.dto.reinforcement.ScheduledReinforcementDTO;
import com.CLMTZ.Backend.service.reinforcement.IScheduledReinforcementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reinforcement/scheduled-reinforcements")
@RequiredArgsConstructor
public class ScheduledReinforcementController {

    private final IScheduledReinforcementService service;

    @GetMapping
    public ResponseEntity<List<ScheduledReinforcementDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledReinforcementDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<ScheduledReinforcementDTO> save(@RequestBody ScheduledReinforcementDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduledReinforcementDTO> update(@PathVariable("id") Integer id, @RequestBody ScheduledReinforcementDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }
}
