package com.CLMTZ.Backend.dto.security.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO concreto para la respuesta de listado de roles de gestión.
 * Implementa la interfaz RoleListManagementResponseDTO para compatibilidad.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleListManagementDTO implements RoleListManagementResponseDTO {
    private Integer idg;
    private String nombreg;
    private String descripciong;
    private String estadog;
    private Long permisosg;
    private LocalDate fechacreaciong;

    @Override
    public Integer getIdg() { return idg; }

    @Override
    public String getNombreg() { return nombreg; }

    @Override
    public String getDescripciong() { return descripciong; }

    @Override
    public String getEstadog() { return estadog; }

    @Override
    public Long getPermisosg() { return permisosg; }

    @Override
    public LocalDate getFechacreaciong() { return fechacreaciong; }
}

