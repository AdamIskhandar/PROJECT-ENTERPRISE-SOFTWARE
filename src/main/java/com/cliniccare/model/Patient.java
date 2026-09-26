package com.cliniccare.model;

import java.time.LocalDate;

public class Patient {

    private int patientId;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String gender;
    private LocalDate dateOfBirth;

    // Empty constructor
    public Patient() {
    }

    // Constructor for registration
    public Patient(String fullName, String email, String phone,
                   String password, String gender, LocalDate dateOfBirth) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    // Full constructor
    public Patient(int patientId, String fullName, String email,
                   String phone, String password, String gender,
                   LocalDate dateOfBirth) {
        this.patientId = patientId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}