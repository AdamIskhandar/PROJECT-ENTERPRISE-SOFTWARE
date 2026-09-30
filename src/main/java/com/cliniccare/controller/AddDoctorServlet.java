package com.cliniccare.controller;

import com.cliniccare.dao.DoctorDAO;
import com.cliniccare.dao.DoctorDAOImpl;
import com.cliniccare.model.Doctor;
import com.cliniccare.util.FormUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/addDoctor")
public class AddDoctorServlet extends HttpServlet {

    private DoctorDAO doctorDAO;

    @Override
    public void init() {
        doctorDAO = new DoctorDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(request.getContextPath() + "/addDoctor.jsp");
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Keep what the user typed so the form can be refilled on error
        Doctor doctor = FormUtil.doctorFromRequest(request);

        String error = FormUtil.validateDoctor(request);

        if (error == null && doctorDAO.emailExists(doctor.getEmail(), 0)) {
            error = "Email is already used by another doctor.";
        }

        if (error == null && !doctorDAO.add(doctor)) {
            error = "Failed to add doctor.";
        }

        if (error != null) {

            request.setAttribute("error", error);
            request.setAttribute("doctor", doctor);

            request.getRequestDispatcher("addDoctor.jsp")
                    .forward(request, response);

            return;
        }

        response.sendRedirect(
                request.getContextPath() + "/manageDoctors?message=added");
    }
}
