<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.Patient" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>
<%
    List<Patient> patients = (List<Patient>) request.getAttribute("patients");
    String message = request.getParameter("message");
    String error = request.getParameter("error");
    String keyword = request.getParameter("keyword");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Patients - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=3">
</head>
<body>
<%@ include file="nav.jsp" %>
<div class="page">
    <div class="page-header">
        <div>
            <span class="eyebrow">Patient Records</span>
            <h1>Manage Patients</h1>
            <p>Create, search and maintain patient information.</p>
        </div>
        <a class="btn btn-primary" href="addPatient.jsp">+ Add Patient</a>
    </div>

    <% if ("added".equals(message)) { %><div class="success">Patient added successfully.</div><% } %>
    <% if ("updated".equals(message)) { %><div class="success">Patient updated successfully.</div><% } %>
    <% if ("deleted".equals(message)) { %><div class="success">Patient deleted successfully.</div><% } %>
    <% if ("update".equals(error)) { %><div class="error">Failed to update patient.</div><% } %>
    <% if ("delete".equals(error)) { %><div class="error">Failed to delete patient.</div><% } %>

    <div class="panel" style="padding:20px;">
        <form action="managePatients" method="get" class="toolbar">
            <input type="text" name="keyword" value="<%= HtmlUtil.escape(keyword) %>" placeholder="Search by name, email or phone">
            <button type="submit">Search</button>
            <a class="button" href="managePatients">Show All</a>
        </form>

        <div class="table-wrap">
            <table>
                <thead><tr><th>ID</th><th>Full Name</th><th>Email</th><th>Phone</th><th>Gender</th><th>Date of Birth</th><th>Actions</th></tr></thead>
                <tbody>
                <% if (patients == null || patients.isEmpty()) { %>
                    <tr><td colspan="7" class="empty">No patient records found.</td></tr>
                <% } else { for (Patient patient : patients) { %>
                    <tr>
                        <td>#<%= patient.getPatientId() %></td>
                        <td><strong><%= HtmlUtil.escape(patient.getFullName()) %></strong></td>
                        <td><%= HtmlUtil.escape(patient.getEmail()) %></td>
                        <td><%= HtmlUtil.escape(patient.getPhone()) %></td>
                        <td><%= HtmlUtil.escape(patient.getGender()) %></td>
                        <td><%= patient.getDateOfBirth() == null ? "-" : patient.getDateOfBirth() %></td>
                        <td>
                            <a class="action-link" href="editPatient.jsp?id=<%= patient.getPatientId() %>&source=admin">Edit</a>
                            <a class="action-link" style="color:var(--danger);" href="deletePatient?id=<%= patient.getPatientId() %>" onclick="return confirm('Are you sure you want to delete this patient?');">Delete</a>
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
