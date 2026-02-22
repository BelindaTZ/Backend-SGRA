package com.CLMTZ.Backend.repository.academic;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CLMTZ.Backend.model.academic.ClassSchedule;

public interface IClassScheduleRepository extends JpaRepository<ClassSchedule, Integer> {
	boolean existsByAssignedClassId_IdClassAndPeriodId_PeriodIdAndDayAndTimeSlotId_TimeSlotId(
			Integer classId,
			Integer periodId,
			Short day,
			Integer timeSlotId);
}
