<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cliniccare.model.Patient" %>

<%
    Patient patient =
        (Patient) request.getAttribute("patient");
%>

<%
    String message = request.getParameter("message");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Patient Profile - ClinicCare</title>



    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
        }

        .container {
            width: 500px;
            margin: 50px auto;
            background: white;
            padding: 25px;
            border-radius: 8px;
        }

        h2 {
            text-align: center;
        }

        .row {
            margin-bottom: 15px;
        }

        .label {
            font-weight: bold;
        }

        .button {
            display: inline-block;
            padding: 10px 15px;
            text-decoration: none;
            background-color: #eee;
            margin-top: 15px;
        }
    </style>
</head>

<body>

<div class="container">

    <h2>Patient Profile</h2>

    <% if ("updated".equals(message)) { %>

    <p style="text-align:center;">
        Profile updated successfully.
    </p>

    <% } %>

    <% if (patient != null) { %>

        <div class="row">
            <span class="label">Patient ID:</span>
            <%= patient.getPatientId() %>
        </div>

        <div class="row">
            <span class="label">Full Name:</span>
            <%= patient.getFullName() %>
        </div>

        <div class="row">
            <span class="label">Email:</span>
            <%= patient.getEmail() %>
        </div>

        <div class="row">
            <span class="label">Phone:</span>
            <%= patient.getPhone() %>
        </div>

        <div class="row">
            <span class="label">Gender:</span>
            <%= patient.getGender() %>
        </div>

        <div class="row">
            <span class="label">Date of Birth:</span>
            <%= patient.getDateOfBirth() %>
        </div>

        <a class="button"
           href="editPatient.jsp?id=<%= patient.getPatientId() %>">
            Edit Profile
        </a>

    <% } %>

</div>

</body>
</html>