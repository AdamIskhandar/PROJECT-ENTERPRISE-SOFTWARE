<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.Appointment" %>
<%@ page import="com.cliniccare.model.ScheduleOption" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>
<%
    Appointment appointment = (Appointment) request.getAttribute("appointment");
    List<ScheduleOption> schedules = (List<ScheduleOption>) request.getAttribute("schedules");
    if (appointment == null) { response.sendRedirect("manageAppointments"); return; }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Appointment - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=3">
</head>
<body>
<div class="form-container" style="width:min(760px,92%);">
    <span class="eyebrow">Appointment #<%= appointment.getAppointmentId() %></span>
    <h2>Edit Appointment</h2>
    <p class="subtitle">Current status: <span class="status-badge status-<%= appointment.getStatus() %>"><%= HtmlUtil.escape(appointment.getStatus()) %></span></p>

    <% if (request.getAttribute("error") != null) { %><div class="error"><%= HtmlUtil.escape(request.getAttribute("error")) %></div><% } %>

    <form action="editAppointment" method="post">
        <input type="hidden" name="appointmentId" value="<%= appointment.getAppointmentId() %>">
        <div class="form-group">
            <label>Patient ID</label>
            <input type="number" name="patientId" min="1" value="<%= appointment.getPatientId() %>" required>
        </div>

        <div class="form-group" style="margin-top:16px;">
            <label>Doctor Schedule</label>
            <select name="scheduleId" id="scheduleId" required onchange="showScheduleDetails()">
                <% if (schedules != null) { for (ScheduleOption schedule : schedules) { %>
                    <option value="<%= schedule.getScheduleId() %>"
                            data-doctor="<%= HtmlUtil.escape(schedule.getDoctorName()) %>"
                            data-specialization="<%= HtmlUtil.escape(schedule.getSpecialization()) %>"
                            data-date="<%= schedule.getAvailableDate() %>"
                            data-start="<%= schedule.getStartTime() %>"
                            data-end="<%= schedule.getEndTime() %>"
                            data-rate-type="<%= HtmlUtil.escape(schedule.getRateType()) %>"
                            data-rate="<%= schedule.getRateUsed() %>"
                            data-duration="<%= schedule.getDurationHours() %>"
                            data-charge="<%= schedule.getEstimatedCharge() %>"
                            <%= schedule.getScheduleId() == appointment.getScheduleId() ? "selected" : "" %>>
                        <%= schedule.getAvailableDate() %> · <%= schedule.getStartTime() %>–<%= schedule.getEndTime() %> · <%= HtmlUtil.escape(schedule.getDoctorName()) %> (<%= HtmlUtil.escape(schedule.getSpecialization()) %>)
                    </option>
                <% } } %>
            </select>
        </div>

        <div class="calculation-box" id="scheduleInfo">
            <strong>Recalculated Charge</strong><br>
            Doctor: <span id="doctorText"></span><br>
            Date & Time: <span id="dateTimeText"></span><br>
            Rate Type: <span id="rateTypeText"></span><br>
            Hourly Rate: RM <span id="rateText"></span><br>
            Duration: <span id="durationText"></span> hour(s)<br>
            <strong>Total Charge: RM <span id="chargeText"></span></strong>
        </div>

        <div class="form-group" style="margin-top:16px;">
            <label>Reason for Appointment</label>
            <textarea name="reason" maxlength="255"><%= HtmlUtil.escape(appointment.getReason()) %></textarea>
        </div>

        <div class="form-actions">
            <button type="submit">Save Changes</button>
            <a class="button" href="manageAppointments?patientId=<%= appointment.getPatientId() %>">Cancel</a>
        </div>
    </form>
</div>

<script>
function showScheduleDetails() {
    const select = document.getElementById('scheduleId');
    if (!select) return;
    const option = select.options[select.selectedIndex];
    if (!option) return;
    document.getElementById('doctorText').textContent = option.dataset.doctor + ' (' + option.dataset.specialization + ')';
    document.getElementById('dateTimeText').textContent = option.dataset.date + ' · ' + option.dataset.start + '–' + option.dataset.end;
    document.getElementById('rateTypeText').textContent = option.dataset.rateType;
    document.getElementById('rateText').textContent = option.dataset.rate;
    document.getElementById('durationText').textContent = option.dataset.duration;
    document.getElementById('chargeText').textContent = option.dataset.charge;
}
showScheduleDetails();
</script>
</body>
</html>
