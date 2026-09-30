package com.cliniccare.controller;

import com.cliniccare.dao.DoctorAvailabilityDAO;
import com.cliniccare.dao.DoctorAvailabilityDAOImpl;
import com.cliniccare.dao.DoctorDAO;
import com.cliniccare.dao.DoctorDAOImpl;
import com.cliniccare.model.Doctor;
import com.cliniccare.model.DoctorAvailability;
import com.cliniccare.util.FormUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@WebServlet("/doctorAvailability")
public class DoctorAvailabilityServlet extends HttpServlet {

    private static final List<String> DAYS = Arrays.asList(
            "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday");

    private DoctorDAO doctorDAO;
    private DoctorAvailabilityDAO availabilityDAO;

    @Override
    public void init() {
        doctorDAO = new DoctorDAOImpl();
        availabilityDAO = new DoctorAvailabilityDAOImpl();
    }

    // Show one doctor's availability slots
    @Override
    protected void doGet(
            HttpServletRequest request,
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
        request.setAttribute("slots", availabilityDAO.getByDoctor(doctorId));

        request.getRequestDispatcher("doctorAvailability.jsp")
                .forward(request, response);
    }

    // Add or delete a slot
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int doctorId = FormUtil.parseId(request.getParameter("doctorId"));

        if (doctorDAO.getById(doctorId) == null) {

            response.sendRedirect(
                    request.getContextPath() + "/manageDoctors?error=notfound");

            return;
        }

        String base = request.getContextPath()
                + "/doctorAvailability?doctorId=" + doctorId;

        String action = request.getParameter("action");

        // =========================
        // DELETE SLOT
        // =========================
        if ("delete".equals(action)) {

            int slotId = FormUtil.parseId(request.getParameter("availabilityId"));

            boolean success = slotId > 0 && availabilityDAO.deleteSlot(slotId);

            response.sendRedirect(base
                    + (success ? "&message=deleted" : "&error=failed"));

            return;
        }

        // =========================
        // ADD SLOT
        // =========================
        String day = request.getParameter("dayOfWeek");

        LocalTime start;
        LocalTime end;

        try {
            start = LocalTime.parse(request.getParameter("startTime"));
            end = LocalTime.parse(request.getParameter("endTime"));
        } catch (Exception e) {
            response.sendRedirect(base + "&error=invalid");
            return;
        }

        if (day == null || !DAYS.contains(day)) {
            response.sendRedirect(base + "&error=invalid");
            return;
        }

        if (!end.isAfter(start)) {
            response.sendRedirect(base + "&error=time");
            return;
        }

        if (availabilityDAO.hasOverlap(doctorId, day, start, end)) {
            response.sendRedirect(base + "&error=overlap");
            return;
        }

        boolean success = availabilityDAO.addSlot(
                new DoctorAvailability(doctorId, day, start, end));

        response.sendRedirect(base
                + (success ? "&message=added" : "&error=failed"));
    }
}
