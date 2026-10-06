<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cliniccare.model.Patient" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>
<%
    Patient patient = (Patient) request.getAttribute("patient");
    String message = request.getParameter("message");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Profile - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=3">
</head>
<body>
<div class="profile-card">
    <% if ("updated".equals(message)) { %><div class="success">Profile updated successfully.</div><% } %>
    <% if (patient != null) { %>
        <div class="profile-head">
            <div class="avatar">P</div>
            <div>
                <span class="eyebrow">Patient Portal</span>
                <h2 style="margin-bottom:3px;"><%= HtmlUtil.escape(patient.getFullName()) %></h2>
                <div class="muted">Patient ID #<%= patient.getPatientId() %></div>
            </div>
        </div>

        <div class="details-grid">
            <div class="detail"><div class="label">Email</div><div class="value"><%= HtmlUtil.escape(patient.getEmail()) %></div></div>
            <div class="detail"><div class="label">Phone</div><div class="value"><%= HtmlUtil.escape(patient.getPhone()) %></div></div>
            <div class="detail"><div class="label">Gender</div><div class="value"><%= HtmlUtil.escape(patient.getGender()) %></div></div>
            <div class="detail"><div class="label">Date of Birth</div><div class="value"><%= patient.getDateOfBirth() == null ? "-" : patient.getDateOfBirth() %></div></div>
        </div>

        <div class="form-actions">
            <a class="btn btn-primary" href="bookAppointment?source=patient">Book Appointment</a>
            <a class="button" href="myAppointments">My Appointments</a>
            <a class="button" href="editPatient.jsp?id=<%= patient.getPatientId() %>">Edit Profile</a>
            <a class="button" href="login.jsp">Sign Out</a>
        </div>
    <% } else { %>
        <div class="error">Patient information could not be loaded.</div>
        <a class="button" href="login.jsp">Back to Login</a>
    <% } %>
</div>
</body>
</html>
