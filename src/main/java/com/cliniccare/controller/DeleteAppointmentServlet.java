package com.cliniccare.controller;

import com.cliniccare.dao.AppointmentDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/deleteAppointment")
public class DeleteAppointmentServlet extends HttpServlet {

    private AppointmentDAO appointmentDAO;

    @Override
    public void init() {
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Only administrators can perform this action.");
            return;
        }

        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            Integer patientId = parseOptionalInt(request.getParameter("patientId"));

            boolean success = appointmentDAO.deleteAppointment(appointmentId);
            redirect(request, response, patientId,
                    success ? "message=deleted" : "error=delete");

        } catch (NumberFormatException e) {
            redirect(request, response, null, "error=delete");
        }
    }

    private void redirect(HttpServletRequest request,
                          HttpServletResponse response,
                          Integer patientId,
                          String result)
            throws IOException {
        String url = request.getContextPath() + "/manageAppointments?" + result;
        if (patientId != null) {
            url += "&patientId=" + patientId;
        }
        response.sendRedirect(url);
    }

    private Integer parseOptionalInt(String value) {
        try {
            return value == null || value.isBlank() ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
