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
import com.CLMTZ.Backend.service.security.IUserManagementService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements IUserManagementService {

    private final IUserManagementRepository userManagementRepo;
    private final EntityManager entityManager;

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
            return userManagementRepo.listUsersManagement(filterUser, date, state); 
        } catch (Exception e) {
            throw new RuntimeException("Error al listar a los usuarios" + e.getMessage());
        }  
    }

    @Override
    @Transactional
    public SpResponseDTO createGUser(UserManagementDTO userRequest){

        SpResponseDTO responseDTO;
        
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("seguridad.sp_in_creargusuario");

        query.registerStoredProcedureParameter("p_gusuario", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_gcontrasena", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);

        query.setParameter("p_gusuario", userRequest.getUser());
        query.setParameter("p_gcontrasena", userRequest.getPassword());

        query.execute();

        String message = (String) query.getOutputParameterValue("p_mensaje");
        Boolean success = (Boolean) query.getOutputParameterValue("p_exito");

        responseDTO = new SpResponseDTO(message,success);

        return responseDTO;
    }

    @Override
    @Transactional
    public SpResponseDTO updateGUser(UserManagementDTO userRequest){

        SpResponseDTO responseDTO;

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("seguridad.sp_up_gusuario");

        query.registerStoredProcedureParameter("p_idgusuario", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_gusuario", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_gcontrasena", String.class, ParameterMode.IN);
        
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);

        query.setParameter("p_idgusuario", userRequest.getUserGId());
        query.setParameter("p_gusuario", userRequest.getUser());
        query.setParameter("p_gcontrasena", userRequest.getPassword());

        query.execute();

        String message = (String) query.getOutputParameterValue("p_mensaje");
        Boolean success = (Boolean) query.getOutputParameterValue("p_exito");

        responseDTO = new SpResponseDTO(message,success);

        return responseDTO;
    }
}
