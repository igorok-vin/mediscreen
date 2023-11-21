package com.mediscreen.patientservice.service;

import com.mediscreen.patientservice.exception.BirthdateException;
import com.mediscreen.patientservice.exception.PatientNotFoundException;
import com.mediscreen.patientservice.model.Gender;
import com.mediscreen.patientservice.model.Patient;
import com.mediscreen.patientservice.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    PatientRepository patientRepository;

    @InjectMocks
    PatientService patientService;

    @Test
    public void createPatientTest() {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient patientResult = patientService.createPatient(patient);

        assertEquals(patientResult.getPatientId(), patient.getPatientId());
        assertEquals(patientResult.getFirstName(), patient.getFirstName());
        assertEquals(patientResult.getLastName(), patient.getLastName());
    }

    @Test
    public void getPatientByIdTest() {
        Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        Patient patientResult = patientService.getPatientById(anyLong());

        assertEquals(patientResult.getDob(), patient.getDob());
        assertEquals(patientResult.getAddress(), patient.getAddress());
        assertEquals(patientResult.getAge(), patient.getAge());
        assertEquals(patientResult.getPhoneNumber(), patient.getPhoneNumber());
        assertEquals(patientResult.getGender(), patient.getGender());
    }

    @Test
    public void getPatientsTest() {
        List<Patient> patientList = new ArrayList<>(Arrays.asList(
                new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222"),
                new Patient(2, "Anna", "Walker", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-772"),
                new Patient(3, "Bonny", "Russell", LocalDate.of(2023, 7, 25), Gender.Female, "59", "1133 King street", "775-366-200"),
                new Patient(4,"Jack","Logan", LocalDate.of(2023,6,5), Gender.Male,"59","1133 King street","511-322-288")));

        when(patientRepository.findAll()).thenReturn(patientList);

        List<Patient> patientListResult = patientService.getPatients();

        assertEquals(patientListResult.size(), 4);
    }

    @Test
    public void getPatientByIdWhenPatientNotFoundTest() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, ()-> patientService.getPatientById(1));
        verify(patientRepository).findById(1L);
    }

    @Test
    public void updatePatientTest() throws BirthdateException {
        Patient patientSaved = new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "3 days", "1133 King street", "555-333-222");
        Patient patientToUpdate = new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "3 days", "554 Queen street", "999-888-444");

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patientSaved));

        when(patientRepository.save(any(Patient.class))).thenReturn(patientToUpdate);

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

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patientSaved));

        assertThrows(BirthdateException.class, ()-> patientService.updatePatient(1,patientToUpdate));
    }

    @Test
    public void deleteNoteByIdTest() {
        Patient patientSaved = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patientSaved));

        patientService.deletePatient(1);
        verify(patientRepository).delete(patientSaved);
    }

    @Test
    public void getPatientsByLastName () throws PatientNotFoundException {
        List<Patient>  patientList = new ArrayList<>(Arrays.asList( new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222")));

        when(patientRepository.findByLastName(anyString())).thenReturn(patientList);

        List<Patient> patientsListResult = patientService.getPatientsByLastName("Smith");

        assertEquals("Smith", patientsListResult.get(0).getLastName());
    }

    @Test
    public void getPatientsByLastNameWhenPatientNotFoundException() throws PatientNotFoundException {

        when(patientRepository.findByLastName(anyString())).thenReturn((Collections.emptyList()));

        assertThrows(PatientNotFoundException.class, ()-> patientService.getPatientsByLastName("Smith"));
    }

    @Test
    public void paginatedTest() {
        Pageable pageable = PageRequest.of(1,2);
        List<Patient> patientList = new ArrayList<>(Arrays.asList(
                new Patient(1, "John", "Smith", LocalDate.of(2023, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222"),
                new Patient(2, "Anna", "Walker", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-772"),
                new Patient(3, "Bonny", "Russell", LocalDate.of(2023, 7, 25), Gender.Female, "59", "1133 King street", "775-366-200"),
                new Patient(4,"Jack","Logan", LocalDate.of(2023,6,5), Gender.Male,"59","1133 King street","511-322-288")));
        Page<Patient> patientPage = new PageImpl<>(patientList);

        when(patientRepository.findAllWithPagination(any(Pageable.class))).thenReturn(patientPage);

        Page<Patient> pageResult = patientService.paginated(pageable);

        assertEquals(pageResult.stream().count(), 4);
        assertEquals(pageResult.getTotalElements(),4);
        assertEquals(pageResult.getTotalPages(),2);
        assertEquals(pageResult.getContent().get(2).getPatientId(),3);
        assertEquals(pageResult.getContent().get(0).getPhoneNumber(),"555-333-222");
    }
}
