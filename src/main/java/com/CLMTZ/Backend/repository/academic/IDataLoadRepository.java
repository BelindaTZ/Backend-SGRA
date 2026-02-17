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

    // Ejecución Carga (RF26)
    //@Procedure(procedureName = "academico.sp_in_carga_estudiante")
    //Map<String, Object> cargarEstudiante(
    //    @Param("p_identificador") String cedula,
    //    @Param("p_nombres") String nombres,
    //    @Param("p_apellidos") String apellidos,
    //    @Param("p_correo") String correo,
    //    @Param("p_direccion") String direccion,
    //    @Param("p_telefono") String telefono,
    //    @Param("p_idcarrera") Integer idCarrera,
    //   @Param("p_idgenero") Integer idGenero,
    //    @Param("p_idperiodo") Integer idPeriodo
    //);

}
