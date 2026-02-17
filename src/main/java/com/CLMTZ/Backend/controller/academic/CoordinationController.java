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

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                // 1. Convertimos Excel -> Lista de DTOs
                List<StudentLoadDTO> dtos = ExcelHelper.excelToStudents(file.getInputStream());
                
                // 2. Enviamos la lista al servicio (tu lógica RF26/RF27)
                List<String> reporte = service.uploadStudents(dtos);
                
                return ResponseEntity.ok(reporte);

            } catch (Exception e) {
                message = "No se pudo procesar el archivo: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        }

        message = "Por favor, sube un archivo Excel válido (.xlsx)";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @PostMapping("/upload-teachers")
    public ResponseEntity<?> uploadTeachers(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                List<TeachingDTO> dtos = ExcelHelper.excelToTeaching(file.getInputStream());
                List<String> reporte = service.uploadTeachers(dtos);
                return ResponseEntity.ok(reporte);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Error: " + e.getMessage());
            }
        }
        return ResponseEntity.badRequest().body("Formato inválido");
    }
}
