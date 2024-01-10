package com.mediscreen.reportservice.controller;

import com.mediscreen.reportservice.model.Report;
import com.mediscreen.reportservice.service.ReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ReportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    public void getReportByPatientId() throws Exception {
        Report report = new Report("Early onset","Patient: Anna Walker 59 years(year) old, gender: Female, address: 1133 King street, phone number: 525-663-772. The diabetes test result is - Early onset.");

        when(reportService.getReportByPatientId(anyLong())).thenReturn(report);
        mockMvc.perform(get("/assess/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel", is("Early onset")))
                .andExpect(jsonPath("$.finalResult", is("Patient: Anna Walker 59 years(year) old, gender: Female, address: 1133 King street, phone number: 525-663-772. The diabetes test result is - Early onset.")))
                .andDo(print());
    }
}
