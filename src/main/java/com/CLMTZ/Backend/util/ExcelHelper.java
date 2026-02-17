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

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    // Verifica si el archivo es Excel
    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<StudentLoadDTO> excelToStudents(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0); // Leemos la primera hoja
            List<StudentLoadDTO> estudiantes = new ArrayList<>();

            // Empezamos en la fila 1 (asumiendo que la fila 0 son cabeceras)
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                StudentLoadDTO estudiante = new StudentLoadDTO();
                
                // NOTA: Ajusta los índices (0, 1, 2...) según el orden de tus columnas en el Excel real
                estudiante.setCedula(getCellValue(row, 0));
                estudiante.setNombres(getCellValue(row, 1));
                estudiante.setApellidos(getCellValue(row, 2));
                estudiante.setCorreo(getCellValue(row, 3));
                estudiante.setTelefono(getCellValue(row, 4));
                estudiante.setDireccion(getCellValue(row, 5));
                estudiante.setGenero(getCellValue(row, 6)); // "M" o "F"
                estudiante.setCarreraTexto(getCellValue(row, 7));
                estudiante.setModalidadTexto(getCellValue(row, 8));
                estudiante.setPeriodoTexto(getCellValue(row, 9));

                estudiantes.add(estudiante);
            }
            workbook.close();
            return estudiantes;
        } catch (IOException e) {
            throw new RuntimeException("Error al parsear el archivo Excel: " + e.getMessage());
        }
    }

    // Método auxiliar para evitar nulos y convertir todo a String
    private static String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return "";
        
        // Forzamos que todo se lea como texto (incluso números de teléfono o cédulas)
        cell.setCellType(CellType.STRING); 
        return cell.getStringCellValue();
    }

    public static List<TeachingDTO> excelToTeaching(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            List<TeachingDTO> docentes = new ArrayList<>();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row1 = sheet.getRow(i);
                if (row1 == null) continue;

                TeachingDTO docente = new TeachingDTO();
                // Datos personales (0-6)
                docente.setCedula(getCellValue(row1, 0));
                docente.setNombres(getCellValue(row1, 1));
                docente.setApellidos(getCellValue(row1, 2));
                docente.setCorreo(getCellValue(row1, 3));
                docente.setTelefono(getCellValue(row1, 4));
                docente.setDireccion(getCellValue(row1, 5));
                docente.setGenero(getCellValue(row1, 6));
                
                // Datos académicos
                docente.setCarreraTexto(getCellValue(row1, 7));
                docente.setModalidadTexto(getCellValue(row1, 8));
                docente.setPeriodoTexto(getCellValue(row1, 9));
                
                // Nuevas columnas
                docente.setAsignaturaTexto(getCellValue(row1, 10)); // Columna K
                docente.setParaleloTexto(getCellValue(row1, 11));   // Columna L

                docentes.add(docente);
            }
            workbook.close();
            return docentes;
        } catch (IOException e) {
            throw new RuntimeException("Error Excel Docentes: " + e.getMessage());
        }
    }
}