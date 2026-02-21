package com.CLMTZ.Backend.service.security;

import java.util.List;

import com.CLMTZ.Backend.dto.security.Request.ModuleManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterDataListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterTableListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.ModuleListManagementResponseDTO;

public interface IModuleManagementService {
    List<ModuleManagementRequestDTO> findAll();
    ModuleManagementRequestDTO findById(Integer id);
    ModuleManagementRequestDTO save(ModuleManagementRequestDTO dto);
    ModuleManagementRequestDTO update(Integer id, ModuleManagementRequestDTO dto);
    void deleteById(Integer id);

    List<ModuleListManagementResponseDTO> listModuleManagements(String role); 
    List<MasterTableListManagementResponseDTO> listMasterTables();
    List<MasterDataListManagementResponseDTO> listDataMasterTables(String schemaTables);
}
