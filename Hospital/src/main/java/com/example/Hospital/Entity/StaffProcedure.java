package com.example.Hospital.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class StaffProcedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spId;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    private int costPerHour;



}

