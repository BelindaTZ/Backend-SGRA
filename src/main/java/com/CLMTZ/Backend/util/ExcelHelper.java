package com.CLMTZ.Backend.util;

import com.CLMTZ.Backend.dto.academic.CareerLoadDTO;
import com.CLMTZ.Backend.dto.academic.ClassScheduleLoadDTO;
import com.CLMTZ.Backend.dto.academic.EnrollmentDetailLoadDTO;
import com.CLMTZ.Backend.dto.academic.PeriodLoadDTO;
import com.CLMTZ.Backend.dto.academic.StudentLoadDTO;
import com.CLMTZ.Backend.dto.academic.SubjectLoadDTO;
import com.CLMTZ.Backend.dto.academic.SyllabiLoadDTO;
import com.CLMTZ.Backend.dto.academic.TeachingDTO;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;



public class ExcelHelper {

    private static final DataFormatter formatter = new DataFormatter();
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static String TYPE_XLS = "application/vnd.ms-excel";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        
        // Validar por extensión del archivo (más confiable)
        if (filename != null && (filename.toLowerCase().endsWith(".xlsx") || filename.toLowerCase().endsWith(".xls"))) {
            return true;
        }
        
        // Validar por content-type
        if (contentType != null) {
            return TYPE.equals(contentType) || TYPE_XLS.equals(contentType) || 
                contentType.contains("spreadsheet") || contentType.contains("excel");
        }
        
        return false;
    }

    public static List<StudentLoadDTO> excelToStudents(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<StudentLoadDTO> estudiantes = new ArrayList<>();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                StudentLoadDTO estudiante = new StudentLoadDTO();
                estudiante.setCedula(getCellValue(row, 0));
                estudiante.setNombres(getCellValue(row, 1));
                estudiante.setApellidos(getCellValue(row, 2));
                estudiante.setCorreo(getCellValue(row, 3));
                estudiante.setTelefono(getCellValue(row, 4));
                estudiante.setDireccion(getCellValue(row, 5));
                estudiante.setGenero(getCellValue(row, 6));
                estudiante.setCarreraTexto(getCellValue(row, 7));
                estudiante.setModalidadTexto(getCellValue(row, 8));
                estudiante.setPeriodoTexto(getCellValue(row, 9));

                estudiantes.add(estudiante);
            }
            return estudiantes;
        } catch (IOException e) {
            throw new RuntimeException("Error al parsear el archivo Excel de Estudiantes: " + e.getMessage());
        }
    }

    public static List<TeachingDTO> excelToTeaching(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<TeachingDTO> docentes = new ArrayList<>();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                TeachingDTO docente = new TeachingDTO();
                docente.setCedula(getCellValue(row, 0));
                docente.setNombres(getCellValue(row, 1));
                docente.setApellidos(getCellValue(row, 2));
                docente.setCorreo(getCellValue(row, 3));
                docente.setTelefono(getCellValue(row, 4));
                docente.setDireccion(getCellValue(row, 5));
                docente.setGenero(getCellValue(row, 6));
                docente.setCarreraTexto(getCellValue(row, 7));
                docente.setModalidadTexto(getCellValue(row, 8));
                docente.setPeriodoTexto(getCellValue(row, 9));
                docente.setAsignaturaTexto(getCellValue(row, 10));
                docente.setParaleloTexto(getCellValue(row, 11));

                docentes.add(docente);
            }
            return docentes;
        } catch (IOException e) {
            throw new RuntimeException("Error al parsear el archivo Excel de Docentes: " + e.getMessage());
        }
    }

    public static List<SyllabiLoadDTO> excelToSyllabi(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<SyllabiLoadDTO> syllabiList = new ArrayList<>();

            // Empezamos en la fila 1 (saltando cabeceras)
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                SyllabiLoadDTO syllabi = new SyllabiLoadDTO();
                // Columna A (0): Carrera
                syllabi.setCarreraTexto(getCellValue(row, 0));
                // Columna B (1): Asignatura
                syllabi.setAsignaturaTexto(getCellValue(row, 1));
                // Columna C (2): Unidad (Como es número, lo parseamos y evitamos el "1.0" que a veces saca Excel)
                String unidadStr = getCellValue(row, 2).replace(".0", "");
                syllabi.setUnidad(unidadStr.isEmpty() ? 0 : Integer.parseInt(unidadStr));
                // Columna D (3): Nombre del Tema
                syllabi.setNombreTema(getCellValue(row, 3));
                syllabiList.add(syllabi);
            }
            return syllabiList;
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear el archivo Excel de Temarios: " + e.getMessage());
        }
    }
    public static List<CareerLoadDTO> excelToCareers(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<CareerLoadDTO> careerList = new ArrayList<>();

            // Fila 1 en adelante (saltamos las cabeceras en la fila 0)
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                CareerLoadDTO career = new CareerLoadDTO();
                // Columna A (0): Área Académica (Facultad)
                career.setNombreArea(getCellValue(row, 0));
                // Columna B (1): Abreviatura
                career.setAbrevArea(getCellValue(row, 1));
                // Columna C (2): Modalidad
                career.setNombreModalidad(getCellValue(row, 2));
                // Columna D (3): Nombre de la Carrera
                career.setNombreCarrera(getCellValue(row, 3));
                // Columna E (4): Número de Semestres
                // Usamos replace para evitar que Excel lea "8" como "8.0"
                String semestresStr = getCellValue(row, 4).replace(".0", "");
                career.setSemestres(semestresStr.isEmpty() ? 0 : Short.parseShort(semestresStr));
                careerList.add(career);
            }
            return careerList;
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear el archivo Excel de Carreras: " + e.getMessage());
        }
    }

    public static List<SubjectLoadDTO> excelToSubjects(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<SubjectLoadDTO> subjectList = new ArrayList<>();

            // Fila 1 en adelante (saltamos las cabeceras)
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                SubjectLoadDTO subject = new SubjectLoadDTO();
                // Columna A (0): Nombre de la Carrera
                subject.setNombreCarrera(getCellValue(row, 0));
                // Columna B (1): Nombre de la Asignatura
                subject.setNombreAsignatura(getCellValue(row, 1));
                // Columna C (2): Semestre (Es un número, así que lo limpiamos)
                String semestreStr = getCellValue(row, 2).replace(".0", "");
                subject.setSemestre(semestreStr.isEmpty() ? 0 : Short.parseShort(semestreStr));

                subjectList.add(subject);
            }
            return subjectList;
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear el archivo Excel de Asignaturas: " + e.getMessage());
        }
    }

    public static List<PeriodLoadDTO> excelToPeriods(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<PeriodLoadDTO> periodList = new ArrayList<>();

            // Fila 1 en adelante (saltamos las cabeceras)
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                PeriodLoadDTO period = new PeriodLoadDTO();
                
                // Columna A (0): Nombre del Periodo
                period.setNombrePeriodo(getCellValue(row, 0));
                
                // Columna B (1): Fecha de Inicio (Formato esperado: YYYY-MM-DD)
                String fechaInicioStr = getCellValue(row, 1);
                String fechaInicioNormalizada = ExcelValidator.normalizeDate(fechaInicioStr);
                period.setFechaInicio(LocalDate.parse(fechaInicioNormalizada));
                
                // Columna C (2): Fecha de Fin (Formato esperado: YYYY-MM-DD)
                String fechaFinStr = getCellValue(row, 2);
                String fechaFinNormalizada = ExcelValidator.normalizeDate(fechaFinStr);
                period.setFechaFin(LocalDate.parse(fechaFinNormalizada));

                periodList.add(period);
            }
            return periodList;
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear el archivo Excel de Periodos. Asegúrese de que las fechas tengan formato YYYY-MM-DD. Detalles: " + e.getMessage());
        }
    }

    public static List<EnrollmentDetailLoadDTO> excelToEnrollmentDetails(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<EnrollmentDetailLoadDTO> detailList = new ArrayList<>();

            // Fila 1 en adelante (saltamos cabeceras)
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                EnrollmentDetailLoadDTO detail = new EnrollmentDetailLoadDTO();
                
                // Columna A (0): Cédula del Estudiante
                detail.setCedulaEstudiante(getCellValue(row, 0));
                
                // Columna B (1): Periodo
                detail.setPeriodo(getCellValue(row, 1));
                
                // Columna C (2): Nombre de la Asignatura
                detail.setNombreAsignatura(getCellValue(row, 2));
                
                // Columna D (3): Paralelo
                detail.setParalelo(getCellValue(row, 3));

                detailList.add(detail);
            }
            return detailList;
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear el archivo Excel de Detalles de Matrícula: " + e.getMessage());
        }
    }

    public static List<ClassScheduleLoadDTO> excelToClassSchedules(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<ClassScheduleLoadDTO> scheduleList = new ArrayList<>();

            // Fila 1 en adelante (saltamos cabeceras)
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                ClassScheduleLoadDTO schedule = new ClassScheduleLoadDTO();
                
                // Columna A (0): Cédula del Docente
                schedule.setCedulaDocente(getCellValue(row, 0));
                
                // Columna B (1): Nombre Asignatura
                schedule.setNombreAsignatura(getCellValue(row, 1));
                
                // Columna C (2): Paralelo
                schedule.setNombreParalelo(getCellValue(row, 2));
                
                // Columna D (3): Periodo
                schedule.setNombrePeriodo(getCellValue(row, 3));
                
                // Columna E (4): Día de la semana (1 a 7)
                String diaStr = getCellValue(row, 4).replace(".0", "");
                schedule.setDiaSemana(diaStr.isEmpty() ? 0 : Integer.parseInt(diaStr));
                
                // Columna F (5): Hora Inicio
                String horaInicioStr = getCellValue(row, 5);
                schedule.setHoraInicio(parseExcelTime(horaInicioStr));
                
                // Columna G (6): Hora Fin
                String horaFinStr = getCellValue(row, 6);
                schedule.setHoraFin(parseExcelTime(horaFinStr));

                scheduleList.add(schedule);
            }
            return scheduleList;
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear el archivo Excel de Horarios. Verifica que las horas estén en formato HH:mm. Detalles: " + e.getMessage());
        }
    }

    // MÉTODO SEGURO PARA OBTENER VALORES
    private static String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return "";
        }
        // Usamos el formatter para obtener el texto tal cual se ve, 
        // evitando errores de "Cannot get a STRING value from a NUMERIC cell"
        return formatter.formatCellValue(cell).trim();
    }

    // Método extra para ignorar filas que visualmente están vacías pero tienen formato
    private static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK)
                return false;
        }
        return true;
    }
    // Método robusto para parsear horas desde Excel
    private static LocalTime parseExcelTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        
        timeStr = timeStr.trim();
        
        // Si la hora viene como "8:00" (1 dígito : 2 dígitos), le agregamos el '0' al inicio
        if (timeStr.matches("^\\d:\\d{2}.*")) {
            timeStr = "0" + timeStr;
        }
        
        // Si la hora trae segundos como "08:00:00", nos quedamos solo con los primeros 5 caracteres "08:00"
        if (timeStr.length() > 5) {
            timeStr = timeStr.substring(0, 5);
        }
        
        return LocalTime.parse(timeStr);
    }
    // Método para ignorar filas que parecen existir pero no tienen datos
    private static boolean isRowEmpty1(Row row) {
        if (row == null || row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String cellValue = getCellValue(row, cellNum);
                if (!cellValue.trim().isEmpty()) {
                    return false; // Si al menos una celda tiene texto real, la fila no está vacía
                }
            }
        }
        return true;
    }
}