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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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
        // VALIDATION
        // =========================

        if (fullName == null || fullName.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Full name is required."
            );

            request.getRequestDispatcher(
                    "register.jsp"
            ).forward(request, response);

            return;
        }

        if (email == null ||
                !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            request.setAttribute(
                    "error",
                    "Please enter a valid email address."
            );

            request.getRequestDispatcher(
                    "register.jsp"
            ).forward(request, response);

            return;
        }

        if (phone != null &&
                !phone.isEmpty() &&
                !phone.matches("\\d{10,12}")) {

            request.setAttribute(
                    "error",
                    "Phone number must contain 10 to 12 digits."
            );

            request.getRequestDispatcher(
                    "register.jsp"
            ).forward(request, response);

            return;
        }

        if (password == null ||
                password.length() < 6) {

            request.setAttribute(
                    "error",
                    "Password must be at least 6 characters."
            );

            request.getRequestDispatcher(
                    "register.jsp"
            ).forward(request, response);

            return;
        }

        if (gender == null ||
                gender.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Please select a gender."
            );

            request.getRequestDispatcher(
                    "register.jsp"
            ).forward(request, response);

            return;
        }

        LocalDate dateOfBirth = null;

        if (dateOfBirthString != null &&
                !dateOfBirthString.isEmpty()) {

            try {

                dateOfBirth =
                        LocalDate.parse(dateOfBirthString);

                if (dateOfBirth.isAfter(LocalDate.now())) {

                    request.setAttribute(
                            "error",
                            "Date of birth cannot be in the future."
                    );

                    request.getRequestDispatcher(
                            "register.jsp"
                    ).forward(request, response);

                    return;
                }

            } catch (Exception e) {

                request.setAttribute(
                        "error",
                        "Invalid date of birth."
                );

                request.getRequestDispatcher(
                        "register.jsp"
                ).forward(request, response);

                return;
            }
        }

        // Check duplicate email
        Patient existingPatient =
                patientDAO.getPatientByEmail(email);

        if (existingPatient != null) {

            request.setAttribute(
                    "error",
                    "Email is already registered."
            );

            request.getRequestDispatcher(
                    "register.jsp"
            ).forward(request, response);

            return;
        }

        // Hash password
        String hashedPassword =
                PasswordUtil.hashPassword(password);

        Patient patient = new Patient(
                fullName.trim(),
                email.trim(),
                phone,
                hashedPassword,
                gender,
                dateOfBirth
        );

        boolean success =
                patientDAO.addPatient(patient);

        if (success) {

            request.setAttribute(
                    "success",
                    "Registration successful. You can now login."
            );

        } else {

            request.setAttribute(
                    "error",
                    "Registration failed. Please try again."
            );
        }

        request.getRequestDispatcher(
                "register.jsp"
        ).forward(request, response);
    }
}