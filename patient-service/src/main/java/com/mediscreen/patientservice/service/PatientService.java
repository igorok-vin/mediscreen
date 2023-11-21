package com.mediscreen.patientservice.service;

import com.mediscreen.patientservice.exception.BirthdateException;
import com.mediscreen.patientservice.exception.PatientNotFoundException;
import com.mediscreen.patientservice.model.Patient;
import com.mediscreen.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService implements IPatientService {

    PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient getPatientById(long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if(!patient.isPresent()) {
            throw new PatientNotFoundException("Patient with ID " + id +" not found.");
        }
        Patient result = patient.get();
        calculatePatientAge(result);
        return result;
    }

    @Override
    public List<Patient> getPatients() {
        return patientRepository.findAll().stream().peek(this::calculatePatientAge).collect(Collectors.toList());
    }

     @Override
    public void calculatePatientAge(Patient patient) {
        LocalDate patientBD = patient.getDob();
        LocalDate currentDate = LocalDate.now();

       Period age = Period.between(patientBD,currentDate);
       String patientAge = null;

       if(age.getYears()==0 && age.getMonths()==0) {
           if (age.getDays() == 1 || age.getDays() == 0) {
               patientAge = "1 day";
           } else {
               patientAge = age.getDays() + " days";
           }
       } else if (age.getYears()==0)  {
           patientAge = age.getMonths() + " month";
       } else if (age.getYears()>0) {
           patientAge = age.getYears()+"";
       }
        patient.setAge(patientAge);
    }

    @Override
    public Patient updatePatient(long id, Patient patient) throws BirthdateException {
        Patient currentPatient = getPatientById(id);
        currentPatient.setFirstName(patient.getFirstName());
        currentPatient.setLastName(patient.getLastName());
        currentPatient.setDob(patient.getDob());
        currentPatient.setGender(patient.getGender());
        currentPatient.setAddress(patient.getAddress());
        currentPatient.setPhoneNumber(patient.getPhoneNumber());

        LocalDate currentDate = LocalDate.now();
        LocalDate patientBD = currentPatient.getDob();
        if (patientBD.isAfter(currentDate)){
            throw new BirthdateException("Invalid date of birth");
        }
        return patientRepository.save(currentPatient);
    }

    @Override
    public void deletePatient(long id) {
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
    }

    @Override
    public List<Patient> getPatientsByLastName(String lastName){
        List<Patient> patientListByLastName = patientRepository.findByLastName(lastName);
        if(patientListByLastName.isEmpty()){
            throw new PatientNotFoundException("Patient with last name "+ lastName+" not found");
        }
        return patientListByLastName.stream().peek(this::calculatePatientAge).collect(Collectors.toList());
    }

    @Override
    public Page<Patient> paginated(Pageable pageable) {
        Page<Patient> patientsList = patientRepository.findAllWithPagination(pageable);

        long totalItems = patientsList.getTotalElements();

        return new PageImpl<>(patientsList.stream().peek(this::calculatePatientAge).toList(),pageable,totalItems);
    }
}
