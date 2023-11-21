package com.mediscreen.historyservice.controller;

import com.mediscreen.historyservice.model.Note;
import com.mediscreen.historyservice.service.INoteService;
import com.mediscreen.historyservice.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);

    private INoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/patHistory/patient/{patientId}")
    public ResponseEntity<List<Note>> findListOfNotesByPatientId(@PathVariable("patientId") long patientId) {
        List<Note> noteList = noteService.getNotesByPatientId(patientId);
        if (noteList == null) {
            return ResponseEntity.notFound().build();
        } else {
            logger.info("HISTORY-SERVICE: NoteController - Get notes list by patient ID");
            return ResponseEntity.ok(noteList);
        }
    }

    @PostMapping("/patHistory/add")
    public ResponseEntity<Note> addNote(@RequestBody Note note) {
        Note noteCreate = noteService.saveNote(note);
        if (noteCreate == null) {
            return ResponseEntity.noContent().build();
        } else {
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .port(8072)
                    .path("/{id}")
                    .buildAndExpand(noteCreate.getId())
                    .toUri();
            logger.info("HISTORY-SERVICE: NoteController - The note has been created successfully.");
            return ResponseEntity.created(uri).body(noteCreate);
        }
    }

    @PostMapping("/patHistory/update/{id}")
    public Note updateNote(@PathVariable("id") String id, @RequestBody Note note) {
        logger.info("HISTORY-SERVICE: NoteController - The note has been updated by note ID successfully.");
        return noteService.updateNote(id, note);
    }

    @GetMapping("/patHistory/delete/{id}")
    public void delete(@PathVariable String id) {
        logger.info("HISTORY-SERVICE: NoteController - Delete note by note ID");
        noteService.deleteNote(id);
    }

    @GetMapping("/patHistory/note/{id}")
    public Note getNoteById(@PathVariable("id") String id) {
        logger.info("HISTORY-SERVICE: NoteController - Get note by ID");
        return noteService.getNoteById(id);
    }
}
