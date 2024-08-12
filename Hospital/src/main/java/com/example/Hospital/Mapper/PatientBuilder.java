package com.example.Hospital.Mapper;
import com.example.Hospital.Entity.Patient;
import com.example.Hospital.Utils.Constants;
import com.example.Hospital.advice.InvalidInputException;
import com.example.Hospital.dto.PatientDTO;


public class PatientBuilder {

    public static Patient buildPatientFromDTO(PatientDTO patientDTO) {
        Patient patient = new Patient();
        if(patientDTO.getPatientName().isBlank()) {
            throw new InvalidInputException(Constants.PATIENT_CANNOT_BE_EMPTY);
        }
        if(patientDTO.getAdmissionDate()==null){
            throw new InvalidInputException(Constants.ADMISSION_DATE_CANNOT_BE_EMPTY);
        }
        patient.setPatientName(patientDTO.getPatientName());
        patient.setBloodGroup(patientDTO.getBloodGroup());
        patient.setAdmissionDate(patientDTO.getAdmissionDate());
        patient.setDischargeDate(null);

        return patient;
    }

}

