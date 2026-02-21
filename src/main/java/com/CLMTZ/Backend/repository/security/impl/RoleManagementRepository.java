package com.CLMTZ.Backend.repository.security.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.CLMTZ.Backend.config.DynamicDataSourceService;
import com.CLMTZ.Backend.dto.security.Response.RoleListManagementResponseDTO;
import com.CLMTZ.Backend.repository.security.icustom.IRoleManagementCustomRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RoleManagementRepository implements IRoleManagementCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;

    private final DynamicDataSourceService dynamicDataSourceService;

    private NamedParameterJdbcTemplate getJdbcTemplate() {
        return dynamicDataSourceService.getJdbcTemplate();
    }

    @Override
    public List<RoleListManagementResponseDTO> listRolesManagement(String filter,Boolean state){
        String query = "SELECT * FROM seguridad.fn_sl_groles(:p_filtro_texto, :p_estado)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_filtro_texto", filter != null ? filter : "")
                .addValue("p_estado", state != null ? state : true);

        return getJdbcTemplate().query(query, params, new BeanPropertyRowMapper<>(RoleListManagementResponseDTO.class));
    }
}
