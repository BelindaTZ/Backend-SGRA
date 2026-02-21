package com.CLMTZ.Backend.repository.security.impl;

import com.CLMTZ.Backend.config.DynamicDataSourceService;
import com.CLMTZ.Backend.dto.security.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListManagementDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListManagementResponseDTO;
import com.CLMTZ.Backend.repository.security.IAdminDynamicRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AdminDynamicRepositoryImpl implements IAdminDynamicRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final DynamicDataSourceService dynamicDataSourceService;

    private NamedParameterJdbcTemplate getJdbcTemplate() {
        return dynamicDataSourceService.getJdbcTemplate();
    }

    // ==================== USUARIOS ====================

    @Override
    public SpResponseDTO createGUser(String user, String password, String roles) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("seguridad.sp_in_creargusuario");

        query.registerStoredProcedureParameter("p_gusuario", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_gcontrasena", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_roles", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);

        query.setParameter("p_gusuario", user);
        query.setParameter("p_gcontrasena", password);
        query.setParameter("p_roles", roles);

        query.execute();

        String message = (String) query.getOutputParameterValue("p_mensaje");
        Boolean success = (Boolean) query.getOutputParameterValue("p_exito");

        return new SpResponseDTO(message, success);
    }

    @Override
    public SpResponseDTO updateGUser(Integer userId, String user, String password, String roles) {
        return executeStoredProcedure("seguridad", "sp_up_gusuario",
                new MapSqlParameterSource()
                        .addValue("p_idgusuario", userId)
                        .addValue("p_gusuario", user)
                        .addValue("p_gcontrasena", password));
    }

    // ==================== ROLES ====================

    @Override
    public List<RoleListManagementResponseDTO> listRoles(String filter, Boolean state) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_filtro_texto", filter != null ? filter : "")
                .addValue("p_estado", state != null ? state : true);

        List<RoleListManagementResponseDTO> results = new ArrayList<>();

        getJdbcTemplate().query(
                "SELECT * FROM seguridad.fn_sl_groles(:p_filtro_texto, :p_estado)",
                params,
                rs -> {
                    RoleListManagementDTO dto = new RoleListManagementDTO();
                    dto.setIdg(rs.getInt("idg"));
                    dto.setNombreg(rs.getString("nombreg"));
                    dto.setDescripciong(rs.getString("descripciong"));
                    dto.setEstadog(rs.getString("estadog"));
                    dto.setPermisosg(rs.getLong("permisosg"));
                    dto.setFechacreaciong(rs.getDate("fechacreaciong") != null
                            ? rs.getDate("fechacreaciong").toLocalDate() : null);
                    results.add(dto);
                });

        return results;
    }

    @Override
    public SpResponseDTO createGRole(String role, String description) {
        return executeStoredProcedure("seguridad", "sp_in_creargrol",
                new MapSqlParameterSource()
                        .addValue("p_grol", role)
                        .addValue("p_descripcion", description));
    }

    @Override
    public SpResponseDTO updateGRole(Integer roleId, String role, String description) {
        return executeStoredProcedure("seguridad", "sp_up_grol",
                new MapSqlParameterSource()
                        .addValue("p_idgrol", roleId)
                        .addValue("p_grol", role)
                        .addValue("p_descripcion", description));
    }

    // ==================== UTILIDADES ====================

    private SpResponseDTO executeStoredProcedure(String schema, String procedureName, MapSqlParameterSource params) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dynamicDataSourceService.getDataSource())
                    .withSchemaName(schema)
                    .withProcedureName(procedureName);

            Map<String, Object> result = jdbcCall.execute(params);

            String message = (String) result.get("p_mensaje");
            Boolean success = (Boolean) result.get("p_exito");

            return new SpResponseDTO(message, success);
        } catch (Exception e) {
            return new SpResponseDTO("Error: " + e.getMessage(), false);
        }
    }
}





