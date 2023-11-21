package com.mediscreen.uiservice.controller;

import com.mediscreen.uiservice.model.Gender;
import com.mediscreen.uiservice.model.Note;
import com.mediscreen.uiservice.model.Patient;
import com.mediscreen.uiservice.proxy.HistoryProxy;
import com.mediscreen.uiservice.service.PatientService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryProxy historyProxy;

    @MockBean
    private PatientService patientService;

    @Test
    public void showNotesListTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");
        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1", 1, "Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.of(2020, 8, 9), patient),
                new Note("2", 1, "Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated", LocalDate.now(), patient),
                new Note("3", 1, "Patient states that they not feeling as tired. Smoker, quit within last year. Lab results indicate Antibodies present elevated", LocalDate.now(), patient)
        ));

        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);
        when(patientService.getPatientById(anyLong())).thenReturn(patient);

        mockMvc.perform(get("/patHistory/notes/1"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("note/list"))
                .andExpect(model().attributeExists("notesPatient", "patientFirstName", "patientLastName"))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("patientId", is(1L)))))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("id", is("1")))))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("date", is(LocalDate.of(2020, 8, 9))))))
                .andDo(print());
    }

    @Test
    public void deleteNoteTest() throws Exception {
        doNothing().when(historyProxy).delete(anyString());
        mockMvc.perform(get("/patHistory/notes/delete/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"1\"}"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    public void showAddNotesFormTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");
        when(patientService.getPatientById(anyLong())).thenReturn(patient);
        mockMvc.perform(get("/patHistory/addNotes/1"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("note/add"))
                .andExpect(model().attribute("patientFirstName", "John"))
                .andExpect(model().attribute("patientLastName", "Smith"))
                .andDo(print());
    }

    @Test
    public void createNoteAndFormHasErrorTest() throws Exception {
        mockMvc.perform(post("/patHistory/addNotes/1")
                        .param("patientId", "1")
                        .param("note", ""))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("note/add"))
                .andExpect(model().attributeHasFieldErrorCode("noteUi", "note", "NotBlank"))
                .andDo(print());
    }

    @Test
    public void createNoteTest() throws Exception {
        mockMvc.perform(post("/patHistory/addNotes/1")
                        .param("patientId", "1")
                        .param("note", "Note"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/patHistory/notes/1"))
                .andDo(print());

    }

    @Test
    public void displayNoteUpdateFormTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");
        Note note = new Note("1", 1L, "Note", LocalDate.of(2020, 5, 7), patient);

        when(historyProxy.getNoteById(anyString())).thenReturn(note);

        mockMvc.perform(get("/patHistory/updateNote/1/1")
                        .param("id", "1")
                        .param("patientId", String.valueOf(1L))
                        .param("note", note.getNote())
                )
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("note/update"))
                .andDo(print());
    }

    @Test
    public void updateNoteTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");
        Note note = new Note("1", 1L, "note", LocalDate.of(2020, 5, 7), patient);

        when(historyProxy.getNoteById(anyString())).thenReturn(note);

        mockMvc.perform(post("/patHistory/updateNote/1/1")
                        //.param("id", "1")
                        .param("patientId", String.valueOf(1L))
                        .param("note", note.getNote())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/patHistory/notes/1"))
                .andDo(print());
    }

    @Test
    public void updateNoteAndFormHasErrorTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");
        Note note = new Note("1", 1L, "note", LocalDate.of(2020, 5, 7), patient);

        when(historyProxy.getNoteById(anyString())).thenReturn(note);

        mockMvc.perform(post("/patHistory/updateNote/1/1")
                        //.param("id", "1")
                        .param("patientId", String.valueOf(1L))
                        .param("note", "")
                )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("note/update"))
                .andDo(print());
    }
}
