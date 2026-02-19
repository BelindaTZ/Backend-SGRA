package com.CLMTZ.Backend.repository.security;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.CLMTZ.Backend.dto.security.Response.UserListManagementResponseDTO;
import com.CLMTZ.Backend.model.security.UserManagement;
@Repository
public interface IUserManagementRepository extends JpaRepository<UserManagement, Integer> {
    @Query(value = "Select * from seguridad.fn_sl_gusuarios(:p_filtro_usuario, :p_fecha, :p_estado)", nativeQuery = true)
    List<UserListManagementResponseDTO> listUsersManagement(@Param("p_filtro_usuario") String filterUser, @Param("p_fecha") LocalDate date, @Param("p_estado") Boolean state);
}
