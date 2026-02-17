package com.CLMTZ.Backend.dto.reinforcement.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestPreviewRequestDTO {
    private Integer syllabusId;
    private Integer teacherId;
    private Integer timeSlotId;
    private Integer modalityId;
    private Integer sessionTypeId;
}