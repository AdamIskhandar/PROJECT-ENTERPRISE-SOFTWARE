package com.cliniccare.controller;

import com.cliniccare.dao.DoctorDAO;
import com.cliniccare.dao.DoctorDAOImpl;
import com.cliniccare.util.FormUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteDoctor")
public class DeleteDoctorServlet extends HttpServlet {

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

    // Delete is a POST so a link or a crawler cannot delete data by accident
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id = FormUtil.parseId(request.getParameter("id"));

        boolean success = id > 0 && doctorDAO.delete(id);

        if (success) {
            response.sendRedirect(
                    request.getContextPath() + "/manageDoctors?message=deleted");
        } else {
            response.sendRedirect(
                    request.getContextPath() + "/manageDoctors?error=delete");
        }
    }
}
