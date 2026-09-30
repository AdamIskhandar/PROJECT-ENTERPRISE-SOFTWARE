package com.cliniccare.controller;

import com.cliniccare.dao.MedicalServiceDAO;
import com.cliniccare.dao.MedicalServiceDAOImpl;
import com.cliniccare.util.FormUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteService")
public class DeleteServiceServlet extends HttpServlet {

    private MedicalServiceDAO serviceDAO;

    @Override
    public void init() {
        serviceDAO = new MedicalServiceDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(request.getContextPath() + "/manageServices");
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int id = FormUtil.parseId(request.getParameter("id"));

        boolean success = id > 0 && serviceDAO.delete(id);

        if (success) {
            response.sendRedirect(
                    request.getContextPath() + "/manageServices?message=deleted");
        } else {
            response.sendRedirect(
                    request.getContextPath() + "/manageServices?error=delete");
        }
    }
}
