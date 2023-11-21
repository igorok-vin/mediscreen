package com.mediscreen.reportservice.service;

import com.mediscreen.reportservice.model.Gender;
import com.mediscreen.reportservice.model.Note;
import com.mediscreen.reportservice.model.Patient;
import com.mediscreen.reportservice.model.Report;
import com.mediscreen.reportservice.proxy.HistoryProxy;
import com.mediscreen.reportservice.proxy.PatientProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    @Mock
    HistoryProxy historyProxy;

    @Mock
    PatientProxy patientProxy;

    @InjectMocks
    ReportService reportService;

    @Test
    public void getListNotesByPatientIdTest() {
      Patient patient = new Patient(1, "John", "Smith", LocalDate.of(1964, 7, 22), Gender.Male, "59", "1133 King street", "555-333-222");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now()),
                new Note("2",1,"Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated", LocalDate.now()),
                new Note("3",1,"Patient states that they not feeling as tired. Smoker, quit within last year. Lab results indicate Antibodies present elevated", LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        List<String> notesListResult = reportService.getListNotesByPatientId(1);

        System.out.println(notesListResult);

        assertEquals(3,notesListResult.size());
    }

    @Test
    public void getReportByPatientId_whenPatientFemaleGreaterThan30And3Triggers_ThenReportResultBorderLineTest() {
       Patient patient = new Patient(2, "Anna", "Walker", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-772");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now()),
                new Note("2",1,"Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated", LocalDate.now()),
                new Note("3",1,"Patient states that they not feeling as tired. Smoker, quit within last year. Lab results indicate Antibodies present elevated", LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        Report reportResult = reportService.getReportByPatientId(1);

        assertEquals(true, reportResult.getFinalResult().contains("Anna"));
        assertEquals("Borderline", reportResult.getRiskLevel());
    }

    @Test
    public void getReportByPatientId_whenPatientFemaleGreaterThan30And6Triggers_ThenReportResultInDangerTest() {
        Patient patient = new Patient(2, "Anna", "Walker", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-772");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now()),
                new Note("2",1,"Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated. Hemoglobin a1c", LocalDate.now()),
                new Note("3",1,"Patient states that they not feeling as tired. Smoker, quit within last year. Lab results indicate Antibodies present elevated. Body weight. Cholesterol", LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        Report reportResult = reportService.getReportByPatientId(1);

        assertEquals(true, reportResult.getFinalResult().contains("Anna"));
        assertEquals("In danger", reportResult.getRiskLevel());
    }

    @Test
    public void getReportByPatientId_whenPatientFemaleGreaterThan30And8Triggers_ThenReportResultEarlyOnSetTest() {
        Patient patient = new Patient(2, "Anna", "Walker", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-772");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now()),
                new Note("2",1,"Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated. Hemoglobin a1c", LocalDate.now()),
                new Note("3",1,"Patient states that they not feeling as tired. Smoker, quit within last year. Lab results indicate Antibodies present elevated. Body weight. Cholesterol. Abnormal dizziness" , LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        Report reportResult = reportService.getReportByPatientId(1);

        assertEquals(true, reportResult.getFinalResult().contains("Walker"));
        assertEquals("Early onset", reportResult.getRiskLevel());
    }

    @Test
    public void getReportByPatientId_whenPatientFemaleGreaterThan30And0Triggers_ThenReportResultNoneTest() {
        Patient patient = new Patient(2, "Anna", "Walker", LocalDate.of(1972, 5, 14), Gender.Female, "51", "1133 King street", "525-663-772");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        Report reportResult = reportService.getReportByPatientId(1);

        assertEquals(true, reportResult.getFinalResult().contains("Walker"));
        assertEquals("None", reportResult.getRiskLevel());
    }

    @Test
    public void getReportByPatientId_whenPatientFemaleLessThan30And5Triggers_ThenReportResultInDangerTest() {
        Patient patient = new Patient(2, "Anna", "Walker", LocalDate.of(2001, 5, 14), Gender.Female, "22", "1133 King street", "525-663-772");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now()),
                new Note("2",1,"Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated. Hemoglobin a1c", LocalDate.now()),
                new Note("3",1,"Patient states that they not feeling as tired. Smoker, quit within last year. Lab results indicate Antibodies present elevated." , LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        Report reportResult = reportService.getReportByPatientId(1);

        assertEquals(true, reportResult.getFinalResult().contains("Walker"));
        assertEquals("In danger", reportResult.getRiskLevel());
    }

    @Test
    public void getReportByPatientId_whenPatientFemaleLessThan30And8Triggers_ThenReportResultEarlyOnSetTest() {
        Patient patient = new Patient(2, "Anna", "Walker", LocalDate.of(2001, 5, 14), Gender.Female, "22", "1133 King street", "525-663-772");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now()),
                new Note("2",1,"Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated. Hemoglobin a1c", LocalDate.now()),
                new Note("3",1,"Patient states that they not feeling as tired. Smoker, quit within last year. Lab results indicate Antibodies present elevated. Body weight. Cholesterol. Abnormal dizziness" , LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        Report reportResult = reportService.getReportByPatientId(1);

        assertEquals(true, reportResult.getFinalResult().contains("Walker"));
        assertEquals("Early onset", reportResult.getRiskLevel());
    }

    @Test
    public void getReportByPatientId_whenPatientFemaleLessThan30And0Triggers_ThenReportResultNoneTest() {
        Patient patient = new Patient(2, "Anna", "Walker", LocalDate.of(2001, 5, 14), Gender.Female, "22", "1133 King street", "525-663-772");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        Report reportResult = reportService.getReportByPatientId(1);

        assertEquals(true, reportResult.getFinalResult().contains("Walker"));
        assertEquals("None", reportResult.getRiskLevel());
    }

    @Test
    public void getReportByPatientId_whenPatientFemale2DaysOldAnd0Triggers_ThenReportResultNoneTest() {
        Patient patient = new Patient(2, "Anna", "Walker", LocalDate.of(2023, 7, 14), Gender.Female, "2 days", "1133 King street", "525-663-772");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        Report reportResult = reportService.getReportByPatientId(1);

        assertEquals(true, reportResult.getFinalResult().contains("Walker"));
        assertEquals("None", reportResult.getRiskLevel());
    }

    @Test
    public void getReportByPatientId_whenPatientMaleLessThan30And3Triggers_ThenReportResultInDangerTest() {
        Patient patient = new Patient(2, "John", "Smith", LocalDate.of(2001, 5, 14), Gender.Male, "22", "1133 King street", "525-663-772");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now()),
                new Note("2",1,"Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated. Hemoglobin a1c", LocalDate.now()),
                new Note("3",1,"Patient states that they not feeling as tired. Lab results indicate Antibodies present elevated." , LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        Report reportResult = reportService.getReportByPatientId(1);

        assertEquals(true, reportResult.getFinalResult().contains("Smith"));
        assertEquals("In danger", reportResult.getRiskLevel());
    }

    @Test
    public void getReportByPatientId_whenPatientMaleLessThan30And6Triggers_ThenReportResultEarlyOnsetTest() {
        Patient patient = new Patient(2, "John", "Smith", LocalDate.of(2001, 5, 14), Gender.Male, "22", "1133 King street", "525-663-772");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now()),
                new Note("2",1,"Patient states that they feel tired during the day. Patient also complains about muscle aches. Lab reports Microalbumin elevated. Hemoglobin a1c", LocalDate.now()),
                new Note("3",1,"Patient states that they not feeling as tired. Lab results indicate Antibodies present elevated. Body weight. Cholesterol. Abnormal dizziness" , LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        Report reportResult = reportService.getReportByPatientId(1);

        assertEquals(true, reportResult.getFinalResult().contains("Smith"));
        assertEquals("Early onset", reportResult.getRiskLevel());
    }

    @Test
    public void getReportByPatientId_whenPatientMaleLessThan30And0Triggers_ThenReportResultNoneTest() {
        Patient patient = new Patient(2, "John", "Smith", LocalDate.of(2001, 5, 14), Gender.Male, "22", "1133 King street", "525-663-772");

        List<Note> noteList = new ArrayList<>(Arrays.asList(
                new Note("1",1,"Patient states that they are feeling terrific. Weight at or below recommended level", LocalDate.now())
        ));

        when(patientProxy.getPatientById(anyLong())).thenReturn(patient);
        when(historyProxy.findListOfNotesByPatientId(anyLong())).thenReturn(noteList);

        Report reportResult = reportService.getReportByPatientId(1);

        assertEquals(true, reportResult.getFinalResult().contains("Smith"));
        assertEquals("None", reportResult.getRiskLevel());
    }


}
