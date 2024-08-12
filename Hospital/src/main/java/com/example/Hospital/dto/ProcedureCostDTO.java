package com.example.Hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureCostDTO {
    private String patientName;
    private String procedureName;
    private double totalCost;
}


