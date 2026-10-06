package com.cliniccare.model;

/**
 * Represents one row from the doctor table.
 */
public class Doctor {

    private int doctorId;
    private String doctorName;
    private String specialization;
    private String phone;
    private String email;

    public Doctor() {
    }

    public Doctor(String doctorName,
                  String specialization,
                  String phone,
                  String email) {
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
    }

    public Doctor(int doctorId,
                  String doctorName,
                  String specialization,
                  String phone,
                  String email) {
        this(doctorName, specialization, phone, email);
        this.doctorId = doctorId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
