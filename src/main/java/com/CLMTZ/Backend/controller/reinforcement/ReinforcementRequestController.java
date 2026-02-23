package com.CLMTZ.Backend.controller.reinforcement;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.CLMTZ.Backend.dto.reinforcement.ReinforcementRequestDTO;
import com.CLMTZ.Backend.service.reinforcement.IReinforcementRequestService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reinforcement/reinforcement-requests")
@RequiredArgsConstructor
public class ReinforcementRequestController {

    private final IReinforcementRequestService service;

    @GetMapping
    public ResponseEntity<List<ReinforcementRequestDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<ReinforcementRequestDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<ReinforcementRequestDTO> save(@RequestBody ReinforcementRequestDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<ReinforcementRequestDTO> update(@PathVariable("id") Integer id, @RequestBody ReinforcementRequestDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }
}
