<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.Appointment" %>
<%@ page import="com.cliniccare.model.ScheduleOption" %>

<%
    Appointment appointment =
            (Appointment) request.getAttribute("appointment");
    List<ScheduleOption> schedules =
            (List<ScheduleOption>) request.getAttribute("schedules");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Appointment - ClinicCare</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6f8; margin: 0; }
        .container { width: 680px; max-width: 92%; margin: 40px auto; background: white; padding: 28px; border-radius: 10px; box-sizing: border-box; }
        h2 { text-align: center; margin-top: 0; }
        label { display: block; font-weight: bold; margin-top: 14px; }
        input, select, textarea { width: 100%; padding: 10px; margin-top: 6px; box-sizing: border-box; }
        textarea { min-height: 90px; resize: vertical; }
        button, .button { display: inline-block; padding: 10px 16px; margin-top: 18px; border: 0; cursor: pointer; text-decoration: none; }
        button { background: #222; color: white; }
        .button { background: #eee; color: #222; }
        .error { background: #ffebee; padding: 10px; border-radius: 5px; margin-bottom: 15px; }
        .info { background: #eef7ff; padding: 14px; margin-top: 16px; border-radius: 6px; line-height: 1.6; }
        .status { background: #f6f6f6; padding: 10px; border-radius: 5px; }
    </style>
</head>
<body>
<div class="container">

    <h2>Edit Appointment</h2>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <% if (appointment != null) { %>

        <div class="status">
            Appointment #<%= appointment.getAppointmentId() %> —
            Status: <strong><%= appointment.getStatus() %></strong>
        </div>

        <form action="editAppointment" method="post">

            <input type="hidden" name="appointmentId" value="<%= appointment.getAppointmentId() %>">

            <label>Patient ID</label>
            <input type="number" name="patientId" min="1" value="<%= appointment.getPatientId() %>" required>

            <label>Doctor Schedule</label>
            <select name="scheduleId" id="scheduleId" required onchange="showScheduleDetails()">
                <% if (schedules != null) {
                    for (ScheduleOption schedule : schedules) { %>
                        <option
                                value="<%= schedule.getScheduleId() %>"
                                data-doctor="<%= schedule.getDoctorName() %>"
                                data-specialization="<%= schedule.getSpecialization() %>"
                                data-date="<%= schedule.getAvailableDate() %>"
                                data-start="<%= schedule.getStartTime() %>"
                                data-end="<%= schedule.getEndTime() %>"
                                data-rate-type="<%= schedule.getRateType() %>"
                                data-rate="<%= schedule.getRateUsed() %>"
                                data-duration="<%= schedule.getDurationHours() %>"
                                data-charge="<%= schedule.getEstimatedCharge() %>"
                                <%= schedule.getScheduleId() == appointment.getScheduleId() ? "selected" : "" %>>
                            <%= schedule.getAvailableDate() %>
                            | <%= schedule.getStartTime() %> - <%= schedule.getEndTime() %>
                            | Dr. <%= schedule.getDoctorName() %>
                            (<%= schedule.getSpecialization() %>)
                        </option>
                <%  }
                   } %>
            </select>

            <div class="info" id="scheduleInfo">
                <strong>Recalculated Appointment Charge</strong><br>
                Doctor: <span id="doctorText"></span><br>
                Date/Time: <span id="dateTimeText"></span><br>
                Rate Type: <span id="rateTypeText"></span><br>
                Rate Used: RM <span id="rateText"></span> per hour<br>
                Duration: <span id="durationText"></span> hour(s)<br>
                <strong>Total Charge: RM <span id="chargeText"></span></strong>
            </div>

            <label>Reason for Appointment</label>
            <textarea name="reason" maxlength="255"><%= appointment.getReason() == null ? "" : appointment.getReason() %></textarea>

            <button type="submit">Save Changes</button>
            <a class="button" href="manageAppointments?patientId=<%= appointment.getPatientId() %>">Back</a>

        </form>
    <% } %>

</div>

<script>
    function showScheduleDetails() {
        const select = document.getElementById('scheduleId');
        if (!select) return;

        const option = select.options[select.selectedIndex];
        if (!option) return;

        document.getElementById('doctorText').textContent =
            'Dr. ' + option.dataset.doctor + ' (' + option.dataset.specialization + ')';
        document.getElementById('dateTimeText').textContent =
            option.dataset.date + ' | ' + option.dataset.start + ' - ' + option.dataset.end;
        document.getElementById('rateTypeText').textContent = option.dataset.rateType;
        document.getElementById('rateText').textContent = option.dataset.rate;
        document.getElementById('durationText').textContent = option.dataset.duration;
        document.getElementById('chargeText').textContent = option.dataset.charge;
    }

    showScheduleDetails();
</script>
</body>
</html>
