package com.mediscreen.reportservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getReportByPatientId() throws Exception {

        mockMvc.perform(get("/assess/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel", is("Borderline")))
                .andExpect(jsonPath("$.finalResult", is("Patient: Lucas Ferguson 55 years(year) old, gender: Male, address: 2 Warren Street, phone number: 387-866-1399. The diabetes test result is - Borderline.")))
                .andDo(print());
    }
}
