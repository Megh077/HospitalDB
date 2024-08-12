package com.example.Hospital.Mapper;
import com.example.Hospital.Entity.Patient;
import com.example.Hospital.Entity.PatientProcedure;
import com.example.Hospital.Entity.Procedure;
import com.example.Hospital.dto.PatientProcedureDTO;
import com.example.Hospital.advice.InvalidInputException;
import com.example.Hospital.Utils.Constants;

public class PatientProcedureBuilder {

    public static PatientProcedure buildPatientProcedureFromDTO(PatientProcedureDTO dto, Patient patient, Procedure procedure) {
        if (dto.getProcedureDate() == null) {
            throw new InvalidInputException(Constants.PROCEDURE_DATE_CANNOT_BE_EMPTY);
        }

        PatientProcedure patientProcedure = new PatientProcedure();
        patientProcedure.setPatient(patient);
        patientProcedure.setProcedure(procedure);
        patientProcedure.setProcedureDate(dto.getProcedureDate());

        return patientProcedure;
    }
    public static PatientProcedureDTO buildDTOFromPatientProcedure(PatientProcedure patientProcedure) {
        return PatientProcedureDTO.builder()
                .id(patientProcedure.getPpId())
                .patientId(patientProcedure.getPatient().getPatientId())
                .procedureId(patientProcedure.getProcedure().getProcedureId())
                .procedureDate(patientProcedure.getProcedureDate())
                .build();
    }
}

