package com.mediscreen.uiservice.service;

import com.mediscreen.uiservice.exception.BirthdateException;
import com.mediscreen.uiservice.exception.PatientNotFoundException;
import com.mediscreen.uiservice.model.Gender;
import com.mediscreen.uiservice.model.Patient;
import com.mediscreen.uiservice.proxy.PatientProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UIServiceTest {

    @Mock
    PatientProxy patientProxy;

    @InjectMocks
    PatientService patientService;

    @Test
    public void createPatientTest() throws BirthdateException {
        Patient patient = new Patient( 1,"John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");

        when(patientProxy.createPatient(any(Patient.class))).thenReturn(patient);

        Patient patientResult = patientService.createPatient(patient);

        assertEquals("John",patientResult.getFirstName());
        assertEquals("Smith",patientResult.getLastName());
        assertEquals("1133 King street",patientResult.getAddress());
        assertEquals(LocalDate.of(1964, 7, 22),patientResult.getDob());
        assertEquals(Gender.Male,patientResult.getGender());
        assertEquals("59",patientResult.getAge());
        assertEquals("555-333-222",patientResult.getPhoneNumber());
    }

    @Test
    public void createPatientWhenBirthdateExceptionTest() throws BirthdateException {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(2027, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");

        assertThrows(BirthdateException.class,()-> patientService.createPatient(patient));
    }

    @Test
    public void getPatientNameByIdTest() {
        Patient patient = new Patient( 1,"John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);

        Patient patientResult = patientService.getPatientById(1);

        assertEquals("John",patientResult.getFirstName());
        assertEquals("Smith",patientResult.getLastName());
        assertEquals("1133 King street",patientResult.getAddress());
        assertEquals(LocalDate.of(1964, 7, 22),patientResult.getDob());
        assertEquals(Gender.Male,patientResult.getGender());
        assertEquals("59",patientResult.getAge());
        assertEquals("555-333-222",patientResult.getPhoneNumber());
    }

    @Test
    public void getPatientsByLastNameForSearchTest() throws PatientNotFoundException {
        List<Patient> patientList = new ArrayList<>(Arrays.asList( new Patient( 1,"John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222")));

        when(patientProxy.getPatientsByLastName(anyString())).thenReturn(patientList);

        List<Patient> patientListResult = patientService.getPatientsByLastNameForSearch("Smith");

        assertEquals(1,patientListResult.size());
        assertEquals("Smith",patientListResult.get(0).getLastName());
        assertEquals("1133 King street",patientListResult.get(0).getAddress());
        assertEquals(LocalDate.of(1964, 7, 22),patientListResult.get(0).getDob());
        assertEquals(Gender.Male,patientListResult.get(0).getGender());
        assertEquals("59",patientListResult.get(0).getAge());
        assertEquals("555-333-222",patientListResult.get(0).getPhoneNumber());
    }

    @Test
    public void getPatientsByLastNameForSearchWhenPatientNotFoundExceptionTest() throws PatientNotFoundException {

        when(patientProxy.getPatientsByLastName(anyString())).thenReturn(Collections.emptyList());

        assertThrows(PatientNotFoundException.class,()-> patientService.getPatientsByLastNameForSearch("Smith"));
    }

    @Test
    public void paginatedTest() {
        List<Patient> patientList = new ArrayList<>(Arrays.asList(
                new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222"),
                new Patient(2, "Anna", "Walker", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-772"),
                new Patient(3, "Bonny", "Russell", LocalDate.of(2023, 7, 25), Gender.Female, "59", "1133 King street", "775-366-200"),
                new Patient(4,"Jack","Logan", LocalDate.of(2023,6,5), Gender.Male,"59","1133 King street","511-322-288")));
        Page<Patient> patientPage = new PageImpl<>(patientList);

        when(patientProxy.paginated(any(Pageable.class))).thenReturn(patientPage);

        Page<Patient> pageResult = patientService.getPaginatedPatientList(1,4);

        assertEquals(pageResult.stream().count(), 4);
        assertEquals(pageResult.getTotalElements(),4);
        assertEquals(pageResult.getContent().get(2).getPatientId(),3);
        assertEquals(pageResult.getContent().get(0).getPhoneNumber(),"555-333-222");
    }

    @Test
    public void updatePatientTest() throws BirthdateException {
        Patient patientSaved = new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "3 days", "1133 King street", "555-333-222");
        Patient patientToUpdate = new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "3 days", "554 Queen street", "999-888-444");

        when(patientProxy.updatePatient(anyLong(),any(Patient.class))).thenReturn(patientToUpdate);

        Patient patientResult = patientService.updatePatient(1,patientSaved);

        assertEquals(1,patientResult.getPatientId());
        assertEquals("554 Queen street",patientResult.getAddress());
        assertEquals("999-888-444", patientResult.getPhoneNumber());
        assertEquals(LocalDate.of(2023, 7, 22),patientResult.getDob());
    }

    @Test
    public void updatePatientWhenBirthdateExceptionTest() throws BirthdateException {
        Patient patientSaved = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");
        Patient patientToUpdate = new Patient(1, "John", "Smith", LocalDate.of(2030, 7, 22), Gender.Male, "59", "554 Queen street", "999-888-444");

        assertThrows(BirthdateException.class, ()-> patientService.updatePatient(1,patientToUpdate));
    }
}
