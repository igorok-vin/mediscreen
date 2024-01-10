package com.mediscreen.historyservice.controller;

import com.mediscreen.historyservice.model.Note;
import com.mediscreen.historyservice.service.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Test
    public void findListOfNotesByPatientIdTest() throws Exception {
        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1", 1, "Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now()),
                new Note("2", 1, "Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated", LocalDate.now()),
                new Note("3", 1, "Patient states that they not feeling as tired. Smoker, quit within last year. Lab results indicate Antibodies present elevated", LocalDate.now())
        ));

        when(noteService.getNotesByPatientId(anyLong())).thenReturn(noteList);

        mockMvc.perform(get("/patHistory/patient/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1].id", is("2")))
                .andExpect(jsonPath("$.[1].patientId", is(1)))
                .andExpect(jsonPath("$.[1].note", is("Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated")))
                .andExpect(jsonPath("$.[2].id", is("3")))
                .andExpect(jsonPath("$.[2].patientId", is(1)))
                .andExpect(jsonPath("$.[2].note", is("Patient states that they not feeling as tired. Smoker, quit within last year. Lab results indicate Antibodies present elevated")))
                .andDo(print());
    }

    @Test
    public void addNoteTest() throws Exception {
        Note noteToSave = new Note("1", 1, "Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.of(2022,05,22));

        when(noteService.saveNote(any(Note.class))).thenReturn(noteToSave);
        mockMvc.perform(post("/patHistory/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"1\",\"note\":\"Patient states that they are feeling terrific. Weight at or below recommended level\",\"date\":\"2022-05-22\"}"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.note", is("Patient states that they are feeling terrific. Weight at or below recommended level")))
                .andExpect(jsonPath("$.date").value("2022-05-22"))
                .andDo(print());
    }

    @Test
    public void updateNoteTest() throws Exception {
        Note noteUpdated = new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.of(2022,05,22));

        when(noteService.updateNote(anyString(),any(Note.class))).thenReturn(noteUpdated);
        mockMvc.perform(post("/patHistory/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"patientId\":\"1\",\"note\":\"Patient states that they are feeling terrific. Weight at or below recommended level\",\"date\":\"2022-05-22\"}"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.note", is("Patient states that they are feeling terrific. Weight at or below recommended level")))
                .andExpect(jsonPath("$.date").value("2022-05-22"))
                .andDo(print());
    }

    @Test
    public void deleteNoteTest() throws Exception {
        mockMvc.perform(get("/patHistory/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\"}"))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void getNoteByIdTest() throws Exception {
        Note noteSaved = new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.of(2022,05,22));

        when(noteService.getNoteById(anyString())).thenReturn(noteSaved);
        mockMvc.perform(get("/patHistory/note/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.note", is("Patient states that they are feeling terrific. Weight at or below recommended level")))
                .andExpect(jsonPath("$.date").value("2022-05-22"))
                .andDo(print());
    }
}
