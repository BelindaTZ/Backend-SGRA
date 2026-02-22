package com.CLMTZ.Backend.controller.academic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.CLMTZ.Backend.dto.academic.CareerLoadDTO;
import com.CLMTZ.Backend.dto.academic.ClassScheduleLoadDTO;
import com.CLMTZ.Backend.dto.academic.CoordinationDTO;
import com.CLMTZ.Backend.dto.academic.EnrollmentDetailLoadDTO;
import com.CLMTZ.Backend.dto.academic.PeriodLoadDTO;
import com.CLMTZ.Backend.dto.academic.StudentLoadDTO;
import com.CLMTZ.Backend.dto.academic.SubjectLoadDTO;
import com.CLMTZ.Backend.dto.academic.TeachingDTO;
import com.CLMTZ.Backend.service.academic.ICareerService;
import com.CLMTZ.Backend.service.academic.IClassScheduleService;
import com.CLMTZ.Backend.service.academic.ICoordinationService;
import com.CLMTZ.Backend.service.academic.IEnrollmentDetailService;
import com.CLMTZ.Backend.service.academic.IPeriodService;
import com.CLMTZ.Backend.service.academic.ISubjectService;
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

        // LOGS DETALLADOS
        System.out.println("\n==============================");
        System.out.println("[UPLOAD-STUDENTS] Endpoint llamado");
        System.out.println("Archivo recibido: " + (file != null ? file.getOriginalFilename() : "null"));
        System.out.println("Content-Type: " + (file != null ? file.getContentType() : "null"));
        System.out.println("Tamaño: " + (file != null ? file.getSize() : "null") + " bytes");
        System.out.println("==============================\n");

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                System.out.println("[UPLOAD-STUDENTS] Validación de formato OK");
                // 1. Convertimos Excel -> Lista de DTOs
                List<StudentLoadDTO> dtos = ExcelHelper.excelToStudents(file.getInputStream());
                System.out.println("[UPLOAD-STUDENTS] Filas leídas del Excel: " + dtos.size());
                if (dtos.isEmpty()) {
                    System.out.println("[UPLOAD-STUDENTS] El archivo Excel no contiene datos válidos.");
                } else {
                    System.out.println("[UPLOAD-STUDENTS] Primer estudiante: " + dtos.get(0));
                }
                // 2. Enviamos la lista al servicio (tu lógica RF26/RF27)
                List<String> reporte = service.uploadStudents(dtos);
                System.out.println("[UPLOAD-STUDENTS] Reporte generado: " + reporte);
                return ResponseEntity.ok(reporte);

            } catch (Exception e) {
                System.out.println("[UPLOAD-STUDENTS] ERROR al procesar el archivo:");
                e.printStackTrace(); // Imprime el stack trace completo en consola
                message = "No se pudo procesar el archivo: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        } else {
            System.out.println("[UPLOAD-STUDENTS] Formato de archivo inválido");
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
     
    @Autowired
    private ICareerService careerService; // Inyectamos el servicio de carreras

    @PostMapping("/upload-careers")
    public ResponseEntity<?> uploadCareers(@RequestParam("file") MultipartFile file) {
        
        // 1. Validar que el archivo sea realmente un Excel
        if (!ExcelHelper.hasExcelFormat(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(List.of("Error: Por favor, suba un archivo Excel válido (.xlsx)"));
        }

        try {
            // 2. Convertir el archivo Excel a nuestra lista de DTOs usando el Helper
            List<CareerLoadDTO> careerDTOs = ExcelHelper.excelToCareers(file.getInputStream());
            
            // 3. Pasar los DTOs al servicio para que ejecute el Stored Procedure
            List<String> report = careerService.uploadCareers(careerDTOs);
            
            // 4. Devolver el reporte fila por fila al Frontend/Postman
            return ResponseEntity.ok(report);
            
        } catch (Exception e) {
            // Manejo de errores generales (ej. Excel mal formateado)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Error interno al procesar el archivo: " + e.getMessage()));
        }
    }

    @Autowired
    private ISubjectService subjectService; // Inyectamos el servicio de asignaturas

    @PostMapping("/upload-subjects")
    public ResponseEntity<?> uploadSubjects(@RequestParam("file") MultipartFile file) {
        
        // 1. Validar formato Excel
        if (!ExcelHelper.hasExcelFormat(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(List.of("Error: Por favor, suba un archivo Excel válido (.xlsx)"));
        }

        try {
            // 2. Extraer datos del Excel a la lista de DTOs usando el método que creamos
            List<SubjectLoadDTO> subjectDTOs = ExcelHelper.excelToSubjects(file.getInputStream());
            
            // 3. Procesar en la base de datos a través del servicio
            List<String> report = subjectService.uploadSubjects(subjectDTOs);
            
            // 4. Devolver la respuesta con el reporte detallado
            return ResponseEntity.ok(report);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Error interno al procesar el archivo de asignaturas: " + e.getMessage()));
        }
    }
    @Autowired
    private IPeriodService periodService; // Inyectamos el servicio de periodos

    @PostMapping("/upload-periods")
    public ResponseEntity<?> uploadPeriods(@RequestParam("file") MultipartFile file) {
        
        // 1. Validar formato Excel
        if (!ExcelHelper.hasExcelFormat(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(List.of("Error: Por favor, suba un archivo Excel válido (.xlsx)"));
        }

        try {
            // 2. Extraer datos del Excel a la lista de DTOs
            List<PeriodLoadDTO> periodDTOs = ExcelHelper.excelToPeriods(file.getInputStream());
            
            // 3. Procesar en la base de datos a través del servicio
            List<String> report = periodService.uploadPeriods(periodDTOs);
            
            // 4. Devolver la respuesta con el reporte detallado
            return ResponseEntity.ok(report);
            
        } catch (Exception e) {
            // Este catch atrapará nuestro mensaje personalizado si las fechas no vienen en YYYY-MM-DD
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Error al procesar el archivo de periodos: " + e.getMessage()));
        }
    }
    @Autowired
    private IEnrollmentDetailService enrollmentDetailService; // Inyectamos el servicio

    @PostMapping("/upload-registrations")
    public ResponseEntity<?> uploadRegistrations(@RequestParam("file") MultipartFile file) {
        
        // 1. Validar formato Excel
        if (!ExcelHelper.hasExcelFormat(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(List.of("Error: Por favor, suba un archivo Excel válido (.xlsx)"));
        }

        try {
            // 2. Extraer datos del Excel a la lista de DTOs usando nuestro Helper
            List<EnrollmentDetailLoadDTO> registrationDTOs = ExcelHelper.excelToEnrollmentDetails(file.getInputStream());
            
            // 3. Procesar en la base de datos a través del servicio
            List<String> report = enrollmentDetailService.uploadEnrollmentDetails(registrationDTOs);
            
            // 4. Devolver la respuesta con el reporte detallado
            return ResponseEntity.ok(report);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Error al procesar el archivo de matrículas: " + e.getMessage()));
        }
    }
    @Autowired
    private IClassScheduleService classScheduleService; // Inyectamos el servicio de horarios

    @PostMapping("/upload-class-schedules")
    public ResponseEntity<?> uploadClassSchedules(@RequestParam("file") MultipartFile file) {
        
        // 1. Validar que el archivo sea un Excel
        if (!ExcelHelper.hasExcelFormat(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(List.of("Error: Por favor, suba un archivo Excel válido (.xlsx)"));
        }

        try {
            // 2. Extraer los datos del Excel a nuestra lista de DTOs
            List<ClassScheduleLoadDTO> scheduleDTOs = ExcelHelper.excelToClassSchedules(file.getInputStream());
            
            // 3. Procesar los horarios en la base de datos a través del servicio
            List<String> report = classScheduleService.uploadClassSchedules(scheduleDTOs);
            
            // 4. Devolver la respuesta con el reporte detallado fila por fila
            return ResponseEntity.ok(report);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Error al procesar el archivo de horarios: " + e.getMessage()));
        }
        
    }
    
}
