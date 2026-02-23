package com.CLMTZ.Backend.controller.security;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CLMTZ.Backend.dto.security.Request.AccessRequestDTO;
import com.CLMTZ.Backend.service.security.IAccessService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/security/accesses")
@RequiredArgsConstructor
public class AccessController {

    private final IAccessService service;

    @GetMapping
    public ResponseEntity<List<AccessRequestDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<AccessRequestDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<AccessRequestDTO> save(@RequestBody AccessRequestDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<AccessRequestDTO> update(@PathVariable("id") Integer id, @RequestBody AccessRequestDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }
}
