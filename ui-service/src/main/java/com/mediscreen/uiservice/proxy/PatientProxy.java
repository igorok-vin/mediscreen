package com.mediscreen.uiservice.proxy;

import com.mediscreen.uiservice.exception.BirthdateException;
import com.mediscreen.uiservice.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="${patients-service-name}",url = "${patients-service-url}")
public interface PatientProxy {

    @PostMapping("/patient/add")
    Patient createPatient (@RequestBody Patient patient);

    @GetMapping("/patient/{id}")
    Patient getPatientById(@PathVariable ("id") long id);

    @GetMapping ("/patient/list/{lastName}")
    List<Patient> getPatientsByLastName(@PathVariable String lastName);

    @PostMapping("/patient/update/{id}")
    Patient updatePatient (@PathVariable long id, @RequestBody Patient patient) throws BirthdateException;

    @GetMapping("/patient/delete/{id}")
    void deletePatient (@PathVariable long id);

     @GetMapping ("/patient/list")
    Page<Patient> paginated (Pageable pageable);
}
