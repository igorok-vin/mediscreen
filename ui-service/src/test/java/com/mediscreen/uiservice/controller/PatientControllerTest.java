package com.mediscreen.uiservice.controller;

import com.mediscreen.uiservice.exception.BirthdateException;
import com.mediscreen.uiservice.exception.PatientNotFoundException;
import com.mediscreen.uiservice.model.Gender;
import com.mediscreen.uiservice.model.Patient;
import com.mediscreen.uiservice.proxy.PatientProxy;
import com.mediscreen.uiservice.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @MockBean
    private PatientProxy patientProxy;

    @MockBean
    private BindingResult bindingResult;


    @Test
    public void createPatientTest() throws Exception {
        Patient patient = new Patient( "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "1133 King street", "555-333-2222");

        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/patient/add")
                        .param("firstName",patient.getFirstName())
                        .param("lastName", patient.getLastName())
                        .param("dob", String.valueOf(patient.getDob()))
                        .param("gender",String.valueOf(Gender.Male))
                        .param("address",patient.getAddress())
                        .param("phoneNumber",patient.getPhoneNumber()))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    public void createPatientWhenBirthdateExceptionTest() throws Exception {
        Patient patient = new Patient("John", "Smith", LocalDate.of(2080, 7, 22), Gender.Male, "1133 King street", "555-333-2222");

        when(patientService.createPatient(any(Patient.class))).thenThrow(new BirthdateException("Birth date is incorrect"));

        mockMvc.perform(post("/patient/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("firstName",patient.getFirstName())
                        .param("lastName", patient.getLastName())
                        .param("dob", String.valueOf(patient.getDob()))
                        .param("gender",String.valueOf(Gender.Male))
                        .param("address",patient.getAddress())
                        .param("phoneNumber",patient.getPhoneNumber()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientUI"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("patientUI","dob","BirthDateIsIncorrect"))
                .andDo(print());
    }

    @Test
    public void displayPatientsUpdateFormTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "1133 King street", "555-333-2222");

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);

        mockMvc.perform(get("/patient/update/1")
                        .param("id",String.valueOf( patient.getPatientId()))
                        .param("firstName",patient.getFirstName())
                        .param("lastName",patient.getLastName())
                        .param("dob",patient.getDob().toString())
                        .param("gender",String.valueOf(patient.getGender()))
                        .param("address",patient.getAddress())
                        .param("phoneNumber",patient.getPhoneNumber()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientUI"))
                .andExpect(view().name("patient/update"))
                .andDo(print());
    }

    @Test
    public void updatePatientTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "1133 King street", "555-333-2222");

        when(patientProxy.updatePatient(anyLong(),any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/patient/update/1")
                        .param("firstName",patient.getFirstName())
                        .param("lastName",patient.getLastName())
                        .param("dob",patient.getDob().toString())
                        .param("gender",String.valueOf(patient.getGender()))
                        .param("address",patient.getAddress())
                        .param("phoneNumber",patient.getPhoneNumber()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andDo(print());
    }

    @Test
    public void updatePatientWhenUpdateFormHasFieldsErrorsTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "1133 King street", "555-333-2222");

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);

        mockMvc.perform(post("/patient/update/1")
                        .param("firstName","")
                        .param("lastName","")
                        .param("dob","")
                        .param("gender","")
                        .param("address","")
                        .param("phoneNumber",""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientUI"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("patientUI","firstName","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("patientUI","lastName","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("patientUI","dob","NotNull"))
                .andExpect(model().attributeHasFieldErrorCode("patientUI","gender","NotNull"))
                .andExpect(model().attributeHasFieldErrorCode("patientUI","address","NotBlank"))
                .andExpect(view().name("patient/update"))
                .andDo(print());
    }

    @Test
    public void updatePatientWhenUpdateFormHasBirthdateExceptionTest() throws Exception {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(2080, 7, 22), Gender.Male, "1133 King street", "555-333-2222");

        when(patientService.updatePatient(anyLong(),any(Patient.class))).thenThrow(new BirthdateException("Birth date is incorrect"));

        mockMvc.perform(post("/patient/update/1")
                        .param("id",String.valueOf( patient.getPatientId()))
                        .param("firstName",patient.getFirstName())
                        .param("lastName",patient.getLastName())
                        .param("dob",patient.getDob().toString())
                        .param("gender",String.valueOf(patient.getGender()))
                        .param("address",patient.getAddress())
                        .param("phoneNumber",patient.getPhoneNumber()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientUI"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("patientUI","dob","BirthDateIsIncorrect"))
                .andDo(print());
    }

    @Test
    public void displayPatientFormTest() throws Exception {
        mockMvc.perform(get("/patient/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientUI"))
                .andExpect(view().name("patient/add"))
                .andExpect(model().hasNoErrors())
                .andDo(print());
    }

    @Test
    public void pageableListTest() throws Exception {
        List<Patient> patientList = new ArrayList<>(Arrays.asList(
                new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "59", "1133 King street", "555-333-2222"),
                new Patient(2, "Anna", "Walker", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-7723"),
                new Patient(3, "Bonny", "Russell", LocalDate.of(2023, 7, 25), Gender.Female, "59", "1133 King street", "775-366-2004"),
                new Patient(4,"Jack","Logan", LocalDate.of(2023,6,5), Gender.Male,"59","1133 King street","511-322-2885")));
        Page<Patient> patientPage = new PageImpl<>(patientList);

        when(patientService.getPaginatedPatientList(anyInt(),anyInt())).thenReturn(patientPage);

        mockMvc.perform(get("/patient/list")
                        .param("pageNumber", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currentPage","totalPages","totalItems","patientsList","patient"))
                .andExpect(model().attribute("currentPage", is(1)))
                .andExpect(model().attribute("totalPages", is(1)))
                .andExpect(model().attribute("totalItems", is(4L)))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("firstName",equalTo("Anna")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("lastName",equalTo("Walker")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("dob",is(LocalDate.of(1972,5,14))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("gender",is(Gender.valueOf("Female"))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("age",is("59")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("address",is("1133 King street")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("phoneNumber",is("525-663-7723")))))
                .andDo(print());
    }

    @Test
    public void searchPatientByLastNameWithPageNumberTest() throws Exception {
        List<Patient> patientList = new ArrayList<>(Arrays.asList(
                new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "59", "1133 King street", "555-333-2225"),
                new Patient(2, "Anna", "Smith", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-772"),
                new Patient(3, "Bonny", "Smith", LocalDate.of(2023, 7, 25), Gender.Female, "59", "1133 King street", "775-366-2050"),
                new Patient(4,"Jack","Smith", LocalDate.of(2023,6,5), Gender.Male,"59","1133 King street","511-322-2884")));
         when(patientService.getPatientsByLastNameForSearch(anyString())).thenReturn(patientList);
        Page<Patient> patientPage = new PageImpl<>(patientList);
        when(patientService.getPaginatedPatientList(anyInt(),anyInt())).thenReturn(patientPage);

        mockMvc.perform(post("/page/1")
                        .param("pageNumber", String.valueOf(1))
                        .param("lastName", "Smith"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("currentPage","totalPages","totalItems","patientsList","patient"))
                .andExpect(model().attribute("currentPage", is(1)))
                .andExpect(model().attribute("totalPages", is(1)))
                .andExpect(model().attribute("totalItems", is(4L)))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("firstName",equalTo("Anna")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("lastName",equalTo("Smith")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("dob",is(LocalDate.of(1972,5,14))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("gender",is(Gender.valueOf("Female"))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("age",is("59")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("address",is("1133 King street")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("phoneNumber",is("555-333-2225")))))
                .andDo(print());
    }

    @Test
    public void searchPatientByLastNameWithPageNumberWhenPatientNotFoundTest() throws Exception {
        when(patientService.getPatientsByLastNameForSearch(anyString())).thenThrow(new PatientNotFoundException("Patient not found"));

        mockMvc.perform(post("/page/1")
                        .param("pageNumber", String.valueOf(1))
                        .param("lastName", "Smith"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("patient","lastName","PatientWasNotFoundBySuchALastName"))
                .andDo(print());
    }

    @Test
    public void searchPatientByLastNameTest() throws Exception {
        List<Patient> patientList = new ArrayList<>(Arrays.asList(
                new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "59", "1133 King street", "555-333-2224"),
                new Patient(2, "Anna", "Smith", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-772"),
                new Patient(3, "Bonny", "Smith", LocalDate.of(2023, 7, 25), Gender.Female, "59", "1133 King street", "775-366-2020"),
                new Patient(4,"Jack","Smith", LocalDate.of(2023,6,5), Gender.Male,"59","1133 King street","511-322-2881")));
        when(patientService.getPatientsByLastNameForSearch(anyString())).thenReturn(patientList);

        mockMvc.perform(post("/patient/list")
                        .param("pageNumber", String.valueOf(1))
                        .param("lastName", "Smith"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("patientsList","patient"))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("firstName",equalTo("Anna")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("lastName",equalTo("Smith")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("dob",is(LocalDate.of(1972,5,14))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("gender",is(Gender.valueOf("Female"))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("age",is("59")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("address",is("1133 King street")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("phoneNumber",is("525-663-772")))))
                .andDo(print());
    }

    @Test
    public void searchPatientByLastNameWhenPatientNotFoundTest() throws Exception {
        when(patientService.getPatientsByLastNameForSearch(anyString())).thenThrow(new PatientNotFoundException("Patient not found"));

        mockMvc.perform(post("/patient/list")
                        .param("pageNumber", String.valueOf(1))
                        .param("lastName", "Smith"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("patient","lastName","PatientWasNotFoundBySuchALastName"))
                .andDo(print());
    }

    @Test
    public void deletePatientTest() throws Exception {

       doNothing().when(patientProxy).deletePatient(anyLong());
        mockMvc.perform(get("/patient/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"1\"}"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }
}
