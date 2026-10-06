<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cliniccare.dao.PatientDAO" %>
<%@ page import="com.cliniccare.model.Patient" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>
<%
    String id = request.getParameter("id");
    Patient patient = (Patient) request.getAttribute("patient");
    if (patient == null && id != null) {
        try { patient = new PatientDAO().getPatientById(Integer.parseInt(id)); } catch (Exception ignored) {}
    }
    String source = request.getParameter("source");
    if (patient == null) { response.sendRedirect("managePatients"); return; }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Patient - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=3">
</head>
<body>
<% if ("admin".equals(source)) { %><%@ include file="nav.jsp" %><% } %>
<div class="form-container">
    <span class="eyebrow">Patient Record</span>
    <h2>Edit Patient</h2>
    <p class="subtitle">Update patient contact and personal information.</p>

    <% if (request.getAttribute("error") != null) { %><div class="error"><%= request.getAttribute("error") %></div><% } %>

    <form action="updatePatient" method="post">
        <input type="hidden" name="patientId" value="<%= patient.getPatientId() %>">
        <input type="hidden" name="source" value="<%= source != null ? source : "patient" %>">
        <div class="form-grid">
            <div class="form-group full">
                <label>Full Name</label>
                <input type="text" name="fullName" maxlength="100" value="<%= HtmlUtil.escape(patient.getFullName()) %>" required>
            </div>
            <div class="form-group full">
                <label>Email Address</label>
                <input type="email" name="email" maxlength="100" value="<%= HtmlUtil.escape(patient.getEmail()) %>" required>
            </div>
            <div class="form-group">
                <label>Phone Number</label>
                <input type="text" name="phone" pattern="[0-9]{10,12}" value="<%= HtmlUtil.escape(patient.getPhone()) %>">
            </div>
            <div class="form-group">
                <label>Gender</label>
                <select name="gender" required>
                    <option value="Male" <%= "Male".equals(patient.getGender()) ? "selected" : "" %>>Male</option>
                    <option value="Female" <%= "Female".equals(patient.getGender()) ? "selected" : "" %>>Female</option>
                </select>
            </div>
            <div class="form-group full">
                <label>Date of Birth</label>
                <input type="date" name="dateOfBirth" max="<%= java.time.LocalDate.now() %>" value="<%= patient.getDateOfBirth() == null ? "" : patient.getDateOfBirth() %>">
            </div>
        </div>
        <div class="form-actions">
            <button type="submit">Save Changes</button>
            <a class="button" href="<%= "admin".equals(source) ? "managePatients" : "patientProfile?id=" + patient.getPatientId() %>">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>
