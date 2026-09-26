<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cliniccare.dao.PatientDAO" %>
<%@ page import="com.cliniccare.model.Patient" %>

<%
    String id = request.getParameter("id");

    Patient patient = null;

    if (id != null) {

        PatientDAO dao = new PatientDAO();

        patient = dao.getPatientById(
                Integer.parseInt(id)
        );
    }
%>

<%
    String source = request.getParameter("source");
%>

<% if (request.getAttribute("error") != null) { %>

    <p style="color:red; text-align:center;">
        <%= request.getAttribute("error") %>
    </p>

<% } %>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Patient Profile</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
        }

        .container {
            width: 450px;
            margin: 50px auto;
            background: white;
            padding: 25px;
            border-radius: 8px;
        }

        input,
        select {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            margin-bottom: 12px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            padding: 10px;
            cursor: pointer;
        }
    </style>
</head>

<body>

<div class="container">

    <h2>Edit Profile</h2>

    <% if (patient != null) { %>

    <form action="updatePatient" method="post">

        <input type="hidden"
               name="patientId"
               value="<%= patient.getPatientId() %>">

        <input type="hidden"
                name="source"
                value="<%= source != null ? source : "patient" %>">

        <label>Full Name</label>
        <input type="text"
               name="fullName"
               value="<%= patient.getFullName() %>"
               required>

        <label>Email</label>
        <input type="email"
               name="email"
               value="<%= patient.getEmail() %>"
               required>

        <label>Phone</label>
        <input type="text"
               name="phone"
               value="<%= patient.getPhone() %>">

        <label>Gender</label>

        <select name="gender">

            <option value="Male"
                <%= "Male".equals(patient.getGender())
                    ? "selected" : "" %>>
                Male
            </option>

            <option value="Female"
                <%= "Female".equals(patient.getGender())
                    ? "selected" : "" %>>
                Female
            </option>

        </select>

        <label>Date of Birth</label>

        <input type="date"
               name="dateOfBirth"
               value="<%= patient.getDateOfBirth() %>">

        <button type="submit">
            Update Profile
        </button>

    </form>

    <% } %>

</div>

</body>
</html>