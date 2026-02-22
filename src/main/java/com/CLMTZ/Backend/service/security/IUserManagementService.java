package com.CLMTZ.Backend.service.security;

import java.time.LocalDate;
import java.util.List;

import com.CLMTZ.Backend.dto.security.Request.UserManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Response.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.UserListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.UserRoleManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.UserRolesUpdateManagementResponseDTO;

public interface IUserManagementService {
    List<UserManagementRequestDTO> findAll();
    UserManagementRequestDTO findById(Integer id);
    UserManagementRequestDTO save(UserManagementRequestDTO dto);
    UserManagementRequestDTO update(Integer id, UserManagementRequestDTO dto);
    void deleteById(Integer id);
    
    SpResponseDTO createUserManagement(UserManagementRequestDTO userRequest);
    SpResponseDTO updateUserManagement(UserRolesUpdateManagementResponseDTO userRequest);
    List<UserListManagementResponseDTO> listUserListManagement(String filterUser, LocalDate date, Boolean state);
    UserRoleManagementResponseDTO DataUserById(Integer idUser);
}
