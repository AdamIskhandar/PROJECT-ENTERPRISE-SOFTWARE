package com.cliniccare.controller;

import com.cliniccare.dao.AppointmentDAO;
import com.cliniccare.dao.PatientDAO;
import com.cliniccare.model.Appointment;
import com.cliniccare.model.Patient;
import com.cliniccare.model.ScheduleOption;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        String role =
                getRole(session);


        // =========================
        // PATIENT BOOKING
        // =========================
        if ("PATIENT".equals(role)) {

            Patient patient =
                    getLoggedInPatient(session);

            if (patient == null) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/login.jsp"
                );

                return;
            }

            request.setAttribute(
                    "patientId",
                    String.valueOf(
                            patient.getPatientId()
                    )
            );

            request.setAttribute(
                    "source",
                    "patient"
            );
        }


        // =========================
        // ADMIN BOOKING
        // =========================
        else if ("ADMIN".equals(role)) {

            request.setAttribute(
                    "patientId",
                    request.getParameter("patientId")
            );

            request.setAttribute(
                    "source",
                    "admin"
            );
        }


        // =========================
        // NOT LOGGED IN
        // =========================
        else {

            String source =
                    request.getParameter("source");

            if ("admin".equalsIgnoreCase(source)) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/adminLogin.jsp"
                );

            } else {

                response.sendRedirect(
                        request.getContextPath()
                        + "/login.jsp"
                );
            }

            return;
        }


        loadSchedules(request);

        request.getRequestDispatcher(
                "/bookAppointment.jsp"
        ).forward(
                request,
                response
        );
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        String role =
                getRole(session);


        // =========================
        // CHECK LOGIN
        // =========================

        if (!"PATIENT".equals(role)
                && !"ADMIN".equals(role)) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/login.jsp"
            );

            return;
        }


        boolean patientMode =
                "PATIENT".equals(role);


        try {

            int patientId;


            // =========================
            // PATIENT
            // =========================

            if (patientMode) {

                Patient patient =
                        getLoggedInPatient(session);

                if (patient == null) {

                    response.sendRedirect(
                            request.getContextPath()
                            + "/login.jsp"
                    );

                    return;
                }

                // Patient ID comes from session
                patientId =
                        patient.getPatientId();
            }


            // =========================
            // ADMIN
            // =========================

            else {

                patientId =
                        Integer.parseInt(
                                request.getParameter(
                                        "patientId"
                                )
                        );
            }


            int scheduleId =
                    Integer.parseInt(
                            request.getParameter(
                                    "scheduleId"
                            )
                    );


            String reason =
                    cleanReason(
                            request.getParameter(
                                    "reason"
                            )
                    );


            // =========================
            // CHECK PATIENT
            // =========================

            if (patientDAO.getPatientById(
                    patientId) == null) {

                forwardWithError(
                        request,
                        response,
                        "Patient does not exist."
                );

                return;
            }


            // =========================
            // GET SCHEDULE
            // =========================

            ScheduleOption schedule =
                    appointmentDAO
                            .getScheduleById(
                                    scheduleId
                            );


            if (schedule == null) {

                forwardWithError(
                        request,
                        response,
                        "Invalid doctor schedule."
                );

                return;
            }


            // =========================
            // CHECK DATE
            // =========================

            LocalDateTime appointmentDateTime =
                    LocalDateTime.of(
                            schedule.getAvailableDate(),
                            schedule.getStartTime()
                    );


            if (!appointmentDateTime
                    .isAfter(LocalDateTime.now())) {

                forwardWithError(
                        request,
                        response,
                        "Please select a future appointment."
                );

                return;
            }


            // =========================
            // CHECK AVAILABILITY
            // =========================

            if (!appointmentDAO
                    .isScheduleAvailableForBooking(
                            scheduleId,
                            null
                    )) {

                forwardWithError(
                        request,
                        response,
                        "This schedule has already been booked."
                );

                return;
            }


            // =========================
            // CREATE APPOINTMENT
            // =========================

            Appointment appointment =
                    buildAppointment(
                            0,
                            patientId,
                            schedule,
                            reason,
                            "Pending"
                    );


            boolean success =
                    appointmentDAO
                            .addAppointment(
                                    appointment
                            );


            // =========================
            // REDIRECT
            // =========================

            if (success) {

                if (patientMode) {

                    response.sendRedirect(
                            request.getContextPath()
                            + "/myAppointments"
                            + "?message=booked"
                    );

                } else {

                    response.sendRedirect(
                            request.getContextPath()
                            + "/manageAppointments"
                            + "?message=booked"
                    );
                }

            } else {

                forwardWithError(
                        request,
                        response,
                        "Unable to book appointment."
                );
            }


        } catch (NumberFormatException e) {

            forwardWithError(
                    request,
                    response,
                    "Please select a valid schedule."
            );
        }
    }


    // =========================
    // GET ROLE
    // =========================

    private String getRole(
            HttpSession session) {

        if (session == null) {
            return null;
        }

        Object role =
                session.getAttribute(
                        "userRole"
                );

        return role == null
                ? null
                : role.toString();
    }


    // =========================
    // GET PATIENT
    // =========================

    private Patient getLoggedInPatient(
            HttpSession session) {

        if (session == null) {
            return null;
        }

        Object patient =
                session.getAttribute(
                        "loggedInPatient"
                );

        if (patient instanceof Patient) {

            return (Patient) patient;
        }

        return null;
    }


    // =========================
    // BUILD APPOINTMENT
    // =========================

    private Appointment buildAppointment(
            int appointmentId,
            int patientId,
            ScheduleOption schedule,
            String reason,
            String status) {

        Appointment appointment =
                new Appointment();


        appointment.setAppointmentId(
                appointmentId
        );

        appointment.setPatientId(
                patientId
        );

        appointment.setScheduleId(
                schedule.getScheduleId()
        );

        appointment.setDoctorId(
                schedule.getDoctorId()
        );

        appointment.setDoctorName(
                schedule.getDoctorName()
        );

        appointment.setSpecialization(
                schedule.getSpecialization()
        );

        appointment.setAppointmentDate(
                schedule.getAvailableDate()
        );

        appointment.setStartTime(
                schedule.getStartTime()
        );

        appointment.setEndTime(
                schedule.getEndTime()
        );

        appointment.setRateType(
                schedule.getRateType()
        );

        appointment.setRateUsed(
                schedule.getRateUsed()
        );

        appointment.setDurationHours(
                schedule.getDurationHours()
        );

        appointment.setTotalCharge(
                schedule.getEstimatedCharge()
        );

        appointment.setReason(
                reason
        );

        appointment.setStatus(
                status
        );


        return appointment;
    }


    // =========================
    // LOAD SCHEDULE
    // =========================

    private void loadSchedules(
            HttpServletRequest request) {

        request.setAttribute(
                "schedules",
                appointmentDAO
                        .getAvailableSchedules()
        );
    }


    // =========================
    // ERROR
    // =========================

    private void forwardWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            String error)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        String role =
                getRole(session);


        if ("PATIENT".equals(role)) {

            Patient patient =
                    getLoggedInPatient(session);

            if (patient != null) {

                request.setAttribute(
                        "patientId",
                        String.valueOf(
                                patient.getPatientId()
                        )
                );
            }

            request.setAttribute(
                    "source",
                    "patient"
            );

        } else if ("ADMIN".equals(role)) {

            request.setAttribute(
                    "source",
                    "admin"
            );
        }


        request.setAttribute(
                "error",
                error
        );


        loadSchedules(request);


        request.getRequestDispatcher(
                "/bookAppointment.jsp"
        ).forward(
                request,
                response
        );
    }


    // =========================
    // CLEAN REASON
    // =========================

    private String cleanReason(
            String reason) {

        if (reason == null) {
            return null;
        }

        String value =
                reason.trim();


        if (value.isEmpty()) {
            return null;
        }


        if (value.length() > 255) {

            return value.substring(
                    0,
                    255
            );
        }


        return value;
    }
}