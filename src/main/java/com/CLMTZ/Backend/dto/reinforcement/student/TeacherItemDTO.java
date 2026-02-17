package com.CLMTZ.Backend.dto.reinforcement.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherItemDTO {
    private Integer teacherId;
    private String fullName;
    private String email;
    private Integer modalityId;
}