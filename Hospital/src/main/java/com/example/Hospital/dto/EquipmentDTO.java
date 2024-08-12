package com.example.Hospital.dto;
import com.example.Hospital.Entity.EquipmentProcedure;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class EquipmentDTO {
    private Long equipmentId;
    private String equipmentName;
    private int costPerUse;

}
