package com.CLMTZ.Backend.repository.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.CLMTZ.Backend.dto.security.Response.RoleListManagementResponseDTO;
import com.CLMTZ.Backend.model.security.RoleManagement;
@Repository
public interface IRoleManagementRepository extends JpaRepository<RoleManagement, Integer> {
    @Query(value = "Select * from seguridad.fn_sl_groles(:p_filtro_texto, :p_estado)", nativeQuery = true)
    List<RoleListManagementResponseDTO> listRoles(@Param("p_filtro_texto") String filter, @Param("p_estado") Boolean state);

    List<RoleManagement> findByStateTrue();
}