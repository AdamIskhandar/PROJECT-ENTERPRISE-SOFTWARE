package com.cliniccare.controller;

import com.cliniccare.dao.MedicalServiceDAO;
import com.cliniccare.dao.MedicalServiceDAOImpl;
import com.cliniccare.model.MedicalService;
import com.cliniccare.util.FormUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/updateService")
public class UpdateServiceServlet extends HttpServlet {

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

        request.setCharacterEncoding("UTF-8");

        MedicalService service = FormUtil.serviceFromRequest(request);

        if (service.getServiceId() <= 0
                || serviceDAO.getById(service.getServiceId()) == null) {

            response.sendRedirect(
                    request.getContextPath() + "/manageServices?error=notfound");

            return;
        }

        String error = FormUtil.validateService(request);

        if (error == null
                && serviceDAO.nameExists(service.getServiceName(), service.getServiceId())) {
            error = "A service with this name already exists.";
        }

        if (error == null && !serviceDAO.update(service)) {
            error = "Failed to update service.";
        }

        if (error != null) {

            request.setAttribute("error", error);
            request.setAttribute("service", service);

            request.getRequestDispatcher("editService.jsp")
                    .forward(request, response);

            return;
        }

        response.sendRedirect(
                request.getContextPath() + "/manageServices?message=updated");
    }
}
