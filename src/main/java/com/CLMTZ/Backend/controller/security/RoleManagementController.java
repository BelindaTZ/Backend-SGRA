package com.CLMTZ.Backend.controller.security;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.CLMTZ.Backend.dto.security.RoleManagementDTO;
import com.CLMTZ.Backend.dto.security.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListResponseDTO;
import com.CLMTZ.Backend.service.security.IRoleManagementService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/security/role-managements")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoleManagementController {

    private final IRoleManagementService roleManagementSer;

    @GetMapping
    public ResponseEntity<List<RoleManagementDTO>> findAll() { return ResponseEntity.ok(roleManagementSer.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<RoleManagementDTO> findById(@PathVariable Integer id) { return ResponseEntity.ok(roleManagementSer.findById(id)); }

    @PostMapping
    public ResponseEntity<RoleManagementDTO> save(@RequestBody RoleManagementDTO dto) { return new ResponseEntity<>(roleManagementSer.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<RoleManagementDTO> update(@PathVariable Integer id, @RequestBody RoleManagementDTO dto) { return ResponseEntity.ok(roleManagementSer.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) { roleManagementSer.deleteById(id); return ResponseEntity.noContent().build(); }

    @PostMapping("/create-role")
    public ResponseEntity<SpResponseDTO> createGRole(@RequestBody RoleManagementDTO requestRole) {
        SpResponseDTO request = roleManagementSer.createGRole(requestRole);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/update-role")
    public ResponseEntity<SpResponseDTO> updateGRole(@RequestBody RoleManagementDTO requestRole) {
        SpResponseDTO request = roleManagementSer.updateGRole(requestRole);
        return ResponseEntity.ok(request);
    }
    
    @GetMapping("/list-roles")
    public ResponseEntity<List<RoleListResponseDTO>> listRoles(@RequestParam(required = false) String filter, @RequestParam(defaultValue = "true") Boolean state){
        List<RoleListResponseDTO> list = roleManagementSer.listRoles(filter, state);
        return ResponseEntity.ok(list);
    }
}
