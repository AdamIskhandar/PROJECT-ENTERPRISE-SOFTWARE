package com.cliniccare.controller;

import com.cliniccare.dao.PatientDAO;
import com.cliniccare.model.Patient;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/updatePatient")
public class UpdatePatientServlet extends HttpServlet {

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

        String patientIdString =
                request.getParameter("patientId");

        String fullName =
                request.getParameter("fullName");

        String email =
                request.getParameter("email");

        String phone =
                request.getParameter("phone");

        String gender =
                request.getParameter("gender");

        String dateOfBirthString =
                request.getParameter("dateOfBirth");

        String source =
                request.getParameter("source");

        int patientId;

        // =========================
        // PATIENT ID VALIDATION
        // =========================
        try {

            patientId =
                    Integer.parseInt(patientIdString);

        } catch (Exception e) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/managePatients?error=update"
            );

            return;
        }

        // =========================
        // FULL NAME VALIDATION
        // =========================
        if (fullName == null ||
                fullName.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Full name is required."
            );

            request.setAttribute(
                    "patient",
                    patientDAO.getPatientById(patientId)
            );

            request.getRequestDispatcher(
                    "editPatient.jsp"
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

            request.setAttribute(
                    "patient",
                    patientDAO.getPatientById(patientId)
            );

            request.getRequestDispatcher(
                    "editPatient.jsp"
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

            request.setAttribute(
                    "patient",
                    patientDAO.getPatientById(patientId)
            );

            request.getRequestDispatcher(
                    "editPatient.jsp"
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

            request.setAttribute(
                    "patient",
                    patientDAO.getPatientById(patientId)
            );

            request.getRequestDispatcher(
                    "editPatient.jsp"
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

                    request.setAttribute(
                            "patient",
                            patientDAO.getPatientById(patientId)
                    );

                    request.getRequestDispatcher(
                            "editPatient.jsp"
                    ).forward(request, response);

                    return;
                }

            } catch (Exception e) {

                request.setAttribute(
                        "error",
                        "Invalid date of birth."
                );

                request.setAttribute(
                        "patient",
                        patientDAO.getPatientById(patientId)
                );

                request.getRequestDispatcher(
                        "editPatient.jsp"
                ).forward(request, response);

                return;
            }
        }

        // =========================
        // DUPLICATE EMAIL VALIDATION
        // =========================
        Patient existingPatient =
                patientDAO.getPatientByEmail(email.trim());

        if (existingPatient != null &&
                existingPatient.getPatientId() != patientId) {

            request.setAttribute(
                    "error",
                    "Email is already used by another patient."
            );

            request.setAttribute(
                    "patient",
                    patientDAO.getPatientById(patientId)
            );

            request.getRequestDispatcher(
                    "editPatient.jsp"
            ).forward(request, response);

            return;
        }

        // =========================
        // CREATE UPDATED PATIENT
        // =========================
        Patient patient = new Patient();

        patient.setPatientId(patientId);
        patient.setFullName(fullName.trim());
        patient.setEmail(email.trim());

        if (phone != null) {
            patient.setPhone(phone.trim());
        }

        patient.setGender(gender);
        patient.setDateOfBirth(dateOfBirth);

        // =========================
        // UPDATE DATABASE
        // =========================
        boolean success =
                patientDAO.updatePatient(patient);

        if (success) {

            // Admin came from Manage Patients
            if ("admin".equals(source)) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/managePatients?message=updated"
                );

            } else {

                // Patient editing own profile
                response.sendRedirect(
                        request.getContextPath()
                        + "/patientProfile?id="
                        + patientId
                        + "&message=updated"
                );
            }

        } else {

            if ("admin".equals(source)) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/managePatients?error=update"
                );

            } else {

                response.sendRedirect(
                        request.getContextPath()
                        + "/patientProfile?id="
                        + patientId
                        + "&error=update"
                );
            }
        }
    }
}