<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cliniccare.model.Doctor" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>
<%
    Doctor doctor = (Doctor) request.getAttribute("doctor");
    String doctorName = doctor != null ? doctor.getDoctorName() : request.getParameter("doctorName");
    String specialization = doctor != null ? doctor.getSpecialization() : request.getParameter("specialization");
    String email = doctor != null ? doctor.getEmail() : request.getParameter("email");
    String phone = doctor != null ? doctor.getPhone() : request.getParameter("phone");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Doctor - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=4">
</head>
<body>
<%@ include file="nav.jsp" %>
<div class="form-container">
    <span class="eyebrow">Doctor Management</span>
    <h2>Add New Doctor</h2>
    <p class="subtitle">Enter the doctor's information. Appointment rates are fixed by ClinicCare and are added automatically.</p>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= HtmlUtil.escape(request.getAttribute("error")) %></div>
    <% } %>

    <form action="addDoctor" method="post">
        <div class="form-grid">
            <div class="form-group full">
                <label>Doctor Name</label>
                <input type="text" name="doctorName" maxlength="100"
                       value="<%= HtmlUtil.escape(doctorName) %>"
                       placeholder="e.g. Dr. Aisyah Rahman" required>
            </div>

            <div class="form-group full">
                <label>Specialization</label>
                <input type="text" name="specialization" maxlength="100"
                       value="<%= HtmlUtil.escape(specialization) %>"
                       placeholder="e.g. General Medicine" required>
            </div>

            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" maxlength="100"
                       value="<%= HtmlUtil.escape(email) %>"
                       placeholder="doctor@cliniccare.com">
            </div>

            <div class="form-group">
                <label>Phone Number</label>
                <input type="text" name="phone" maxlength="20"
                       pattern="[0-9]{10,12}"
                       value="<%= HtmlUtil.escape(phone) %>"
                       title="Phone number must contain 10 to 12 digits"
                       placeholder="0123456789">
            </div>
        </div>

        <div class="info-card" style="margin-top:18px;">
            <strong>Fixed Clinic Rates</strong>
            <div class="muted" style="margin-top:6px;">Morning: RM60.00/hour · Night: RM90.00/hour</div>
        </div>

        <div class="form-actions">
            <button type="submit">Add Doctor</button>
            <a class="button" href="manageDoctors">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>
