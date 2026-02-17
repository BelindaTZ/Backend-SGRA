package com.CLMTZ.Backend.repository.reinforcement.student;

import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestCreateRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewResponseDTO;

public interface StudentRequestRepository {
    StudentRequestPreviewResponseDTO previewRequest(StudentRequestPreviewRequestDTO req);
    Integer createRequest(Integer userId, StudentRequestCreateRequestDTO req);
}