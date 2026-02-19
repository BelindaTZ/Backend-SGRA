package com.CLMTZ.Backend.dto.security.Response;

import java.time.LocalDate;

public interface UserListManagementResponseDTO {
    Integer getIdgu();
    String getUsuariogu();
    Long getRolesasignadosgu();
    String getEstadogu();
    LocalDate getFechacreaciongu();
}
