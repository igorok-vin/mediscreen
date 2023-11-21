package com.mediscreen.historyservice.service;

import com.mediscreen.historyservice.exception.NoteNotFoundException;
import com.mediscreen.historyservice.model.Gender;
import com.mediscreen.historyservice.model.Note;
import com.mediscreen.historyservice.model.Patient;
import com.mediscreen.historyservice.proxy.PatientProxy;
import com.mediscreen.historyservice.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    NoteRepository noteRepository;

    @Mock
    PatientProxy patientProxy;

    @InjectMocks
    NoteService noteService;

    @Test
    public void getNotesByPatientIdTest() {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");
        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1", 1, "Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now()),
                new Note("2", 1, "Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated", LocalDate.now()),
                new Note("3", 1, "Patient states that they not feeling as tired. Smoker, quit within last year. Lab results indicate Antibodies present elevated", LocalDate.now())
        ));
        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(noteRepository.findAllByPatientIdOrderByDateDesc(anyLong())).thenReturn(noteList);

        List<Note> notesResult = noteService.getNotesByPatientId(1);

        assertEquals(1, notesResult.get(0).getPatientId());
        assertEquals("1", notesResult.get(0).getId());
        assertEquals("Patient states that they not feeling as tired. Smoker, quit within last year. Lab results indicate Antibodies present elevated", notesResult.get(2).getNote());
    }

    @Test
    public void saveNoteTest() {
        List<Patient> patientList = new ArrayList<>(Arrays.asList(
                new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222"),
                new Patient(2, "Anna", "Walker", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-772"),
                new Patient(3, "Bonny", "Russell", LocalDate.of(2023, 7, 25), Gender.Female, "59", "1133 King street", "775-366-200"),
                new Patient(4,"Jack","Logan", LocalDate.of(2023,6,5), Gender.Male,"59","1133 King street","511-322-288")));
        Note noteToSave = new Note("1", 1, "Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now());

        when(patientProxy.getPatients()).thenReturn(patientList);
        when(noteRepository.save(any(Note.class))).thenReturn(noteToSave);

        Note noteResult = noteService.saveNote(noteToSave);

        assertEquals("1", noteResult.getId());
        assertEquals(1, noteResult.getPatientId());
        assertEquals("Patient states that they are feeling terrific. Weight at or below recommended level", noteResult.getNote());
        assertEquals(LocalDate.now(), noteResult.getDate());
    }

    @Test
    public void updateNoteTest() {
        Note savedNote = new Note("1", 1, "Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now());

        Note noteUpdated = new Note("1", 1, "Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated", LocalDate.now());

        when(noteRepository.findById(anyString())).thenReturn(Optional.of(savedNote));

        when(noteRepository.save(any(Note.class))).thenReturn(noteUpdated);

        Note noteResult = noteService.updateNote("1", savedNote);

        assertEquals("1", noteResult.getId());
        assertEquals(1, noteResult.getPatientId());
        assertEquals("Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated", noteResult.getNote());
        assertEquals(LocalDate.now(), noteResult.getDate());
    }

    @Test
    public void getNoteByIdTest() {
        Note savedNote = new Note("1", 1, "Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now());

        when(noteRepository.findById(anyString())).thenReturn(Optional.of(savedNote));

        Note noteResult = noteService.getNoteById("1");

        assertEquals("1", noteResult.getId());
        assertEquals(1, noteResult.getPatientId());
        assertEquals("Patient states that they are feeling terrific. Weight at or below recommended level", noteResult.getNote());
        assertEquals(LocalDate.now(), noteResult.getDate());
    }

    @Test
    public void getNoteByIdWhenNoteNotFoundTest() {
        when(noteRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.getNoteById("1"));
        verify(noteRepository).findById(anyString());
    }

    @Test
    public void deleteNoteByIdTest() {
        Note savedNote = new Note("1", 1, "Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now());

        when(noteRepository.findById(anyString())).thenReturn(Optional.of(savedNote));

        noteService.deleteNote("1");
        verify(noteRepository).delete(savedNote);
    }
}

