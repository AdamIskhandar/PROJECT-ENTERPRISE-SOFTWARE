package com.cliniccare.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;

public final class AppointmentChargeCalculator {

    // Night rate starts exactly at 7:00 PM
    public static final LocalTime NIGHT_RATE_START =
            LocalTime.of(19, 0);

    private static final BigDecimal SIXTY =
            BigDecimal.valueOf(60);

    private AppointmentChargeCalculator() {
    }


    public static ChargeBreakdown calculate(
            LocalTime startTime,
            LocalTime endTime,
            BigDecimal morningRate,
            BigDecimal nightRate) {

        validateTimes(
                startTime,
                endTime
        );

        validateRate(
                morningRate,
                "Morning rate"
        );

        validateRate(
                nightRate,
                "Night rate"
        );


        // =========================
        // TOTAL DURATION
        // =========================

        long totalMinutes =
                Duration.between(
                        startTime,
                        endTime
                ).toMinutes();

        BigDecimal totalDurationHours =
                minutesToHours(
                        totalMinutes
                );


        long morningMinutes = 0;
        long nightMinutes = 0;


        // =========================
        // CASE 1:
        // Entire appointment
        // before 7 PM
        // =========================

        if (!startTime.isAfter(NIGHT_RATE_START)
                &&
                !endTime.isAfter(NIGHT_RATE_START)) {

            morningMinutes =
                    Duration.between(
                            startTime,
                            endTime
                    ).toMinutes();
        }


        // =========================
        // CASE 2:
        // Entire appointment
        // at/after 7 PM
        // =========================

        else if (!startTime.isBefore(
                NIGHT_RATE_START)) {

            nightMinutes =
                    Duration.between(
                            startTime,
                            endTime
                    ).toMinutes();
        }


        // =========================
        // CASE 3:
        // Appointment crosses 7 PM
        // =========================

        else {

            morningMinutes =
                    Duration.between(
                            startTime,
                            NIGHT_RATE_START
                    ).toMinutes();


            nightMinutes =
                    Duration.between(
                            NIGHT_RATE_START,
                            endTime
                    ).toMinutes();
        }


        // =========================
        // CONVERT TO HOURS
        // =========================

        BigDecimal morningHours =
                minutesToHours(
                        morningMinutes
                );


        BigDecimal nightHours =
                minutesToHours(
                        nightMinutes
                );


        // =========================
        // MORNING CHARGE
        // =========================

        BigDecimal morningCharge =
                morningRate
                        .multiply(
                                morningHours
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );


        // =========================
        // NIGHT CHARGE
        // =========================

        BigDecimal nightCharge =
                nightRate
                        .multiply(
                                nightHours
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );


        // =========================
        // TOTAL CHARGE
        // =========================

        BigDecimal totalCharge =
                morningCharge
                        .add(
                                nightCharge
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );


        String rateType;

        BigDecimal rateUsed;


        // =========================
        // MIXED RATE
        // =========================

        if (morningMinutes > 0
                &&
                nightMinutes > 0) {

            rateType = "Mixed";


            /*
             * Database only has one
             * rate_used column.
             *
             * For mixed appointments,
             * store the effective
             * average hourly rate.
             */
            rateUsed =
                    totalCharge.divide(
                            totalDurationHours,
                            2,
                            RoundingMode.HALF_UP
                    );
        }


        // =========================
        // NIGHT ONLY
        // =========================

        else if (nightMinutes > 0) {

            rateType = "Night";

            rateUsed =
                    nightRate.setScale(
                            2,
                            RoundingMode.HALF_UP
                    );
        }


        // =========================
        // MORNING ONLY
        // =========================

        else {

            rateType = "Morning";

            rateUsed =
                    morningRate.setScale(
                            2,
                            RoundingMode.HALF_UP
                    );
        }


        return new ChargeBreakdown(

                rateType,

                rateUsed,

                totalDurationHours,

                totalCharge,

                morningRate.setScale(
                        2,
                        RoundingMode.HALF_UP
                ),

                nightRate.setScale(
                        2,
                        RoundingMode.HALF_UP
                ),

                morningHours,

                nightHours,

                morningCharge,

                nightCharge
        );
    }


    // =========================
    // DURATION
    // =========================

    public static BigDecimal calculateDurationHours(
            LocalTime startTime,
            LocalTime endTime) {

        validateTimes(
                startTime,
                endTime
        );


        long minutes =
                Duration.between(
                        startTime,
                        endTime
                ).toMinutes();


        return minutesToHours(
                minutes
        );
    }


    // =========================
    // RATE TYPE
    // =========================

    public static String determineRateType(
            LocalTime startTime) {

        if (startTime == null) {

            throw new IllegalArgumentException(
                    "Start time is required."
            );
        }


        return startTime.isBefore(
                NIGHT_RATE_START
        )
                ? "Morning"
                : "Night";
    }


    // =========================
    // SELECT RATE
    // =========================

    public static BigDecimal selectRate(
            String rateType,
            BigDecimal morningRate,
            BigDecimal nightRate) {

        validateRate(
                morningRate,
                "Morning rate"
        );

        validateRate(
                nightRate,
                "Night rate"
        );


        if ("Night".equalsIgnoreCase(
                rateType)) {

            return nightRate.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        }


        return morningRate.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }


    // =========================
    // SIMPLE TOTAL
    // =========================

    public static BigDecimal calculateTotal(
            BigDecimal rateUsed,
            BigDecimal durationHours) {

        if (rateUsed == null
                ||
                durationHours == null
                ||
                rateUsed.signum() < 0
                ||
                durationHours.signum() <= 0) {

            throw new IllegalArgumentException(
                    "Rate and duration must be valid."
            );
        }


        return rateUsed
                .multiply(
                        durationHours
                )
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }


    // =========================
    // VALIDATE TIME
    // =========================

    private static void validateTimes(
            LocalTime startTime,
            LocalTime endTime) {

        if (startTime == null
                ||
                endTime == null) {

            throw new IllegalArgumentException(
                    "Start time and end time are required."
            );
        }


        long minutes =
                Duration.between(
                        startTime,
                        endTime
                ).toMinutes();


        if (minutes <= 0) {

            throw new IllegalArgumentException(
                    "End time must be after start time."
            );
        }
    }


    // =========================
    // VALIDATE RATE
    // =========================

    private static void validateRate(
            BigDecimal rate,
            String label) {

        if (rate == null
                ||
                rate.signum() < 0) {

            throw new IllegalArgumentException(
                    label
                    + " must be valid."
            );
        }
    }


    // =========================
    // MINUTES TO HOURS
    // =========================

    private static BigDecimal minutesToHours(
            long minutes) {

        return BigDecimal
                .valueOf(minutes)
                .divide(
                        SIXTY,
                        2,
                        RoundingMode.HALF_UP
                );
    }


    // =========================
    // RESULT
    // =========================

    public record ChargeBreakdown(

            String rateType,

            BigDecimal rateUsed,

            BigDecimal durationHours,

            BigDecimal totalCharge,

            BigDecimal morningRate,

            BigDecimal nightRate,

            BigDecimal morningHours,

            BigDecimal nightHours,

            BigDecimal morningCharge,

            BigDecimal nightCharge

    ) {
    }
}