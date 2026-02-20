package com.CLMTZ.Backend.repository.security;

import com.CLMTZ.Backend.dto.security.SpResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.RoleListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.UserListManagementResponseDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para operaciones de administración que requieren sesión dinámica.
 * Usa las credenciales del usuario logueado para ejecutar las operaciones en la BD.
 */
public interface IAdminDynamicRepository {

    // ==================== USUARIOS ====================

    /**
     * Lista usuarios de gestión con filtros.
     * Ejecuta: seguridad.fn_sl_gusuarios
     */
    List<UserListManagementResponseDTO> listUsersManagement(String filterUser, LocalDate date, Boolean state);

    /**
     * Crea un usuario de gestión.
     * Ejecuta: seguridad.sp_in_creargusuario
     */
    SpResponseDTO createGUser(String user, String password);

    /**
     * Actualiza un usuario de gestión.
     * Ejecuta: seguridad.sp_up_gusuario
     */
    SpResponseDTO updateGUser(Integer userId, String user, String password);

    // ==================== ROLES ====================

    /**
     * Lista roles de gestión con filtros.
     * Ejecuta: seguridad.fn_sl_groles
     */
    List<RoleListManagementResponseDTO> listRoles(String filter, Boolean state);

    /**
     * Crea un rol de gestión.
     * Ejecuta: seguridad.sp_in_creargrol
     */
    SpResponseDTO createGRole(String role, String description);

    /**
     * Actualiza un rol de gestión.
     * Ejecuta: seguridad.sp_up_grol
     */
    SpResponseDTO updateGRole(Integer roleId, String role, String description);
}

