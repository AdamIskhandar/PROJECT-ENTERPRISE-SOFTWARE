package com.cliniccare.controller;

import com.cliniccare.dao.PatientDAO;
import com.cliniccare.model.Patient;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/managePatients")
public class ManagePatientsServlet extends HttpServlet {

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

        String keyword =
                request.getParameter("keyword");

        List<Patient> patients;

        if (keyword != null &&
                !keyword.trim().isEmpty()) {

            patients =
                    patientDAO.searchPatients(
                            keyword.trim()
                    );

        } else {

            patients =
                    patientDAO.getAllPatients();
        }

        request.setAttribute(
                "patients",
                patients
        );

        request.getRequestDispatcher(
                "managePatients.jsp"
        ).forward(request, response);
    }
}