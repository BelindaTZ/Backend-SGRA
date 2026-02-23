package com.CLMTZ.Backend.service.reinforcement.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.CLMTZ.Backend.dto.reinforcement.TeacherAvailabilityDTO;
import com.CLMTZ.Backend.dto.reinforcement.TeacherAvailabilityBatchDTO;
import com.CLMTZ.Backend.model.reinforcement.TeacherAvailability;
import com.CLMTZ.Backend.model.academic.Teaching;
import com.CLMTZ.Backend.model.academic.Period;
import com.CLMTZ.Backend.repository.reinforcement.ITeacherAvailabilityRepository;
import com.CLMTZ.Backend.repository.academic.ITeachingRepository;
import com.CLMTZ.Backend.repository.academic.ITimeSlotRepository;
import com.CLMTZ.Backend.repository.academic.IPeriodRepository;
import com.CLMTZ.Backend.service.reinforcement.ITeacherAvailabilityService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherAvailabilityServiceImpl implements ITeacherAvailabilityService {

    private final ITeacherAvailabilityRepository repository;
    private final ITeachingRepository teachingRepository;
    private final ITimeSlotRepository timeSlotRepository;
    private final IPeriodRepository periodRepository;

    @Override
    public List<TeacherAvailabilityDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public TeacherAvailabilityDTO findById(Integer id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("TeacherAvailability not found with id: " + id));
    }

    @Override
    public TeacherAvailabilityDTO save(TeacherAvailabilityDTO dto) {
        TeacherAvailability e = new TeacherAvailability();
        e.setDayOfWeek(dto.getDayOfWeek());
        e.setState(dto.getState() != null ? dto.getState() : true);
        if (dto.getTeacherId() != null)
            e.setTeacherId(teachingRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teaching not found")));
        if (dto.getTimeSlotId() != null)
            e.setTimeSlotId(timeSlotRepository.findById(dto.getTimeSlotId())
                    .orElseThrow(() -> new RuntimeException("TimeSlot not found")));
        if (dto.getPeriodId() != null)
            e.setPeriodId(periodRepository.findById(dto.getPeriodId())
                    .orElseThrow(() -> new RuntimeException("Period not found")));
        return toDTO(repository.save(e));
    }

    @Override
    public TeacherAvailabilityDTO update(Integer id, TeacherAvailabilityDTO dto) {
        TeacherAvailability e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeacherAvailability not found with id: " + id));
        e.setDayOfWeek(dto.getDayOfWeek());
        e.setState(dto.getState());
        if (dto.getTeacherId() != null)
            e.setTeacherId(teachingRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teaching not found")));
        if (dto.getTimeSlotId() != null)
            e.setTimeSlotId(timeSlotRepository.findById(dto.getTimeSlotId())
                    .orElseThrow(() -> new RuntimeException("TimeSlot not found")));
        if (dto.getPeriodId() != null)
            e.setPeriodId(periodRepository.findById(dto.getPeriodId())
                    .orElseThrow(() -> new RuntimeException("Period not found")));
        return toDTO(repository.save(e));
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public List<TeacherAvailabilityDTO> saveBatch(TeacherAvailabilityBatchDTO dto) {
        Teaching teacher = teachingRepository.findByUserId_UserId(dto.getUserId())
                .orElseThrow(
                        () -> new RuntimeException("Docente no encontrado para usuario con id: " + dto.getUserId()));
        Period period = periodRepository.findById(dto.getPeriodId())
                .orElseThrow(() -> new RuntimeException("Periodo no encontrado con id: " + dto.getPeriodId()));

        // Eliminar disponibilidades existentes del docente para este periodo
        repository.deleteByTeacherId_TeachingIdAndPeriodId_PeriodId(teacher.getTeachingId(), dto.getPeriodId());

        // Crear las nuevas disponibilidades
        List<TeacherAvailability> entities = new ArrayList<>();
        for (TeacherAvailabilityBatchDTO.SlotDTO slot : dto.getSlots()) {
            TeacherAvailability entity = new TeacherAvailability();
            entity.setTeacherId(teacher);
            entity.setPeriodId(period);
            entity.setDayOfWeek(slot.getDayOfWeek());
            entity.setTimeSlotId(timeSlotRepository.findById(slot.getTimeSlotId())
                    .orElseThrow(() -> new RuntimeException(
                            "Franja horaria no encontrada con id: " + slot.getTimeSlotId())));
            entity.setState(true);
            entities.add(entity);
        }

        return repository.saveAll(entities).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TeacherAvailabilityDTO> findByUserId(Integer userId) {
        return teachingRepository.findByUserId_UserId(userId)
                .map(teaching -> repository.findByTeacherId_TeachingId(teaching.getTeachingId())
                        .stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @Override
    public List<TeacherAvailabilityDTO> findByTeachingId(Integer teachingId) {
        return repository.findByTeacherId_TeachingId(teachingId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TeacherAvailabilityDTO toDTO(TeacherAvailability e) {
        TeacherAvailabilityDTO d = new TeacherAvailabilityDTO();
        d.setTeacherAvailabilityId(e.getTeacherAvailabilityId());
        d.setDayOfWeek(e.getDayOfWeek());
        d.setState(e.getState());
        d.setTeacherId(e.getTeacherId() != null ? e.getTeacherId().getTeachingId() : null);
        d.setTimeSlotId(e.getTimeSlotId() != null ? e.getTimeSlotId().getTimeSlotId() : null);
        d.setPeriodId(e.getPeriodId() != null ? e.getPeriodId().getPeriodId() : null);
        return d;
    }
}
