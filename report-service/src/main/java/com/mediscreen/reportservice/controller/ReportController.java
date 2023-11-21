package com.mediscreen.reportservice.controller;

import com.mediscreen.reportservice.model.Report;
import com.mediscreen.reportservice.service.IReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    IReportService reportService;

    @Autowired
    public ReportController(IReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/assess/{patientId}")
    public Report getReportByPatientId (@PathVariable("patientId") long patientId) {
        logger.info("REPORT-SERVICE: ReportController - Get report by patient ID");
        return reportService.getReportByPatientId(patientId);
    }
}
