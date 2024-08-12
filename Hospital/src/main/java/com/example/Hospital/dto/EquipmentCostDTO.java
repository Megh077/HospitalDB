package com.example.Hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentCostDTO {
    private String procedureName;
    private String equipmentName;
    private int quantity;
    private double costPerUse;
    private double totalCost;
}