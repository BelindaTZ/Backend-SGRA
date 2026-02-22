package com.CLMTZ.Backend.service.security.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CLMTZ.Backend.dto.security.Request.MasterDataManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Request.MasterManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Request.ModuleManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Request.UpdateRolePermissionsRequestDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterDataListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterTableListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.ModuleListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.SpResponseDTO;
import com.CLMTZ.Backend.model.security.ModuleManagement;
import com.CLMTZ.Backend.repository.security.IModuleManagementRepository;
import com.CLMTZ.Backend.repository.security.custom.IModuleCustomManagementRepository;
import com.CLMTZ.Backend.service.security.IModuleManagementService;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class ModuleManagementServiceImpl implements IModuleManagementService {

    private final IModuleManagementRepository moduleManagementRepo;
    private final ObjectMapper objectMapper;
    private final IModuleCustomManagementRepository moduleManagementCustomRepo;

    @Override
    public List<ModuleManagementRequestDTO> findAll() { return moduleManagementRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList()); }

    @Override
    public ModuleManagementRequestDTO findById(Integer id) { return moduleManagementRepo.findById(id).map(this::toDTO).orElseThrow(() -> new RuntimeException("ModuleManagement not found with id: " + id)); }

    @Override
    public ModuleManagementRequestDTO save(ModuleManagementRequestDTO dto) {
        ModuleManagement e = new ModuleManagement();
        e.setModuleG(dto.getModuleG()); e.setState(dto.getState() != null ? dto.getState() : true);
        return toDTO(moduleManagementRepo.save(e));
    }

    @Override
    public ModuleManagementRequestDTO update(Integer id, ModuleManagementRequestDTO dto) {
        ModuleManagement e = moduleManagementRepo.findById(id).orElseThrow(() -> new RuntimeException("ModuleManagement not found with id: " + id));
        e.setModuleG(dto.getModuleG()); e.setState(dto.getState());
        return toDTO(moduleManagementRepo.save(e));
    }

    @Override
    public void deleteById(Integer id) { moduleManagementRepo.deleteById(id); }

    private ModuleManagementRequestDTO toDTO(ModuleManagement e) { ModuleManagementRequestDTO d = new ModuleManagementRequestDTO(); d.setRoleGId(e.getRoleGId()); d.setModuleG(e.getModuleG()); d.setState(e.getState()); return d; }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleListManagementResponseDTO> listModuleManagements(String grole){
        String vgrole = (grole == null) ? "" : grole;
        return moduleManagementCustomRepo.listModuleManagements(vgrole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MasterTableListManagementResponseDTO> listMasterTables(){
        return moduleManagementCustomRepo.listMasterTables();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MasterDataListManagementResponseDTO> listDataMasterTables(String schemaTables){
        return moduleManagementCustomRepo.listDataMasterTables(schemaTables);
    }

    @Override
    @Transactional
    public SpResponseDTO updateRolePermissions(UpdateRolePermissionsRequestDTO updateRolesPermissionsRequest){
        try {
            String jsonPermisos = objectMapper.writeValueAsString(updateRolesPermissionsRequest);

            return moduleManagementCustomRepo.updateRolePermissions(jsonPermisos);

        } catch (Exception e) {
            return new SpResponseDTO("Error al guardar los permisos: " + e.getMessage(), false);
        }
    }

    @Override
    @Transactional
    public SpResponseDTO masterTablesManagement(MasterManagementRequestDTO masterTables){
        try {
            return moduleManagementCustomRepo.masterTablesManagement(masterTables);
        } catch (Exception e) {
            return new SpResponseDTO("Error al guardar el nuevo registro de la tabla: " + e.getMessage(), false);
        }
    }

    @Override
    @Transactional
    public SpResponseDTO masterDataUpdateManagement(MasterDataManagementRequestDTO dataUpdate){
        try {
            System.out.println(dataUpdate);
            return moduleManagementCustomRepo.masterDataUpdateManagement(dataUpdate);
        } catch (Exception e) {
            return new SpResponseDTO("Error al actualizar el registro de la tabla: " + e.getMessage(), false);
        }
    }
}
