package com.CLMTZ.Backend.service.reinforcement.student.impl;

import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestCreateRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestCreateResponseDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewResponseDTO;
import com.CLMTZ.Backend.repository.reinforcement.student.StudentRequestRepository;
import com.CLMTZ.Backend.service.reinforcement.student.StudentRequestService;
import org.springframework.stereotype.Service;

@Service
public class StudentRequestServiceImpl implements StudentRequestService {

    private final StudentRequestRepository studentRequestRepository;

    public StudentRequestServiceImpl(StudentRequestRepository studentRequestRepository) {
        this.studentRequestRepository = studentRequestRepository;
    }

    @Override
    public StudentRequestPreviewResponseDTO preview(StudentRequestPreviewRequestDTO req) {
        return studentRequestRepository.previewRequest(req);
    }

    @Override
    public StudentRequestCreateResponseDTO create(StudentRequestCreateRequestDTO req, Integer userId) {
        Integer requestId = studentRequestRepository.createRequest(userId, req);
        return new StudentRequestCreateResponseDTO(requestId);
    }
}