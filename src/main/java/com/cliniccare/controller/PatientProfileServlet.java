package com.cliniccare.controller;

import com.cliniccare.dao.PatientDAO;
import com.cliniccare.model.Patient;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/patientProfile")
public class PatientProfileServlet extends HttpServlet {

    private PatientDAO patientDAO;

    @Override
    public void init() {
        patientDAO = new PatientDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int patientId = Integer.parseInt(id);

            Patient patient =
                    patientDAO.getPatientById(patientId);

            if (patient != null) {

                request.setAttribute(
                        "patient",
                        patient
                );

                request.getRequestDispatcher(
                        "patientProfile.jsp"
                ).forward(request, response);

            } else {

                response.sendRedirect("login.jsp");
            }

        } catch (NumberFormatException e) {

            response.sendRedirect("login.jsp");
        }
    }
}