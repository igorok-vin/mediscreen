package com.mediscreen.reportservice.proxy;

import com.mediscreen.reportservice.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="${patients-service-name}",url = "${patients-service-url}")
public interface PatientProxy {
    @GetMapping("/patient/{id}")
    Patient getPatientById(@PathVariable long id);

    @GetMapping("/patient/listPatients")
    List<Patient> getPatients();
}
