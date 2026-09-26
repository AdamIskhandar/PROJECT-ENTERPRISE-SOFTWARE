package com.cliniccare.controller;

import com.cliniccare.dao.PatientDAO;
import com.cliniccare.model.Patient;
import com.cliniccare.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/addPatient")
public class AddPatientServlet extends HttpServlet {

    private PatientDAO patientDAO;

    @Override
    public void init() {
        patientDAO = new PatientDAO();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");
        String dateOfBirthString = request.getParameter("dateOfBirth");

        // =========================
        // FULL NAME VALIDATION
        // =========================
        if (fullName == null || fullName.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Full name is required."
            );

            request.getRequestDispatcher(
                    "addPatient.jsp"
            ).forward(request, response);

            return;
        }

        // =========================
        // EMAIL VALIDATION
        // =========================
        if (email == null ||
                !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            request.setAttribute(
                    "error",
                    "Please enter a valid email address."
            );

            request.getRequestDispatcher(
                    "addPatient.jsp"
            ).forward(request, response);

            return;
        }

        // =========================
        // PHONE VALIDATION
        // =========================
        if (phone != null &&
                !phone.trim().isEmpty() &&
                !phone.matches("\\d{10,12}")) {

            request.setAttribute(
                    "error",
                    "Phone number must contain 10 to 12 digits."
            );

            request.getRequestDispatcher(
                    "addPatient.jsp"
            ).forward(request, response);

            return;
        }

        // =========================
        // PASSWORD VALIDATION
        // =========================
        if (password == null ||
                password.length() < 6) {

            request.setAttribute(
                    "error",
                    "Password must be at least 6 characters."
            );

            request.getRequestDispatcher(
                    "addPatient.jsp"
            ).forward(request, response);

            return;
        }

        // =========================
        // GENDER VALIDATION
        // =========================
        if (gender == null ||
                gender.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Please select a gender."
            );

            request.getRequestDispatcher(
                    "addPatient.jsp"
            ).forward(request, response);

            return;
        }

        // =========================
        // DATE OF BIRTH VALIDATION
        // =========================
        LocalDate dateOfBirth = null;

        if (dateOfBirthString != null &&
                !dateOfBirthString.trim().isEmpty()) {

            try {

                dateOfBirth =
                        LocalDate.parse(dateOfBirthString);

                if (dateOfBirth.isAfter(LocalDate.now())) {

                    request.setAttribute(
                            "error",
                            "Date of birth cannot be in the future."
                    );

                    request.getRequestDispatcher(
                            "addPatient.jsp"
                    ).forward(request, response);

                    return;
                }

            } catch (Exception e) {

                request.setAttribute(
                        "error",
                        "Invalid date of birth."
                );

                request.getRequestDispatcher(
                        "addPatient.jsp"
                ).forward(request, response);

                return;
            }
        }

        // =========================
        // DUPLICATE EMAIL CHECK
        // =========================
        Patient existingPatient =
                patientDAO.getPatientByEmail(email.trim());

        if (existingPatient != null) {

            request.setAttribute(
                    "error",
                    "Email is already registered."
            );

            request.getRequestDispatcher(
                    "addPatient.jsp"
            ).forward(request, response);

            return;
        }

        // =========================
        // HASH PASSWORD
        // =========================
        String hashedPassword =
                PasswordUtil.hashPassword(password);

        // =========================
        // CREATE PATIENT OBJECT
        // =========================
        Patient patient = new Patient(
                fullName.trim(),
                email.trim(),
                phone != null ? phone.trim() : null,
                hashedPassword,
                gender,
                dateOfBirth
        );

        // =========================
        // INSERT INTO DATABASE
        // =========================
        boolean success =
                patientDAO.addPatient(patient);

        if (success) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/managePatients?message=added"
            );

        } else {

            request.setAttribute(
                    "error",
                    "Failed to add patient."
            );

            request.getRequestDispatcher(
                    "addPatient.jsp"
            ).forward(request, response);
        }
    }
}