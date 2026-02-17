package com.CLMTZ.Backend.service.reinforcement.student;

import com.CLMTZ.Backend.dto.reinforcement.student.*;

import java.util.List;

public interface StudentCatalogService {
    List<SubjectItemDTO> getSubjects();
    List<SyllabusItemDTO> getSyllabiBySubject(Integer subjectId);
    List<TeacherItemDTO> getTeachers(Integer modalityId);
    List<ModalityItemDTO> getModalities();
    List<SessionTypeItemDTO> getSessionTypes();
    List<TimeSlotItemDTO> getTimeSlots();
}