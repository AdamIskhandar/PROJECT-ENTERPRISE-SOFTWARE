package com.cliniccare.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;

public final class AppointmentChargeCalculator {

    // cliniccare.sql defines morning_rate and night_rate, but does not define
    // the cutoff time. ClinicCare uses 6:00 PM as the application rule.
    public static final LocalTime NIGHT_RATE_START = LocalTime.of(18, 0);

    private AppointmentChargeCalculator() {
    }

    public static ChargeBreakdown calculate(LocalTime startTime,
                                            LocalTime endTime,
                                            BigDecimal morningRate,
                                            BigDecimal nightRate) {

        BigDecimal durationHours = calculateDurationHours(startTime, endTime);
        String rateType = determineRateType(startTime);
        BigDecimal rateUsed = selectRate(rateType, morningRate, nightRate);
        BigDecimal totalCharge = calculateTotal(rateUsed, durationHours);

        return new ChargeBreakdown(
                rateType,
                rateUsed,
                durationHours,
                totalCharge
        );
    }

    public static BigDecimal calculateDurationHours(LocalTime startTime,
                                                    LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time are required.");
        }

        long minutes = Duration.between(startTime, endTime).toMinutes();

        if (minutes <= 0) {
            throw new IllegalArgumentException("End time must be after start time.");
        }

        return BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }

    public static String determineRateType(LocalTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("Start time is required.");
        }

        return startTime.isBefore(NIGHT_RATE_START) ? "Morning" : "Night";
    }

    public static BigDecimal selectRate(String rateType,
                                        BigDecimal morningRate,
                                        BigDecimal nightRate) {
        BigDecimal selectedRate;

        if ("Night".equalsIgnoreCase(rateType)) {
            selectedRate = nightRate;
        } else {
            selectedRate = morningRate;
        }

        if (selectedRate == null || selectedRate.signum() < 0) {
            throw new IllegalArgumentException("A valid doctor rate is required.");
        }

        return selectedRate.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateTotal(BigDecimal rateUsed,
                                            BigDecimal durationHours) {
        if (rateUsed == null || durationHours == null
                || rateUsed.signum() < 0 || durationHours.signum() <= 0) {
            throw new IllegalArgumentException("Rate and duration must be valid.");
        }

        return rateUsed.multiply(durationHours)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public record ChargeBreakdown(
            String rateType,
            BigDecimal rateUsed,
            BigDecimal durationHours,
            BigDecimal totalCharge) {
    }
}
