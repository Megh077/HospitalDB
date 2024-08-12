package com.example.Hospital.Service;

import com.example.Hospital.dto.PatientDTO;
import com.example.Hospital.dto.PatientProcedureDTO;
import com.example.Hospital.dto.ResponseDTO;
import java.sql.Date;
import java.util.List;

public interface IPatientService {

    ResponseDTO addPatient(PatientDTO patientDTO);

    ResponseDTO dischargePatient(Long id, Date dischargeDate);

    ResponseDTO updateProcedures(Long patientId, List<PatientProcedureDTO> patientProcedureDTOs);

    byte[] generateInvoice(Long patientId);
}
