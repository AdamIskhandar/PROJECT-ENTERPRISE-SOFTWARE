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

@WebServlet("/bookAppointment")
public class BookAppointmentServlet extends HttpServlet {

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

        loadSchedules(request);
        request.setAttribute("patientId", request.getParameter("patientId"));
        request.getRequestDispatcher("/bookAppointment.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int patientId = Integer.parseInt(request.getParameter("patientId"));
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
            String reason = cleanReason(request.getParameter("reason"));

            request.setAttribute("patientId", String.valueOf(patientId));

            if (patientDAO.getPatientById(patientId) == null) {
                forwardWithError(request, response, "Patient ID does not exist.");
                return;
            }

            ScheduleOption schedule = appointmentDAO.getScheduleById(scheduleId);
            if (schedule == null) {
                forwardWithError(request, response, "The selected doctor schedule is invalid.");
                return;
            }

            LocalDateTime appointmentDateTime = LocalDateTime.of(
                    schedule.getAvailableDate(),
                    schedule.getStartTime()
            );

            if (!appointmentDateTime.isAfter(LocalDateTime.now())) {
                forwardWithError(request, response, "Please select a future appointment schedule.");
                return;
            }

            if (!appointmentDAO.isScheduleAvailableForBooking(scheduleId, null)) {
                forwardWithError(
                        request,
                        response,
                        "That schedule has already been booked. Please choose another available schedule."
                );
                return;
            }

            Appointment appointment = buildAppointment(
                    0,
                    patientId,
                    schedule,
                    reason,
                    "Pending"
            );

            if (appointmentDAO.addAppointment(appointment)) {
                response.sendRedirect(
                        request.getContextPath()
                                + "/manageAppointments?patientId="
                                + patientId
                                + "&message=booked"
                );
            } else {
                forwardWithError(
                        request,
                        response,
                        "Unable to book the appointment. The selected schedule may no longer be available."
                );
            }

        } catch (NumberFormatException e) {
            forwardWithError(
                    request,
                    response,
                    "Please enter a valid patient ID and select an available schedule."
            );
        }
    }

    private Appointment buildAppointment(int appointmentId,
                                         int patientId,
                                         ScheduleOption schedule,
                                         String reason,
                                         String status) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(appointmentId);
        appointment.setPatientId(patientId);
        appointment.setScheduleId(schedule.getScheduleId());
        appointment.setDoctorId(schedule.getDoctorId());
        appointment.setDoctorName(schedule.getDoctorName());
        appointment.setSpecialization(schedule.getSpecialization());
        appointment.setAppointmentDate(schedule.getAvailableDate());
        appointment.setStartTime(schedule.getStartTime());
        appointment.setEndTime(schedule.getEndTime());
        appointment.setRateType(schedule.getRateType());
        appointment.setRateUsed(schedule.getRateUsed());
        appointment.setDurationHours(schedule.getDurationHours());
        appointment.setTotalCharge(schedule.getEstimatedCharge());
        appointment.setReason(reason);
        appointment.setStatus(status);
        return appointment;
    }

    private void loadSchedules(HttpServletRequest request) {
        request.setAttribute("schedules", appointmentDAO.getAvailableSchedules());
    }

    private void forwardWithError(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        loadSchedules(request);
        request.getRequestDispatcher("/bookAppointment.jsp")
                .forward(request, response);
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
