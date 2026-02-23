package com.CLMTZ.Backend.service.academic.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.CLMTZ.Backend.dto.academic.SyllabiDTO;
import com.CLMTZ.Backend.dto.academic.SyllabiLoadDTO;
import com.CLMTZ.Backend.model.academic.Syllabi;
import com.CLMTZ.Backend.repository.academic.ISubjectRepository;
import com.CLMTZ.Backend.repository.academic.ISyllabiRepository;
import com.CLMTZ.Backend.service.academic.ISyllabiService;
import com.CLMTZ.Backend.util.ExcelValidator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SyllabiServiceImpl implements ISyllabiService {

    private final ISyllabiRepository repository;
    private final ISubjectRepository subjectRepository;
    private final EntityManager entityManager;

    @Override
    public List<SyllabiDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public SyllabiDTO findById(Integer id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Syllabi not found with id: " + id));
    }

    @Override
    public SyllabiDTO save(SyllabiDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public SyllabiDTO update(Integer id, SyllabiDTO dto) {
        Syllabi entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Syllabi not found with id: " + id));
        entity.setNameSyllabi(dto.getNameSyllabi());
        entity.setUnit(dto.getUnit());
        entity.setState(dto.getState());
        if (dto.getSubjectId() != null) entity.setSubjectId(subjectRepository.findById(dto.getSubjectId()).orElseThrow(() -> new RuntimeException("Subject not found")));
        return toDTO(repository.save(entity));
    }

    @Override
    public void deleteById(Integer id) { repository.deleteById(id); }

    private SyllabiDTO toDTO(Syllabi e) {
        SyllabiDTO dto = new SyllabiDTO();
        dto.setSyllabiId(e.getSyllabiId());
        dto.setNameSyllabi(e.getNameSyllabi());
        dto.setUnit(e.getUnit());
        dto.setState(e.getState());
        dto.setSubjectId(e.getSubjectId() != null ? e.getSubjectId().getIdSubject() : null);
        return dto;
    }

    private Syllabi toEntity(SyllabiDTO dto) {
        Syllabi entity = new Syllabi();
        entity.setNameSyllabi(dto.getNameSyllabi());
        entity.setUnit(dto.getUnit());
        entity.setState(dto.getState() != null ? dto.getState() : true);
        if (dto.getSubjectId() != null) entity.setSubjectId(subjectRepository.findById(dto.getSubjectId()).orElseThrow(() -> new RuntimeException("Subject not found")));
        return entity;
    }

    @Override
    public List<String> uploadSyllabi(List<SyllabiLoadDTO> syllabiDTOs) {
        // Validar y corregir datos
        List<String> resultados = new java.util.ArrayList<>();
        for (SyllabiLoadDTO dto : syllabiDTOs) {
            // Validar/corregir campos
            ExcelValidator.validarYCorregir(dto);
            try {
                // Llamar stored procedure
                String resultado = ejecutarCargaTemarioSP(
                    dto.getCarreraTexto(),
                    dto.getAsignaturaTexto(),
                    dto.getUnidad(), 
                    dto.getNombreTema()
                );
                resultados.add(resultado);
            } catch (Exception e) {
                resultados.add("Error: " + e.getMessage());
            }
        }
        return resultados;
    }

    private String ejecutarCargaTemarioSP(String carreraTexto, String asignaturaTexto, Integer unidad,
            String nombreTema) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("academico.sp_in_carga_temario");
        query.registerStoredProcedureParameter("p_carrera", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_asignatura", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_unidad", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_descripcion", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);
        
        query.setParameter("p_carrera", carreraTexto);
        query.setParameter("p_asignatura", asignaturaTexto);
        query.setParameter("p_unidad", unidad);
        query.setParameter("p_descripcion", nombreTema);
        
        query.execute();
        
        String mensaje = (String) query.getOutputParameterValue("p_mensaje");
        Boolean exito = (Boolean) query.getOutputParameterValue("p_exito");
        return Boolean.TRUE.equals(exito) ? "OK" : "FALLÓ SP: " + mensaje;
    }
}
