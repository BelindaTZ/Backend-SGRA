package com.CLMTZ.Backend.repository.security.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.CLMTZ.Backend.config.DynamicDataSourceService;
import com.CLMTZ.Backend.dto.security.Response.UserListManagementResponseDTO;
import com.CLMTZ.Backend.repository.security.icustom.IUserManagementCustomRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserManagementRepositoryImpl implements IUserManagementCustomRepository{
    
    @PersistenceContext
    private EntityManager entityManager;

    private final DynamicDataSourceService dynamicDataSourceService;

    private NamedParameterJdbcTemplate getJdbcTemplate() {
        return dynamicDataSourceService.getJdbcTemplate();
    }

    @Override
    public List<UserListManagementResponseDTO> listUsersManagement(String filterUser, LocalDate date, Boolean state) {
        String query = "SELECT * FROM seguridad.fn_sl_gusuarios(:p_filtro_usuario, :p_fecha, :p_estado)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_filtro_usuario", filterUser)
                .addValue("p_fecha", date)
                .addValue("p_estado", state);
        
        return getJdbcTemplate().query(query, params, new BeanPropertyRowMapper<>(UserListManagementResponseDTO.class));
    }
}
