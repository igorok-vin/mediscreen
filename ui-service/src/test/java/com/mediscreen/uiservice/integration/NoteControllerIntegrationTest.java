package com.mediscreen.uiservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void showNotesListTest() throws Exception {

        mockMvc.perform(get("/patHistory/notes/2"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("note/list"))
                .andExpect(model().attributeExists("notesPatient", "patientFirstName", "patientLastName"))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("patientId", is(2L)))))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("id", is("64b33a371ee44c56b7d15a93")))))
                .andExpect(model().attribute("notesPatient", hasItem(hasProperty("date", is(LocalDate.of(2023, 7, 15))))))
                .andDo(print());
    }

    @Test
    public void showAddNotesFormTest() throws Exception {

        mockMvc.perform(get("/patHistory/addNotes/2"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("note/add"))
                .andExpect(model().attribute("patientFirstName", "Pippa"))
                .andExpect(model().attribute("patientLastName", "Rees"))
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

        mockMvc.perform(get("/patHistory/updateNote/1/1")
                        .param("id", "64b33a371ee44c56b7d15a93")
                        .param("patientId", String.valueOf(2L))
                        .param("note", "Patient states that they are feeling a great deal of stress at work. Patient also complains that their hearing seems Abnormal as of late")
                )
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("note/update"))
                .andDo(print());
    }

    @Test
    public void updateNoteTest() throws Exception {

        mockMvc.perform(post("/patHistory/updateNote/64b33a371ee44c56b7d15a93/2")
                        .param("patientId", String.valueOf(2L))
                        .param("note", "Patient states that they are feeling a great deal of stress at work. Patient also complains that their hearing seems Abnormal as of late")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/patHistory/notes/2"))
                .andDo(print());
    }

    @Test
    public void updateNoteAndFormHasErrorTest() throws Exception {

        mockMvc.perform(post("/patHistory/updateNote/64b33a371ee44c56b7d15a93/2")
                        .param("patientId", String.valueOf(2L))
                        .param("note", "")
                )
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("note/update"))
                .andDo(print());
    }

}
