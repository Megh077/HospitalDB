package com.example.Hospital.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffCostDTO {
    private String procedureName;
    private String staffName;
    private double costPerHour;
    private double totalCost;

    public StaffCostDTO(String staffName, double totalCost) {
        this.staffName = staffName;
        this.totalCost = totalCost;
    }
}

