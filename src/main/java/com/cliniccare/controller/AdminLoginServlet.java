package com.cliniccare.controller;

import com.cliniccare.dao.AdminDAO;
import com.cliniccare.model.Admin;
import com.cliniccare.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/adminLogin")
public class AdminLoginServlet extends HttpServlet {

    private AdminDAO adminDAO;

    @Override
    public void init() {
        adminDAO = new AdminDAO();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");

        Admin admin =
                adminDAO.getAdminByEmail(email);

        if (admin != null &&
                PasswordUtil.checkPassword(
                        password,
                        admin.getPassword())) {

            request.getSession().setAttribute("userRole", "ADMIN");
            request.getSession().setAttribute("loggedInAdmin", admin);

            response.sendRedirect(
                    request.getContextPath()
                    + "/adminDashboard.jsp"
            );

        } else {

            request.setAttribute(
                    "error",
                    "Invalid admin email or password."
            );

            request.getRequestDispatcher(
                    "adminLogin.jsp"
            ).forward(request, response);
        }
    }
}