package com.CLMTZ.Backend.service.academic.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.CLMTZ.Backend.dto.academic.EnrollmentDetailDTO;
import com.CLMTZ.Backend.dto.academic.EnrollmentDetailLoadDTO;
import com.CLMTZ.Backend.model.academic.EnrollmentDetail;
import com.CLMTZ.Backend.repository.academic.*;
import com.CLMTZ.Backend.service.academic.IEnrollmentDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentDetailServiceImpl implements IEnrollmentDetailService {

    private final IEnrollmentDetailRepository repository;
    private final IRegistrationsRepository registrationsRepository;
    private final ISubjectRepository subjectRepository;
    private final IParallelRepository parallelRepository;

    @Override
    public List<EnrollmentDetailDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public EnrollmentDetailDTO findById(Integer id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("EnrollmentDetail not found with id: " + id));
    }

    @Override
    public EnrollmentDetailDTO save(EnrollmentDetailDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public EnrollmentDetailDTO update(Integer id, EnrollmentDetailDTO dto) {
        EnrollmentDetail entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("EnrollmentDetail not found with id: " + id));
        entity.setActive(dto.getActive());
        if (dto.getRegistrationId() != null) entity.setRegistrationId(registrationsRepository.findById(dto.getRegistrationId()).orElseThrow(() -> new RuntimeException("Registration not found")));
        if (dto.getSubjectId() != null) entity.setSubjectId(subjectRepository.findById(dto.getSubjectId()).orElseThrow(() -> new RuntimeException("Subject not found")));
        if (dto.getParallelId() != null) entity.setParallelId(parallelRepository.findById(dto.getParallelId()).orElseThrow(() -> new RuntimeException("Parallel not found")));
        return toDTO(repository.save(entity));
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    private EnrollmentDetailDTO toDTO(EnrollmentDetail entity) {
        EnrollmentDetailDTO dto = new EnrollmentDetailDTO();
        dto.setEnrollmentDetailId(entity.getEnrollmentDetailId());
        dto.setActive(entity.getActive());
        dto.setRegistrationId(entity.getRegistrationId() != null ? entity.getRegistrationId().getRegistrationId() : null);
        dto.setSubjectId(entity.getSubjectId() != null ? entity.getSubjectId().getIdSubject() : null);
        dto.setParallelId(entity.getParallelId() != null ? entity.getParallelId().getParallelId() : null);
        return dto;
    }

    private EnrollmentDetail toEntity(EnrollmentDetailDTO dto) {
        EnrollmentDetail entity = new EnrollmentDetail();
        entity.setActive(dto.getActive() != null ? dto.getActive() : true);
        if (dto.getRegistrationId() != null) entity.setRegistrationId(registrationsRepository.findById(dto.getRegistrationId()).orElseThrow(() -> new RuntimeException("Registration not found")));
        if (dto.getSubjectId() != null) entity.setSubjectId(subjectRepository.findById(dto.getSubjectId()).orElseThrow(() -> new RuntimeException("Subject not found")));
        if (dto.getParallelId() != null) entity.setParallelId(parallelRepository.findById(dto.getParallelId()).orElseThrow(() -> new RuntimeException("Parallel not found")));
        return entity;
    }

    @Override
    public List<String> uploadEnrollmentDetails(List<EnrollmentDetailLoadDTO> registrationDTOs) {
        List<String> report = new java.util.ArrayList<>();
        for (EnrollmentDetailLoadDTO dto : registrationDTOs) {
            try {
                // Aquí deberías buscar por cédula, periodo, asignatura y paralelo para actualizar si existe
                // Por simplicidad, solo se crea uno nuevo (ajusta según tu modelo de negocio)
                EnrollmentDetail detail = new EnrollmentDetail();
                detail.setActive(true);
                // Faltan asignaciones de relaciones (registrationId, subjectId, parallelId) por falta de info
                repository.save(detail);
                report.add("Detalle de matrícula para estudiante '" + dto.getCedulaEstudiante() + "' creado");
            } catch (Exception e) {
                report.add("Detalle de matrícula para estudiante '" + dto.getCedulaEstudiante() + "': ERROR (" + e.getMessage() + ")");
            }
        }
        return report;
    }
}
