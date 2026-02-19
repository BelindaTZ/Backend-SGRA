package com.CLMTZ.Backend.service.security;

import java.time.LocalDate;
import java.util.List;

import com.CLMTZ.Backend.dto.security.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.UserManagementDTO;
import com.CLMTZ.Backend.dto.security.Response.UserListManagementResponseDTO;

public interface IUserManagementService {
    List<UserManagementDTO> findAll();
    UserManagementDTO findById(Integer id);
    UserManagementDTO save(UserManagementDTO dto);
    UserManagementDTO update(Integer id, UserManagementDTO dto);
    void deleteById(Integer id);
    SpResponseDTO createGUser(UserManagementDTO userRequest);
    SpResponseDTO updateGUser(UserManagementDTO userRequest);
    List<UserListManagementResponseDTO> listUserListManagement(String filterUser, LocalDate date, Boolean state);
}
