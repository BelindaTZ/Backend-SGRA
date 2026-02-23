package com.CLMTZ.Backend.service.reinforcement;

import java.util.List;
import com.CLMTZ.Backend.dto.reinforcement.TeacherAvailabilityDTO;
import com.CLMTZ.Backend.dto.reinforcement.TeacherAvailabilityBatchDTO;

public interface ITeacherAvailabilityService {
    List<TeacherAvailabilityDTO> findAll();

    TeacherAvailabilityDTO findById(Integer id);

    TeacherAvailabilityDTO save(TeacherAvailabilityDTO dto);

    TeacherAvailabilityDTO update(Integer id, TeacherAvailabilityDTO dto);

    void deleteById(Integer id);

    List<TeacherAvailabilityDTO> saveBatch(TeacherAvailabilityBatchDTO dto);

    List<TeacherAvailabilityDTO> findByUserId(Integer userId);

    List<TeacherAvailabilityDTO> findByTeachingId(Integer teachingId);
}
