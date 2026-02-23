package com.CLMTZ.Backend.controller.reinforcement;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.CLMTZ.Backend.dto.reinforcement.OnSiteReinforcementDTO;
import com.CLMTZ.Backend.service.reinforcement.IOnSiteReinforcementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reinforcement/on-site-reinforcements")
@RequiredArgsConstructor
public class OnSiteReinforcementController {

    private final IOnSiteReinforcementService service;

    @GetMapping
    public ResponseEntity<List<OnSiteReinforcementDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<OnSiteReinforcementDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<OnSiteReinforcementDTO> save(@RequestBody OnSiteReinforcementDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<OnSiteReinforcementDTO> update(@PathVariable("id") Integer id, @RequestBody OnSiteReinforcementDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }
}
