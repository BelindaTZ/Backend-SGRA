package com.CLMTZ.Backend.repository.reinforcement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CLMTZ.Backend.model.reinforcement.TeacherAvailability;

public interface ITeacherAvailabilityRepository extends JpaRepository<TeacherAvailability, Integer> {

    List<TeacherAvailability> findByTeacherId_TeachingIdAndPeriodId_PeriodId(Integer teachingId, Integer periodId);

    void deleteByTeacherId_TeachingIdAndPeriodId_PeriodId(Integer teachingId, Integer periodId);

    List<TeacherAvailability> findByTeacherId_TeachingId(Integer teachingId);
}
