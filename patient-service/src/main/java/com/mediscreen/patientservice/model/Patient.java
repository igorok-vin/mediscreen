package com.mediscreen.patientservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Entity
@Table(name="patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long patientId;

    @NotBlank(message = "Patient first name is required")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Patient last name is required")
    @Column(name = "last_name")
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "birth_date")
    @NotNull(message = "Day of birthday should not be blank")
    private LocalDate dob;

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "Patient gender is required")
    private Gender gender;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    String age;

    @NotNull(message = "The field cannot be blank")
    private String address;

    @Pattern(regexp = "^\\d{3}\\-\\d{3}\\-\\d{4}",
            message = "Invalid phone number pattern. Phone number must be following a pattern 111-111-1111 ")
    @NotNull(message = "The field cannot be blank")
    @Column(name = "phone")
    private String phoneNumber;

    public Patient() {
    }

    public Patient(long patientId, String firstName, String lastName, LocalDate dob, Gender gender, String age, String address, String phoneNumber) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patient id=" + patientId +
                ", first name='" + firstName + '\'' +
                ", last name='" + lastName + '\'' +
                ", date of birth =" + dob +
                ", gender=" + gender +
                ", age=" + age +" years"+
                ", address='" + address + '\'' +
                ", phone number='" + phoneNumber + '\'' +
                '}';
    }
}
