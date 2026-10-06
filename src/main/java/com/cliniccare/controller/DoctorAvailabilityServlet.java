package com.cliniccare.controller;

import com.cliniccare.dao.DoctorDAO;
import com.cliniccare.dao.DoctorDAOImpl;
import com.cliniccare.dao.SchedulingDAO;
import com.cliniccare.dao.SchedulingDAOImpl;
import com.cliniccare.model.Doctor;
import com.cliniccare.model.Scheduling;
import com.cliniccare.util.FormUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@WebServlet("/doctorAvailability")
public class DoctorAvailabilityServlet extends HttpServlet {

    private DoctorDAO doctorDAO;
    private SchedulingDAO schedulingDAO;

    @Override
    public void init() {
        doctorDAO = new DoctorDAOImpl();
        schedulingDAO = new SchedulingDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        int doctorId = FormUtil.parseId(request.getParameter("doctorId"));
        Doctor doctor = doctorDAO.getById(doctorId);

        if (doctor == null) {
            response.sendRedirect(
                    request.getContextPath() + "/manageDoctors?error=notfound");
            return;
        }

        request.setAttribute("doctor", doctor);
        request.setAttribute("slots", schedulingDAO.getByDoctor(doctorId));
        request.getRequestDispatcher("/doctorAvailability.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int doctorId = FormUtil.parseId(request.getParameter("doctorId"));

        if (doctorDAO.getById(doctorId) == null) {
            response.sendRedirect(
                    request.getContextPath() + "/manageDoctors?error=notfound");
            return;
        }

        String base = request.getContextPath()
                + "/doctorAvailability?doctorId=" + doctorId;

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int scheduleId = FormUtil.parseId(request.getParameter("scheduleId"));
            boolean success = scheduleId > 0
                    && schedulingDAO.deleteSchedule(scheduleId);

            response.sendRedirect(base
                    + (success ? "&message=deleted" : "&error=delete"));
            return;
        }

        LocalDate availableDate;
        LocalTime startTime;
        LocalTime endTime;

        try {
            availableDate = LocalDate.parse(request.getParameter("availableDate"));
            startTime = LocalTime.parse(request.getParameter("startTime"));
            endTime = LocalTime.parse(request.getParameter("endTime"));
        } catch (Exception e) {
            response.sendRedirect(base + "&error=invalid");
            return;
        }

        if (!endTime.isAfter(startTime)) {
            response.sendRedirect(base + "&error=time");
            return;
        }

        if (availableDate.isBefore(LocalDate.now())) {
            response.sendRedirect(base + "&error=past");
            return;
        }

        if (availableDate.equals(LocalDate.now())
                && !LocalDateTime.of(availableDate, startTime)
                .isAfter(LocalDateTime.now())) {
            response.sendRedirect(base + "&error=past");
            return;
        }

        if (schedulingDAO.hasOverlap(
                doctorId, availableDate, startTime, endTime)) {
            response.sendRedirect(base + "&error=overlap");
            return;
        }

        Scheduling schedule = new Scheduling(
                doctorId,
                availableDate,
                startTime,
                endTime,
                "Available"
        );

        boolean success = schedulingDAO.addSchedule(schedule);

        response.sendRedirect(base
                + (success ? "&message=added" : "&error=failed"));
    }
}
