package com.CLMTZ.Backend.controller.reinforcement;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.CLMTZ.Backend.dto.reinforcement.ReinforcementRequestStatusDTO;
import com.CLMTZ.Backend.service.reinforcement.IReinforcementRequestStatusService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reinforcement/reinforcement-request-statuses")
@RequiredArgsConstructor
public class ReinforcementRequestStatusController {

    private final IReinforcementRequestStatusService service;

    @GetMapping
    public ResponseEntity<List<ReinforcementRequestStatusDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<ReinforcementRequestStatusDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<ReinforcementRequestStatusDTO> save(@RequestBody ReinforcementRequestStatusDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<ReinforcementRequestStatusDTO> update(@PathVariable("id") Integer id, @RequestBody ReinforcementRequestStatusDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }
}
