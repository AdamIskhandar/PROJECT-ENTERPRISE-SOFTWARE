package com.cliniccare.controller;

import com.cliniccare.dao.AppointmentDAO;
import com.cliniccare.model.Appointment;
import com.cliniccare.model.Patient;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/myAppointments")
public class MyAppointmentsServlet extends HttpServlet {

    private AppointmentDAO appointmentDAO;

    @Override
    public void init() {
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                !"PATIENT".equals(
                        session.getAttribute("userRole"))) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/login.jsp"
            );

            return;
        }

        Patient patient =
                (Patient) session.getAttribute(
                        "loggedInPatient"
                );

        if (patient == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/login.jsp"
            );

            return;
        }

        String keyword =
                request.getParameter("keyword");

        String status =
                request.getParameter("status");

        List<Appointment> appointments =
                appointmentDAO.findAppointments(
                        keyword,
                        status,
                        patient.getPatientId()
                );

        request.setAttribute(
                "appointments",
                appointments
        );

        request.setAttribute(
                "keyword",
                keyword == null ? "" : keyword
        );

        request.setAttribute(
                "status",
                status == null ? "" : status
        );

        request.setAttribute(
                "patient",
                patient
        );

        request.getRequestDispatcher(
                "/myAppointments.jsp"
        ).forward(request, response);
    }
}