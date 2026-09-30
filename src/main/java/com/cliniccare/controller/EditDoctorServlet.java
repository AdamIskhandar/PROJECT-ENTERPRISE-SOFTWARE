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

@WebServlet("/editDoctor")
public class EditDoctorServlet extends HttpServlet {

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

        int id = FormUtil.parseId(request.getParameter("id"));

        Doctor doctor = doctorDAO.getById(id);

        if (doctor == null) {

            response.sendRedirect(
                    request.getContextPath() + "/manageDoctors?error=notfound");

            return;
        }

        request.setAttribute("doctor", doctor);

        request.getRequestDispatcher("editDoctor.jsp")
                .forward(request, response);
    }
}
