package com.example.Hospital.Controller;
import com.example.Hospital.Service.PatientService;
import com.example.Hospital.dto.PatientDTO;
import com.example.Hospital.dto.PatientProcedureDTO;
import com.example.Hospital.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/entry")
    public ResponseEntity<ResponseDTO> addPatient(@RequestBody PatientDTO patientDTO) {
        ResponseDTO response = patientService.addPatient(patientDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/discharge")
    public ResponseEntity<ResponseDTO> dischargePatient(@PathVariable Long id, @RequestBody Date dischargeDate) {
        ResponseDTO response = patientService.dischargePatient(id, dischargeDate);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/procedures")
    public ResponseEntity<ResponseDTO> updateProcedures(@PathVariable Long id, @RequestBody List<PatientProcedureDTO> procedureDTOs) {
        ResponseDTO response = patientService.updateProcedures(id, procedureDTOs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/invoice")
    public ResponseEntity<byte[]> generateInvoice(@PathVariable Long id) {
        byte[] pdfBytes = patientService.generateInvoice(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "invoice.pdf");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
