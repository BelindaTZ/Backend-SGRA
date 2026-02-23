package com.CLMTZ.Backend.controller.security;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CLMTZ.Backend.dto.security.Request.MasterDataManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Request.MasterManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Request.ModuleManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Request.UpdateRolePermissionsRequestDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterDataListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterTableListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.ModuleListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.SpResponseDTO;
import com.CLMTZ.Backend.service.security.IModuleManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/security/module-managements")
@RequiredArgsConstructor
public class ModuleManagementController {

    private final IModuleManagementService moduleManagementSer;

    @GetMapping
    public ResponseEntity<List<ModuleManagementRequestDTO>> findAll() {
        return ResponseEntity.ok(moduleManagementSer.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleManagementRequestDTO> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(moduleManagementSer.findById(id));
    }

    @PostMapping
    public ResponseEntity<ModuleManagementRequestDTO> save(@RequestBody ModuleManagementRequestDTO dto) {
        return new ResponseEntity<>(moduleManagementSer.save(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleManagementRequestDTO> update(@PathVariable("id") Integer id,
            @RequestBody ModuleManagementRequestDTO dto) {
        return ResponseEntity.ok(moduleManagementSer.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        moduleManagementSer.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list-modules-permisis")
    public ResponseEntity<List<ModuleListManagementResponseDTO>> listGModulesPermisis(
            @RequestParam("role") String role) {
        List<ModuleListManagementResponseDTO> listModules = moduleManagementSer.listModuleManagements(role);
        return ResponseEntity.ok(listModules);
    }

    @GetMapping("/list-master-tables")
    public ResponseEntity<List<MasterTableListManagementResponseDTO>> listGMasterTables() {
        List<MasterTableListManagementResponseDTO> listTables = moduleManagementSer.listMasterTables();
        return ResponseEntity.ok(listTables);
    }

    @GetMapping("/list-data-master-table")
    public ResponseEntity<List<MasterDataListManagementResponseDTO>> listGDataMasterTables(
            @RequestParam("schemaTable") String schemaTable) {
        List<MasterDataListManagementResponseDTO> listaData = moduleManagementSer.listDataMasterTables(schemaTable);
        return ResponseEntity.ok(listaData);
    }

    @PutMapping("/update-permissions")
    public ResponseEntity<SpResponseDTO> updatePermissions(
            @RequestBody UpdateRolePermissionsRequestDTO updateRolesPermissionsRequest) {
        SpResponseDTO responseDTO = moduleManagementSer.updateRolePermissions(updateRolesPermissionsRequest);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/create-master-record")
    public ResponseEntity<SpResponseDTO> masterTablesManagement(@RequestBody MasterManagementRequestDTO masterTables) {
        SpResponseDTO responseDTO = moduleManagementSer.masterTablesManagement(masterTables);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/update-master-record")
    public ResponseEntity<SpResponseDTO> masterDataUpdateManagement(
            @RequestBody MasterDataManagementRequestDTO dataUpdate) {
        SpResponseDTO responseDTO = moduleManagementSer.masterDataUpdateManagement(dataUpdate);
        return ResponseEntity.ok(responseDTO);
    }
}
