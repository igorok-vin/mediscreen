package com.mediscreen.patientservice.controller;

import com.mediscreen.patientservice.exception.BirthdateException;
import com.mediscreen.patientservice.model.Patient;
import com.mediscreen.patientservice.service.IPatientService;
import com.mediscreen.patientservice.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PatientController {

    private final static Logger logger = LoggerFactory.getLogger(PatientController.class);

    private IPatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/patient/add")
    public Patient createPatient(@RequestBody @Valid Patient patient) {
        logger.info("PATIENT-SERVICE: PatientController - The patient has been created successfully.");
        return patientService.createPatient(patient);
    }

    @GetMapping("/patient/{id}")
    public Patient getPatientById(@PathVariable long id) {
        logger.info("PATIENT-SERVICE: PatientController - Get patient by ID.");
        return patientService.getPatientById(id);
    }

    @GetMapping("/patient/listPatients")
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }

    @PostMapping("/patient/update/{id}")
    public Patient updatePatient(@PathVariable long id, @RequestBody @Valid Patient patient) throws BirthdateException {
        logger.info("PATIENT-SERVICE: PatientController - Update patient by ID.");
        return patientService.updatePatient(id, patient);
    }

    @GetMapping("/patient/delete/{id}")
    public void deletePatient(@PathVariable long id) {
        logger.info("PATIENT-SERVICE: PatientController - Delete patient by ID.");
        patientService.deletePatient(id);
    }

    @GetMapping("/patient/list/{lastName}")
    public List<Patient> getPatientsByLastName(@PathVariable("lastName") String lastName) {
        logger.info("PATIENT-SERVICE: PatientController - Get patient by last name.");
        return patientService.getPatientsByLastName(lastName);
    }

    @GetMapping("/patient/list")
    public Page<Patient> pageableList (Pageable pageable) {
        logger.info("PATIENT-SERVICE: PatientController - Get a list of all patients.");
        return patientService.paginated(pageable);
    }

}