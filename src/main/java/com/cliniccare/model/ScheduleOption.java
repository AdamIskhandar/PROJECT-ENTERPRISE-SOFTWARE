package com.cliniccare.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleOption {

    private int scheduleId;
    private int doctorId;
    private String doctorName;
    private String specialization;
    private LocalDate availableDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String scheduleStatus;
    private BigDecimal morningRate;
    private BigDecimal nightRate;
    private String rateType;
    private BigDecimal rateUsed;
    private BigDecimal durationHours;
    private BigDecimal estimatedCharge;

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

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
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

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public BigDecimal getMorningRate() {
        return morningRate;
    }

    public void setMorningRate(BigDecimal morningRate) {
        this.morningRate = morningRate;
    }

    public BigDecimal getNightRate() {
        return nightRate;
    }

    public void setNightRate(BigDecimal nightRate) {
        this.nightRate = nightRate;
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

    public BigDecimal getEstimatedCharge() {
        return estimatedCharge;
    }

    public void setEstimatedCharge(BigDecimal estimatedCharge) {
        this.estimatedCharge = estimatedCharge;
    }
}
