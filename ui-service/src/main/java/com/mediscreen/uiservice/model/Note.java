package com.mediscreen.uiservice.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class Note {

    private String id;

    private long patientId;

    @NotBlank(message = "Note field is required")
    private String note;

    private LocalDate date;

    private Patient patient;

    public Note() {
    }

    public Note(String id, long patientId, String note, LocalDate date, Patient patient) {
        this.id = id;
        this.patientId = patientId;
        this.note = note;
        this.date = date;
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", patientId=" + patientId +
                ", note='" + note + '\'' +
                ", date=" + date +
                ", patient=" + patient +
                '}';
    }
}
