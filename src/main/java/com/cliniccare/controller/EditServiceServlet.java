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

@WebServlet("/editService")
public class EditServiceServlet extends HttpServlet {

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

        int id = FormUtil.parseId(request.getParameter("id"));

        MedicalService service = serviceDAO.getById(id);

        if (service == null) {

            response.sendRedirect(
                    request.getContextPath() + "/manageServices?error=notfound");

            return;
        }

        request.setAttribute("service", service);

        request.getRequestDispatcher("editService.jsp")
                .forward(request, response);
    }
}
