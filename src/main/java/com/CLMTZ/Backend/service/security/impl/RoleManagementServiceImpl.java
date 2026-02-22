package com.CLMTZ.Backend.service.security.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CLMTZ.Backend.dto.security.Request.RoleManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.SpResponseDTO;
import com.CLMTZ.Backend.model.security.RoleManagement;
import com.CLMTZ.Backend.repository.security.IRoleManagementRepository;
import com.CLMTZ.Backend.repository.security.custom.IRoleManagementCustomRepository;
import com.CLMTZ.Backend.service.security.IRoleManagementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleManagementServiceImpl implements IRoleManagementService {

    private final IRoleManagementRepository roleManagementRepo;
    private final IRoleManagementCustomRepository roleManagementCustomRepo;

    @Override
    public List<RoleManagementRequestDTO> findAll() { return roleManagementRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList()); }

    @Override
    public RoleManagementRequestDTO findById(Integer id) { return roleManagementRepo.findById(id).map(this::toDTO).orElseThrow(() -> new RuntimeException("RoleManagement not found with id: " + id)); }

    @Override
    public RoleManagementRequestDTO save(RoleManagementRequestDTO dto) {
        RoleManagement e = new RoleManagement();
        e.setRoleG(dto.getRoleG()); e.setServerRole(dto.getServerRole()); e.setDescription(dto.getDescription());
        e.setCreatedAt(dto.getCreatedAt()); e.setState(dto.getState() != null ? dto.getState() : true);
        return toDTO(roleManagementRepo.save(e));
    }

    @Override
    public RoleManagementRequestDTO update(Integer id, RoleManagementRequestDTO dto) {
        RoleManagement e = roleManagementRepo.findById(id).orElseThrow(() -> new RuntimeException("RoleManagement not found with id: " + id));
        e.setRoleG(dto.getRoleG()); e.setServerRole(dto.getServerRole()); e.setDescription(dto.getDescription());
        e.setCreatedAt(dto.getCreatedAt()); e.setState(dto.getState());
        return toDTO(roleManagementRepo.save(e));
    }

    @Override
    public void deleteById(Integer id) { roleManagementRepo.deleteById(id); }
    

    private RoleManagementRequestDTO toDTO(RoleManagement e) {
        RoleManagementRequestDTO d = new RoleManagementRequestDTO();
        d.setRoleGId(e.getRoleGId()); d.setRoleG(e.getRoleG()); d.setServerRole(e.getServerRole());
        d.setDescription(e.getDescription()); d.setCreatedAt(e.getCreatedAt()); d.setState(e.getState());
        return d;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleManagementRequestDTO> listRoleNames(){

        List<RoleManagement> rolesEntity = roleManagementRepo.findByStateTrue();

        return rolesEntity.stream().map(rolEntity -> {
            RoleManagementRequestDTO dto = new RoleManagementRequestDTO();           
            dto.setRoleGId(rolEntity.getRoleGId()); 
            dto.setRoleG(rolEntity.getRoleG());           
            return dto;
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleListManagementResponseDTO> listRolesManagement(String filter, Boolean state){
        try{
            String textFilter = (filter == null) ? "" : filter;
            Boolean stateFilter = (state == null) ? true : state;
            return roleManagementCustomRepo.listRolesManagement(textFilter, stateFilter);
        } catch(Exception e) {
            throw new RuntimeException("Error al cargar el listado de usuarios: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public SpResponseDTO createRoleManagement(RoleManagementRequestDTO roleRequest){
        try{
            return roleManagementCustomRepo.createRoleManagement(roleRequest.getRoleG(), roleRequest.getDescription());
        } catch (Exception e){
            throw new RuntimeException("Error al crear el rol: " + e.getMessage());
        } 
        
    }

    @Override
    @Transactional
    public SpResponseDTO updateRoleManagement(RoleManagementRequestDTO roleRequest){
        try{
            return roleManagementCustomRepo.updateRoleManagement(roleRequest.getRoleGId(), roleRequest.getRoleG(), roleRequest.getDescription());
        } catch (Exception e){
            throw new RuntimeException("Error al editar el rol: " + e.getMessage());
        }
    }
}
