package com.example.Hospital.Service;

import com.example.Hospital.Repository.PatientProcedureRepository;
import com.example.Hospital.Utils.Constants;
import com.example.Hospital.dto.ProcedureCostDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExcelReportService implements IExcelReportService {

    private final PatientProcedureRepository patientProcedureRepository;

    @Override
    public byte[] generateMonthlyReport(int month, int year) {
        List<ProcedureCostDTO> procedureCosts = patientProcedureRepository.findProceduresAndCostsByMonth(month, year);
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(Constants.MONTHLY_REPORT);
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue(Constants.PATIENT_NAME);
            header.createCell(1).setCellValue(Constants.PROCEDURE_NAME);
            header.createCell(2).setCellValue(Constants.TOTAL_COST);
            int rowNum = 1;
            double grandTotal = 0;
            String lastPatientName = null;
            for (ProcedureCostDTO dto : procedureCosts) {
                Row row = sheet.createRow(rowNum++);
                if (!dto.getPatientName().equals(lastPatientName)) {
                    row.createCell(0).setCellValue(dto.getPatientName());
                    lastPatientName = dto.getPatientName();
                }
                row.createCell(1).setCellValue(dto.getProcedureName());
                row.createCell(2).setCellValue(dto.getTotalCost());
                grandTotal += dto.getTotalCost();
            }
            Row totalRow = sheet.createRow(rowNum);
            totalRow.createCell(1).setCellValue(Constants.GRAND_TOTAL);
            totalRow.createCell(2).setCellValue(grandTotal);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException(Constants.FAILED_TO_GENERATE_EXCEL, e);
        }
    }
}

