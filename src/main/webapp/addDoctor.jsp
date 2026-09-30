<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.cliniccare.model.Doctor" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>

<%
    // Filled only when the servlet sends the form back after an error
    Doctor doctor = (Doctor) request.getAttribute("doctor");

    String fullName = doctor != null ? doctor.getFullName() : "";
    String specialization = doctor != null ? doctor.getSpecialization() : "";
    String email = doctor != null ? doctor.getEmail() : "";
    String phone = doctor != null ? doctor.getPhone() : "";
    String fee = doctor != null
            ? String.format("%.2f", doctor.getConsultationFee()) : "";
%>

<!DOCTYPE html>
<html>
<head>
    <title>Add Doctor - ClinicCare</title>
    <link rel="stylesheet" href="css/clinic.css">
</head>

<body>

<%@ include file="nav.jspf" %>

<div class="form-container">

    <h2>Add New Doctor</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= HtmlUtil.escape(request.getAttribute("error")) %></p>
    <% } %>

    <form action="addDoctor" method="post">

        <label>Full Name</label>
        <input type="text" name="fullName" maxlength="100"
               value="<%= HtmlUtil.escape(fullName) %>" required>

        <label>Specialization</label>
        <input type="text" name="specialization" maxlength="100"
               value="<%= HtmlUtil.escape(specialization) %>" required>

        <label>Email</label>
        <input type="email" name="email" maxlength="100"
               value="<%= HtmlUtil.escape(email) %>" required>

        <label>Phone Number</label>
        <input type="text" name="phone"
               pattern="[0-9]{10,12}"
               title="Phone number must contain 10 to 12 digits"
               value="<%= HtmlUtil.escape(phone) %>">

        <label>Consultation Fee (RM)</label>
        <input type="number" name="consultationFee"
               min="0" step="0.01"
               value="<%= HtmlUtil.escape(fee) %>" required>

        <button type="submit">Add Doctor</button>

    </form>

    <p class="back"><a href="manageDoctors">Back to Manage Doctors</a></p>

</div>

</body>
</html>
