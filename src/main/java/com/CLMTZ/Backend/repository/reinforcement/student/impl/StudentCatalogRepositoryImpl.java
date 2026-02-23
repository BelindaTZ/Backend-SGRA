package com.CLMTZ.Backend.repository.reinforcement.student.impl;

import com.CLMTZ.Backend.config.DynamicDataSourceService;
import com.CLMTZ.Backend.dto.reinforcement.student.*;
import com.CLMTZ.Backend.repository.reinforcement.student.StudentCatalogRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentCatalogRepositoryImpl implements StudentCatalogRepository {

    private final DynamicDataSourceService dynamicDataSourceService;

    public StudentCatalogRepositoryImpl(DynamicDataSourceService dynamicDataSourceService) {
        this.dynamicDataSourceService = dynamicDataSourceService;
    }

    private NamedParameterJdbcTemplate getJdbcTemplate() {
        return dynamicDataSourceService.getJdbcTemplate();
    }

    @Override
    public List<SubjectItemDTO> listSubjects() {
        String sql = "SELECT * FROM academico.fn_sl_cat_asignaturas()";
        return getJdbcTemplate().query(sql, (rs, rowNum) -> new SubjectItemDTO(
                rs.getInt("idasignatura"),
                rs.getString("asignatura"),
                rs.getShort("semestre")
        ));
    }

    @Override
    public List<SyllabusItemDTO> listSyllabiBySubject(Integer subjectId) {
        String sql = "SELECT * FROM academico.fn_sl_temarios_por_asignatura(:subjectId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("subjectId", subjectId);
        return getJdbcTemplate().query(sql, params, (rs, rowNum) -> new SyllabusItemDTO(
                rs.getInt("idtemario"),
                rs.getString("nombretemario"),
                rs.getShort("unidad")
        ));
    }

    @Override
    public List<TeacherItemDTO> listTeachers(Integer modalityId) {
        String sql = "SELECT * FROM academico.fn_sl_cat_docentes_ui(:modalityId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modalityId", modalityId);
        return getJdbcTemplate().query(sql, params, (rs, rowNum) -> new TeacherItemDTO(
                rs.getInt("iddocente"),
                rs.getString("nombre_completo"),
                rs.getString("correo"),
                rs.getObject("idmodalidad") != null ? rs.getInt("idmodalidad") : null
        ));
    }

    @Override
    public List<ModalityItemDTO> listModalities() {
        String sql = "SELECT * FROM academico.fn_sl_cat_modalidades()";
        return getJdbcTemplate().query(sql, (rs, rowNum) -> new ModalityItemDTO(
                rs.getInt("idmodalidad"),
                rs.getString("modalidad")
        ));
    }

    @Override
    public List<SessionTypeItemDTO> listSessionTypes() {
        String sql = "SELECT * FROM reforzamiento.fn_sl_cat_tipos_sesion()";
        return getJdbcTemplate().query(sql, (rs, rowNum) -> new SessionTypeItemDTO(
                rs.getInt("idtiposesion"),
                rs.getString("tiposesion")
        ));
    }

    @Override
    public List<TimeSlotItemDTO> listTimeSlots() {
        String sql = "SELECT * FROM academico.fn_sl_cat_franjas_horarias_ui()";
        return getJdbcTemplate().query(sql, (rs, rowNum) -> new TimeSlotItemDTO(
                rs.getInt("idfranjahoraria"),
                rs.getString("label"),
                rs.getString("franja")
        ));
    }

    @Override
    public List<AvailableTimeSlotDTO> listAvailableTimeSlots(Integer teacherId, Short dayOfWeek, Integer periodId) {
        String sql = "SELECT * FROM academico.fn_sl_franjas_docente_disponibles_ui(:teacherId, :dayOfWeek, :periodId, :nonBlockingStatuses)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("teacherId", teacherId);
        params.addValue("dayOfWeek", dayOfWeek);
        params.addValue("periodId", periodId);
        // Estados no bloqueantes: por defecto vacío, se pueden configurar si se requiere
        params.addValue("nonBlockingStatuses", new Integer[]{}, java.sql.Types.ARRAY);

        return getJdbcTemplate().query(sql, params, (rs, rowNum) -> new AvailableTimeSlotDTO(
                rs.getInt("idfranjahoraria"),
                rs.getString("label"),
                rs.getString("franja")
        ));
    }

    @Override
    public boolean isTimeSlotAvailable(Integer teacherId, Short dayOfWeek, Integer periodId, Integer timeSlotId) {
        List<AvailableTimeSlotDTO> availableSlots = listAvailableTimeSlots(teacherId, dayOfWeek, periodId);
        return availableSlots.stream()
                .anyMatch(slot -> slot.getTimeSlotId().equals(timeSlotId));
    }
}