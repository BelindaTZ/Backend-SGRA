package com.CLMTZ.Backend.service.security.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.CLMTZ.Backend.dto.security.Request.ModuleManagementRequestDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterDataListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterTableListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.ModuleListManagementResponseDTO;
import com.CLMTZ.Backend.model.security.ModuleManagement;
import com.CLMTZ.Backend.repository.security.IModuleManagementRepository;
import com.CLMTZ.Backend.repository.security.custom.IModuleCustomManagementRepository;
import com.CLMTZ.Backend.service.security.IModuleManagementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModuleManagementServiceImpl implements IModuleManagementService {

    private final IModuleManagementRepository moduleManagementRepo;
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
    public List<ModuleListManagementResponseDTO> listModuleManagements(String grole){
        String vgrole = (grole == null) ? "" : grole;
        return moduleManagementCustomRepo.listModuleManagements(vgrole);
    }

    @Override
    public List<MasterTableListManagementResponseDTO> listMasterTables(){
        return moduleManagementCustomRepo.listMasterTables();
    }

    @Override
    public List<MasterDataListManagementResponseDTO> listDataMasterTables(String schemaTables){
        return moduleManagementCustomRepo.listDataMasterTables(schemaTables);
    }
}
