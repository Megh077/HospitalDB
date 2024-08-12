package com.example.Hospital.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientProcedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ppId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    private Date procedureDate;


}
