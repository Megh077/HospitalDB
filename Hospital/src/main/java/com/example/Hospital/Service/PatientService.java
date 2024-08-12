package com.example.Hospital.Service;
import com.example.Hospital.Entity.Patient;
import com.example.Hospital.Entity.PatientProcedure;
import com.example.Hospital.Entity.Procedure;
import com.example.Hospital.Mapper.PatientBuilder;
import com.example.Hospital.Mapper.PatientProcedureBuilder;
import com.example.Hospital.Repository.PatientProcedureRepository;
import com.example.Hospital.Repository.PatientRepository;
import com.example.Hospital.Repository.ProcedureRepository;
import com.example.Hospital.Utils.Constants;
import com.example.Hospital.advice.InvalidInputException;
import com.example.Hospital.advice.NotFoundException;
import com.example.Hospital.advice.ValidationException;
import com.example.Hospital.dto.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService implements IPatientService {

    private final PatientRepository patientRepository;
    private final ProcedureRepository procedureRepository;
    private final PatientProcedureRepository patientProcedureRepository;
    @Override
    public ResponseDTO addPatient(PatientDTO patientDTO) {
        Patient patient = PatientBuilder.buildPatientFromDTO(patientDTO);
        patient = patientRepository.save(patient);
        patientDTO.setPatientId(patient.getPatientId());
        return ResponseDTO.builder()
                .message(Constants.PATIENT_ADDED_SUCCESSFULLY)
                .data(patientDTO)
                .build();
    }
    @Override
    public ResponseDTO dischargePatient(Long id, Date dischargeDate) {
        if (dischargeDate == null) {
            throw new InvalidInputException(Constants.DISCHARGE_DATE_CANNOT_BE_EMPTY);
        }
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.PATIENT_DOES_NOT_EXIST));
        if(dischargeDate.before(patient.getAdmissionDate())){
            throw new ValidationException(Constants.DISCHARGE_DATE_SHOULD_BE_AFTER_ADMISSION_DATE);
        }
        patient.setDischargeDate(dischargeDate);
        patientRepository.save(patient);
        return ResponseDTO.builder()
                .message(Constants.PATIENT_DISCHARGED_SUCCESSFULLY)
                .data(patient)
                .build();
    }
    @Override
    public ResponseDTO updateProcedures(Long patientId, List<PatientProcedureDTO> patientProcedureDTOs) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException(Constants.PATIENT_DOES_NOT_EXIST));
        List<PatientProcedure> savedProcedures = new ArrayList<>();
        for (PatientProcedureDTO dto : patientProcedureDTOs) {
            if (dto.getProcedureId() == null) {
                throw new InvalidInputException(Constants.PROCEDURE_ID_CANNOT_BE_EMPTY);
            }
            if (dto.getProcedureDate() == null) {
                throw new InvalidInputException(Constants.PROCEDURE_DATE_CANNOT_BE_EMPTY);
            }
            if (dto.getProcedureDate().before(patient.getAdmissionDate())) {
                throw new InvalidInputException(Constants.INVALID_PROCEDURE_DATEA);
            }
            if (patient.getDischargeDate() != null && dto.getProcedureDate().after(patient.getDischargeDate())) {
                throw new InvalidInputException(Constants.INVALID_PROCEDURE_DATED);
            }
            Procedure procedure = procedureRepository.findById(dto.getProcedureId())
                    .orElseThrow(() -> new NotFoundException(Constants.PROCEDURE_DOES_NOT_EXIST));
            PatientProcedure patientProcedure = PatientProcedureBuilder.buildPatientProcedureFromDTO(dto, patient, procedure);
            savedProcedures.add(patientProcedureRepository.save(patientProcedure));
            }
            List<PatientProcedureDTO> responseDTOs = savedProcedures.stream()
                    .map(PatientProcedureBuilder::buildDTOFromPatientProcedure)
                    .collect(Collectors.toList());
            return ResponseDTO.builder()
                    .message(Constants.PROCEDURE_UPDATED_SUCCESSFULLY)
                    .data(responseDTOs)
                    .build();
    }
    @Override
    public byte[] generateInvoice(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new InvalidInputException(Constants.PATIENT_DOES_NOT_EXIST));
        List<StaffCostDTO> staffCosts = patientProcedureRepository.findStaffCostsByPatientId(patientId);
        List<EquipmentCostDTO> equipmentCosts = patientProcedureRepository.findEquipmentCostsByPatientId(patientId);
        double totalStaffCost = staffCosts.stream().mapToDouble(StaffCostDTO::getTotalCost).sum();
        double totalEquipmentCost = equipmentCosts.stream().mapToDouble(EquipmentCostDTO::getTotalCost).sum();
        double grandTotal = totalStaffCost + totalEquipmentCost;
        Map<String, List<EquipmentCostDTO>> equipmentCostsByProcedure = equipmentCosts.stream().collect(Collectors.groupingBy(EquipmentCostDTO::getProcedureName));
        Map<String, List<StaffCostDTO>> staffCostsByProcedure = staffCosts.stream().collect(Collectors.groupingBy(StaffCostDTO::getProcedureName));
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Paragraph header = new Paragraph(Constants.INVOICE, headFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            Paragraph patientName = new Paragraph(Constants.PATIENT_NAME + patient.getPatientName(), boldFont);
            document.add(patientName);
            Paragraph admnDate = new Paragraph(Constants.ADMISSION_DATE + patient.getAdmissionDate(), boldFont);
            document.add(admnDate);
            Paragraph disDate = new Paragraph(Constants.DISCHARGE_DATE + patient.getDischargeDate(), boldFont);
            document.add(disDate);
            document.add(new Paragraph("\n"));
            for (String procedureName : equipmentCostsByProcedure.keySet()) {
                document.add(new Paragraph("\n"));
                document.add(new Paragraph(Constants.PROCEDURE + procedureName));
                document.add(new Paragraph(Constants.EQUIPMENT_DETAILS));
                PdfPTable equipTable = createEquipmentTable(equipmentCostsByProcedure, procedureName);
                document.add(equipTable);
                document.add(new Paragraph(Constants.STAFF_DETAILS));
                PdfPTable staffTable = createStaffTable(staffCostsByProcedure, procedureName);
                document.add(staffTable);
            }
            document.add(new Paragraph(Constants.TOTAL_COST_BY_PROCEDURE));
            PdfPTable totalCostTable = createTotalCostTable(staffCostsByProcedure, equipmentCostsByProcedure);
            document.add(totalCostTable);
            Paragraph grandTotalParagraph = new Paragraph(Constants.GRAND_TOTAL + grandTotal);
            grandTotalParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(grandTotalParagraph);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(Constants.FAILED_TO_GENERATE_PDF, e);
        }
    }

    private PdfPTable createEquipmentTable(Map<String, List<EquipmentCostDTO>> equipmentCostsByProcedure, String procedureName) {
        PdfPTable equipTable = new PdfPTable(4);
        equipTable.setWidthPercentage(100);
        equipTable.setSpacingBefore(10f);
        equipTable.setSpacingAfter(10f);
        addEquipTableHeader(equipTable);
        addEquipRows(equipTable, equipmentCostsByProcedure.get(procedureName));
        return equipTable;
    }
    private PdfPTable createStaffTable(Map<String, List<StaffCostDTO>> staffCostsByProcedure, String procedureName) {
        PdfPTable staffTable = new PdfPTable(3);
        staffTable.setWidthPercentage(100);
        staffTable.setSpacingBefore(10f);
        staffTable.setSpacingAfter(10f);
        addStaffTableHeader(staffTable);
        addStaffRows(staffTable, staffCostsByProcedure.get(procedureName));
        return staffTable;
    }
    private PdfPTable createTotalCostTable(Map<String, List<StaffCostDTO>> staffCostsByProcedure, Map<String, List<EquipmentCostDTO>> equipmentCostsByProcedure) {
        PdfPTable totalCostTable = new PdfPTable(3);
        totalCostTable.setWidthPercentage(100);
        totalCostTable.setSpacingBefore(10f);
        totalCostTable.setSpacingAfter(10f);
        addTotalCostTableHeader(totalCostTable);
        addTotalCostRows(totalCostTable, staffCostsByProcedure, equipmentCostsByProcedure);
        return totalCostTable;
    }

    private void addEquipTableHeader(PdfPTable table) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(Constants.EQUIPMENT_NAME));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(Constants.COST_PER_UNIT));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(Constants.QUANTITY));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(Constants.TOTAL_EQUIPMENT_COST));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addStaffTableHeader(PdfPTable table) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(Constants.STAFF_NAME));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(Constants.COST_PER_HOUR));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(Constants.TOTAL_STAFF_COST));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addTotalCostTableHeader(PdfPTable table) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(Constants.PROCEDURE_NAME));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(Constants.TOTAL_STAFF_COST));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(Constants.TOTAL_EQUIPMENT_COST));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addEquipRows(PdfPTable table, List<EquipmentCostDTO> equipmentCosts) {
        for (EquipmentCostDTO dto : equipmentCosts) {
            table.addCell(dto.getEquipmentName());
            table.addCell(String.valueOf(dto.getCostPerUse()));
            table.addCell(String.valueOf(dto.getQuantity()));
            table.addCell(String.valueOf(dto.getTotalCost()));
        }
    }

    private void addStaffRows(PdfPTable table, List<StaffCostDTO> staffCosts) {
        for (StaffCostDTO dto : staffCosts) {
            table.addCell(dto.getStaffName());
            table.addCell(String.valueOf(dto.getCostPerHour()));
            table.addCell(String.valueOf(dto.getTotalCost()));
        }
    }

    private void addTotalCostRows(PdfPTable table, Map<String, List<StaffCostDTO>> staffCostsByProcedure, Map<String, List<EquipmentCostDTO>> equipmentCostsByProcedure) {
        for (String procedureName : staffCostsByProcedure.keySet()) {
            double totalStaffCost = staffCostsByProcedure.get(procedureName).stream().mapToDouble(StaffCostDTO::getTotalCost).sum();
            double totalEquipmentCost = equipmentCostsByProcedure
                    .getOrDefault(procedureName, Collections.emptyList())
                    .stream().mapToDouble(EquipmentCostDTO::getTotalCost)
                    .sum();
            table.addCell(procedureName);
            table.addCell(String.valueOf(totalStaffCost));
            table.addCell(String.valueOf(totalEquipmentCost));

        }
    }
}
