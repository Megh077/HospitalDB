package com.example.Hospital.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
@Builder
public class PatientProcedureDTO {
    private Long id;
    private Long patientId;
    private Long procedureId;
    private Date procedureDate;


}

