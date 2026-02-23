package com.CLMTZ.Backend.controller.security;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CLMTZ.Backend.dto.security.Request.RoleManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.SpResponseDTO;
import com.CLMTZ.Backend.service.security.IRoleManagementService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/security/role-managements")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoleManagementController {

    private final IRoleManagementService roleManagementSer;

    @GetMapping
    public ResponseEntity<List<RoleManagementRequestDTO>> findAll() { return ResponseEntity.ok(roleManagementSer.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<RoleManagementRequestDTO> findById(@PathVariable("id") Integer id) { return ResponseEntity.ok(roleManagementSer.findById(id)); }

    @PostMapping
    public ResponseEntity<RoleManagementRequestDTO> save(@RequestBody RoleManagementRequestDTO dto) { return new ResponseEntity<>(roleManagementSer.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<RoleManagementRequestDTO> update(@PathVariable("id") Integer id, @RequestBody RoleManagementRequestDTO dto) { return ResponseEntity.ok(roleManagementSer.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) { roleManagementSer.deleteById(id); return ResponseEntity.noContent().build(); }

    @PostMapping("/create-role")
    public ResponseEntity<SpResponseDTO> createGRole(@RequestBody RoleManagementRequestDTO requestRole) {
        SpResponseDTO request = roleManagementSer.createRoleManagement(requestRole);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/update-role")
    public ResponseEntity<SpResponseDTO> updateRoleManagement(@RequestBody RoleManagementRequestDTO requestRole) {
        SpResponseDTO request = roleManagementSer.updateRoleManagement(requestRole);
        return ResponseEntity.ok(request);
    }
    
    @GetMapping("/list-roles")
    public ResponseEntity<List<RoleListManagementResponseDTO>> listRoles(@RequestParam(required = false) String filter, @RequestParam(defaultValue = "true") Boolean state){
        List<RoleListManagementResponseDTO> list = roleManagementSer.listRolesManagement(filter, state);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/list-roles-combo")
    public ResponseEntity<List<RoleManagementRequestDTO>> listRolesCombobox(){
        List<RoleManagementRequestDTO> list = roleManagementSer.listRoleNames();
        return ResponseEntity.ok(list);
    }
}
