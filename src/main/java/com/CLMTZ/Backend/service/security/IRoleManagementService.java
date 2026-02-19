package com.CLMTZ.Backend.service.security;

import java.util.List;
import com.CLMTZ.Backend.dto.security.RoleManagementDTO;
import com.CLMTZ.Backend.dto.security.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListManagementResponseDTO;

public interface IRoleManagementService {
    List<RoleManagementDTO> findAll();
    RoleManagementDTO findById(Integer id);
    RoleManagementDTO save(RoleManagementDTO dto);
    RoleManagementDTO update(Integer id, RoleManagementDTO dto);
    void deleteById(Integer id);

    SpResponseDTO createGRole(RoleManagementDTO roleRequest);
    SpResponseDTO updateGRole(RoleManagementDTO rolRequest);
    List<RoleListManagementResponseDTO> listRoles(String filter, Boolean state);
    List<RoleManagementDTO> listRoleNames();
}
