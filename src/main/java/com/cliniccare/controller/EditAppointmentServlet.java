package com.cliniccare.controller;

import com.cliniccare.dao.AppointmentDAO;
import com.cliniccare.dao.PatientDAO;
import com.cliniccare.model.Appointment;
import com.cliniccare.model.ScheduleOption;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/editAppointment")
public class EditAppointmentServlet extends HttpServlet {

    private AppointmentDAO appointmentDAO;
    private PatientDAO patientDAO;

    @Override
    public void init() {
        appointmentDAO = new AppointmentDAO();
        patientDAO = new PatientDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int appointmentId = Integer.parseInt(request.getParameter("id"));
            Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);

            if (appointment == null) {
                redirectNotFound(request, response);
                return;
            }

            request.setAttribute("appointment", appointment);
            loadSchedules(request, appointment.getScheduleId());
            request.getRequestDispatcher("/editAppointment.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            redirectNotFound(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            int patientId = Integer.parseInt(request.getParameter("patientId"));
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
            String reason = cleanReason(request.getParameter("reason"));

            Appointment existing = appointmentDAO.getAppointmentById(appointmentId);
            if (existing == null) {
                redirectNotFound(request, response);
                return;
            }

            if ("Cancelled".equalsIgnoreCase(existing.getStatus())
                    || "Completed".equalsIgnoreCase(existing.getStatus())) {
                forwardWithError(
                        request,
                        response,
                        existing,
                        "Completed or cancelled appointments cannot be edited."
                );
                return;
            }

            if (patientDAO.getPatientById(patientId) == null) {
                forwardWithError(
                        request,
                        response,
                        existing,
                        "Patient ID does not exist."
                );
                return;
            }

            ScheduleOption schedule = appointmentDAO.getScheduleById(scheduleId);
            if (schedule == null) {
                forwardWithError(
                        request,
                        response,
                        existing,
                        "The selected doctor schedule is invalid."
                );
                return;
            }

            LocalDateTime appointmentDateTime = LocalDateTime.of(
                    schedule.getAvailableDate(),
                    schedule.getStartTime()
            );

            if (!appointmentDateTime.isAfter(LocalDateTime.now())) {
                forwardWithError(
                        request,
                        response,
                        existing,
                        "Please select a future appointment schedule."
                );
                return;
            }

            if (!appointmentDAO.isScheduleAvailableForBooking(
                    scheduleId,
                    appointmentId)) {
                forwardWithError(
                        request,
                        response,
                        existing,
                        "That schedule has already been booked. Please choose another available schedule."
                );
                return;
            }

            Appointment updated = new Appointment();
            updated.setAppointmentId(appointmentId);
            updated.setPatientId(patientId);
            updated.setScheduleId(schedule.getScheduleId());
            updated.setDoctorId(schedule.getDoctorId());
            updated.setDoctorName(schedule.getDoctorName());
            updated.setSpecialization(schedule.getSpecialization());
            updated.setAppointmentDate(schedule.getAvailableDate());
            updated.setStartTime(schedule.getStartTime());
            updated.setEndTime(schedule.getEndTime());
            updated.setRateType(schedule.getRateType());
            updated.setRateUsed(schedule.getRateUsed());
            updated.setDurationHours(schedule.getDurationHours());
            updated.setTotalCharge(schedule.getEstimatedCharge());
            updated.setReason(reason);
            updated.setStatus(existing.getStatus());

            if (appointmentDAO.updateAppointment(updated)) {
                response.sendRedirect(
                        request.getContextPath()
                                + "/manageAppointments?patientId="
                                + patientId
                                + "&message=updated"
                );
            } else {
                forwardWithError(
                        request,
                        response,
                        existing,
                        "Unable to update the appointment. The selected schedule may no longer be available."
                );
            }

        } catch (NumberFormatException e) {
            redirectNotFound(request, response);
        }
    }

    private void loadSchedules(HttpServletRequest request, int currentScheduleId) {
        request.setAttribute(
                "schedules",
                appointmentDAO.getSchedulesForEdit(currentScheduleId)
        );
    }

    private void forwardWithError(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Appointment appointment,
                                  String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute("appointment", appointment);
        loadSchedules(request, appointment.getScheduleId());
        request.getRequestDispatcher("/editAppointment.jsp")
                .forward(request, response);
    }

    private void redirectNotFound(HttpServletRequest request,
                                  HttpServletResponse response)
            throws IOException {
        response.sendRedirect(
                request.getContextPath() + "/manageAppointments?error=notfound"
        );
    }

    private String cleanReason(String reason) {
        if (reason == null) {
            return null;
        }

        String value = reason.trim();
        if (value.isEmpty()) {
            return null;
        }

        return value.length() > 255 ? value.substring(0, 255) : value;
    }
}
