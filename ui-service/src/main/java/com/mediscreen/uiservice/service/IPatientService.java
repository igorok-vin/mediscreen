package com.mediscreen.uiservice.service;

import com.mediscreen.uiservice.exception.BirthdateException;
import com.mediscreen.uiservice.model.Patient;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPatientService {
    Patient createPatient(Patient patient) throws BirthdateException;

    Patient getPatientById(long id);

    List<Patient> getPatientsByLastNameForSearch(String lastName);

    boolean check(String lastName);

    Page<Patient> getPaginatedPatientList(int pageNumber, int pageSize);

    Patient updatePatient(long id, Patient patient) throws BirthdateException;
}
