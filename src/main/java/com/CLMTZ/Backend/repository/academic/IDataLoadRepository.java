package com.CLMTZ.Backend.repository.academic;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.CLMTZ.Backend.model.academic.Students;

@Repository
public interface IDataLoadRepository extends JpaRepository<Students, Integer> {
    // Validaciones SQL (RF27)
    @Query(value = "SELECT * FROM academico.fn_sl_ids_academicos(:carrera, :modalidad, :periodo)", nativeQuery = true)
    Map<String, Object> obtenerIdsPorTexto(@Param("carrera") String carrera, @Param("modalidad") String modalidad, @Param("periodo") String periodo);

    @Query(value = "SELECT general.fn_vlboolean_disponibilidad_correo(:correo, :cedula)", nativeQuery = true)
    boolean validarCorreoDisponible(@Param("correo") String correo, @Param("cedula") String cedula);
    
@Query(value = "SELECT academico.fn_vlinteger_existe_asignatura_periodo(:asignatura, :idCarrera)", nativeQuery = true)
    Integer obtenerIdAsignatura(@Param("asignatura") String asignatura, @Param("idCarrera") Integer idCarrera);

    // 2. Obtener ID del Paralelo
    // Llama a: academico.fn_vlinteger_id_paralelo (Ej: convierte 'A' en ID 1)
    @Query(value = "SELECT academico.fn_vlinteger_id_paralelo(:paralelo)", nativeQuery = true)
    Integer obtenerIdParalelo(@Param("paralelo") String paralelo);

    // 3. Validar si ya tiene la clase asignada (Para evitar duplicados)
    // Llama a: academico.fn_vlboolean_docente_clase_asignada
    @Query(value = "SELECT academico.fn_vlboolean_docente_clase_asignada(:cedula, :idAsig, :idPeriodo, :idParalelo)", nativeQuery = true)
    boolean validarDocenteConClase(
        @Param("cedula") String cedula, 
        @Param("idAsig") Integer idAsignatura, 
        @Param("idPeriodo") Integer idPeriodo, 
        @Param("idParalelo") Integer idParalelo
    );

}
