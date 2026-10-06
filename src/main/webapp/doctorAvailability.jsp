<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.Doctor" %>
<%@ page import="com.cliniccare.model.Scheduling" %>
<%@ page import="com.cliniccare.config.ClinicRateConfig" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>
<%
    Doctor doctor = (Doctor) request.getAttribute("doctor");
    List<Scheduling> slots = (List<Scheduling>) request.getAttribute("slots");
    if (doctor == null || slots == null) {
        response.sendRedirect("manageDoctors");
        return;
    }

    String message = request.getParameter("message");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Doctor Scheduling - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=4">
</head>
<body>
<%@ include file="nav.jsp" %>
<div class="page">
    <div class="page-header">
        <div>
            <span class="eyebrow">Scheduling</span>
            <h1><%= HtmlUtil.escape(doctor.getDoctorName()) %></h1>
            <p><%= HtmlUtil.escape(doctor.getSpecialization()) %> · Manage dated availability stored in the <strong>scheduling</strong> table.</p>
        </div>
        <a class="button" href="manageDoctors">Back to Doctors</a>
    </div>

    <div class="rate-grid" style="max-width:560px; margin-bottom:22px;">
        <div class="rate-card">
            <span class="muted">Fixed Morning Rate</span>
            <strong>RM <%= ClinicRateConfig.MORNING_RATE.toPlainString() %>/hr</strong>
        </div>
        <div class="rate-card">
            <span class="muted">Fixed Night Rate</span>
            <strong>RM <%= ClinicRateConfig.NIGHT_RATE.toPlainString() %>/hr</strong>
        </div>
    </div>

    <% if ("added".equals(message)) { %><div class="success">Doctor schedule added successfully.</div><% } %>
    <% if ("deleted".equals(message)) { %><div class="success">Doctor schedule deleted successfully.</div><% } %>
    <% if ("invalid".equals(error)) { %><div class="error">Please choose a valid date, start time and end time.</div><% } %>
    <% if ("time".equals(error)) { %><div class="error">End time must be after start time.</div><% } %>
    <% if ("past".equals(error)) { %><div class="error">The schedule must be in the future.</div><% } %>
    <% if ("overlap".equals(error)) { %><div class="error">This schedule overlaps an existing schedule for this doctor.</div><% } %>
    <% if ("delete".equals(error)) { %><div class="error">Booked schedules cannot be deleted.</div><% } %>
    <% if ("failed".equals(error)) { %><div class="error">The action could not be completed.</div><% } %>

    <div class="panel" style="padding:20px;">
        <h3>Existing Schedules</h3>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Schedule ID</th>
                    <th>Available Date</th>
                    <th>Start</th>
                    <th>End</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <% if (slots.isEmpty()) { %>
                    <tr><td colspan="6" class="empty">No schedules have been created for this doctor.</td></tr>
                <% } else { for (Scheduling slot : slots) { %>
                    <tr>
                        <td>#<%= slot.getScheduleId() %></td>
                        <td><strong><%= slot.getAvailableDate() %></strong></td>
                        <td><%= slot.getStartTime() %></td>
                        <td><%= slot.getEndTime() %></td>
                        <td><span class="status-badge status-<%= HtmlUtil.escape(slot.getStatus()) %>"><%= HtmlUtil.escape(slot.getStatus()) %></span></td>
                        <td>
                            <% if (!"Booked".equalsIgnoreCase(slot.getStatus())) { %>
                                <form action="doctorAvailability" method="post" class="inline-form"
                                      onsubmit="return confirm('Delete this doctor schedule?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="doctorId" value="<%= doctor.getDoctorId() %>">
                                    <input type="hidden" name="scheduleId" value="<%= slot.getScheduleId() %>">
                                    <button type="submit" class="link-button">Delete</button>
                                </form>
                            <% } else { %>
                                <span class="muted">In use</span>
                            <% } %>
                        </td>
                    </tr>
                <% } } %>
                </tbody>
            </table>
        </div>

        <form action="doctorAvailability" method="post" class="slot-form">
            <h3>Add Doctor Schedule</h3>
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="doctorId" value="<%= doctor.getDoctorId() %>">

            <div class="form-grid">
                <div class="form-group full">
                    <label>Available Date</label>
                    <input type="date" name="availableDate"
                           min="<%= java.time.LocalDate.now() %>" required>
                </div>

                <div class="form-group">
                    <label>Start Time</label>
                    <input type="time" name="startTime" required>
                </div>

                <div class="form-group">
                    <label>End Time</label>
                    <input type="time" name="endTime" required>
                </div>
            </div>

            <div class="hint">New schedules are automatically saved with status <strong>Available</strong>.</div>
            <div class="form-actions"><button type="submit">Add Schedule</button></div>
        </form>
    </div>
</div>
</body>
</html>
