package com.mediscreen.patientservice.service;

import com.mediscreen.patientservice.exception.BirthdateException;
import com.mediscreen.patientservice.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPatientService {
    Patient createPatient(Patient patient);

    Patient getPatientById(long id);

    List<Patient> getPatients();

    void calculatePatientAge(Patient patient);

    Patient updatePatient(long id, Patient patient) throws BirthdateException;

    void deletePatient(long id);

    List<Patient> getPatientsByLastName(String lastName);

    Page<Patient> paginated(Pageable pageable);


}
