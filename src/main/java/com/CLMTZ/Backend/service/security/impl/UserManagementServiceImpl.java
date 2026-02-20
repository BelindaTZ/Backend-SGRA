package com.CLMTZ.Backend.service.security.impl;

import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.CLMTZ.Backend.dto.security.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.UserManagementDTO;
import com.CLMTZ.Backend.dto.security.Response.UserListManagementResponseDTO;
import com.CLMTZ.Backend.model.security.UserManagement;
import com.CLMTZ.Backend.repository.security.IUserManagementRepository;
import com.CLMTZ.Backend.repository.security.IAdminDynamicRepository;
import com.CLMTZ.Backend.service.security.IUserManagementService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements IUserManagementService {

    private final IUserManagementRepository userManagementRepo;
    private final IAdminDynamicRepository adminDynamicRepo;

    @Override
    public List<UserManagementDTO> findAll() { return userManagementRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList()); }

    @Override
    public UserManagementDTO findById(Integer id) { return userManagementRepo.findById(id).map(this::toDTO).orElseThrow(() -> new RuntimeException("UserManagement not found with id: " + id)); }

    @Override
    public UserManagementDTO save(UserManagementDTO dto) {
        UserManagement e = new UserManagement();
        e.setUser(dto.getUser()); e.setPassword(dto.getPassword()); e.setState(dto.getState() != null ? dto.getState() : true);
        return toDTO(userManagementRepo.save(e));
    }

    @Override
    public UserManagementDTO update(Integer id, UserManagementDTO dto) {
        UserManagement e = userManagementRepo.findById(id).orElseThrow(() -> new RuntimeException("UserManagement not found with id: " + id));
        e.setUser(dto.getUser()); e.setPassword(dto.getPassword()); e.setState(dto.getState());
        return toDTO(userManagementRepo.save(e));
    }

    @Override
    public void deleteById(Integer id) { userManagementRepo.deleteById(id); }

    private UserManagementDTO toDTO(UserManagement e) {
        UserManagementDTO d = new UserManagementDTO();
        d.setUserGId(e.getUserGId()); d.setUser(e.getUser()); d.setPassword(e.getPassword()); d.setState(e.getState());
        return d;
    }

    @Override
    @Transactional
    public List<UserListManagementResponseDTO> listUserListManagement(String filterUser, LocalDate date, Boolean state){
        try {
            return adminDynamicRepo.listUsersManagement(filterUser, date, state);
        } catch (Exception e) {
            throw new RuntimeException("Error al listar a los usuarios" + e.getMessage());
        }  
    }

    @Override
    @Transactional
    public SpResponseDTO createGUser(UserManagementDTO userRequest){
        return adminDynamicRepo.createGUser(userRequest.getUser(), userRequest.getPassword());
    }

    @Override
    @Transactional
    public SpResponseDTO updateGUser(UserManagementDTO userRequest){
        return adminDynamicRepo.updateGUser(userRequest.getUserGId(), userRequest.getUser(), userRequest.getPassword());
    }
}
