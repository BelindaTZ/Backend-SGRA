package com.CLMTZ.Backend.repository.reinforcement.student;

import com.CLMTZ.Backend.dto.reinforcement.student.*;

import java.util.List;

public interface StudentCatalogRepository {
    List<SubjectItemDTO> listSubjects();
    List<SyllabusItemDTO> listSyllabiBySubject(Integer subjectId);
    List<TeacherItemDTO> listTeachers(Integer modalityId);
    List<ModalityItemDTO> listModalities();
    List<SessionTypeItemDTO> listSessionTypes();
    List<TimeSlotItemDTO> listTimeSlots();
}