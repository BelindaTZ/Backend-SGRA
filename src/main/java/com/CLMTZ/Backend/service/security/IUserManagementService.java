package com.CLMTZ.Backend.service.security;

import java.util.List;

import com.CLMTZ.Backend.dto.security.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.UserManagementDTO;

public interface IUserManagementService {
    List<UserManagementDTO> findAll();
    UserManagementDTO findById(Integer id);
    UserManagementDTO save(UserManagementDTO dto);
    UserManagementDTO update(Integer id, UserManagementDTO dto);
    void deleteById(Integer id);
    SpResponseDTO createGUser(UserManagementDTO userRequest);
    SpResponseDTO updateGUser(UserManagementDTO userRequest);
}
