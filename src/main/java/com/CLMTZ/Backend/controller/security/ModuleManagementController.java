package com.CLMTZ.Backend.controller.security;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.CLMTZ.Backend.dto.security.ModuleManagementDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterTableListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.ModuleListManagementResponseDTO;
import com.CLMTZ.Backend.service.security.IModuleManagementService;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/security/module-managements")
@RequiredArgsConstructor
public class ModuleManagementController {

    private final IModuleManagementService moduleManagementSer;

    @GetMapping
    public ResponseEntity<List<ModuleManagementDTO>> findAll() { return ResponseEntity.ok(moduleManagementSer.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleManagementDTO> findById(@PathVariable Integer id) { return ResponseEntity.ok(moduleManagementSer.findById(id)); }

    @PostMapping
    public ResponseEntity<ModuleManagementDTO> save(@RequestBody ModuleManagementDTO dto) { return new ResponseEntity<>(moduleManagementSer.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleManagementDTO> update(@PathVariable Integer id, @RequestBody ModuleManagementDTO dto) { return ResponseEntity.ok(moduleManagementSer.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) { moduleManagementSer.deleteById(id); return ResponseEntity.noContent().build(); }

    @GetMapping("/list-modules-permisis")
    public ResponseEntity<List<ModuleListManagementResponseDTO>> listGModulesPermisis(@RequestParam String role) {
        List<ModuleListManagementResponseDTO> listModules = moduleManagementSer.listModuleManagerment(role);
        return ResponseEntity.ok(listModules);
    }

    @GetMapping("list-master-tables")
    public ResponseEntity<List<MasterTableListManagementResponseDTO>> getMethodName() {
        List<MasterTableListManagementResponseDTO> listTables = moduleManagementSer.listMasterTables();
        return ResponseEntity.ok(listTables);
    }
    
    
}
