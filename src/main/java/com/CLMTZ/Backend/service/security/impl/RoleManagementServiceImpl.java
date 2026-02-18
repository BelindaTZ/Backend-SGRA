package com.CLMTZ.Backend.service.security.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CLMTZ.Backend.dto.security.RoleManagementDTO;
import com.CLMTZ.Backend.dto.security.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListResponseDTO;
import com.CLMTZ.Backend.model.security.RoleManagement;
import com.CLMTZ.Backend.repository.security.IRoleManagementRepository;
import com.CLMTZ.Backend.service.security.IRoleManagementService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleManagementServiceImpl implements IRoleManagementService {

    private final IRoleManagementRepository roleManagementRepo;
    private final EntityManager entityManager;

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
    public List<RoleListResponseDTO> listRoles(String filter, Boolean state){
        String textFilter = (filter == null) ? "" : filter;
        Boolean stateFilter = (state == null) ? true : state;

        return roleManagementRepo.listRoles(textFilter, stateFilter);
    }

    @Override
    @Transactional
    public SpResponseDTO createGRole(RoleManagementDTO roleRequest){
        SpResponseDTO responseDTO;

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("seguridad.sp_in_creargrol");

        query.registerStoredProcedureParameter("p_grol", String.class, ParameterMode.IN);

        query.registerStoredProcedureParameter("p_descripcion", String.class, ParameterMode.IN);

        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);

        query.setParameter("p_grol", roleRequest.getRoleG());
        query.setParameter("p_descripcion", roleRequest.getDescription());

        query.execute();

        String message = (String) query.getOutputParameterValue("p_mensaje");
        Boolean success = (Boolean) query.getOutputParameterValue("p_exito");

        responseDTO = new SpResponseDTO(message,success);

        return responseDTO;
    }

    @Override
    @Transactional
    public SpResponseDTO updateGRole(RoleManagementDTO roleRequest){
        SpResponseDTO responseDTO;

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("seguridad.sp_up_grol");

        query.registerStoredProcedureParameter("p_idgrol", Integer.class, ParameterMode.IN);

        query.registerStoredProcedureParameter("p_grol", String.class, ParameterMode.IN);

        query.registerStoredProcedureParameter("p_descripcion", String.class, ParameterMode.IN);

        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);

        query.setParameter("p_idgrol", roleRequest.getRoleGId());
        query.setParameter("p_grol", roleRequest.getRoleG());
        query.setParameter("p_descripcion", roleRequest.getDescription());

        query.execute();

        String message = (String) query.getOutputParameterValue("p_mensaje");
        Boolean success = (Boolean) query.getOutputParameterValue("p_exito");

        responseDTO = new SpResponseDTO(message,success);

        return responseDTO;
    }
}
