package com.example.Hospital.Service;

public interface IExcelReportService {
    byte[] generateMonthlyReport(int month, int year);
}
