package com.example.Hospital.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Procedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long procedureId;
    private String procedureName;
    private int hours;
    @OneToMany(mappedBy = "procedure")
    private List<EquipmentProcedure> equipmentProcedures;
    @OneToMany(mappedBy = "procedure")
    private List<StaffProcedure> staffProcedures;

//    @OneToMany(mappedBy = "procedure")
//    private List<PatientProcedure> patientProcedures;
//    @OneToMany(mappedBy = "procedure")
//    private List<StaffProcedure> staffProcedures;
//    @OneToMany(mappedBy = "procedure")
//    private List<EquipmentProcedure> equipmentProcedures;


}
