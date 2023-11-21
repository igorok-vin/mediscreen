package com.mediscreen.reportservice.model;

import java.time.LocalDate;

public class Note {

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

    public String getNote() {
        return note;
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
