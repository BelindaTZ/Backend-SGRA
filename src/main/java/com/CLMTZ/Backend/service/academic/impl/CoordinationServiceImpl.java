package com.CLMTZ.Backend.service.academic.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.CLMTZ.Backend.dto.academic.CoordinationDTO;
import com.CLMTZ.Backend.dto.academic.StudentLoadDTO;
import com.CLMTZ.Backend.dto.academic.SyllabiLoadDTO;
import com.CLMTZ.Backend.dto.academic.TeachingDTO;
import com.CLMTZ.Backend.dto.academic.PeriodDTO;
import com.CLMTZ.Backend.model.academic.Coordination;
import com.CLMTZ.Backend.repository.academic.ICareerRepository;
import com.CLMTZ.Backend.repository.academic.ICoordinationRepository;
import com.CLMTZ.Backend.repository.academic.IDataLoadRepository;
import com.CLMTZ.Backend.repository.general.IUserRepository;
import com.CLMTZ.Backend.service.academic.ICoordinationService;
import com.CLMTZ.Backend.util.ExcelValidator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode; // Importante
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery; // Importante
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class CoordinationServiceImpl implements ICoordinationService {

    private final ICoordinationRepository repository;
    private final IUserRepository userRepository;
    private final ICareerRepository careerRepository;
    private final IDataLoadRepository dataLoadRepository;

    @PersistenceContext
    private EntityManager entityManager; // Inyección para SP manual

    // --- MÉTODOS CRUD EXISTENTES (Se mantienen igual) ---

    @Override
    public List<CoordinationDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CoordinationDTO findById(Integer id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Coordination not found with id: " + id));
    }

    @Override
    public CoordinationDTO save(CoordinationDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    @Override
    public CoordinationDTO update(Integer id, CoordinationDTO dto) {
        Coordination entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coordination not found with id: " + id));
        if (dto.getUserId() != null)
            entity.setUserId(
                    userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
        if (dto.getCareerId() != null)
            entity.setCareerId(careerRepository.findById(dto.getCareerId())
                    .orElseThrow(() -> new RuntimeException("Career not found")));
        return toDTO(repository.save(entity));
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    // --- MÉTODOS PARA CARGA MASIVA (RF26 y RF27) ---

    @Override
    public List<String> uploadStudents(List<StudentLoadDTO> dtos) {
        List<String> resultados = new ArrayList<>();

        for (StudentLoadDTO fila : dtos) {
            try {
                // 1. Obtener IDs desde SQL (Funciones)
                Map<String, Object> ids = dataLoadRepository.obtenerIdsPorTexto(
                        fila.getCarreraTexto(), fila.getModalidadTexto(), fila.getPeriodoTexto());

                // DEBUG: Ver en consola qué devuelve la BD
                System.out.println("Procesando Cédula: " + fila.getCedula() + " | IDs encontrados: " + ids);

                // Validación de nulos (incluyendo periodo)
                if (ids == null || ids.get("id_carrera_encontrado") == null
                        || ids.get("id_periodo_encontrado") == null) {
                    resultados.add("Cédula " + fila.getCedula() + ": ERROR (Carrera o Periodo no encontrados en BD)");
                    continue; // Saltamos al siguiente
                }

                // Conversión de tipos segura
                Integer idCarrera = (Integer) ids.get("id_carrera_encontrado");
                Integer idPeriodo = (Integer) ids.get("id_periodo_encontrado");
                // Lógica simple de género (M=1, F=2)
                Integer idGenero = "M".equalsIgnoreCase(fila.getGenero()) ? 1 : 2;

                // 2. Validación SQL (Correo duplicado)
                if (!dataLoadRepository.validarCorreoDisponible(fila.getCorreo(), fila.getCedula())) {
                    resultados.add("Cédula " + fila.getCedula() + ": ERROR (Correo duplicado)");
                    continue;
                }

                // 3. Ejecutar SP (Usando el método manual con EntityManager)
                String resultadoSP = ejecutarCargaEstudianteSP(
                        fila.getCedula(), fila.getNombres(), fila.getApellidos(),
                        fila.getCorreo(), fila.getDireccion(), fila.getTelefono(),
                        idCarrera, idGenero, idPeriodo);

                resultados.add("Cédula " + fila.getCedula() + ": " + resultadoSP);

            } catch (Exception e) {
                resultados.add("Cédula " + fila.getCedula() + ": ERROR (" + e.getMessage() + ")");
                e.printStackTrace();
            }
        }
        return resultados;
    }

    // --- MÉTODO PRIVADO PARA EJECUTAR SP CON ENTITY MANAGER ---
    // Esto soluciona el error "Could not determine bind type" manejando los
    // parámetros OUT explícitamente
    private String ejecutarCargaEstudianteSP(String cedula, String nom, String ape, String correo,
            String dir, String tel, Integer idCarrera, Integer idGen, Integer idPer) {

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("academico.sp_in_carga_estudiante");

        // REGISTRO DE PARÁMETROS (Tipos explícitos)
        query.registerStoredProcedureParameter("p_identificador", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_nombres", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_apellidos", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_correo", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_direccion", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_telefono", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idcarrera", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idgenero", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idperiodo", Integer.class, ParameterMode.IN);

        // Parámetros de Salida (OUT)
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);

        // SETEO DE VALORES
        query.setParameter("p_identificador", cedula);
        query.setParameter("p_nombres", nom);
        query.setParameter("p_apellidos", ape);
        query.setParameter("p_correo", correo);
        query.setParameter("p_direccion", dir);
        query.setParameter("p_telefono", tel);
        query.setParameter("p_idcarrera", idCarrera);
        query.setParameter("p_idgenero", idGen);
        query.setParameter("p_idperiodo", idPer);

        // EJECUCIÓN
        query.execute();

        // OBTENER RESULTADOS
        String mensajeRetorno = (String) query.getOutputParameterValue("p_mensaje");
        Boolean exito = (Boolean) query.getOutputParameterValue("p_exito");

        if (Boolean.TRUE.equals(exito)) {
            return "OK";
        } else {
            return "FALLÓ SP: " + mensajeRetorno;
        }
    }

    // --- CONVERSORES DTO (Se mantienen igual) ---

    private CoordinationDTO toDTO(Coordination entity) {
        CoordinationDTO dto = new CoordinationDTO();
        dto.setCoordinationId(entity.getCoordinationId());
        dto.setUserId(entity.getUserId() != null ? entity.getUserId().getUserId() : null);
        dto.setCareerId(entity.getCareerId() != null ? entity.getCareerId().getCareerId() : null);
        return dto;
    }

    private Coordination toEntity(CoordinationDTO dto) {
        Coordination entity = new Coordination();
        if (dto.getUserId() != null)
            entity.setUserId(
                    userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
        if (dto.getCareerId() != null)
            entity.setCareerId(careerRepository.findById(dto.getCareerId())
                    .orElseThrow(() -> new RuntimeException("Career not found")));
        return entity;
    }

    @Override
    public List<String> uploadTeachers(List<TeachingDTO> dtos) {
        List<String> resultados = new ArrayList<>();

        for (TeachingDTO fila : dtos) {
            try {
                // 1. Obtener IDs base (Carrera, Modalidad, Periodo)
                // Reutilizamos la misma función de estudiantes
                Map<String, Object> ids = dataLoadRepository.obtenerIdsPorTexto(
                        fila.getCarreraTexto(), fila.getModalidadTexto(), fila.getPeriodoTexto());

                if (ids == null || ids.get("id_carrera_encontrado") == null
                        || ids.get("id_periodo_encontrado") == null) {
                    resultados.add(
                            "Docente " + fila.getCedula() + ": ERROR (Carrera, Modalidad o Periodo no encontrados)");
                    continue;
                }

                Integer idCarrera = (Integer) ids.get("id_carrera_encontrado");
                Integer idPeriodo = (Integer) ids.get("id_periodo_encontrado");
                Integer idModalidad = (Integer) ids.get("id_modalidad_encontrado"); // Necesario para docente
                Integer idGenero = "M".equalsIgnoreCase(fila.getGenero()) ? 1 : 2;

                // 2. Obtener IDs específicos (Asignatura y Paralelo)
                Integer idAsignatura = dataLoadRepository.obtenerIdAsignatura(fila.getAsignaturaTexto(), idCarrera);
                if (idAsignatura == null) {
                    resultados.add("Docente " + fila.getCedula() + ": ERROR (Asignatura '" + fila.getAsignaturaTexto()
                            + "' no existe en esa carrera)");
                    continue;
                }

                Integer idParalelo = dataLoadRepository.obtenerIdParalelo(fila.getParaleloTexto());
                if (idParalelo == null) {
                    resultados.add("Docente " + fila.getCedula() + ": ERROR (Paralelo '" + fila.getParaleloTexto()
                            + "' inválido)");
                    continue;
                }

                // 3. Validar Correo (Igual que estudiante)
                // Nota: Si el usuario ya existe, esta validación debe ser cuidadosa (tu SP ya
                // maneja updates)
                // Podrías omitirla si confías en el SP, o usarla solo para nuevos.

                // 4. Validar Choque de Clases (RF27)
                boolean claseOcupada = dataLoadRepository.validarDocenteConClase(
                        fila.getCedula(), idAsignatura, idPeriodo, idParalelo);

                if (claseOcupada) {
                    resultados.add("Docente " + fila.getCedula()
                            + ": ADVERTENCIA (Ya tiene esta clase asignada, se actualizarán datos personales)");
                    // No hacemos 'continue' para permitir que el SP actualice nombre/teléfono si
                    // cambiaron
                }

                // 5. Ejecutar SP Manualmente (EntityManager)
                String resultadoSP = ejecutarCargaDocenteSP(
                        fila.getCedula(), fila.getNombres(), fila.getApellidos(),
                        fila.getCorreo(), fila.getDireccion(), fila.getTelefono(),
                        idModalidad, idGenero, idPeriodo, idAsignatura, idParalelo);

                resultados.add("Docente " + fila.getCedula() + ": " + resultadoSP);

            } catch (Exception e) {
                resultados.add("Docente " + fila.getCedula() + ": ERROR INTERNO (" + e.getMessage() + ")");
                e.printStackTrace();
            }
        }
        return resultados;
    }

    // --- MÉTODO PRIVADO PARA SP DE DOCENTES ---
    private String ejecutarCargaDocenteSP(String cedula, String nom, String ape, String correo,
            String dir, String tel, Integer idMod, Integer idGen, Integer idPer,
            Integer idAsig, Integer idPar) {

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("academico.sp_in_carga_docente");

        // REGISTRO PARÁMETROS IN
        query.registerStoredProcedureParameter("p_identificador", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_nombres", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_apellidos", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_correo", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_direccion", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_telefono", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idmodalidad", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idgenero", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idperiodo", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idasignatura", Integer.class, ParameterMode.IN); // Extra
        query.registerStoredProcedureParameter("p_idparalelo", Integer.class, ParameterMode.IN); // Extra

        // REGISTRO PARÁMETROS OUT
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);

        // SETEO VALORES
        query.setParameter("p_identificador", cedula);
        query.setParameter("p_nombres", nom);
        query.setParameter("p_apellidos", ape);
        query.setParameter("p_correo", correo);
        query.setParameter("p_direccion", dir);
        query.setParameter("p_telefono", tel);
        query.setParameter("p_idmodalidad", idMod);
        query.setParameter("p_idgenero", idGen);
        query.setParameter("p_idperiodo", idPer);
        query.setParameter("p_idasignatura", idAsig);
        query.setParameter("p_idparalelo", idPar);

        // EJECUCIÓN
        query.execute();

        String mensajeRetorno = (String) query.getOutputParameterValue("p_mensaje");
        Boolean exito = (Boolean) query.getOutputParameterValue("p_exito");

        return Boolean.TRUE.equals(exito) ? "OK" : "FALLÓ SP: " + mensajeRetorno;
    }

    // 1. Estructura Universitaria (Carrera)
    private String ejecutarCargaCarreraSP(String nombre, String codigo, Integer idFacultad) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("academico.sp_in_carga_carrera");
        query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_codigo", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idfacultad", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);
        query.setParameter("p_nombre", nombre);
        query.setParameter("p_codigo", codigo);
        query.setParameter("p_idfacultad", idFacultad);
        query.execute();
        String mensaje = (String) query.getOutputParameterValue("p_mensaje");
        Boolean exito = (Boolean) query.getOutputParameterValue("p_exito");
        return Boolean.TRUE.equals(exito) ? "OK" : "FALLÓ SP: " + mensaje;
    }

    // 2. Malla Curricular (Asignatura)
    private String ejecutarCargaAsignaturaSP(String nombre, String codigo, Integer idCarrera, Integer nivel) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("academico.sp_in_carga_asignatura");
        query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_codigo", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idcarrera", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_nivel", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);
        query.setParameter("p_nombre", nombre);
        query.setParameter("p_codigo", codigo);
        query.setParameter("p_idcarrera", idCarrera);
        query.setParameter("p_nivel", nivel);
        query.execute();
        String mensaje = (String) query.getOutputParameterValue("p_mensaje");
        Boolean exito = (Boolean) query.getOutputParameterValue("p_exito");
        return Boolean.TRUE.equals(exito) ? "OK" : "FALLÓ SP: " + mensaje;
    }

    // 3. Periodos
    private String ejecutarCargaPeriodoSP(String nombre, String codigo, LocalDate fechaInicio, LocalDate fechaFin) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("academico.sp_in_carga_periodo");
        query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_codigo", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_fechainicio", LocalDate.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_fechafin", LocalDate.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);
        query.setParameter("p_nombre", nombre);
        query.setParameter("p_codigo", codigo);
        query.setParameter("p_fechainicio", fechaInicio);
        query.setParameter("p_fechafin", fechaFin);
        query.execute();
        String mensaje = (String) query.getOutputParameterValue("p_mensaje");
        Boolean exito = (Boolean) query.getOutputParameterValue("p_exito");
        return Boolean.TRUE.equals(exito) ? "OK" : "FALLÓ SP: " + mensaje;
    }

    // 4. Registration/Matricula de Estudiantes
    private String ejecutarCargaDetalleMatriculaSP(String cedula, Integer idAsignatura, Integer idParalelo, Integer idPeriodo) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("academico.sp_in_carga_detalle_matricula");
        query.registerStoredProcedureParameter("p_identificador", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idasignatura", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idparalelo", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idperiodo", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);
        query.setParameter("p_identificador", cedula);
        query.setParameter("p_idasignatura", idAsignatura);
        query.setParameter("p_idparalelo", idParalelo);
        query.setParameter("p_idperiodo", idPeriodo);
        query.execute();
        String mensaje = (String) query.getOutputParameterValue("p_mensaje");
        Boolean exito = (Boolean) query.getOutputParameterValue("p_exito");
        return Boolean.TRUE.equals(exito) ? "OK" : "FALLÓ SP: " + mensaje;
    }

    // 5. SHEDULE/Horario de Clases
    private String ejecutarCargaHorarioClaseSP(Integer idAsignatura, Integer idParalelo, Integer idPeriodo, String dia, String horaInicio, String horaFin) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("academico.sp_in_carga_horarioclase");
        query.registerStoredProcedureParameter("p_idasignatura", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idparalelo", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_idperiodo", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_dia", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_horainicio", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_horafin", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);
        query.setParameter("p_idasignatura", idAsignatura);
        query.setParameter("p_idparalelo", idParalelo);
        query.setParameter("p_idperiodo", idPeriodo);
        query.setParameter("p_dia", dia);
        query.setParameter("p_horainicio", horaInicio);
        query.setParameter("p_horafin", horaFin);
        query.execute();
        String mensaje = (String) query.getOutputParameterValue("p_mensaje");
        Boolean exito = (Boolean) query.getOutputParameterValue("p_exito");
        return Boolean.TRUE.equals(exito) ? "OK" : "FALLÓ SP: " + mensaje;
    }


    public List<String> uploadSyllabi(List<SyllabiLoadDTO> dtos) {
        List<String> resultados = new ArrayList<>();

        for (SyllabiLoadDTO fila : dtos) {
            try {
                // Aquí usamos el ExcelValidator que arreglaste antes para limpiar espacios
                ExcelValidator.validarYCorregir(fila);

                // IMPORTANTE: Asegúrate de pasar los parámetros en este orden exacto:
                // 1. Asignatura, 2. Carrera, 3. Unidad, 4. Nombre del Tema
                String resultadoSP = ejecutarCargaTemarioSP(
                        fila.getAsignaturaTexto(), 
                        fila.getCarreraTexto(), 
                        fila.getUnidad(), 
                        fila.getNombreTema()
                );

                resultados.add("Temario '" + fila.getNombreTema() + "': " + resultadoSP);

            } catch (Exception e) {
                resultados.add("Temario '" + fila.getNombreTema() + "': ERROR (" + e.getMessage() + ")");
                e.printStackTrace();
            }
        }
        return resultados;
    }

    // 6. Syllabus/Temarios
    private String ejecutarCargaTemarioSP(String nombreAsignatura, String nombreCarrera, Integer unidad, String nombreTema) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("academico.sp_in_carga_temario");

        // REGISTRO DE PARÁMETROS: Deben coincidir EXACTAMENTE con los de PostgreSQL
        query.registerStoredProcedureParameter("p_nombre_asignatura", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_nombre_carrera", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_unidad", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_nombre_tema", String.class, ParameterMode.IN);
        
        // Parámetros OUT
        query.registerStoredProcedureParameter("p_mensaje", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_exito", Boolean.class, ParameterMode.OUT);

        // SETEO DE VALORES
        query.setParameter("p_nombre_asignatura", nombreAsignatura);
        query.setParameter("p_nombre_carrera", nombreCarrera);
        query.setParameter("p_unidad", unidad);
        query.setParameter("p_nombre_tema", nombreTema);

        // EJECUCIÓN
        query.execute();

        String mensaje = (String) query.getOutputParameterValue("p_mensaje");
        Boolean exito = (Boolean) query.getOutputParameterValue("p_exito");
        
        return Boolean.TRUE.equals(exito) ? "OK" : "FALLÓ SP: " + mensaje;
    }

    @Override
    public List<String> Period(List<PeriodDTO> dtos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Period'");
    }

    @Override
    public List<String> uploadPeriods(List<PeriodDTO> dtos) {
        List<String> resultados = new ArrayList<>();

        for (PeriodDTO fila : dtos) {
            try {
                String resultadoSP = ejecutarCargaPeriodoSP(
                        fila.getPeriod(), "COD-" + fila.getPeriodId(), fila.getStartDate(), fila.getEndDate());
                resultados.add("Periodo " + fila.getPeriod() + ": " + resultadoSP);
            } catch (Exception e) {
                resultados.add("Periodo " + fila.getPeriod() + ": ERROR (" + e.getMessage() + ")");
                e.printStackTrace();
            }
        }
        return resultados;
    }

}