package com.cliniccare.controller;

import com.cliniccare.dao.AppointmentDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

@WebServlet("/updateAppointmentStatus")
public class UpdateAppointmentStatusServlet extends HttpServlet {

    private static final Set<String> ALLOWED_STATUSES =
            Set.of("Pending", "Confirmed", "Completed", "Cancelled");

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
            String status = canonicalStatus(request.getParameter("status"));

            if (status == null || !ALLOWED_STATUSES.contains(status)) {
                redirect(request, response, patientId, "error=status");
                return;
            }

            boolean success = appointmentDAO.updateAppointmentStatus(
                    appointmentId,
                    status
            );

            redirect(request, response, patientId,
                    success ? "message=status" : "error=status");

        } catch (NumberFormatException e) {
            redirect(request, response, null, "error=status");
        }
    }

    private String canonicalStatus(String status) {
        if (status == null) {
            return null;
        }

        return switch (status.trim().toUpperCase()) {
            case "PENDING" -> "Pending";
            case "CONFIRMED" -> "Confirmed";
            case "COMPLETED" -> "Completed";
            case "CANCELLED", "CANCELED" -> "Cancelled";
            default -> null;
        };
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
