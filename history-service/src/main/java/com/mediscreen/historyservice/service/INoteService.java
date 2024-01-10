package com.mediscreen.historyservice.service;

import com.mediscreen.historyservice.model.Note;

import java.util.List;

public interface INoteService {
    List<Note> getNotesByPatientId(long patientId);

    Note saveNote(Note note);

    Note updateNote(String id, Note note);

    Note getNoteById(String id);

    void deleteNote(String id);
}
