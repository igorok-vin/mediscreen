package com.mediscreen.uiservice.service;

import com.mediscreen.uiservice.exception.BirthdateException;
import com.mediscreen.uiservice.exception.PatientNotFoundException;
import com.mediscreen.uiservice.model.Patient;
import com.mediscreen.uiservice.proxy.PatientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class PatientService implements IPatientService {

    PatientProxy patientProxy;

    @Autowired
    public PatientService(PatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    @Override
    public Patient createPatient(Patient patient) throws BirthdateException {
        LocalDate currentDate = LocalDate.now();
        LocalDate patientBD = patient.getDob();
        Period age = Period.between(patientBD,currentDate);
        if (patientBD.isAfter(currentDate)||(age.getYears()>120)){
            throw new BirthdateException("Invalid date of birth");
        }
       return patientProxy.createPatient(patient);
    };

    @Override
    public Patient updatePatient(long id, Patient patient) throws BirthdateException {
        LocalDate currentDate = LocalDate.now();
        LocalDate patientBD = patient.getDob();
        Period age = Period.between(patientBD,currentDate);
        if (patientBD.isAfter(currentDate)||(age.getYears()>120)){
            throw new BirthdateException("Invalid date of birth");
        }
        return patientProxy.updatePatient(id,patient);
    }

    @Override
    public Patient getPatientById(long id) {
        return patientProxy.getPatientById(id);
    }

   @Override
   public List<Patient> getPatientsByLastNameForSearch(String lastName) {
       List<Patient> patientList=null;
       if(check(lastName)){
           patientList= patientProxy.getPatientsByLastName(lastName);
       }else{
           throw new PatientNotFoundException("Patient with last name "+ lastName+" not found");
       }
       return patientList;

   }

    @Override
    public boolean check(String lastName) {
        List<Patient> list = patientProxy.getPatientsByLastName(lastName);
        if(list.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public Page<Patient> getPaginatedPatientList(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return patientProxy.paginated(pageable);
    }
}
