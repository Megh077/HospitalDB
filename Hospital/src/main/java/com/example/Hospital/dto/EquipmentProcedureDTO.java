package com.example.Hospital.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EquipmentProcedureDTO {
    private Long id;
    private Long equipmentId;
    private Long procedureId;
    private int quantity;


}
