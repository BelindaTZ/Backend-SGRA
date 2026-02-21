package com.CLMTZ.Backend.repository.security.custom.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.CLMTZ.Backend.config.DynamicDataSourceService;
import com.CLMTZ.Backend.dto.security.Response.MasterDataListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterTableListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.ModuleListManagementResponseDTO;
import com.CLMTZ.Backend.repository.security.custom.IModuleCustomManagementRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ModuleCustomManagementRepository implements IModuleCustomManagementRepository{
    @PersistenceContext
    private EntityManager entityManager;

    private final DynamicDataSourceService dynamicDataSourceService;

    private NamedParameterJdbcTemplate getJdbcTemplate() {
        return dynamicDataSourceService.getJdbcTemplate();
    }

    @Override
    public List<ModuleListManagementResponseDTO> listModuleManagements(String grole){
        String query = "Select * from seguridad.fn_sl_privilegios_tablas_roles(:rol)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("rol", grole);
        
        return getJdbcTemplate().query(query, params, new BeanPropertyRowMapper<>(ModuleListManagementResponseDTO.class));
    }

    public List<MasterTableListManagementResponseDTO> listMasterTables(){
        String query = "Select * from seguridad.fn_sl_tablas_maestras()";
        
        return getJdbcTemplate().query(query, new BeanPropertyRowMapper<>(MasterTableListManagementResponseDTO.class));
    }

    public List<MasterDataListManagementResponseDTO> listDataMasterTables(String schemaTable){
        String query = "Select * from seguridad.fn_sl_datos_tablas_maestras(:p_esquematabla)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_esquematabla", schemaTable);

        return getJdbcTemplate().query(query, params, new BeanPropertyRowMapper<>(MasterDataListManagementResponseDTO.class));
    }
}
