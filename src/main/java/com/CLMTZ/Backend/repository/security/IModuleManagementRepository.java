package com.CLMTZ.Backend.repository.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.CLMTZ.Backend.dto.security.Response.MasterTableListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.ModuleListManagementResponseDTO;
import com.CLMTZ.Backend.model.security.ModuleManagement;

public interface IModuleManagementRepository extends JpaRepository<ModuleManagement, Integer> {
    @Query(value = "Select * from seguridad.fn_sl_privilegios_tablas_roles(:rol)", nativeQuery = true)
    List<ModuleListManagementResponseDTO> listModuleManagements(@Param("rol") String grole);

    @Query(value = "Select * from seguridad.fn_sl_tablas_maestras()", nativeQuery = true)
    List<MasterTableListManagementResponseDTO> listMasterTables();
}
