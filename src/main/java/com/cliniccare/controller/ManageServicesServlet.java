package com.cliniccare.controller;

import com.cliniccare.dao.MedicalServiceDAO;
import com.cliniccare.dao.MedicalServiceDAOImpl;
import com.cliniccare.model.MedicalService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/manageServices")
public class ManageServicesServlet extends HttpServlet {

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

        String keyword = request.getParameter("keyword");

        List<MedicalService> services;

        if (keyword != null && !keyword.trim().isEmpty()) {
            services = serviceDAO.search(keyword.trim());
        } else {
            services = serviceDAO.getAll();
        }

        request.setAttribute("services", services);

        request.getRequestDispatcher("manageServices.jsp")
                .forward(request, response);
    }
}
