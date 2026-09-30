package com.cliniccare.controller;

import com.cliniccare.dao.DoctorDAO;
import com.cliniccare.dao.DoctorDAOImpl;
import com.cliniccare.model.Doctor;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/manageDoctors")
public class ManageDoctorsServlet extends HttpServlet {

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

        String keyword = request.getParameter("keyword");

        List<Doctor> doctors;

        if (keyword != null && !keyword.trim().isEmpty()) {
            doctors = doctorDAO.search(keyword.trim());
        } else {
            doctors = doctorDAO.getAll();
        }

        request.setAttribute("doctors", doctors);

        request.getRequestDispatcher("manageDoctors.jsp")
                .forward(request, response);
    }
}
