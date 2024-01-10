package com.mediscreen.uiservice.controller;

import com.mediscreen.uiservice.model.Patient;
import com.mediscreen.uiservice.proxy.PatientProxy;
import com.mediscreen.uiservice.proxy.ReportProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ReportController {

    private final static Logger logger = LoggerFactory.getLogger(ReportController.class);
    ReportProxy reportProxy;

    PatientProxy patientProxy;

    @Autowired
    public ReportController(ReportProxy reportProxy, PatientProxy patientProxy) {
        this.reportProxy = reportProxy;
        this.patientProxy = patientProxy;
    }

    @GetMapping("/patHistory/assess/{patientId}")
    public String getReportByPatientId (@PathVariable("patientId") long patientId, Model model){
       String report = reportProxy.getReportByPatientId(patientId).getRiskLevel();
       Patient patient = patientProxy.getPatientById(patientId);
       model.addAttribute("report", report);
       model.addAttribute("patientUI",patient);
        logger.info("UI-SERVICE: ReportController - Get report by patient ID");
        return "report/report";
    }
}
