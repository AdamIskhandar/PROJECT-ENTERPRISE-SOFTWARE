<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.cliniccare.model.Doctor" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>

<%
    Doctor doctor = (Doctor) request.getAttribute("doctor");

    // Opened directly without an id? Go back to the list.
    if (doctor == null) {
        response.sendRedirect("manageDoctors");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Doctor - ClinicCare</title>
    <link rel="stylesheet" href="css/clinic.css">
</head>

<body>

<%@ include file="nav.jspf" %>

<div class="form-container">

    <h2>Edit Doctor</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= HtmlUtil.escape(request.getAttribute("error")) %></p>
    <% } %>

    <form action="updateDoctor" method="post">

        <input type="hidden" name="doctorId" value="<%= doctor.getDoctorId() %>">

        <label>Full Name</label>
        <input type="text" name="fullName" maxlength="100"
               value="<%= HtmlUtil.escape(doctor.getFullName()) %>" required>

        <label>Specialization</label>
        <input type="text" name="specialization" maxlength="100"
               value="<%= HtmlUtil.escape(doctor.getSpecialization()) %>" required>

        <label>Email</label>
        <input type="email" name="email" maxlength="100"
               value="<%= HtmlUtil.escape(doctor.getEmail()) %>" required>

        <label>Phone Number</label>
        <input type="text" name="phone"
               pattern="[0-9]{10,12}"
               title="Phone number must contain 10 to 12 digits"
               value="<%= HtmlUtil.escape(doctor.getPhone()) %>">

        <label>Consultation Fee (RM)</label>
        <input type="number" name="consultationFee"
               min="0" step="0.01"
               value="<%= String.format("%.2f", doctor.getConsultationFee()) %>" required>

        <button type="submit">Update Doctor</button>

    </form>

    <p class="back"><a href="manageDoctors">Back to Manage Doctors</a></p>

</div>

</body>
</html>
