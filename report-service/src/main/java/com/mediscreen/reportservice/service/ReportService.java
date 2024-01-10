package com.mediscreen.reportservice.service;

import com.mediscreen.reportservice.exception.PatientNotFoundException;
import com.mediscreen.reportservice.model.Gender;
import com.mediscreen.reportservice.model.Note;
import com.mediscreen.reportservice.model.Patient;
import com.mediscreen.reportservice.model.Report;
import com.mediscreen.reportservice.proxy.HistoryProxy;
import com.mediscreen.reportservice.proxy.PatientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService implements IReportService {
    HistoryProxy historyProxy;
    PatientProxy patientProxy;

    @Autowired
    public ReportService(HistoryProxy historyProxy, PatientProxy patientProxy) {
        this.historyProxy = historyProxy;
        this.patientProxy = patientProxy;
    }
    @Override
    public int getPatientAge(long id){
        Patient patient = patientProxy.getPatientById(id);
        String age = patient.getAge();
        String processAge;
        boolean day = age.matches("\\d day");
        boolean days = age.matches("\\d days");
        boolean month = age.matches("\\d month");
        if(day||days||month){
            processAge ="1";
        } else {
            processAge=age;
        }
        String ageNumber= processAge.replaceAll("[^0-9]", "");
        int ageResult = Integer.valueOf(ageNumber);
        return ageResult;
    }

    @Override
    public List<String> getListNotesByPatientId(long id) {
        Patient patient = patientProxy.getPatientById(id);
        List<String> notesByPatient = historyProxy.findListOfNotesByPatientId(patient.getPatientId()).stream().map(Note::getNote).collect(Collectors.toList());
        return notesByPatient;
    }

    @Override
    public List<String> toLowerCasePatientNotes(List<String> patientNotes) {
        List<String> patientNotesFromDB = patientNotes;
        patientNotesFromDB.replaceAll(note->note.toLowerCase());
        return patientNotesFromDB;
    }

    @Override
    public int getNumberTriggerTerms(List<String> patientNotes) {
        Set<String> triggerTerms = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        triggerTerms.add("hemoglobin a1c");
        triggerTerms.add("microalbumin");
        triggerTerms.add("body height");
        triggerTerms.add("body weight");
        triggerTerms.add("smoker");
        triggerTerms.add("abnormal");
        triggerTerms.add("cholesterol");
        triggerTerms.add("dizziness");
        triggerTerms.add("relapse");
        triggerTerms.add("reaction");
        triggerTerms.add("antibodies");

        List<String> patientNotesFromDB = toLowerCasePatientNotes(patientNotes);

        Set<String> triggerSet = new HashSet<>();
        patientNotesFromDB.forEach(note -> {
            triggerTerms.forEach(term -> {
                if (note.contains(term)) {
                    triggerSet.add(term);
                }
            });
        });

        int number = triggerSet.size();
        System.out.println(triggerSet);
        return number;
    }

    @Override
    public Report getReportByPatientId(long id) {
        String riskLevel;
        List <Patient> patientList = patientProxy.getPatients();
            if (patientList.contains(id)){
                throw new PatientNotFoundException("Report creation unsuccessful. Patient with ID " + id + " not found.");
            }
        Patient patient = patientProxy.getPatientById(id);
        List<String> notesByPatient = getListNotesByPatientId(id);
        int patientAge = getPatientAge(id);
        int triggersNumber = getNumberTriggerTerms(notesByPatient);

        if(patientAge<30){
            //for patient female with less 30 years
            if(patient.getGender().equals(Gender.Female)) {
                if(triggersNumber >=4 & triggersNumber < 7){
                    riskLevel = "In danger";
                } else if (triggersNumber >= 7) {
                    riskLevel = "Early onset";
                } else {
                    riskLevel = "None";
                }
            } else {
                //for patient male with less 30 years
                if(triggersNumber >=3 & triggersNumber <5){
                    riskLevel = "In danger";
                } else if (triggersNumber>=5) {
                    riskLevel = "Early onset";
                } else{
                    riskLevel = "None";
                }
            }
        } else {
            // patient male or female greater than 30 years
            if(triggersNumber >= 6 & triggersNumber < 8) {
                riskLevel = "In danger";
            } else if (triggersNumber >=2 & triggersNumber < 6) {
                riskLevel = "Borderline";
            } else if (triggersNumber >= 8) {
                riskLevel = "Early onset";
            } else {
                riskLevel = "None";
            }
        }
        String finalResul  = "Patient: " + patient.getFirstName() + " " + patient.getLastName() + " " + patientAge + " years(year) old, gender: "+ patient.getGender() + ", address: " +patient.getAddress()+", phone number: " + patient.getPhoneNumber() + ". The diabetes test result is - " + riskLevel +".";

        return new Report(riskLevel, finalResul);
    }
}
