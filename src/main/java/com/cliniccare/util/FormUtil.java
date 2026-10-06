package com.cliniccare.util;

import com.cliniccare.model.Doctor;
import com.cliniccare.model.MedicalService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Small helper for reading and validating HTML form values.
 */
public final class FormUtil {

    private FormUtil() {
    }

    // =========================================================
    // DOCTOR
    // =========================================================

    public static Doctor doctorFromRequest(HttpServletRequest request) {
        Doctor doctor = new Doctor();

        doctor.setDoctorId(parseId(firstNonBlank(
                request.getParameter("doctorId"),
                request.getParameter("id"))));

        doctor.setDoctorName(trimOrEmpty(firstNonBlank(
                request.getParameter("doctorName"),
                request.getParameter("fullName"))));

        doctor.setSpecialization(
                trimOrEmpty(request.getParameter("specialization")));

        doctor.setPhone(trimOrNull(request.getParameter("phone")));
        doctor.setEmail(trimOrNull(request.getParameter("email")));

        return doctor;
    }

    public static Doctor getDoctorFromRequest(HttpServletRequest request) {
        return doctorFromRequest(request);
    }

    public static Doctor parseDoctor(HttpServletRequest request) {
        return doctorFromRequest(request);
    }

    /**
     * Validates only doctor-table fields. Rates are fixed by the application
     * and are not accepted from add/edit doctor forms.
     */
    public static String validateDoctor(HttpServletRequest request) {
        String doctorName = trimOrEmpty(firstNonBlank(
                request.getParameter("doctorName"),
                request.getParameter("fullName")));
        String specialization = trimOrEmpty(request.getParameter("specialization"));
        String email = trimOrNull(request.getParameter("email"));
        String phone = trimOrNull(request.getParameter("phone"));

        if (doctorName.isEmpty()) {
            return "Doctor name is required.";
        }
        if (doctorName.length() > 100) {
            return "Doctor name must not exceed 100 characters.";
        }
        if (specialization.isEmpty()) {
            return "Specialization is required.";
        }
        if (specialization.length() > 100) {
            return "Specialization must not exceed 100 characters.";
        }
        if (email != null) {
            if (email.length() > 100) {
                return "Email must not exceed 100 characters.";
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return "Please enter a valid doctor email address.";
            }
        }
        if (phone != null) {
            if (phone.length() > 20) {
                return "Phone number must not exceed 20 characters.";
            }
            if (!phone.matches("\\d{10,12}")) {
                return "Phone number must contain 10 to 12 digits.";
            }
        }

        return null;
    }

    // =========================================================
    // MEDICAL SERVICE
    // Kept for compatibility with the existing service module.
    // =========================================================

    public static MedicalService serviceFromRequest(HttpServletRequest request) {
        MedicalService service = new MedicalService();
        service.setServiceId(parseId(firstNonBlank(
                request.getParameter("serviceId"),
                request.getParameter("id"))));
        service.setServiceName(trimOrEmpty(request.getParameter("serviceName")));
        service.setDescription(trimOrNull(request.getParameter("description")));
        service.setPrice(parseDouble(request.getParameter("price")));
        return service;
    }

    public static String validateService(HttpServletRequest request) {
        String serviceName = trimOrEmpty(request.getParameter("serviceName"));
        String description = trimOrNull(request.getParameter("description"));
        String priceText = trimOrNull(request.getParameter("price"));

        if (serviceName.isEmpty()) {
            return "Service name is required.";
        }
        if (serviceName.length() > 100) {
            return "Service name must not exceed 100 characters.";
        }
        if (description != null && description.length() > 255) {
            return "Description must not exceed 255 characters.";
        }
        if (priceText == null) {
            return "Price is required.";
        }

        try {
            double price = Double.parseDouble(priceText);
            if (price < 0) {
                return "Price cannot be negative.";
            }
        } catch (NumberFormatException e) {
            return "Please enter a valid price.";
        }

        return null;
    }

    // =========================================================
    // GENERAL HELPERS
    // =========================================================

    public static int parseId(String value) {
        return parseInt(value);
    }

    public static int parseInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static String trimOrNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static String trimOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static String firstNonBlank(String first, String second) {
        if (first != null && !first.trim().isEmpty()) {
            return first;
        }
        return second;
    }
}
