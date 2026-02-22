package com.CLMTZ.Backend.service.security;

import java.util.List;

import com.CLMTZ.Backend.dto.security.Request.RoleManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.SpResponseDTO;

public interface IRoleManagementService {
    List<RoleManagementRequestDTO> findAll();
    RoleManagementRequestDTO findById(Integer id);
    RoleManagementRequestDTO save(RoleManagementRequestDTO dto);
    RoleManagementRequestDTO update(Integer id, RoleManagementRequestDTO dto);
    void deleteById(Integer id);

    SpResponseDTO createRoleManagement(RoleManagementRequestDTO roleRequest);
    SpResponseDTO updateRoleManagement(RoleManagementRequestDTO rolRequest);
    List<RoleListManagementResponseDTO> listRolesManagement(String filter, Boolean state);
    List<RoleManagementRequestDTO> listRoleNames();
}
