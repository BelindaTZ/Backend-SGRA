package com.CLMTZ.Backend.dto.reinforcement.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestCreateRequestDTO {
    private Integer syllabusId;
    private Integer teacherId;
    private Integer timeSlotId;
    private Integer modalityId;
    private Integer sessionTypeId;
    private String reason;
    private Short requestedDay;
    private String fileUrl;
    private Integer periodId;
}