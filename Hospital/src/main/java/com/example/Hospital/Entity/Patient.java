package com.example.Hospital.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;
    private String patientName;
    private String bloodGroup;
    private Date admissionDate;
    private Date dischargeDate;
//    @OneToMany(mappedBy = "patient")
//    private List<PatientProcedure> procedures;


}
