package com.CLMTZ.Backend.service.reinforcement.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.CLMTZ.Backend.dto.reinforcement.ScheduledReinforcementDTO;
import com.CLMTZ.Backend.model.reinforcement.ScheduledReinforcement;
import com.CLMTZ.Backend.repository.reinforcement.IScheduledReinforcementRepository;
import com.CLMTZ.Backend.repository.academic.IModalityRepository;
import com.CLMTZ.Backend.repository.academic.ITimeSlotRepository;
import com.CLMTZ.Backend.repository.reinforcement.ISessionTypesRepository;
import com.CLMTZ.Backend.service.reinforcement.IScheduledReinforcementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduledReinforcementServiceImpl implements IScheduledReinforcementService {

    private final IScheduledReinforcementRepository repository;
    private final ISessionTypesRepository sessionTypesRepository;
    private final IModalityRepository modalityRepository;
    private final ITimeSlotRepository timeSlotRepository;

    @Override
    public List<ScheduledReinforcementDTO> findAll() { return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList()); }

    @Override
    public ScheduledReinforcementDTO findById(Integer id) { return repository.findById(id).map(this::toDTO).orElseThrow(() -> new RuntimeException("ScheduledReinforcement not found with id: " + id)); }

    @Override
    public ScheduledReinforcementDTO save(ScheduledReinforcementDTO dto) {
        ScheduledReinforcement e = new ScheduledReinforcement();
        e.setEstimatedTime(dto.getEstimatedTime()); e.setReason(dto.getReason()); e.setNewSchedule(dto.getNewSchedule()); e.setState(dto.getState());
        if (dto.getSessionTypeId() != null) e.setSessionTypeId(sessionTypesRepository.findById(dto.getSessionTypeId()).orElseThrow(() -> new RuntimeException("SessionTypes not found")));
        if (dto.getModalityId() != null) e.setModalityId(modalityRepository.findById(dto.getModalityId()).orElseThrow(() -> new RuntimeException("Modality not found")));
        if (dto.getTimeSlotId() != null) e.setTimeSlotId(timeSlotRepository.findById(dto.getTimeSlotId()).orElseThrow(() -> new RuntimeException("TimeSlot not found")));
        return toDTO(repository.save(e));
    }

    @Override
    public ScheduledReinforcementDTO update(Integer id, ScheduledReinforcementDTO dto) {
        ScheduledReinforcement e = repository.findById(id).orElseThrow(() -> new RuntimeException("ScheduledReinforcement not found with id: " + id));
        e.setEstimatedTime(dto.getEstimatedTime()); e.setReason(dto.getReason()); e.setNewSchedule(dto.getNewSchedule()); e.setState(dto.getState());
        if (dto.getSessionTypeId() != null) e.setSessionTypeId(sessionTypesRepository.findById(dto.getSessionTypeId()).orElseThrow(() -> new RuntimeException("SessionTypes not found")));
        if (dto.getModalityId() != null) e.setModalityId(modalityRepository.findById(dto.getModalityId()).orElseThrow(() -> new RuntimeException("Modality not found")));
        if (dto.getTimeSlotId() != null) e.setTimeSlotId(timeSlotRepository.findById(dto.getTimeSlotId()).orElseThrow(() -> new RuntimeException("TimeSlot not found")));
        return toDTO(repository.save(e));
    }

    @Override
    public void deleteById(Integer id) { repository.deleteById(id); }

    private ScheduledReinforcementDTO toDTO(ScheduledReinforcement e) {
        ScheduledReinforcementDTO d = new ScheduledReinforcementDTO();
        d.setScheduledReinforcementId(e.getScheduledReinforcementId());
        d.setEstimatedTime(e.getEstimatedTime()); d.setReason(e.getReason()); d.setNewSchedule(e.getNewSchedule()); d.setState(e.getState());
        d.setSessionTypeId(e.getSessionTypeId() != null ? e.getSessionTypeId().getSesionTypesId() : null);
        d.setModalityId(e.getModalityId() != null ? e.getModalityId().getIdModality() : null);
        d.setTimeSlotId(e.getTimeSlotId() != null ? e.getTimeSlotId().getTimeSlotId() : null);
        return d;
    }
}
