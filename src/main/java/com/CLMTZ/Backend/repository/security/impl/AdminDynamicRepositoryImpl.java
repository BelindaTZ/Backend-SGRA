package com.CLMTZ.Backend.repository.security.impl;

import com.CLMTZ.Backend.config.DynamicDataSourceService;
import com.CLMTZ.Backend.dto.security.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListManagementDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.UserListManagementDTO;
import com.CLMTZ.Backend.dto.security.Response.UserListManagementResponseDTO;
import com.CLMTZ.Backend.repository.security.IAdminDynamicRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AdminDynamicRepositoryImpl implements IAdminDynamicRepository {

    private final DynamicDataSourceService dynamicDataSourceService;

    public AdminDynamicRepositoryImpl(DynamicDataSourceService dynamicDataSourceService) {
        this.dynamicDataSourceService = dynamicDataSourceService;
    }

    private NamedParameterJdbcTemplate getJdbcTemplate() {
        return dynamicDataSourceService.getJdbcTemplate();
    }

    // ==================== USUARIOS ====================

    @Override
    public List<UserListManagementResponseDTO> listUsersManagement(String filterUser, LocalDate date, Boolean state) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_filtro_usuario", filterUser)
                .addValue("p_fecha", date)
                .addValue("p_estado", state);

        List<UserListManagementResponseDTO> results = new ArrayList<>();

        getJdbcTemplate().query(
                "SELECT * FROM seguridad.fn_sl_gusuarios(:p_filtro_usuario, :p_fecha, :p_estado)",
                params,
                rs -> {
                    UserListManagementDTO dto = new UserListManagementDTO();
                    dto.setIdgu(rs.getInt("idgu"));
                    dto.setUsuariogu(rs.getString("usuariogu"));
                    dto.setRolesasignadosgu(rs.getLong("rolesasignadosgu"));
                    dto.setEstadogu(rs.getString("estadogu"));
                    dto.setFechacreaciongu(rs.getDate("fechacreaciongu") != null
                            ? rs.getDate("fechacreaciongu").toLocalDate() : null);
                    results.add(dto);
                });

        return results;
    }

    @Override
    public SpResponseDTO createGUser(String user, String password) {
        return executeStoredProcedure("seguridad", "sp_in_creargusuario",
                new MapSqlParameterSource()
                        .addValue("p_gusuario", user)
                        .addValue("p_gcontrasena", password));
    }

    @Override
    public SpResponseDTO updateGUser(Integer userId, String user, String password) {
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





