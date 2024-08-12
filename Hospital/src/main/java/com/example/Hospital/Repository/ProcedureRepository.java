package com.example.Hospital.Repository;

import com.example.Hospital.Entity.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedureRepository extends JpaRepository<Procedure, Long> {
}

