package com.cliniccare.util;

import com.cliniccare.model.Doctor;
import com.cliniccare.model.MedicalService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Reads and validates the doctor / service forms.
 * validateXxx returns an error message, or null when everything is valid.
 */
public final class FormUtil {

    private static final double MAX_AMOUNT = 99999999.99;

    private FormUtil() {
    }

    // =========================
    // Small helpers
    // =========================
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /** Returns the amount, or null if it is not a number between 0 and 99,999,999.99. */
    public static Double parseAmount(String value) {

        try {
            double amount = Double.parseDouble(value.trim());

            if (Double.isNaN(amount) || amount < 0 || amount > MAX_AMOUNT) {
                return null;
            }

            return amount;

        } catch (Exception e) {
            return null;
        }
    }

    public static int parseId(String value) {

        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private static String trimOrNull(String value) {
        return isBlank(value) ? null : value.trim();
    }

    // =========================
    // Doctor form
    // =========================
    public static String validateDoctor(HttpServletRequest request) {

        String fullName = request.getParameter("fullName");
        String specialization = request.getParameter("specialization");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String fee = request.getParameter("consultationFee");

        if (isBlank(fullName)) {
            return "Full name is required.";
        }

        if (fullName.trim().length() > 100) {
            return "Full name must be 100 characters or less.";
        }

        if (isBlank(specialization)) {
            return "Specialization is required.";
        }

        if (specialization.trim().length() > 100) {
            return "Specialization must be 100 characters or less.";
        }

        if (email == null
                || !email.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return "Please enter a valid email address.";
        }

        if (!isBlank(phone) && !phone.trim().matches("\\d{10,12}")) {
            return "Phone number must contain 10 to 12 digits.";
        }

        if (parseAmount(fee) == null) {
            return "Consultation fee must be a number, 0 or more.";
        }

        return null;
    }

    /** Builds a Doctor from the form (used to refill the form after an error). */
    public static Doctor doctorFromRequest(HttpServletRequest request) {

        Double fee = parseAmount(request.getParameter("consultationFee"));

        Doctor doctor = new Doctor(
                request.getParameter("fullName") == null
                        ? "" : request.getParameter("fullName").trim(),
                request.getParameter("specialization") == null
                        ? "" : request.getParameter("specialization").trim(),
                request.getParameter("email") == null
                        ? "" : request.getParameter("email").trim(),
                trimOrNull(request.getParameter("phone")),
                fee == null ? 0 : fee
        );

        doctor.setDoctorId(parseId(request.getParameter("doctorId")));

        return doctor;
    }

    // =========================
    // Service form
    // =========================
    public static String validateService(HttpServletRequest request) {

        String name = request.getParameter("serviceName");
        String description = request.getParameter("description");
        String price = request.getParameter("price");

        if (isBlank(name)) {
            return "Service name is required.";
        }

        if (name.trim().length() > 100) {
            return "Service name must be 100 characters or less.";
        }

        if (description != null && description.trim().length() > 255) {
            return "Description must be 255 characters or less.";
        }

        if (parseAmount(price) == null) {
            return "Price must be a number, 0 or more.";
        }

        return null;
    }

    public static MedicalService serviceFromRequest(HttpServletRequest request) {

        Double price = parseAmount(request.getParameter("price"));

        MedicalService service = new MedicalService(
                request.getParameter("serviceName") == null
                        ? "" : request.getParameter("serviceName").trim(),
                trimOrNull(request.getParameter("description")),
                price == null ? 0 : price
        );

        service.setServiceId(parseId(request.getParameter("serviceId")));

        return service;
    }
}
