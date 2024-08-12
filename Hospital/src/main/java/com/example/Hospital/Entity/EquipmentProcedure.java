package com.example.Hospital.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentProcedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long epId;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    private int quantity;
}
