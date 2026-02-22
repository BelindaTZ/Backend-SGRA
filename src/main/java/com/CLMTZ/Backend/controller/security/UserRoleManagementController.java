package com.CLMTZ.Backend.controller.security;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CLMTZ.Backend.dto.security.Request.UserRoleManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Response.KpiDashboardManagementResponseDTO;
import com.CLMTZ.Backend.service.security.IRoleManagementService;
import com.CLMTZ.Backend.service.security.IUserRoleManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/security/user-role-managements")
@RequiredArgsConstructor
public class UserRoleManagementController {

    private final IUserRoleManagementService userRoleSer;
    private final IRoleManagementService roleManagementSer;

    @GetMapping
    public ResponseEntity<List<UserRoleManagementRequestDTO>> findAll() { return ResponseEntity.ok(userRoleSer.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleManagementRequestDTO> findById(@PathVariable Integer id) { return ResponseEntity.ok(userRoleSer.findById(id)); }

    @PostMapping
    public ResponseEntity<UserRoleManagementRequestDTO> save(@RequestBody UserRoleManagementRequestDTO dto) { return new ResponseEntity<>(userRoleSer.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoleManagementRequestDTO> update(@PathVariable Integer id, @RequestBody UserRoleManagementRequestDTO dto) { return ResponseEntity.ok(userRoleSer.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) { userRoleSer.deleteById(id); return ResponseEntity.noContent().build(); }

    @GetMapping("/kpi-dashboard-management")
    public ResponseEntity<KpiDashboardManagementResponseDTO> kpiDashboardManagement(){
        KpiDashboardManagementResponseDTO kpidashboard = roleManagementSer.kpisDashboadrManagement();
        return ResponseEntity.ok(kpidashboard);
    }
}
