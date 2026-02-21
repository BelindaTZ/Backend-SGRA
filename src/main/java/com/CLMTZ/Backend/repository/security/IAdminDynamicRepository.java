package com.CLMTZ.Backend.repository.security;

import com.CLMTZ.Backend.dto.security.SpResponseDTO;

/**
 * Repositorio para operaciones de administración que requieren sesión dinámica.
 * Usa las credenciales del usuario logueado para ejecutar las operaciones en la BD.
 */
public interface IAdminDynamicRepository {

    // ==================== USUARIOS ====================

    /**
     * Crea un usuario de gestión.
     * Ejecuta: seguridad.sp_in_creargusuario
     */
    SpResponseDTO createGUser(String user, String password, String roles);

    /**
     * Actualiza un usuario de gestión.
     * Ejecuta: seguridad.sp_up_gusuario
     */
    SpResponseDTO updateGUser(Integer userId, String user, String password, String roles);

    // ==================== ROLES ====================

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

