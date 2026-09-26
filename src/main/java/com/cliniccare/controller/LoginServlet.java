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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

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

        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");

        Patient patient =
                patientDAO.getPatientByEmail(email);

        if (patient != null &&
                PasswordUtil.checkPassword(
                        password,
                        patient.getPassword())) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/patientProfile?id="
                    + patient.getPatientId()
            );

        } else {

            request.setAttribute(
                    "error",
                    "Invalid email or password."
            );

            request.getRequestDispatcher(
                    "login.jsp"
            ).forward(request, response);
        }
    }
}