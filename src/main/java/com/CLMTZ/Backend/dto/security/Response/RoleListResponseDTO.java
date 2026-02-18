package com.CLMTZ.Backend.dto.security.Response;

import java.time.LocalDate;

public interface RoleListResponseDTO {
    Integer getIdg();
    String getNombreg();
    String getDescripciong();
    String getEstadog();
    Long getPermisosg();
    LocalDate getFechacreaciong();
}
