package com.cliniccare.controller;

import com.cliniccare.dao.AppointmentDAO;
import com.cliniccare.model.Appointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/manageAppointments")
public class ManageAppointmentsServlet extends HttpServlet {

    private AppointmentDAO appointmentDAO;

    @Override
    public void init() {
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        Integer patientId = parseOptionalInt(request.getParameter("patientId"));

        List<Appointment> appointments = appointmentDAO.findAppointments(
                keyword,
                status,
                patientId
        );

        request.setAttribute("appointments", appointments);
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.setAttribute("status", status == null ? "" : status);
        request.setAttribute("patientId", patientId);

        request.getRequestDispatcher("/manageAppointments.jsp")
                .forward(request, response);
    }

    private Integer parseOptionalInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
