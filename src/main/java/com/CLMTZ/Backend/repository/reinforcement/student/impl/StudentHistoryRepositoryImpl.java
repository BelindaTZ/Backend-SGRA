package com.CLMTZ.Backend.repository.reinforcement.student.impl;

import com.CLMTZ.Backend.config.DynamicDataSourceService;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentHistoryRequestItemDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentHistoryRequestsPageDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentHistorySessionItemDTO;
import com.CLMTZ.Backend.dto.reinforcement.student.StudentHistorySessionsPageDTO;
import com.CLMTZ.Backend.repository.reinforcement.student.StudentHistoryRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentHistoryRepositoryImpl implements StudentHistoryRepository {

    private final DynamicDataSourceService dynamicDataSourceService;

    public StudentHistoryRepositoryImpl(DynamicDataSourceService dynamicDataSourceService) {
        this.dynamicDataSourceService = dynamicDataSourceService;
    }

    private NamedParameterJdbcTemplate getJdbcTemplate() {
        return dynamicDataSourceService.getJdbcTemplate();
    }

    @Override
    public StudentHistoryRequestsPageDTO getRequestHistory(Integer userId, Integer periodId, Integer page, Integer size, Integer statusId) {
        String sql = "SELECT * FROM reforzamiento.fn_sl_historial_solicitudes_estudiante_ui(:userId, :periodId, :page, :size, :statusId)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("periodId", periodId);
        params.addValue("page", page);
        params.addValue("size", size);
        params.addValue("statusId", statusId);

        List<StudentHistoryRequestItemDTO> items = new ArrayList<>();
        final Long[] totalCount = {0L};

        getJdbcTemplate().query(sql, params, (rs) -> {
            StudentHistoryRequestItemDTO item = new StudentHistoryRequestItemDTO();
            item.setRequestId(rs.getInt("idsolicitudrefuerzo"));

            Timestamp timestamp = rs.getTimestamp("fechahoracreacion");
            item.setCreatedAt(timestamp != null ? timestamp.toInstant().toString() : null);

            item.setSubjectName(rs.getString("asignatura"));
            item.setSyllabusName(rs.getString("temario"));
            item.setUnit(rs.getShort("unidad"));
            item.setTeacherName(rs.getString("docente"));
            item.setSessionType(rs.getString("tipo"));
            item.setStatusId(rs.getInt("estado_id"));
            item.setStatusName(rs.getString("estado"));
            item.setReason(rs.getString("motivo"));
            item.setRequestedDay(rs.getShort("diasolicitado"));

            if (items.isEmpty()) {
                totalCount[0] = rs.getLong("total_count");
            }

            items.add(item);
        });

        return new StudentHistoryRequestsPageDTO(items, totalCount[0], page, size);
    }

    @Override
    public StudentHistorySessionsPageDTO getPreviousSessions(Integer userId, Integer page, Integer size, Boolean onlyAttended) {
        String sql = "SELECT * FROM reforzamiento.fn_sl_sesiones_anteriores_estudiante_ui(:userId, :page, :size, :onlyAttended)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("page", page);
        params.addValue("size", size);
        params.addValue("onlyAttended", onlyAttended);

        List<StudentHistorySessionItemDTO> items = new ArrayList<>();
        final Long[] totalCount = {0L};

        getJdbcTemplate().query(sql, params, (rs) -> {
            StudentHistorySessionItemDTO item = new StudentHistorySessionItemDTO();
            item.setCompletedSessionId(rs.getInt("idrefuerzorealizado"));
            item.setAttended(rs.getBoolean("asistencia"));

            Time time = rs.getTime("duracion");
            item.setDuration(time != null ? time.toString() : null);

            item.setNotes(rs.getString("observacion"));
            item.setRequestId(rs.getInt("idsolicitudrefuerzo"));

            Timestamp timestamp = rs.getTimestamp("fecha_solicitud");
            item.setRequestDateTime(timestamp != null ? timestamp.toInstant().toString() : null);

            item.setSubjectName(rs.getString("asignatura"));
            item.setSyllabusName(rs.getString("temario"));
            item.setUnit(rs.getShort("unidad"));
            item.setTeacherName(rs.getString("docente"));
            item.setSessionType(rs.getString("tipo"));

            if (items.isEmpty()) {
                totalCount[0] = rs.getLong("total_count");
            }

            items.add(item);
        });

        return new StudentHistorySessionsPageDTO(items, totalCount[0], page, size);
    }
}