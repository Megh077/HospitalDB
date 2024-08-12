package com.example.Hospital.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
@Data
@Builder
public class PatientDTO {
    private Long patientId;
    private String patientName;
    private String bloodGroup;
    private Date admissionDate;
    private Date dischargeDate;


}

