package com.mediscreen.patientservice.repository;

import com.mediscreen.patientservice.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByLastName(String lastName);

    @Query(value = "SELECT * FROM patient_service.patients", nativeQuery = true)
    Page<Patient> findAllWithPagination(Pageable pageable);

    Patient save(Patient patient);
}
