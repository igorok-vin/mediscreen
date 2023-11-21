package com.mediscreen.uiservice.proxy;

import com.mediscreen.uiservice.model.Report;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="${report-service-name}",url = "${report-service-url}")
public interface ReportProxy {
    @GetMapping("/assess/{patientId}")
    public Report getReportByPatientId (@PathVariable("patientId") long patientId);
}
