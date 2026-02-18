package com.CLMTZ.Backend.service.security.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.CLMTZ.Backend.dto.security.ModuleManagementDTO;
import com.CLMTZ.Backend.dto.security.Response.MasterTableListManagementResponseDTO;
import com.CLMTZ.Backend.dto.security.Response.ModuleListManagementResponseDTO;
import com.CLMTZ.Backend.model.security.ModuleManagement;
import com.CLMTZ.Backend.repository.security.IModuleManagementRepository;
import com.CLMTZ.Backend.service.security.IModuleManagementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModuleManagementServiceImpl implements IModuleManagementService {

    private final IModuleManagementRepository moduleManagementRepo;

    @Override
    public List<ModuleManagementDTO> findAll() { return moduleManagementRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList()); }

    @Override
    public ModuleManagementDTO findById(Integer id) { return moduleManagementRepo.findById(id).map(this::toDTO).orElseThrow(() -> new RuntimeException("ModuleManagement not found with id: " + id)); }

    @Override
    public ModuleManagementDTO save(ModuleManagementDTO dto) {
        ModuleManagement e = new ModuleManagement();
        e.setModuleG(dto.getModuleG()); e.setState(dto.getState() != null ? dto.getState() : true);
        return toDTO(moduleManagementRepo.save(e));
    }

    @Override
    public ModuleManagementDTO update(Integer id, ModuleManagementDTO dto) {
        ModuleManagement e = moduleManagementRepo.findById(id).orElseThrow(() -> new RuntimeException("ModuleManagement not found with id: " + id));
        e.setModuleG(dto.getModuleG()); e.setState(dto.getState());
        return toDTO(moduleManagementRepo.save(e));
    }

    @Override
    public void deleteById(Integer id) { moduleManagementRepo.deleteById(id); }

    private ModuleManagementDTO toDTO(ModuleManagement e) { ModuleManagementDTO d = new ModuleManagementDTO(); d.setRoleGId(e.getRoleGId()); d.setModuleG(e.getModuleG()); d.setState(e.getState()); return d; }

    @Override
    public List<ModuleListManagementResponseDTO> listModuleManagerment(String grole){
        String vgrole = (grole == null) ? "" : grole;

        return moduleManagementRepo.listModuleManagements(vgrole);
    }

    @Override
    public List<MasterTableListManagementResponseDTO> listMasterTables(){
        return moduleManagementRepo.listMasterTables();
    }
}
