package com.CLMTZ.Backend.service.reinforcement.student;

import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestCreateRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestCreateResponseDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewResponseDTO;

public interface StudentRequestService {
    StudentRequestPreviewResponseDTO preview(StudentRequestPreviewRequestDTO req);
    StudentRequestCreateResponseDTO create(StudentRequestCreateRequestDTO req, Integer userId);
}