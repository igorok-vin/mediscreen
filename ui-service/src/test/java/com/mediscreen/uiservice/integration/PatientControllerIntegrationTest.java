package com.mediscreen.uiservice.integration;

import com.mediscreen.uiservice.model.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void createPatientWhenBirthdateExceptionTest() throws Exception {

        mockMvc.perform(post("/patient/add")
                        .param("firstName","John")
                        .param("lastName", "Smith")
                        .param("dob","2080-07-22")
                        .param("gender","Male")
                        .param("address","1133 King street")
                        .param("phoneNumber","555-333-2222"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientUI"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("patientUI","dob","BirthDateIsIncorrect"))
                .andDo(print());
    }

    @Test
    public void displayPatientsUpdateFormTest() throws Exception {

        mockMvc.perform(get("/patient/update/1")
                        .param("firstName","Lucas")
                        .param("lastName", "Ferguson")
                        .param("dob","1968-02-22")
                        .param("gender","Male")
                        .param("address","2 Warren Street")
                        .param("phoneNumber","387-866-1399"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientUI"))
                .andExpect(view().name("patient/update"))
                .andDo(print());
    }

    @Test
    public void updatePatientTest() throws Exception {

        mockMvc.perform(post("/patient/update/1")
                        .param("firstName","Lucas")
                        .param("lastName", "Ferguson")
                        .param("dob","1968-02-22")
                        .param("gender","Male")
                        .param("address","2 Warren Street")
                        .param("phoneNumber","387-866-1399"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andDo(print());
    }

    @Test
    public void updatePatientWhenUpdateFormHasFieldsErrorsTest() throws Exception {

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

        mockMvc.perform(post("/patient/update/1")
                        .param("firstName","Lucas")
                        .param("lastName", "Ferguson")
                        .param("dob","2080-02-22")
                        .param("gender","Male")
                        .param("address","2 Warren Street")
                        .param("phoneNumber","387-866-1399"))
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

        mockMvc.perform(get("/patient/list")
                        .param("pageNumber", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currentPage","totalPages","totalItems","patientsList","patient"))
                .andExpect(model().attribute("currentPage", is(1)))
                .andExpect(model().attribute("totalPages", is(4)))
                .andExpect(model().attribute("totalItems", is(16L)))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("firstName",equalTo("Pippa")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("lastName",equalTo("Rees")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("dob",is(LocalDate.of(1952,9,27))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("gender",is(Gender.valueOf("Female"))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("age",is("71")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("address",is("745 West Valley Farms Drive")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("phoneNumber",is("628-423-0993")))))
                .andDo(print());
    }

    @Test
    public void searchPatientByLastNameWithPageNumberTest() throws Exception {

        mockMvc.perform(post("/page/1")
                        .param("pageNumber", String.valueOf(1))
                        .param("lastName", "Rees"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("currentPage","totalPages","totalItems","patientsList","patient"))
                .andExpect(model().attribute("currentPage", is(1)))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("firstName",equalTo("Pippa")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("lastName",equalTo("Rees")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("dob",is(LocalDate.of(1952,9,27))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("gender",is(Gender.valueOf("Female"))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("age",is("71")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("address",is("745 West Valley Farms Drive")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("phoneNumber",is("628-423-0993")))))
                .andDo(print());
    }

    @Test
    public void searchPatientByLastNameWithPageNumberWhenPatientNotFoundTest() throws Exception {

        mockMvc.perform(post("/page/1")
                        .param("pageNumber", String.valueOf(1))
                        .param("lastName", "Jonson"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("patient","lastName","PatientWasNotFoundBySuchALastName"))
                .andDo(print());
    }

    @Test
    public void searchPatientByLastNameTest() throws Exception {

        mockMvc.perform(post("/patient/list")
                        .param("pageNumber", String.valueOf(1))
                        .param("lastName", "Rees"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("patientsList","patient"))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("firstName",equalTo("Pippa")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("lastName",equalTo("Rees")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("dob",is(LocalDate.of(1952,9,27))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("gender",is(Gender.valueOf("Female"))))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("age",is("71")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("address",is("745 West Valley Farms Drive")))))
                .andExpect(model().attribute("patientsList", hasItem(hasProperty("phoneNumber",is("628-423-0993")))))
                .andDo(print());
    }

    @Test
    public void searchPatientByLastNameWhenPatientNotFoundTest() throws Exception {

        mockMvc.perform(post("/patient/list")
                        .param("pageNumber", String.valueOf(1))
                        .param("lastName", "Gordon"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("patient","lastName","PatientWasNotFoundBySuchALastName"))
                .andDo(print());
    }
}
