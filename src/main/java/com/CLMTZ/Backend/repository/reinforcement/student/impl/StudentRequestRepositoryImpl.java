package com.CLMTZ.Backend.repository.reinforcement.student.impl;

import com.CLMTZ.Backend.config.DynamicDataSourceService;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestCreateRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewRequestDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentRequestPreviewResponseDTO;
import com.CLMTZ.Backend.repository.reinforcement.student.StudentRequestRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRequestRepositoryImpl implements StudentRequestRepository {

    private final DynamicDataSourceService dynamicDataSourceService;

    public StudentRequestRepositoryImpl(DynamicDataSourceService dynamicDataSourceService) {
        this.dynamicDataSourceService = dynamicDataSourceService;
    }

    private NamedParameterJdbcTemplate getJdbcTemplate() {
        return dynamicDataSourceService.getJdbcTemplate();
    }

    @Override
    public StudentRequestPreviewResponseDTO previewRequest(StudentRequestPreviewRequestDTO req) {
        String sql = "SELECT * FROM reforzamiento.fn_sl_preview_nueva_solicitud(" +
                ":syllabusId, :teacherId, :timeSlotId, :modalityId, :sessionTypeId)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("syllabusId", req.getSyllabusId());
        params.addValue("teacherId", req.getTeacherId());
        params.addValue("timeSlotId", req.getTimeSlotId());
        params.addValue("modalityId", req.getModalityId());
        params.addValue("sessionTypeId", req.getSessionTypeId());

        List<StudentRequestPreviewResponseDTO> results = getJdbcTemplate().query(sql, params, (rs, rowNum) -> {
            StudentRequestPreviewResponseDTO dto = new StudentRequestPreviewResponseDTO();
            dto.setSubjectId(rs.getInt("idasignatura"));
            dto.setSubjectName(rs.getString("asignatura"));
            dto.setSyllabusId(rs.getInt("idtemario"));
            dto.setSyllabusName(rs.getString("temario"));
            dto.setUnit(rs.getShort("unidad"));
            dto.setTeacherId(rs.getInt("iddocente"));
            dto.setTeacherName(rs.getString("docente"));
            dto.setTeacherEmail(rs.getString("docente_correo"));
            dto.setModalityId(rs.getInt("idmodalidad"));
            dto.setModalityName(rs.getString("modalidad"));
            dto.setSessionTypeId(rs.getInt("idtiposesion"));
            dto.setSessionTypeName(rs.getString("tiposesion"));
            dto.setTimeSlotId(rs.getInt("idfranjahoraria"));
            dto.setTimeSlotLabel(rs.getString("franja_label"));
            dto.setTimeSlotJson(rs.getString("franja_json"));
            return dto;
        });

        if (results.isEmpty()) {
            throw new IllegalStateException("Preview not found");
        }

        return results.get(0);
    }

    @Override
    public Integer createRequest(Integer userId, StudentRequestCreateRequestDTO req) {
        String sql = "SELECT reforzamiento.fn_in_nueva_solicitud_estudiante(" +
                ":userId, :syllabusId, :teacherId, :timeSlotId, :modalityId, :sessionTypeId, " +
                ":reason, :requestedDay, :fileUrl, :periodId) AS request_id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("syllabusId", req.getSyllabusId());
        params.addValue("teacherId", req.getTeacherId());
        params.addValue("timeSlotId", req.getTimeSlotId());
        params.addValue("modalityId", req.getModalityId());
        params.addValue("sessionTypeId", req.getSessionTypeId());
        params.addValue("reason", req.getReason());
        params.addValue("requestedDay", req.getRequestedDay());
        params.addValue("fileUrl", req.getFileUrl());
        params.addValue("periodId", req.getPeriodId());

        Integer requestId = getJdbcTemplate().queryForObject(sql, params, Integer.class);

        if (requestId == null) {
            throw new IllegalStateException("Failed to create request: no ID returned");
        }

        return requestId;
    }
}