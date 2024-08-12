package com.example.Hospital.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StaffProcedureDTO {
    private Long id;
    private Long staffId;
    private Long procedureId;
    private int costPerHour;



}

