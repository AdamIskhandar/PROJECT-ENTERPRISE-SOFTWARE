package com.cliniccare.controller;

import com.cliniccare.dao.PatientDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deletePatient")
public class DeletePatientServlet extends HttpServlet {

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

        try {

            int patientId =
                    Integer.parseInt(
                            request.getParameter("id")
                    );

            boolean success =
                    patientDAO.deletePatient(patientId);

            if (success) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/managePatients?message=deleted"
                );

            } else {

                response.sendRedirect(
                        request.getContextPath()
                        + "/managePatients?error=delete"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                    + "/managePatients?error=delete"
            );
        }
    }
}