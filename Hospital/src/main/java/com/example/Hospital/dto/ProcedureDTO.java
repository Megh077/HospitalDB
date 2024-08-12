package com.example.Hospital.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcedureDTO {
    private Long procedureId;
    private String procedureName;
    private int hours;


}

