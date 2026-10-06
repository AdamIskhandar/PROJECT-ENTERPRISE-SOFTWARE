package com.cliniccare.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private int appointmentId;
    private int patientId;
    private String patientName;
    private int scheduleId;
    private int doctorId;
    private String doctorName;
    private String specialization;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String rateType;
    private BigDecimal rateUsed;
    private BigDecimal durationHours;
    private BigDecimal totalCharge;
    private String reason;
    private String status;
    private BigDecimal morningRate;
    private BigDecimal nightRate;

    private BigDecimal morningHours;
    private BigDecimal nightHours;

    private BigDecimal morningCharge;
    private BigDecimal nightCharge;

    public Appointment() {
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
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

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getRateUsed() {
        return rateUsed;
    }

    public void setRateUsed(BigDecimal rateUsed) {
        this.rateUsed = rateUsed;
    }

    public BigDecimal getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(BigDecimal durationHours) {
        this.durationHours = durationHours;
    }

    public BigDecimal getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMorningRate() {
    return morningRate;
    }

    public void setMorningRate(
            BigDecimal morningRate) {

        this.morningRate =
                morningRate;
    }


    public BigDecimal getNightRate() {
        return nightRate;
    }

    public void setNightRate(
            BigDecimal nightRate) {

        this.nightRate =
                nightRate;
    }


    public BigDecimal getMorningHours() {
        return morningHours;
    }

    public void setMorningHours(
            BigDecimal morningHours) {

        this.morningHours =
                morningHours;
    }


    public BigDecimal getNightHours() {
        return nightHours;
    }

    public void setNightHours(
            BigDecimal nightHours) {

        this.nightHours =
                nightHours;
    }


    public BigDecimal getMorningCharge() {
        return morningCharge;
    }

    public void setMorningCharge(
            BigDecimal morningCharge) {

        this.morningCharge =
                morningCharge;
    }


    public BigDecimal getNightCharge() {
        return nightCharge;
    }

    public void setNightCharge(
            BigDecimal nightCharge) {

        this.nightCharge =
                nightCharge;
    }
}
