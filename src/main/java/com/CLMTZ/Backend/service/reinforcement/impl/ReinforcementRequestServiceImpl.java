package com.CLMTZ.Backend.service.reinforcement.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.CLMTZ.Backend.dto.reinforcement.ReinforcementRequestDTO;
import com.CLMTZ.Backend.model.reinforcement.ReinforcementRequest;
import com.CLMTZ.Backend.repository.reinforcement.IReinforcementRequestRepository;
import com.CLMTZ.Backend.repository.reinforcement.IReinforcementRequestStatusRepository;
import com.CLMTZ.Backend.repository.academic.IStudentsRepository;
import com.CLMTZ.Backend.repository.academic.ITeachingRepository;
import com.CLMTZ.Backend.repository.academic.ISyllabiRepository;
import com.CLMTZ.Backend.repository.academic.ITimeSlotRepository;
import com.CLMTZ.Backend.repository.academic.IModalityRepository;
import com.CLMTZ.Backend.repository.academic.IPeriodRepository;
import com.CLMTZ.Backend.repository.reinforcement.ISessionTypesRepository;
import com.CLMTZ.Backend.service.reinforcement.IReinforcementRequestService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReinforcementRequestServiceImpl implements IReinforcementRequestService {

    private final IReinforcementRequestRepository repository;
    private final IStudentsRepository studentsRepository;
    private final ITeachingRepository teachingRepository;
    private final ISyllabiRepository syllabiRepository;
    private final ITimeSlotRepository timeSlotRepository;
    private final IModalityRepository modalityRepository;
    private final ISessionTypesRepository sessionTypesRepository;
    private final IReinforcementRequestStatusRepository requestStatusRepository;
    private final IPeriodRepository periodRepository;

    @Override
    public List<ReinforcementRequestDTO> findAll() { return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList()); }

    @Override
    public ReinforcementRequestDTO findById(Integer id) { return repository.findById(id).map(this::toDTO).orElseThrow(() -> new RuntimeException("ReinforcementRequest not found with id: " + id)); }

    @Override
    public ReinforcementRequestDTO save(ReinforcementRequestDTO dto) {
        ReinforcementRequest e = toEntity(dto);
        return toDTO(repository.save(e));
    }

    @Override
    public ReinforcementRequestDTO update(Integer id, ReinforcementRequestDTO dto) {
        ReinforcementRequest e = repository.findById(id).orElseThrow(() -> new RuntimeException("ReinforcementRequest not found with id: " + id));
        e.setRequestedDay(dto.getRequestedDay()); e.setReason(dto.getReason()); e.setFileUrl(dto.getFileUrl()); e.setCreatedAt(dto.getCreatedAt());
        if (dto.getStudentId() != null) e.setStudentId(studentsRepository.findById(dto.getStudentId()).orElseThrow(() -> new RuntimeException("Student not found")));
        if (dto.getTeacherId() != null) e.setTeacherId(teachingRepository.findById(dto.getTeacherId()).orElseThrow(() -> new RuntimeException("Teaching not found")));
        if (dto.getTopicId() != null) e.setTopicId(syllabiRepository.findById(dto.getTopicId()).orElseThrow(() -> new RuntimeException("Syllabi not found")));
        if (dto.getTimeSlotId() != null) e.setTimeSlotId(timeSlotRepository.findById(dto.getTimeSlotId()).orElseThrow(() -> new RuntimeException("TimeSlot not found")));
        if (dto.getModalityId() != null) e.setModalityId(modalityRepository.findById(dto.getModalityId()).orElseThrow(() -> new RuntimeException("Modality not found")));
        if (dto.getSessionTypeId() != null) e.setSessionTypeId(sessionTypesRepository.findById(dto.getSessionTypeId()).orElseThrow(() -> new RuntimeException("SessionTypes not found")));
        if (dto.getRequestStatusId() != null) e.setRequestStatusId(requestStatusRepository.findById(dto.getRequestStatusId()).orElseThrow(() -> new RuntimeException("ReinforcementRequestStatus not found")));
        if (dto.getPeriodId() != null) e.setPeriodId(periodRepository.findById(dto.getPeriodId()).orElseThrow(() -> new RuntimeException("Period not found")));
        return toDTO(repository.save(e));
    }

    @Override
    public void deleteById(Integer id) { repository.deleteById(id); }

    private ReinforcementRequestDTO toDTO(ReinforcementRequest e) {
        ReinforcementRequestDTO d = new ReinforcementRequestDTO();
        d.setReinforcementRequestId(e.getReinforcementRequestId());
        d.setRequestedDay(e.getRequestedDay()); d.setReason(e.getReason()); d.setFileUrl(e.getFileUrl()); d.setCreatedAt(e.getCreatedAt());
        d.setStudentId(e.getStudentId() != null ? e.getStudentId().getStudentId() : null);
        d.setTeacherId(e.getTeacherId() != null ? e.getTeacherId().getTeachingId() : null);
        d.setTopicId(e.getTopicId() != null ? e.getTopicId().getSyllabiId() : null);
        d.setTimeSlotId(e.getTimeSlotId() != null ? e.getTimeSlotId().getTimeSlotId() : null);
        d.setModalityId(e.getModalityId() != null ? e.getModalityId().getIdModality() : null);
        d.setSessionTypeId(e.getSessionTypeId() != null ? e.getSessionTypeId().getSesionTypesId() : null);
        d.setRequestStatusId(e.getRequestStatusId() != null ? e.getRequestStatusId().getIdReinforcementRequestStatus() : null);
        d.setPeriodId(e.getPeriodId() != null ? e.getPeriodId().getPeriodId() : null);
        return d;
    }

    private ReinforcementRequest toEntity(ReinforcementRequestDTO dto) {
        ReinforcementRequest e = new ReinforcementRequest();
        e.setRequestedDay(dto.getRequestedDay()); e.setReason(dto.getReason()); e.setFileUrl(dto.getFileUrl()); e.setCreatedAt(dto.getCreatedAt());
        if (dto.getStudentId() != null) e.setStudentId(studentsRepository.findById(dto.getStudentId()).orElseThrow(() -> new RuntimeException("Student not found")));
        if (dto.getTeacherId() != null) e.setTeacherId(teachingRepository.findById(dto.getTeacherId()).orElseThrow(() -> new RuntimeException("Teaching not found")));
        if (dto.getTopicId() != null) e.setTopicId(syllabiRepository.findById(dto.getTopicId()).orElseThrow(() -> new RuntimeException("Syllabi not found")));
        if (dto.getTimeSlotId() != null) e.setTimeSlotId(timeSlotRepository.findById(dto.getTimeSlotId()).orElseThrow(() -> new RuntimeException("TimeSlot not found")));
        if (dto.getModalityId() != null) e.setModalityId(modalityRepository.findById(dto.getModalityId()).orElseThrow(() -> new RuntimeException("Modality not found")));
        if (dto.getSessionTypeId() != null) e.setSessionTypeId(sessionTypesRepository.findById(dto.getSessionTypeId()).orElseThrow(() -> new RuntimeException("SessionTypes not found")));
        if (dto.getRequestStatusId() != null) e.setRequestStatusId(requestStatusRepository.findById(dto.getRequestStatusId()).orElseThrow(() -> new RuntimeException("ReinforcementRequestStatus not found")));
        if (dto.getPeriodId() != null) e.setPeriodId(periodRepository.findById(dto.getPeriodId()).orElseThrow(() -> new RuntimeException("Period not found")));
        return e;
    }
}
