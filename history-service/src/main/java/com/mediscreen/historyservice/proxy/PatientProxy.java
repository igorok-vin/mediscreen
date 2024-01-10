package com.mediscreen.historyservice.proxy;

import com.mediscreen.historyservice.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="${patients-service-name}",url = "${patients-service-url}")
public interface PatientProxy {

    @PostMapping("/patient/add")
    Patient createPatient (@RequestBody Patient patient);

    @GetMapping("/patient/{id}")
    Patient getPatientById(@PathVariable long id);

    @GetMapping ("/patient/listPatients")
    List<Patient> getPatients();

    @GetMapping ("/patient/list/{lastName}")
    List<Patient> getPatientsByLastName(@PathVariable String lastName);

    @GetMapping("/patient/delete/{id}")
    void deletePatient (@PathVariable long id);
}
