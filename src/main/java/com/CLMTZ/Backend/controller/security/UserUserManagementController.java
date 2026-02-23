package com.CLMTZ.Backend.controller.security;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CLMTZ.Backend.dto.security.Request.UserUserManagementRequestDTO;
import com.CLMTZ.Backend.service.security.IUserUserManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/security/user-user-managements")
@RequiredArgsConstructor
public class UserUserManagementController {

    private final IUserUserManagementService service;

    @GetMapping
    public ResponseEntity<List<UserUserManagementRequestDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<UserUserManagementRequestDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<UserUserManagementRequestDTO> save(@RequestBody UserUserManagementRequestDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<UserUserManagementRequestDTO> update(@PathVariable("id") Integer id, @RequestBody UserUserManagementRequestDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }
}
