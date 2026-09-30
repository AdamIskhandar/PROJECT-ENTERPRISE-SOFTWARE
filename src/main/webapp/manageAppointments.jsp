<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.Appointment" %>

<%
    List<Appointment> appointments =
            (List<Appointment>) request.getAttribute("appointments");
    String keyword = (String) request.getAttribute("keyword");
    String selectedStatus = (String) request.getAttribute("status");
    Integer patientId = (Integer) request.getAttribute("patientId");
    String message = request.getParameter("message");
    String error = request.getParameter("error");

    String userRole = (String) session.getAttribute("userRole");
    boolean isAdmin = "ADMIN".equals(userRole);
    String homeUrl;
    if (patientId != null) {
        homeUrl = "patientProfile?id=" + patientId;
    } else if ("ADMIN".equals(userRole)) {
        homeUrl = "managePatients";
    } else {
        homeUrl = "index.jsp";
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Appointment Management - ClinicCare</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6f8; margin: 0; }
        .container { width: 98%; max-width: 1500px; margin: 30px auto; background: white; padding: 22px; border-radius: 10px; box-sizing: border-box; }
        h2 { text-align: center; margin-top: 0; }
        .top-nav { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; }
        .top-nav .nav-actions { display: flex; gap: 8px; }
        .top-nav a { border-radius: 4px; }
        .button.home-button { background: #e9ecef; color: #222; }
        .button.logout-button { background: #9f1d1d; color: white; }
        .toolbar { display: flex; gap: 10px; flex-wrap: wrap; align-items: center; margin-bottom: 18px; }
        .toolbar input, .toolbar select { padding: 9px; }
        .toolbar input { min-width: 300px; }
        button, .button { border: 0; padding: 9px 12px; cursor: pointer; text-decoration: none; display: inline-block; }
        button { background: #222; color: white; }
        .button { background: #eee; color: #222; }
        table { width: 100%; border-collapse: collapse; font-size: 13px; }
        th, td { border: 1px solid #ddd; padding: 8px; vertical-align: top; }
        th { background: #eee; text-align: left; }
        .success { background: #e8f5e9; padding: 10px; margin-bottom: 15px; border-radius: 5px; }
        .error { background: #ffebee; padding: 10px; margin-bottom: 15px; border-radius: 5px; }
        .inline-form { display: inline; }
        .inline-form select { padding: 6px; }
        .small-button { padding: 6px 8px; font-size: 12px; }
        .danger { background: #9f1d1d; color: white; }
        .cancel { background: #8a5a00; color: white; }
        .charge { white-space: nowrap; line-height: 1.5; }
        .empty { text-align: center; padding: 25px; }
        .reason { max-width: 220px; word-break: break-word; }
    </style>
</head>
<body>
<div class="container">

    <div class="top-nav">
        <a class="button home-button" href="<%= homeUrl %>">Home</a>
        <div class="nav-actions">
            <a class="button logout-button" href="logout">Logout</a>
        </div>
    </div>

    <h2><%= patientId == null ? "Manage Appointments" : "Patient Appointments" %></h2>

    <% if ("booked".equals(message)) { %><div class="success">Appointment booked successfully.</div><% } %>
    <% if ("updated".equals(message)) { %><div class="success">Appointment updated successfully.</div><% } %>
    <% if ("cancelled".equals(message)) { %><div class="success">Appointment cancelled and the doctor schedule is available again.</div><% } %>
    <% if ("status".equals(message)) { %><div class="success">Appointment status updated successfully.</div><% } %>
    <% if ("deleted".equals(message)) { %><div class="success">Appointment deleted successfully.</div><% } %>

    <% if (error != null) { %>
        <div class="error">The requested appointment action could not be completed.</div>
    <% } %>

    <div class="toolbar">
        <a class="button" href="bookAppointment<%= patientId == null ? "" : "?patientId=" + patientId %>">Book New Appointment</a>

        <form action="manageAppointments" method="get" class="toolbar" style="margin:0;">
            <% if (patientId != null) { %>
                <input type="hidden" name="patientId" value="<%= patientId %>">
            <% } %>

            <input type="text"
                   name="keyword"
                   value="<%= keyword == null ? "" : keyword %>"
                   placeholder="Search ID, patient, doctor, specialization, reason or date">

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
    </div>

    <table>
        <tr>
            <th>ID</th>
            <th>Patient</th>
            <th>Doctor</th>
            <th>Schedule</th>
            <th>Reason</th>
            <th>Status</th>
            <th>Charge Calculation</th>
            <th>Actions</th>
        </tr>

        <% if (appointments == null || appointments.isEmpty()) { %>
            <tr><td colspan="8" class="empty">No appointments found.</td></tr>
        <% } else {
            for (Appointment appointment : appointments) { %>
            <tr>
                <td><%= appointment.getAppointmentId() %></td>
                <td>
                    <strong><%= appointment.getPatientName() %></strong><br>
                    ID: <%= appointment.getPatientId() %>
                </td>
                <td>
                    Dr. <%= appointment.getDoctorName() %><br>
                    <%= appointment.getSpecialization() %>
                </td>
                <td>
                    <%= appointment.getAppointmentDate() %><br>
                    <%= appointment.getStartTime() %> - <%= appointment.getEndTime() %><br>
                    Schedule #<%= appointment.getScheduleId() %>
                </td>
                <td class="reason"><%= appointment.getReason() == null ? "-" : appointment.getReason() %></td>
                <td><strong><%= appointment.getStatus() %></strong></td>
                <td class="charge">
                    <%= appointment.getRateType() %> Rate: RM <%= appointment.getRateUsed() %>/hour<br>
                    Duration: <%= appointment.getDurationHours() %> hour(s)<br>
                    <strong>Total: RM <%= appointment.getTotalCharge() %></strong>
                </td>
                <td>
                    <% if (!"Cancelled".equalsIgnoreCase(appointment.getStatus())
                            && !"Completed".equalsIgnoreCase(appointment.getStatus())) { %>
                        <a class="button small-button" href="editAppointment?id=<%= appointment.getAppointmentId() %>">Edit</a>

                        <form action="cancelAppointment" method="post" class="inline-form"
                              onsubmit="return confirm('Cancel this appointment?');">
                            <input type="hidden" name="appointmentId" value="<%= appointment.getAppointmentId() %>">
                            <% if (patientId != null) { %>
                                <input type="hidden" name="patientId" value="<%= patientId %>">
                            <% } %>
                            <button type="submit" class="small-button cancel">Cancel</button>
                        </form>
                    <% } %>

                    <% if (isAdmin) { %>
                        <form action="updateAppointmentStatus" method="post" style="margin-top:8px;">
                            <input type="hidden" name="appointmentId" value="<%= appointment.getAppointmentId() %>">
                            <% if (patientId != null) { %>
                                <input type="hidden" name="patientId" value="<%= patientId %>">
                            <% } %>
                            <select name="status">
                                <option value="Pending" <%= "Pending".equalsIgnoreCase(appointment.getStatus()) ? "selected" : "" %>>Pending</option>
                                <option value="Confirmed" <%= "Confirmed".equalsIgnoreCase(appointment.getStatus()) ? "selected" : "" %>>Confirmed</option>
                                <option value="Completed" <%= "Completed".equalsIgnoreCase(appointment.getStatus()) ? "selected" : "" %>>Completed</option>
                                <option value="Cancelled" <%= "Cancelled".equalsIgnoreCase(appointment.getStatus()) ? "selected" : "" %>>Cancelled</option>
                            </select>
                            <button type="submit" class="small-button">Update Status</button>
                        </form>

                        <form action="deleteAppointment" method="post" style="margin-top:8px;"
                              onsubmit="return confirm('Permanently delete this appointment record?');">
                            <input type="hidden" name="appointmentId" value="<%= appointment.getAppointmentId() %>">
                            <% if (patientId != null) { %>
                                <input type="hidden" name="patientId" value="<%= patientId %>">
                            <% } %>
                            <button type="submit" class="small-button danger">Delete</button>
                        </form>
                    <% } %>
                </td>
            </tr>
        <%  }
           } %>
    </table>

</div>
</body>
</html>
