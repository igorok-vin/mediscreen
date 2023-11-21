package com.mediscreen.patientservice.integration;

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
public class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getPatientByIdTest() throws Exception {

        mockMvc.perform(get("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Lucas")))
                .andExpect(jsonPath("$.patientId", is(1)))
                .andDo(print());
    }

    @Test
    public void updatePatientTest() throws Exception {

        mockMvc.perform(post("/patient/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Lucas\",\"lastName\":\"Ferguson\",\"dob\":\"1968-06-22\",\"gender\":\"Male\",\"address\":\"2 Warren Street\",\"phoneNumber\":\"387-866-1399\"}"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.lastName", is("Ferguson")))
                .andExpect(jsonPath("$.dob").value("1968-06-22"))
                .andDo(print());
    }

    @Test
    public void getPatientsByLastNameTest() throws Exception {

        mockMvc.perform(get("/patient/list/Ince")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].phoneNumber", is("802-911-9975")))
                .andExpect(jsonPath("$.[0].address", is("4 Southampton Road")))
                .andDo(print());
    }

    @Test
    public void pageableListTest() throws Exception {

        mockMvc.perform(get("/patient/list"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
