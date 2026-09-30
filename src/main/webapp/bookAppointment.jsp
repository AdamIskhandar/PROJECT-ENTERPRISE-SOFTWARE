<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.ScheduleOption" %>

<%
    List<ScheduleOption> schedules =
            (List<ScheduleOption>) request.getAttribute("schedules");

    String patientId = request.getAttribute("patientId") == null
            ? request.getParameter("patientId")
            : String.valueOf(request.getAttribute("patientId"));

    if (patientId == null) {
        patientId = "";
    }

    String selectedScheduleId = request.getParameter("scheduleId");
    String reason = request.getParameter("reason");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Appointment - ClinicCare</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6f8; margin: 0; }
        .container { width: 680px; max-width: 92%; margin: 40px auto; background: white; padding: 28px; border-radius: 10px; box-sizing: border-box; }
        h2 { margin-top: 0; text-align: center; }
        label { display: block; font-weight: bold; margin-top: 15px; }
        input, select, textarea { width: 100%; padding: 10px; margin-top: 6px; box-sizing: border-box; }
        textarea { min-height: 90px; resize: vertical; }
        button, .button { display: inline-block; padding: 10px 16px; margin-top: 18px; border: 0; cursor: pointer; text-decoration: none; }
        button { background: #222; color: white; }
        .button { background: #eee; color: #222; }
        .error { background: #ffebee; padding: 10px; border-radius: 5px; margin-bottom: 15px; }
        .info { background: #eef7ff; padding: 14px; margin-top: 16px; border-radius: 6px; line-height: 1.6; }
        .empty { background: #fff8e1; padding: 12px; border-radius: 5px; margin-top: 15px; }
        .hint { color: #666; font-size: 13px; margin-top: 5px; }
    </style>
</head>
<body>
<div class="container">

    <h2>Book Appointment</h2>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="bookAppointment" method="post">

        <label>Patient ID</label>
        <input type="number"
               name="patientId"
               min="1"
               value="<%= patientId %>"
               required>

        <label>Available Doctor Schedule</label>
        <select name="scheduleId" id="scheduleId" required onchange="showScheduleDetails()">
            <option value="">-- Select available schedule --</option>

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
                            <%= String.valueOf(schedule.getScheduleId()).equals(selectedScheduleId) ? "selected" : "" %>>
                        <%= schedule.getAvailableDate() %>
                        | <%= schedule.getStartTime() %> - <%= schedule.getEndTime() %>
                        | Dr. <%= schedule.getDoctorName() %>
                        (<%= schedule.getSpecialization() %>)
                    </option>
            <%  }
               } %>
        </select>

        <% if (schedules == null || schedules.isEmpty()) { %>
            <div class="empty">
                No available doctor schedules were found. A doctor, fee record, and available schedule must exist before an appointment can be booked.
            </div>
        <% } %>

        <div class="info" id="scheduleInfo" style="display:none;">
            <strong>Appointment Calculation</strong><br>
            Doctor: <span id="doctorText"></span><br>
            Date/Time: <span id="dateTimeText"></span><br>
            Rate Type: <span id="rateTypeText"></span><br>
            Rate Used: RM <span id="rateText"></span> per hour<br>
            Duration: <span id="durationText"></span> hour(s)<br>
            <strong>Total Charge: RM <span id="chargeText"></span></strong>
        </div>

        <label>Reason for Appointment</label>
        <textarea name="reason" maxlength="255" placeholder="Example: General consultation"><%= reason == null ? "" : reason %></textarea>
        <div class="hint">Maximum 255 characters.</div>

        <button type="submit" <%= schedules == null || schedules.isEmpty() ? "disabled" : "" %>>Book Appointment</button>
        <a class="button" href="<%= request.getContextPath() %>/index.jsp">Back</a>

    </form>
</div>

<script>
    function showScheduleDetails() {
        const select = document.getElementById('scheduleId');
        const option = select.options[select.selectedIndex];
        const info = document.getElementById('scheduleInfo');

        if (!option || !option.value) {
            info.style.display = 'none';
            return;
        }

        document.getElementById('doctorText').textContent =
            'Dr. ' + option.dataset.doctor + ' (' + option.dataset.specialization + ')';
        document.getElementById('dateTimeText').textContent =
            option.dataset.date + ' | ' + option.dataset.start + ' - ' + option.dataset.end;
        document.getElementById('rateTypeText').textContent = option.dataset.rateType;
        document.getElementById('rateText').textContent = option.dataset.rate;
        document.getElementById('durationText').textContent = option.dataset.duration;
        document.getElementById('chargeText').textContent = option.dataset.charge;
        info.style.display = 'block';
    }

    showScheduleDetails();
</script>
</body>
</html>
