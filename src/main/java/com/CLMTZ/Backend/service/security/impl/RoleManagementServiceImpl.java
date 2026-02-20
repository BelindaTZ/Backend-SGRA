package com.CLMTZ.Backend.service.security.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CLMTZ.Backend.dto.security.RoleManagementDTO;
import com.CLMTZ.Backend.dto.security.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListManagementResponseDTO;
import com.CLMTZ.Backend.model.security.RoleManagement;
import com.CLMTZ.Backend.repository.security.IRoleManagementRepository;
import com.CLMTZ.Backend.repository.security.IAdminDynamicRepository;
import com.CLMTZ.Backend.service.security.IRoleManagementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleManagementServiceImpl implements IRoleManagementService {

    private final IRoleManagementRepository roleManagementRepo;
    private final IAdminDynamicRepository adminDynamicRepo;

    @Override
    public List<RoleManagementDTO> findAll() { return roleManagementRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList()); }

    @Override
    public RoleManagementDTO findById(Integer id) { return roleManagementRepo.findById(id).map(this::toDTO).orElseThrow(() -> new RuntimeException("RoleManagement not found with id: " + id)); }

    @Override
    public RoleManagementDTO save(RoleManagementDTO dto) {
        RoleManagement e = new RoleManagement();
        e.setRoleG(dto.getRoleG()); e.setServerRole(dto.getServerRole()); e.setDescription(dto.getDescription());
        e.setCreatedAt(dto.getCreatedAt()); e.setState(dto.getState() != null ? dto.getState() : true);
        return toDTO(roleManagementRepo.save(e));
    }

    @Override
    public RoleManagementDTO update(Integer id, RoleManagementDTO dto) {
        RoleManagement e = roleManagementRepo.findById(id).orElseThrow(() -> new RuntimeException("RoleManagement not found with id: " + id));
        e.setRoleG(dto.getRoleG()); e.setServerRole(dto.getServerRole()); e.setDescription(dto.getDescription());
        e.setCreatedAt(dto.getCreatedAt()); e.setState(dto.getState());
        return toDTO(roleManagementRepo.save(e));
    }

    @Override
    public void deleteById(Integer id) { roleManagementRepo.deleteById(id); }
    

    private RoleManagementDTO toDTO(RoleManagement e) {
        RoleManagementDTO d = new RoleManagementDTO();
        d.setRoleGId(e.getRoleGId()); d.setRoleG(e.getRoleG()); d.setServerRole(e.getServerRole());
        d.setDescription(e.getDescription()); d.setCreatedAt(e.getCreatedAt()); d.setState(e.getState());
        return d;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleManagementDTO> listRoleNames(){

        List<RoleManagement> rolesEntity = roleManagementRepo.findByStateTrue();

        return rolesEntity.stream().map(rolEntity -> {
            RoleManagementDTO dto = new RoleManagementDTO();           
            dto.setRoleGId(rolEntity.getRoleGId()); 
            dto.setRoleG(rolEntity.getRoleG());           
            return dto;
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleListManagementResponseDTO> listRoles(String filter, Boolean state){
        String textFilter = (filter == null) ? "" : filter;
        Boolean stateFilter = (state == null) ? true : state;

        return adminDynamicRepo.listRoles(textFilter, stateFilter);
    }

    @Override
    @Transactional
    public SpResponseDTO createGRole(RoleManagementDTO roleRequest){
        return adminDynamicRepo.createGRole(roleRequest.getRoleG(), roleRequest.getDescription());
    }

    @Override
    @Transactional
    public SpResponseDTO updateGRole(RoleManagementDTO roleRequest){
        return adminDynamicRepo.updateGRole(roleRequest.getRoleGId(), roleRequest.getRoleG(), roleRequest.getDescription());
    }
}
