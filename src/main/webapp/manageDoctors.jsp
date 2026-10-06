<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.Doctor" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>
<%
    List<Doctor> doctors = (List<Doctor>) request.getAttribute("doctors");
    if (doctors == null) {
        response.sendRedirect("manageDoctors");
        return;
    }

    String message = request.getParameter("message");
    String error = request.getParameter("error");
    String keyword = request.getParameter("keyword");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Doctors - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=4">
</head>
<body>
<%@ include file="nav.jsp" %>
<div class="page">
    <div class="page-header">
        <div>
            <span class="eyebrow">Doctor Records</span>
            <h1>Manage Doctors</h1>
            <p>Add, update, search and manage doctor schedules.</p>
        </div>
        <a class="btn btn-primary" href="addDoctor">+ Add Doctor</a>
    </div>

    <div class="info-card" style="margin-bottom:20px;">
        <strong>Clinic Fixed Rates</strong>
        <div class="muted" style="margin-top:6px;">Morning: RM60.00/hour · Night: RM90.00/hour. These rates are stored automatically in the fees table for each doctor.</div>
    </div>

    <% if ("added".equals(message)) { %><div class="success">Doctor added successfully.</div><% } %>
    <% if ("updated".equals(message)) { %><div class="success">Doctor information updated successfully.</div><% } %>
    <% if ("deleted".equals(message)) { %><div class="success">Doctor deleted successfully.</div><% } %>
    <% if ("delete".equals(error)) { %><div class="error">Failed to delete doctor.</div><% } %>
    <% if ("notfound".equals(error)) { %><div class="error">Doctor not found.</div><% } %>

    <div class="panel" style="padding:20px;">
        <form action="manageDoctors" method="get" class="toolbar">
            <input type="text" name="keyword" value="<%= HtmlUtil.escape(keyword) %>"
                   placeholder="Search doctor name, specialization, email or phone">
            <button type="submit">Search</button>
            <a class="button" href="manageDoctors">Show All</a>
        </form>

        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Doctor</th>
                    <th>Contact</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% if (doctors.isEmpty()) { %>
                    <tr><td colspan="4" class="empty">No doctor records found.</td></tr>
                <% } else { for (Doctor doctor : doctors) { %>
                    <tr>
                        <td>#<%= doctor.getDoctorId() %></td>
                        <td>
                            <strong><%= HtmlUtil.escape(doctor.getDoctorName()) %></strong><br>
                            <span class="muted"><%= HtmlUtil.escape(doctor.getSpecialization()) %></span>
                        </td>
                        <td>
                            <%= HtmlUtil.escape(doctor.getEmail()) %><br>
                            <span class="muted"><%= HtmlUtil.escape(doctor.getPhone()) %></span>
                        </td>
                        <td>
                            <a class="action-link" href="doctorAvailability?doctorId=<%= doctor.getDoctorId() %>">Schedules</a>
                            <a class="action-link" href="editDoctor?id=<%= doctor.getDoctorId() %>">Edit</a>
                            <form action="deleteDoctor" method="post" class="inline-form"
                                  onsubmit="return confirm('Delete this doctor? Related fee and schedule records will also be removed.');">
                                <input type="hidden" name="id" value="<%= doctor.getDoctorId() %>">
                                <button type="submit" class="link-button">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% } } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
