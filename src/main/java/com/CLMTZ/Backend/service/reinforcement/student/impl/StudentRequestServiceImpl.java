package com.CLMTZ.Backend.service.reinforcement.student.impl;

import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestCreateRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestCreateResponseDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewResponseDTO;
import com.CLMTZ.Backend.exception.TimeSlotNotAvailableException;
import com.CLMTZ.Backend.repository.reinforcement.student.StudentRequestRepository;
import com.CLMTZ.Backend.service.reinforcement.student.StudentCatalogService;
import com.CLMTZ.Backend.service.reinforcement.student.StudentRequestService;
import org.springframework.stereotype.Service;

@Service
public class StudentRequestServiceImpl implements StudentRequestService {

    private final StudentRequestRepository studentRequestRepository;
    private final StudentCatalogService studentCatalogService;

    public StudentRequestServiceImpl(StudentRequestRepository studentRequestRepository,
                                     StudentCatalogService studentCatalogService) {
        this.studentRequestRepository = studentRequestRepository;
        this.studentCatalogService = studentCatalogService;
    }

    @Override
    public StudentRequestPreviewResponseDTO preview(StudentRequestPreviewRequestDTO req) {
        return studentRequestRepository.previewRequest(req);
    }

    @Override
    public StudentRequestCreateResponseDTO create(StudentRequestCreateRequestDTO req, Integer userId) {
        // Validación de disponibilidad de franja horaria
        validateTimeSlotAvailability(req);

        Integer requestId = studentRequestRepository.createRequest(userId, req);
        return new StudentRequestCreateResponseDTO(requestId);
    }

    /**
     * Valida que la franja horaria esté disponible para el docente seleccionado.
     * Verifica disponibilidad del docente y que no haya conflictos con otras solicitudes.
     *
     * @param req Datos de la solicitud a crear
     * @throws TimeSlotNotAvailableException si la franja no está disponible
     */
    private void validateTimeSlotAvailability(StudentRequestCreateRequestDTO req) {
        if (req.getTeacherId() == null || req.getRequestedDay() == null ||
            req.getPeriodId() == null || req.getTimeSlotId() == null) {
            return; // La validación de campos obligatorios se hace en el controlador
        }

        boolean isAvailable = studentCatalogService.isTimeSlotAvailable(
                req.getTeacherId(),
                req.getRequestedDay(),
                req.getPeriodId(),
                req.getTimeSlotId()
        );

        if (!isAvailable) {
            throw new TimeSlotNotAvailableException(
                    "La franja horaria seleccionada no está disponible para el docente " +
                    "(por disponibilidad u ocupación con otra solicitud activa)."
            );
        }
    }
}