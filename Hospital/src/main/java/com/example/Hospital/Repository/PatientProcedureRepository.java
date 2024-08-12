package com.example.Hospital.Repository;
import com.example.Hospital.Entity.PatientProcedure;
import com.example.Hospital.dto.EquipmentCostDTO;
import com.example.Hospital.dto.ProcedureCostDTO;
import com.example.Hospital.dto.StaffCostDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientProcedureRepository extends JpaRepository<PatientProcedure, Long> {


    @Query("SELECT new com.example.Hospital.dto.StaffCostDTO(pr.procedureName, s.staffName,sp.costPerHour, sp.costPerHour * pr.hours) " +
            "FROM PatientProcedure pp " +
            "JOIN pp.procedure pr " +
            "JOIN pr.staffProcedures sp " +
            "JOIN sp.staff s " +
            "WHERE pp.patient.patientId = :patientId")
    List<StaffCostDTO> findStaffCostsByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT new com.example.Hospital.dto.EquipmentCostDTO(pr.procedureName,e.equipmentName, ep.quantity,e.costPerUse, e.costPerUse * ep.quantity) " +
            "FROM PatientProcedure pp " +
            "JOIN pp.procedure pr " +
            "JOIN pr.equipmentProcedures ep " +
            "JOIN ep.equipment e " +
            "WHERE pp.patient.patientId = :patientId")
    List<EquipmentCostDTO> findEquipmentCostsByPatientId(@Param("patientId") Long patientId);

@Query("SELECT new com.example.Hospital.dto.ProcedureCostDTO(p.patientName, pr.procedureName, SUM(pr.hours * sp.costPerHour)) " +
        "FROM PatientProcedure pp " +
        "JOIN pp.procedure pr " +
        "JOIN pr.staffProcedures sp " +
        "JOIN sp.staff s " +
        "JOIN pp.patient p " +
        "WHERE EXTRACT(MONTH FROM pp.procedureDate) = :month " +
        "AND EXTRACT(YEAR FROM pp.procedureDate) = :year " +
        "GROUP BY p.patientName, pr.procedureName")
List<ProcedureCostDTO> findProceduresAndCostsByMonth(@Param("month") int month, @Param("year") int year);


}

