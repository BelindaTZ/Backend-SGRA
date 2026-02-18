package com.CLMTZ.Backend.service.security;

import java.util.List;
import com.CLMTZ.Backend.dto.security.ModuleManagementDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterTableListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.ModuleListManagementResponseDTO;

public interface IModuleManagementService {
    List<ModuleManagementDTO> findAll();
    ModuleManagementDTO findById(Integer id);
    ModuleManagementDTO save(ModuleManagementDTO dto);
    ModuleManagementDTO update(Integer id, ModuleManagementDTO dto);
    void deleteById(Integer id);
    List<ModuleListManagementResponseDTO> listModuleManagerment(String role); 
    List<MasterTableListManagementResponseDTO> listMasterTables();
}
