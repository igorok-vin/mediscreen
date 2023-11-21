package com.mediscreen.reportservice.service;

import com.mediscreen.reportservice.model.Report;

import java.util.List;

public interface IReportService {
    int getPatientAge(long id);

    List<String> getListNotesByPatientId(long id);

    List<String> toLowerCasePatientNotes(List<String> patientNotes);

    int getNumberTriggerTerms(List<String> patientNotes);

    Report getReportByPatientId(long id);
}
