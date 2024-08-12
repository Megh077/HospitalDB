package com.example.Hospital.Repository;

import com.example.Hospital.Entity.StaffProcedure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffProcedureRepository extends JpaRepository<StaffProcedure, Long> {
}
