<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.Doctor" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>

<%
    List<Doctor> doctors = (List<Doctor>) request.getAttribute("doctors");

    // Opened the JSP directly? Go through the servlet so the list is loaded.
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
    <title>Manage Doctors - ClinicCare</title>
    <link rel="stylesheet" href="css/clinic.css">
</head>

<body>

<%@ include file="nav.jspf" %>

<div class="container">

    <h2>Manage Doctors</h2>

    <% if ("added".equals(message)) { %>
        <p class="success">Doctor added successfully.</p>
    <% } %>

    <% if ("updated".equals(message)) { %>
        <p class="success">Doctor updated successfully.</p>
    <% } %>

    <% if ("deleted".equals(message)) { %>
        <p class="success">Doctor deleted successfully.</p>
    <% } %>

    <% if ("delete".equals(error)) { %>
        <p class="error">
            Failed to delete doctor. The doctor may still have appointments.
        </p>
    <% } %>

    <% if ("notfound".equals(error)) { %>
        <p class="error">Doctor not found.</p>
    <% } %>

    <div style="margin-bottom: 20px;">
        <a href="addDoctor.jsp">Add New Doctor</a>
    </div>

    <form action="manageDoctors" method="get" class="search-box">

        <input type="text"
               name="keyword"
               value="<%= HtmlUtil.escape(keyword) %>"
               placeholder="Search name, specialization, email or phone">

        <button type="submit">Search</button>

        <a href="manageDoctors">Show All</a>

    </form>

    <table>

        <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Specialization</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Fee (RM)</th>
            <th>Action</th>
        </tr>

        <% if (doctors.isEmpty()) { %>

            <tr>
                <td colspan="7" class="empty">No doctors found.</td>
            </tr>

        <% } %>

        <% for (Doctor doctor : doctors) { %>

            <tr>
                <td><%= doctor.getDoctorId() %></td>
                <td><%= HtmlUtil.escape(doctor.getFullName()) %></td>
                <td><%= HtmlUtil.escape(doctor.getSpecialization()) %></td>
                <td><%= HtmlUtil.escape(doctor.getEmail()) %></td>
                <td><%= HtmlUtil.escape(doctor.getPhone()) %></td>
                <td><%= String.format("%.2f", doctor.getConsultationFee()) %></td>

                <td>
                    <a class="action-link"
                       href="doctorAvailability?doctorId=<%= doctor.getDoctorId() %>">
                        Availability
                    </a>

                    <a class="action-link"
                       href="editDoctor?id=<%= doctor.getDoctorId() %>">
                        Edit
                    </a>

                    <form action="deleteDoctor"
                          method="post"
                          class="inline-form"
                          onsubmit="return confirm('Are you sure you want to delete this doctor?');">

                        <input type="hidden" name="id"
                               value="<%= doctor.getDoctorId() %>">

                        <button type="submit" class="link-button">Delete</button>
                    </form>
                </td>
            </tr>

        <% } %>

    </table>

</div>

</body>
</html>
