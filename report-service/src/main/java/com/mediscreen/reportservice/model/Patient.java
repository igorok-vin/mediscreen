package com.mediscreen.reportservice.model;

import java.time.LocalDate;

public class Patient {

    private long patientId;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private Gender gender;

    String age;

    private String address;

    private String phoneNumber;

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


    public long getPatientId() {
        return patientId;
    }

    public Gender getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
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
