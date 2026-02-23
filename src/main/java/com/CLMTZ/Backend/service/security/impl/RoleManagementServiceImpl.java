package com.CLMTZ.Backend.service.security.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CLMTZ.Backend.dto.security.Request.RoleManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Response.KpiDashboardManagementResponseDTO;
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
            return roleManagementCustomRepo.updateRoleManagement(roleRequest.getRoleGId(), roleRequest.getRoleG(), roleRequest.getDescription(), roleRequest.getState());
        } catch (Exception e){
            throw new RuntimeException("Error al editar el rol: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public KpiDashboardManagementResponseDTO kpisDashboadrManagement(){

        try {

            Long userActive = roleManagementRepo.countByState(true);
            Long usersInactive = roleManagementRepo.countByState(false);
            Long roleActive = roleManagementRepo.countByState(true);
            Long roleInactive = roleManagementRepo.countByState(false);

            return new KpiDashboardManagementResponseDTO(userActive,usersInactive,roleActive,roleInactive);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar los datos del dashboard: " +e.getMessage());
        }
    }
}
