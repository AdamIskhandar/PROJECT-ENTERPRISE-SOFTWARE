package com.cliniccare.controller;

import com.cliniccare.dao.AppointmentDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/cancelAppointment")
public class CancelAppointmentServlet extends HttpServlet {

    private AppointmentDAO appointmentDAO;

    @Override
    public void init() {
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            Integer patientId = parseOptionalInt(request.getParameter("patientId"));

            boolean success = appointmentDAO.cancelAppointment(appointmentId);
            redirect(request, response, patientId,
                    success ? "message=cancelled" : "error=cancel");

        } catch (NumberFormatException e) {
            redirect(request, response, null, "error=cancel");
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
