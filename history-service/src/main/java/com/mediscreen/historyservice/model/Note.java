package com.mediscreen.historyservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document("note")
public class Note {

    @Id
    private String id;

    private long patientId;

    private String note;

    private LocalDate date;

    public Note() {
    }

    public Note(String id, long patientId, String note, LocalDate date) {
        this.id = id;
        this.patientId = patientId;
        this.note = note;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public long getPatientId() {
        return patientId;
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
                '}';
    }
}
