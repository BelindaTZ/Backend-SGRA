package com.CLMTZ.Backend.controller.security;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CLMTZ.Backend.dto.security.Request.RoleManagementModuleRequestDTO;
import com.CLMTZ.Backend.service.security.IRoleManagementModuleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/security/role-management-modules")
@RequiredArgsConstructor
public class RoleManagementModuleController {

    private final IRoleManagementModuleService service;

    @GetMapping
    public ResponseEntity<List<RoleManagementModuleRequestDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<RoleManagementModuleRequestDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<RoleManagementModuleRequestDTO> save(@RequestBody RoleManagementModuleRequestDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<RoleManagementModuleRequestDTO> update(@PathVariable("id") Integer id, @RequestBody RoleManagementModuleRequestDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }
}
