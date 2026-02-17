package com.CLMTZ.Backend.dto.reinforcement.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestPreviewResponseDTO {
    private Integer subjectId;
    private String subjectName;
    private Integer syllabusId;
    private String syllabusName;
    private Short unit;
    private Integer teacherId;
    private String teacherName;
    private String teacherEmail;
    private Integer modalityId;
    private String modalityName;
    private Integer sessionTypeId;
    private String sessionTypeName;
    private Integer timeSlotId;
    private String timeSlotLabel;
    private String timeSlotJson;
}