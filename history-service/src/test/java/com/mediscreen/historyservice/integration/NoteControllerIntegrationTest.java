package com.mediscreen.historyservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findListOfNotesByPatientIdTest() throws Exception {

        mockMvc.perform(get("/patHistory/patient/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is("64b33acf1ee44c56b7d15a97")))
                .andExpect(jsonPath("$.[0].patientId", is(3)))
                .andExpect(jsonPath("$.[0].note", is("Patient states that they are short term Smoker")))
                .andExpect(jsonPath("$.[1].id", is("64b33ad61ee44c56b7d15a98")))
                .andExpect(jsonPath("$.[1].patientId", is(3)))
                .andExpect(jsonPath("$.[1].note", is("Lab reports Microalbumin elevated")))
                .andExpect(jsonPath("$.[2].id", is("64b33ae91ee44c56b7d15a99")))
                .andExpect(jsonPath("$.[2].patientId", is(3)))
                .andExpect(jsonPath("$.[2].note", is("Patient states that they are a Smoker, quit within last year\r\n" + "Patient also complains that of Abnormal breathing spells\r\n" + "Lab reports Cholesterol LDL high\r\n")))
                .andExpect(jsonPath("$.[3].id", is("64b33af71ee44c56b7d15a9a")))
                .andExpect(jsonPath("$.[3].patientId", is(3)))
                .andExpect(jsonPath("$.[3].note", is("Lab reports Cholesterol LDL high")))
                .andDo(print());
    }

    @Test
    public void addNoteTest() throws Exception {

        mockMvc.perform(post("/patHistory/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"1\",\"note\":\"Patient states that they are feeling terrific. Weight at or below recommended level\"}"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.note", is("Patient states that they are feeling terrific. Weight at or below recommended level")))
                .andDo(print());
    }
}
