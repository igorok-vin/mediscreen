package com.mediscreen.uiservice.controller;

import com.mediscreen.uiservice.model.Gender;
import com.mediscreen.uiservice.model.Patient;
import com.mediscreen.uiservice.model.Report;
import com.mediscreen.uiservice.proxy.PatientProxy;
import com.mediscreen.uiservice.proxy.ReportProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportProxy reportProxy;

    @MockBean
    private PatientProxy patientProxy;

    @Test
    public void getReportByPatientId() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");
        Report report = new Report("Early onset","Patient: John Smith 59 years(year) old, gender: Male, address: 1133 King street, phone number: 555-333-222. The diabetes test result is - Early onset.");

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(reportProxy.getReportByPatientId(anyLong())).thenReturn(report);

        mockMvc.perform(get("/patHistory/assess/1").param("patientId",String.valueOf(patient.getPatientId())))
                .andExpect(status().isOk())
                .andExpect(view().name("report/report"))
                .andExpect(model().attribute("report","Early onset"))
                .andDo(print());
    }
}
