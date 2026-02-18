package com.CLMTZ.Backend.controller.academic;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.CLMTZ.Backend.dto.academic.CoordinationDTO;
import com.CLMTZ.Backend.dto.academic.StudentLoadDTO;
import com.CLMTZ.Backend.dto.academic.TeachingDTO;
import com.CLMTZ.Backend.service.academic.ICoordinationService;
import com.CLMTZ.Backend.util.ExcelHelper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/academic/coordinations")
@RequiredArgsConstructor
public class CoordinationController {

    private final ICoordinationService service;

    @GetMapping
    public ResponseEntity<List<CoordinationDTO>> findAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<CoordinationDTO> findById(@PathVariable Integer id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<CoordinationDTO> save(@RequestBody CoordinationDTO dto) { return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED); }

    @PutMapping("/{id}")
    public ResponseEntity<CoordinationDTO> update(@PathVariable Integer id, @RequestBody CoordinationDTO dto) { return ResponseEntity.ok(service.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) { service.deleteById(id); return ResponseEntity.noContent().build(); }

    @PostMapping("/upload-students")
    public ResponseEntity<?> uploadStudents(@RequestParam("file") MultipartFile file) {
        String message = "";

        // Debug: imprimir info del archivo
        System.out.println("=== UPLOAD STUDENTS ===");
        System.out.println("Archivo recibido: " + file.getOriginalFilename());
        System.out.println("Content-Type: " + file.getContentType());
        System.out.println("Tamaño: " + file.getSize() + " bytes");

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                // 1. Convertimos Excel -> Lista de DTOs
                List<StudentLoadDTO> dtos = ExcelHelper.excelToStudents(file.getInputStream());
                System.out.println("Filas leídas del Excel: " + dtos.size());
                
                // 2. Enviamos la lista al servicio (tu lógica RF26/RF27)
                List<String> reporte = service.uploadStudents(dtos);
                
                return ResponseEntity.ok(reporte);

            } catch (Exception e) {
                e.printStackTrace(); // Imprime el stack trace completo en consola
                message = "No se pudo procesar el archivo: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        }

        message = "Por favor, sube un archivo Excel válido (.xlsx). Archivo recibido: " + file.getOriginalFilename() + ", tipo: " + file.getContentType();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @PostMapping("/upload-teachers")
    public ResponseEntity<?> uploadTeachers(@RequestParam("file") MultipartFile file) {
        String message = "";

        // Debug: imprimir info del archivo
        System.out.println("=== UPLOAD TEACHERS ===");
        System.out.println("Archivo recibido: " + file.getOriginalFilename());
        System.out.println("Content-Type: " + file.getContentType());
        System.out.println("Tamaño: " + file.getSize() + " bytes");

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                List<TeachingDTO> dtos = ExcelHelper.excelToTeaching(file.getInputStream());
                System.out.println("Filas leídas del Excel: " + dtos.size());
                
                List<String> reporte = service.uploadTeachers(dtos);
                return ResponseEntity.ok(reporte);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Error: " + e.getMessage());
            }
        }
        message = "Formato inválido. Archivo: " + file.getOriginalFilename() + ", tipo: " + file.getContentType();
        return ResponseEntity.badRequest().body(message);
    }
}
