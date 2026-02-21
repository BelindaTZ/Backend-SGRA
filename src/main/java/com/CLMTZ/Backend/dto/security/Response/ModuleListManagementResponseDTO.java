package com.CLMTZ.Backend.dto.security.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleListManagementResponseDTO {
    private String pesquema;
    private String ptabla;
    private String pesquematabla;
    private String pnombre;
    private String pdescripcion;
    private Boolean ppselect;
    private Boolean ppinsert;
    private Boolean ppupdate;
    private Boolean ppdelete;
}
