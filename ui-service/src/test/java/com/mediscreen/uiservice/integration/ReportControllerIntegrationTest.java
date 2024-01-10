package com.mediscreen.uiservice.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration Tests")
public class ReportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void getReportByPatientId() throws Exception {

        mockMvc.perform(get("/patHistory/assess/1").param("patientId",String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(view().name("report/report"))
                .andExpect(model().attribute("report","Borderline"))
                .andDo(print());
    }
}
