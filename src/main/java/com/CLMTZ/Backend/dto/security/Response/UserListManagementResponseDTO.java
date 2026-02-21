package com.CLMTZ.Backend.dto.security.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO concreto para la respuesta de listado de usuarios de gestión.
 * Implementa la interfaz UserListManagementResponseDTO para compatibilidad.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListManagementResponseDTO {
    private Integer idgu;
    private String usuariogu;
    private Long rolesasignadosgu;
    private String estadogu;
    private LocalDate fechacreaciongu;
}

