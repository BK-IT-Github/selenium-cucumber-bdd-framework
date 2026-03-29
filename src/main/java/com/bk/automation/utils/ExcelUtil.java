package com.bk.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for reading test data from Excel (.xlsx) files.
 * Supports reading by sheet name, row/column index, and named columns.
 */
public final class ExcelUtil {

    private static final Logger logger = LogManager.getLogger(ExcelUtil.class);

    private ExcelUtil() {
        // Utility class
    }

    /**
     * Reads all data from a specific sheet as a list of maps (column header → cell value).
     *
     * @param filePath  path to the Excel file
     * @param sheetName name of the sheet to read
     * @return list of maps where each map represents a row
     */
    public static List<Map<String, String>> readSheetData(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' not found in file: {}", sheetName, filePath);
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                logger.warn("Sheet '{}' has no header row", sheetName);
                return data;
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(headers.get(j), getCellValueAsString(cell));
                }
                data.add(rowData);
            }

            logger.info("Read {} rows from sheet '{}' in file: {}", data.size(), sheetName, filePath);

        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }

        return data;
    }

    /**
     * Reads a specific cell value from Excel.
     *
     * @param filePath  path to the Excel file
     * @param sheetName name of the sheet
     * @param rowNum    row index (0-based)
     * @param colNum    column index (0-based)
     * @return cell value as string
     */
    public static String getCellData(String filePath, String sheetName, int rowNum, int colNum) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            Row row = sheet.getRow(rowNum);
            if (row == null) {
                return "";
            }

            Cell cell = row.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            return getCellValueAsString(cell);

        } catch (IOException e) {
            logger.error("Error reading cell [{},{}] from sheet '{}' in file: {}",
                    rowNum, colNum, sheetName, filePath, e);
            throw new RuntimeException("Failed to read Excel cell", e);
        }
    }

    /**
     * Returns the total number of data rows in a sheet (excluding header).
     */
    public static int getRowCount(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            return (sheet != null) ? sheet.getLastRowNum() : 0;

        } catch (IOException e) {
            logger.error("Error getting row count from sheet '{}' in file: {}", sheetName, filePath, e);
            return 0;
        }
    }

    /**
     * Converts a cell value to its string representation.
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double numValue = cell.getNumericCellValue();
                if (numValue == Math.floor(numValue)) {
                    return String.valueOf((long) numValue);
                }
                return String.valueOf(numValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
