<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.Appointment" %>
<%@ page import="com.cliniccare.model.Patient" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>

<%
    List<Appointment> appointments =
        (List<Appointment>) request.getAttribute("appointments");

    Patient patient =
        (Patient) request.getAttribute("patient");

    String keyword =
        (String) request.getAttribute("keyword");

    String selectedStatus =
        (String) request.getAttribute("status");

    String message =
        request.getParameter("message");

    String error =
        request.getParameter("error");

    if (keyword == null) {
        keyword = "";
    }

    if (selectedStatus == null) {
        selectedStatus = "";
    }
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        My Appointments - ClinicCare
    </title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/clinic.css?v=4">

</head>

<body>

<div class="page"
     style="width:min(1200px,94%);">


    <div class="page-header">

        <div>

            <span class="eyebrow">
                Patient Portal
            </span>

            <h1>
                My Appointments
            </h1>

            <p>
                View, update or cancel your appointments.
            </p>

        </div>


        <div class="button-row">

            <% if (patient != null) { %>

                <a class="button"
                   href="patientProfile?id=<%= patient.getPatientId() %>">

                    Back to Profile

                </a>


                <a class="btn btn-primary"
                   href="bookAppointment?patientId=<%= patient.getPatientId() %>&source=patient">

                    + Book Appointment

                </a>

            <% } %>

        </div>

    </div>


    <% if ("booked".equals(message)) { %>

        <div class="success">
            Appointment booked successfully.
        </div>

    <% } %>


    <% if ("updated".equals(message)) { %>

        <div class="success">
            Appointment updated successfully.
        </div>

    <% } %>


    <% if ("cancelled".equals(message)) { %>

        <div class="success">
            Appointment cancelled successfully.
        </div>

    <% } %>


    <% if (error != null) { %>

        <div class="error">

            The requested appointment action
            could not be completed.

        </div>

    <% } %>


    <div class="panel"
         style="padding:20px;">


        <form action="myAppointments"
              method="get"
              class="toolbar">


            <input type="text"
                   name="keyword"
                   value="<%= HtmlUtil.escape(keyword) %>"
                   placeholder="Search doctor, specialization, reason or date">


            <select name="status">

                <option value="">
                    All Statuses
                </option>


                <option value="Pending"
                    <%= "Pending".equalsIgnoreCase(selectedStatus)
                    ? "selected" : "" %>>

                    Pending

                </option>


                <option value="Confirmed"
                    <%= "Confirmed".equalsIgnoreCase(selectedStatus)
                    ? "selected" : "" %>>

                    Confirmed

                </option>


                <option value="Completed"
                    <%= "Completed".equalsIgnoreCase(selectedStatus)
                    ? "selected" : "" %>>

                    Completed

                </option>


                <option value="Cancelled"
                    <%= "Cancelled".equalsIgnoreCase(selectedStatus)
                    ? "selected" : "" %>>

                    Cancelled

                </option>

            </select>


            <button type="submit">
                Search
            </button>


            <a class="button"
               href="myAppointments">

                Show All

            </a>

        </form>


        <div class="table-wrap">

            <table>

                <thead>

                <tr>

                    <th>ID</th>
                    <th>Doctor</th>
                    <th>Schedule</th>
                    <th>Reason</th>
                    <th>Status</th>
                    <th>Charge</th>
                    <th>Actions</th>

                </tr>

                </thead>


                <tbody>


                <% if (appointments == null ||
                        appointments.isEmpty()) { %>


                    <tr>

                        <td colspan="7"
                            class="empty">

                            You do not have any appointments yet.

                        </td>

                    </tr>


                <% } else {

                    for (Appointment appointment
                            : appointments) {

                %>


                    <tr>


                        <td>

                            #<%= appointment.getAppointmentId() %>

                        </td>


                        <td>

                            <strong>

                                <%= HtmlUtil.escape(
                                        appointment.getDoctorName()
                                ) %>

                            </strong>

                            <br>

                            <span class="muted">

                                <%= HtmlUtil.escape(
                                        appointment.getSpecialization()
                                ) %>

                            </span>

                        </td>


                        <td>

                            <strong>

                                <%= appointment.getAppointmentDate() %>

                            </strong>

                            <br>

                            <%= appointment.getStartTime() %>

                            –

                            <%= appointment.getEndTime() %>

                        </td>


                        <td class="reason">

                            <%= appointment.getReason() == null
                                || appointment.getReason().isEmpty()

                                ? "-"

                                : HtmlUtil.escape(
                                    appointment.getReason()
                                )
                            %>

                        </td>


                        <td>

                            <span class="
                                status-badge
                                status-<%= appointment.getStatus() %>">

                                <%= HtmlUtil.escape(
                                    appointment.getStatus()
                                ) %>

                            </span>

                        </td>


                        <td class="charge">

                            <span class="muted">

                                <%= HtmlUtil.escape(
                                    appointment.getRateType()
                                ) %>
                                rate

                            </span>

                            <br>


                            RM
                            <%= String.format(
                                "%.2f",
                                appointment.getRateUsed()
                            ) %>

                            /hr ×

                            <%= String.format(
                                "%.2f",
                                appointment.getDurationHours()
                            ) %>

                            hr


                            <br>


                            <strong>

                                RM
                                <%= String.format(
                                    "%.2f",
                                    appointment.getTotalCharge()
                                ) %>

                            </strong>

                        </td>


                        <td>


                            <% if (
                                !"Cancelled".equalsIgnoreCase(
                                        appointment.getStatus()
                                )
                                &&
                                !"Completed".equalsIgnoreCase(
                                        appointment.getStatus()
                                )
                            ) { %>


                                <a class="action-link"
                                   href="editAppointment?id=<%= appointment.getAppointmentId() %>&source=patient">

                                    Edit

                                </a>


                                <form action="cancelAppointment"
                                      method="post"
                                      class="inline-form"

                                      onsubmit="
                                      return confirm(
                                      'Cancel this appointment?'
                                      );">


                                    <input type="hidden"
                                           name="appointmentId"
                                           value="<%= appointment.getAppointmentId() %>">


                                    <input type="hidden"
                                           name="source"
                                           value="patient">


                                    <button type="submit"
                                            class="link-button"
                                            style="
                                            color:var(--warning)!important;">

                                        Cancel

                                    </button>

                                </form>


                            <% } else { %>


                                <span class="muted">

                                    No action available

                                </span>


                            <% } %>


                        </td>


                    </tr>


                <%

                    }

                }

                %>


                </tbody>

            </table>

        </div>

    </div>

</div>

</body>

</html>