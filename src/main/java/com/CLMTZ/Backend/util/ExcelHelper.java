package com.CLMTZ.Backend.util;

import com.CLMTZ.Backend.dto.academic.StudentLoadDTO;
import com.CLMTZ.Backend.dto.academic.TeachingDTO;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
}