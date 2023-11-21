package com.mediscreen.patientservice.controller;

import com.mediscreen.patientservice.model.Gender;
import com.mediscreen.patientservice.model.Patient;
import com.mediscreen.patientservice.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Test
    public void createPatientTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");

        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/patient/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Smith\",\"dob\":\"1964-07-22\",\"gender\":\"Male\",\"address\":\"1133 King street\",\"phoneNumber\":\"555-333-2222\"}"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andDo(print());
    }

    @Test
    public void getPatientByIdTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");

        when(patientService.getPatientById(anyLong())).thenReturn(patient);

        mockMvc.perform(get("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.patientId", is(1)))
                .andDo(print());
    }

    @Test
    public void updatePatientTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");

        when(patientService.updatePatient(anyLong(),any(Patient.class))).thenReturn(patient);
        mockMvc.perform(post("/patient/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Smith\",\"dob\":\"1964-07-22\",\"gender\":\"Male\",\"address\":\"1133 King street\",\"phoneNumber\":\"555-333-2222\"}"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.patientId", is(1)))
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.dob").value("1964-07-22"))
                .andDo(print());
    }

    @Test
    public void deletePatientTest() throws Exception {
        mockMvc.perform(get("/patient/delete/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"patientId\":\"1\"}"))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void getPatientsByLastNameTest() throws Exception {
        List<Patient> patientList = new ArrayList<>(Arrays.asList(new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-2222")));

        when(patientService.getPatientsByLastName(anyString())).thenReturn(patientList);

        mockMvc.perform(get("/patient/list/Smith")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].phoneNumber", is("555-333-2222")))
                .andExpect(jsonPath("$.[0].address", is("1133 King street")))
                .andDo(print());
    }

    @Test
    public void pageableListTest() throws Exception {
        List<Patient> patientList = new ArrayList<>(Arrays.asList(
                new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222"),
                new Patient(2, "Anna", "Walker", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-772"),
                new Patient(3, "Bonny", "Russell", LocalDate.of(2023, 7, 25), Gender.Female, "59", "1133 King street", "775-366-200"),
                new Patient(4,"Jack","Logan", LocalDate.of(2023,6,5), Gender.Male,"59","1133 King street","511-322-288")));
        Page<Patient> patientPage = new PageImpl<>(patientList);

        when(patientService.paginated(any(Pageable.class))).thenReturn(patientPage);

        mockMvc.perform(get("/patient/list"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content",hasSize(4)));
    }
}
