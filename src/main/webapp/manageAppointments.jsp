<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.Appointment" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>
<%
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    String keyword = (String) request.getAttribute("keyword");
    String selectedStatus = (String) request.getAttribute("status");
    Integer patientId = (Integer) request.getAttribute("patientId");
    String message = request.getParameter("message");
    String error = request.getParameter("error");

    String userRole = (String) session.getAttribute("userRole");
    boolean isAdmin = "ADMIN".equals(userRole);
    String homeUrl = patientId != null ? "patientProfile?id=" + patientId : (isAdmin ? "adminDashboard.jsp" : "index.jsp");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Appointments - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=3">
</head>
<body>
<% if (isAdmin) { %><%@ include file="nav.jsp" %><% } %>
<div class="page" style="width:min(1450px,96%);">
    <div class="page-header">
        <div>
            <span class="eyebrow">Appointments</span>
            <h1><%= patientId == null ? "Manage Appointments" : "My Appointments" %></h1>
            <p>Review doctor schedules, appointment status and time-based charge calculations.</p>
        </div>
        <div class="button-row">
            <a class="button" href="<%= homeUrl %>">Back</a>
            <a class="btn btn-primary" href="bookAppointment<%= patientId == null ? "" : "?patientId=" + patientId %>">+ Book Appointment</a>
        </div>
    </div>

    <% if ("booked".equals(message)) { %><div class="success">Appointment booked successfully.</div><% } %>
    <% if ("updated".equals(message)) { %><div class="success">Appointment updated successfully.</div><% } %>
    <% if ("cancelled".equals(message)) { %><div class="success">Appointment cancelled and its doctor schedule is available again.</div><% } %>
    <% if ("status".equals(message)) { %><div class="success">Appointment status updated successfully.</div><% } %>
    <% if ("deleted".equals(message)) { %><div class="success">Appointment deleted successfully.</div><% } %>
    <% if (error != null) { %><div class="error">The requested appointment action could not be completed.</div><% } %>

    <div class="panel" style="padding:20px;">
        <form action="manageAppointments" method="get" class="toolbar">
            <% if (patientId != null) { %><input type="hidden" name="patientId" value="<%= patientId %>"><% } %>
            <input type="text" name="keyword" value="<%= HtmlUtil.escape(keyword) %>" placeholder="Search patient, doctor, specialization, reason or date">
            <select name="status">
                <option value="">All Statuses</option>
                <option value="Pending" <%= "Pending".equalsIgnoreCase(selectedStatus) ? "selected" : "" %>>Pending</option>
                <option value="Confirmed" <%= "Confirmed".equalsIgnoreCase(selectedStatus) ? "selected" : "" %>>Confirmed</option>
                <option value="Completed" <%= "Completed".equalsIgnoreCase(selectedStatus) ? "selected" : "" %>>Completed</option>
                <option value="Cancelled" <%= "Cancelled".equalsIgnoreCase(selectedStatus) ? "selected" : "" %>>Cancelled</option>
            </select>
            <button type="submit">Search</button>
            <a class="button" href="manageAppointments<%= patientId == null ? "" : "?patientId=" + patientId %>">Show All</a>
        </form>

        <div class="table-wrap">
            <table>
                <thead><tr><th>ID</th><th>Patient</th><th>Doctor</th><th>Schedule</th><th>Reason</th><th>Status</th><th>Charge</th><th>Actions</th></tr></thead>
                <tbody>
                <% if (appointments == null || appointments.isEmpty()) { %>
                    <tr><td colspan="8" class="empty">No appointments found.</td></tr>
                <% } else { for (Appointment appointment : appointments) { %>
                    <tr>
                        <td>#<%= appointment.getAppointmentId() %></td>
                        <td><strong><%= HtmlUtil.escape(appointment.getPatientName()) %></strong><br><span class="muted">Patient #<%= appointment.getPatientId() %></span></td>
                        <td><strong><%= HtmlUtil.escape(appointment.getDoctorName()) %></strong><br><span class="muted"><%= HtmlUtil.escape(appointment.getSpecialization()) %></span></td>
                        <td><strong><%= appointment.getAppointmentDate() %></strong><br><%= appointment.getStartTime() %>–<%= appointment.getEndTime() %><br><span class="muted">Schedule #<%= appointment.getScheduleId() %></span></td>
                        <td class="reason"><%= appointment.getReason() == null || appointment.getReason().isEmpty() ? "-" : HtmlUtil.escape(appointment.getReason()) %></td>
                        <td><span class="status-badge status-<%= appointment.getStatus() %>"><%= HtmlUtil.escape(appointment.getStatus()) %></span></td>
                        <td class="charge"><span class="muted"><%= HtmlUtil.escape(appointment.getRateType()) %> rate</span><br>RM <%= appointment.getRateUsed() %>/hr × <%= appointment.getDurationHours() %> hr<br><strong>RM <%= appointment.getTotalCharge() %></strong></td>
                        <td>
                            <% if (!"Cancelled".equalsIgnoreCase(appointment.getStatus()) && !"Completed".equalsIgnoreCase(appointment.getStatus())) { %>
                                <a class="action-link" href="editAppointment?id=<%= appointment.getAppointmentId() %>">Edit</a>
                                <form action="cancelAppointment" method="post" class="inline-form" onsubmit="return confirm('Cancel this appointment?');">
                                    <input type="hidden" name="appointmentId" value="<%= appointment.getAppointmentId() %>">
                                    <% if (patientId != null) { %><input type="hidden" name="patientId" value="<%= patientId %>"><% } %>
                                    <button type="submit" class="link-button" style="color:var(--warning)!important;">Cancel</button>
                                </form>
                            <% } %>

                            <% if (isAdmin) { %>
                                <form action="updateAppointmentStatus" method="post" style="margin-top:10px; min-width:155px;">
                                    <input type="hidden" name="appointmentId" value="<%= appointment.getAppointmentId() %>">
                                    <% if (patientId != null) { %><input type="hidden" name="patientId" value="<%= patientId %>"><% } %>
                                    <select name="status" style="margin-bottom:7px;">
                                        <option value="Pending" <%= "Pending".equalsIgnoreCase(appointment.getStatus()) ? "selected" : "" %>>Pending</option>
                                        <option value="Confirmed" <%= "Confirmed".equalsIgnoreCase(appointment.getStatus()) ? "selected" : "" %>>Confirmed</option>
                                        <option value="Completed" <%= "Completed".equalsIgnoreCase(appointment.getStatus()) ? "selected" : "" %>>Completed</option>
                                        <option value="Cancelled" <%= "Cancelled".equalsIgnoreCase(appointment.getStatus()) ? "selected" : "" %>>Cancelled</option>
                                    </select>
                                    <button type="submit" class="small-button" style="width:100%;">Update Status</button>
                                </form>
                                <form action="deleteAppointment" method="post" style="margin-top:7px;" onsubmit="return confirm('Permanently delete this appointment record?');">
                                    <input type="hidden" name="appointmentId" value="<%= appointment.getAppointmentId() %>">
                                    <% if (patientId != null) { %><input type="hidden" name="patientId" value="<%= patientId %>"><% } %>
                                    <button type="submit" class="small-button danger" style="width:100%;">Delete</button>
                                </form>
                            <% } %>
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
