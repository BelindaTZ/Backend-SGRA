package com.CLMTZ.Backend.service.academic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.CLMTZ.Backend.dto.academic.CoordinationDTO;
import com.CLMTZ.Backend.dto.academic.StudentLoadDTO;
import com.CLMTZ.Backend.model.academic.Coordination;
import com.CLMTZ.Backend.repository.academic.ICareerRepository;
import com.CLMTZ.Backend.repository.academic.ICoordinationRepository;
import com.CLMTZ.Backend.repository.academic.IDataLoadRepository;
import com.CLMTZ.Backend.repository.general.IUserRepository;
import com.CLMTZ.Backend.service.academic.ICoordinationService;

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
        if (dto.getUserId() != null) entity.setUserId(userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
        if (dto.getCareerId() != null) entity.setCareerId(careerRepository.findById(dto.getCareerId()).orElseThrow(() -> new RuntimeException("Career not found")));
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
                    fila.getCarreraTexto(), fila.getModalidadTexto(), fila.getPeriodoTexto()
                );

                // DEBUG: Ver en consola qué devuelve la BD
                System.out.println("Procesando Cédula: " + fila.getCedula() + " | IDs encontrados: " + ids);

                // Validación de nulos (incluyendo periodo)
                if (ids == null || ids.get("id_carrera_encontrado") == null || ids.get("id_periodo_encontrado") == null) {
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
                    idCarrera, idGenero, idPeriodo
                );
                
                resultados.add("Cédula " + fila.getCedula() + ": " + resultadoSP);

            } catch (Exception e) {
                resultados.add("Cédula " + fila.getCedula() + ": ERROR (" + e.getMessage() + ")");
                e.printStackTrace();
            }
        }
        return resultados;
    }

    // --- MÉTODO PRIVADO PARA EJECUTAR SP CON ENTITY MANAGER ---
    // Esto soluciona el error "Could not determine bind type" manejando los parámetros OUT explícitamente
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
        if (dto.getUserId() != null) entity.setUserId(userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
        if (dto.getCareerId() != null) entity.setCareerId(careerRepository.findById(dto.getCareerId()).orElseThrow(() -> new RuntimeException("Career not found")));
        return entity;
    }
}