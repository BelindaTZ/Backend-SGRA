package com.CLMTZ.Backend.dto.academic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeachingDTO {
    private Integer teachingId;
    private Boolean state;
    private Integer userId;
    private Integer modalityId;
    // Datos Personales
    private String cedula;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String direccion;
    private String genero; // "M" o "F"

    // Datos Académicos (Texto del Excel)
    private String carreraTexto;   // Necesario para buscar la materia
    private String modalidadTexto;
    private String periodoTexto;
    
    // CAMPOS NUEVOS ESPECÍFICOS DE DOCENTE
    private String asignaturaTexto; // Ej: "Programación Orientada a Objetos"
    private String paraleloTexto;   // Ej: "A"
}
