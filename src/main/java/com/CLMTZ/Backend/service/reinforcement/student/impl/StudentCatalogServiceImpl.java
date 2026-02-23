package com.CLMTZ.Backend.service.reinforcement.student.impl;

import com.CLMTZ.Backend.dto.reinforcement.student.*;
import com.CLMTZ.Backend.repository.reinforcement.student.StudentCatalogRepository;
import com.CLMTZ.Backend.service.reinforcement.student.StudentCatalogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentCatalogServiceImpl implements StudentCatalogService {

    private final StudentCatalogRepository studentCatalogRepository;

    public StudentCatalogServiceImpl(StudentCatalogRepository studentCatalogRepository) {
        this.studentCatalogRepository = studentCatalogRepository;
    }

    @Override
    public List<SubjectItemDTO> getSubjects() {
        return studentCatalogRepository.listSubjects();
    }

    @Override
    public List<SyllabusItemDTO> getSyllabiBySubject(Integer subjectId) {
        return studentCatalogRepository.listSyllabiBySubject(subjectId);
    }

    @Override
    public List<TeacherItemDTO> getTeachers(Integer modalityId) {
        return studentCatalogRepository.listTeachers(modalityId);
    }

    @Override
    public List<ModalityItemDTO> getModalities() {
        return studentCatalogRepository.listModalities();
    }

    @Override
    public List<SessionTypeItemDTO> getSessionTypes() {
        return studentCatalogRepository.listSessionTypes();
    }

    @Override
    public List<TimeSlotItemDTO> getTimeSlots() {
        return studentCatalogRepository.listTimeSlots();
    }

    @Override
    public List<AvailableTimeSlotDTO> getAvailableTimeSlots(Integer teacherId, Short dayOfWeek, Integer periodId) {
        return studentCatalogRepository.listAvailableTimeSlots(teacherId, dayOfWeek, periodId);
    }

    @Override
    public boolean isTimeSlotAvailable(Integer teacherId, Short dayOfWeek, Integer periodId, Integer timeSlotId) {
        return studentCatalogRepository.isTimeSlotAvailable(teacherId, dayOfWeek, periodId, timeSlotId);
    }
}