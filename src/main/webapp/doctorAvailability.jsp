<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.Doctor" %>
<%@ page import="com.cliniccare.model.DoctorAvailability" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>

<%
    Doctor doctor = (Doctor) request.getAttribute("doctor");
    List<DoctorAvailability> slots =
            (List<DoctorAvailability>) request.getAttribute("slots");

    // Opened the JSP directly? Pick a doctor from the list first.
    if (doctor == null || slots == null) {
        response.sendRedirect("manageDoctors");
        return;
    }

    String message = request.getParameter("message");
    String error = request.getParameter("error");

    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday",
                     "Friday", "Saturday", "Sunday"};
%>

<!DOCTYPE html>
<html>
<head>
    <title>Doctor Availability - ClinicCare</title>
    <link rel="stylesheet" href="css/clinic.css">
</head>

<body>

<%@ include file="nav.jspf" %>

<div class="container">

    <h2>Availability</h2>

    <p class="subtitle">
        <%= HtmlUtil.escape(doctor.getFullName()) %>
        (<%= HtmlUtil.escape(doctor.getSpecialization()) %>)
    </p>

    <% if ("added".equals(message)) { %>
        <p class="success">Availability slot added.</p>
    <% } %>

    <% if ("deleted".equals(message)) { %>
        <p class="success">Availability slot deleted.</p>
    <% } %>

    <% if ("invalid".equals(error)) { %>
        <p class="error">Please choose a day and valid start and end times.</p>
    <% } %>

    <% if ("time".equals(error)) { %>
        <p class="error">End time must be after start time.</p>
    <% } %>

    <% if ("overlap".equals(error)) { %>
        <p class="error">
            This slot overlaps an existing slot on the same day.
        </p>
    <% } %>

    <% if ("failed".equals(error)) { %>
        <p class="error">The action could not be completed. Please try again.</p>
    <% } %>

    <table>

        <tr>
            <th>Day</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Action</th>
        </tr>

        <% if (slots.isEmpty()) { %>

            <tr>
                <td colspan="4" class="empty">No availability slots yet.</td>
            </tr>

        <% } %>

        <% for (DoctorAvailability slot : slots) { %>

            <tr>
                <td><%= HtmlUtil.escape(slot.getDayOfWeek()) %></td>
                <td><%= slot.getStartTime() %></td>
                <td><%= slot.getEndTime() %></td>

                <td>
                    <form action="doctorAvailability"
                          method="post"
                          class="inline-form"
                          onsubmit="return confirm('Delete this slot?');">

                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="doctorId"
                               value="<%= doctor.getDoctorId() %>">
                        <input type="hidden" name="availabilityId"
                               value="<%= slot.getAvailabilityId() %>">

                        <button type="submit" class="link-button">Delete</button>
                    </form>
                </td>
            </tr>

        <% } %>

    </table>

    <form action="doctorAvailability" method="post" class="slot-form">

        <h3>Add Availability Slot</h3>

        <input type="hidden" name="action" value="add">
        <input type="hidden" name="doctorId" value="<%= doctor.getDoctorId() %>">

        <label>Day</label>
        <select name="dayOfWeek" required>
            <option value="">Select Day</option>
            <% for (String day : days) { %>
                <option value="<%= day %>"><%= day %></option>
            <% } %>
        </select>

        <label>Start Time</label>
        <input type="time" name="startTime" required>

        <label>End Time</label>
        <input type="time" name="endTime" required>

        <button type="submit">Add Slot</button>

    </form>

    <p class="back"><a href="manageDoctors">Back to Manage Doctors</a></p>

</div>

</body>
</html>
