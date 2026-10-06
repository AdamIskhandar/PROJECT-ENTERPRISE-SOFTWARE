package com.cliniccare.config;

import java.math.BigDecimal;

/**
 * Central fixed clinic rates used by ClinicCare.
 * These values are also written to the fees table for each doctor so the
 * appointment calculation can continue to read the rates from the database.
 */
public final class ClinicRateConfig {

    public static final BigDecimal MORNING_RATE = new BigDecimal("60.00");
    public static final BigDecimal NIGHT_RATE = new BigDecimal("90.00");

    private ClinicRateConfig() {
    }
}
