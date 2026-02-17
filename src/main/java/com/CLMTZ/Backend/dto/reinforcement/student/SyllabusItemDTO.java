package com.CLMTZ.Backend.dto.reinforcement.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyllabusItemDTO {
    private Integer syllabusId;
    private String syllabusName;
    private Short unit;
}