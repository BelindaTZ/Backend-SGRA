package com.CLMTZ.Backend.controller.academic;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.CLMTZ.Backend.dto.academic.SyllabiDTO;
import com.CLMTZ.Backend.service.academic.ISyllabiService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/academic/syllabi")
@RequiredArgsConstructor
public class SyllabiController {

    private final ISyllabiService service;

    @GetMapping
    public ResponseEntity<List<SyllabiDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<SyllabiDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<SyllabiDTO> save(@RequestBody SyllabiDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<SyllabiDTO> update(@PathVariable("id") Integer id, @RequestBody SyllabiDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }
}
