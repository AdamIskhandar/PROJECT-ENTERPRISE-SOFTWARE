package com.cliniccare.model;

public class Doctor {

    private int doctorId;
    private String fullName;
    private String specialization;
    private String email;
    private String phone;
    private double consultationFee;

    public Doctor() {
    }

    public Doctor(String fullName, String specialization,
                  String email, String phone, double consultationFee) {
        this.fullName = fullName;
        this.specialization = specialization;
        this.email = email;
        this.phone = phone;
        this.consultationFee = consultationFee;
    }

    public Doctor(int doctorId, String fullName, String specialization,
                  String email, String phone, double consultationFee) {
        this(fullName, specialization, email, phone, consultationFee);
        this.doctorId = doctorId;
    }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }
}
