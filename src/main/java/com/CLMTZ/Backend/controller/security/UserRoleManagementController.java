package com.CLMTZ.Backend.controller.security;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CLMTZ.Backend.dto.security.Request.UserRoleManagementRequestDTO;
import com.CLMTZ.Backend.service.security.IUserRoleManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/security/user-role-managements")
@RequiredArgsConstructor
public class UserRoleManagementController {

    private final IUserRoleManagementService service;

    @GetMapping
    public ResponseEntity<List<UserRoleManagementRequestDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleManagementRequestDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<UserRoleManagementRequestDTO> save(@RequestBody UserRoleManagementRequestDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoleManagementRequestDTO> update(@PathVariable("id") Integer id, @RequestBody UserRoleManagementRequestDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }
}
