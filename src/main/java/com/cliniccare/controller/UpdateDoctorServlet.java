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

@WebServlet("/updateDoctor")
public class UpdateDoctorServlet extends HttpServlet {

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

        response.sendRedirect(request.getContextPath() + "/manageDoctors");
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Doctor doctor = FormUtil.doctorFromRequest(request);

        if (doctor.getDoctorId() <= 0
                || doctorDAO.getById(doctor.getDoctorId()) == null) {

            response.sendRedirect(
                    request.getContextPath() + "/manageDoctors?error=notfound");

            return;
        }

        String error = FormUtil.validateDoctor(request);

        if (error == null
                && doctorDAO.emailExists(doctor.getEmail(), doctor.getDoctorId())) {
            error = "Email is already used by another doctor.";
        }

        if (error == null && !doctorDAO.update(doctor)) {
            error = "Failed to update doctor.";
        }

        if (error != null) {

            request.setAttribute("error", error);
            request.setAttribute("doctor", doctor);

            request.getRequestDispatcher("editDoctor.jsp")
                    .forward(request, response);

            return;
        }

        response.sendRedirect(
                request.getContextPath() + "/manageDoctors?message=updated");
    }
}
