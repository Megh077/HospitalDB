package com.example.Hospital.Controller;
import com.example.Hospital.Service.ExcelReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ExcelReportService excelReportService;

    @GetMapping("/monthly/{month}/{year}")
    public ResponseEntity<byte[]> generateMonthlyReport(@PathVariable int month, @PathVariable int year) {
        byte[] excelBytes = excelReportService.generateMonthlyReport(month, year);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "MonthlyReport.xlsx");
        return ResponseEntity.ok().headers(headers).body(excelBytes);
    }
}

