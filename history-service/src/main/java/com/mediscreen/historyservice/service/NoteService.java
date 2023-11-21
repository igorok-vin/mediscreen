package com.mediscreen.historyservice.service;

import com.mediscreen.historyservice.exception.NoteNotFoundException;
import com.mediscreen.historyservice.exception.PatientNotFoundException;
import com.mediscreen.historyservice.model.Note;
import com.mediscreen.historyservice.model.Patient;
import com.mediscreen.historyservice.proxy.PatientProxy;
import com.mediscreen.historyservice.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class NoteService implements INoteService {

    private NoteRepository noteRepository;

    private PatientProxy patientProxy;

    @Autowired
    public NoteService(NoteRepository noteRepository, PatientProxy patientProxy) {
        this.noteRepository = noteRepository;
        this.patientProxy = patientProxy;
    }

    @Override
    public List<Note> getNotesByPatientId(long patientId) {
       Patient patient = patientProxy.getPatientById(patientId);
        return noteRepository.findAllByPatientIdOrderByDateDesc(patientId);
    }

    private static boolean checkIfListContainsPatientId(List<Patient> list, Note note) {
        return list.stream()
                .filter(x -> Objects.equals(x.getPatientId(), note.getPatientId()))
                .count() > 0;
    }

    @Override
    public Note saveNote(Note note){
        List <Patient> patientList = patientProxy.getPatients();
        if(!checkIfListContainsPatientId(patientList,note)){
            throw new PatientNotFoundException("Note creation unsuccessful. Patient with ID " + note.getPatientId() +" not found.");
        }
        note.setDate(LocalDate.now());
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(String id, Note note) {
        Note noteUpdate = getNoteById(id);
        noteUpdate.setNote(note.getNote());
        noteUpdate.setDate(LocalDate.now());
        return noteRepository.save(noteUpdate);
    }

    @Override
    public Note getNoteById(String id) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if(!noteOptional.isPresent()){
            throw new NoteNotFoundException("Note note with ID "+id+" not found");
        }
        //Note noteResult = noteOptional.get();
        return noteOptional.get();
    }

    @Override
    public void deleteNote(String id) {
        Note noteDelete = getNoteById(id);
        noteRepository.delete(noteDelete);
    }
}
