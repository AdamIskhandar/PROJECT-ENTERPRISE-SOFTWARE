<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.Patient" %>

<%
    List<Patient> patients =
        (List<Patient>) request.getAttribute("patients");
%>

<!DOCTYPE html>
<html>
<head>

    <title>Manage Patients - ClinicCare</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
        }

        .container {
            width: 90%;
            margin: 40px auto;
            background: white;
            padding: 25px;
            border-radius: 8px;
        }

        h2 {
            text-align: center;
        }

        .search-box {
            margin-bottom: 20px;
        }

        .search-box input {
            padding: 8px;
            width: 250px;
        }

        .search-box button {
            padding: 8px 15px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th,
        td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #eee;
        }

        .action-link {
            margin-right: 10px;
        }

        .success {
            background-color: #e8f5e9;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            text-align: center;
        }

        .error {
            background-color: #ffebee;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            text-align: center;
        }

    </style>

</head>

<body>

<div class="container">

    <%
        String message = request.getParameter("message");
        String error = request.getParameter("error");
    %>

    <h2>Manage Patients</h2>

    <% if ("added".equals(message)) { %>

    <p class="success">
        Patient added successfully.
    </p>

    <% } %>


    <% if ("updated".equals(message)) { %>

        <p class="success">
            Patient updated successfully.
        </p>

    <% } %>


    <% if ("deleted".equals(message)) { %>

        <p class="success">
            Patient deleted successfully.
        </p>

    <% } %>


    <% if ("update".equals(error)) { %>

        <p class="error">
            Failed to update patient.
        </p>

    <% } %>


    <% if ("delete".equals(error)) { %>

        <p class="error">
            Failed to delete patient.
        </p>

    <% } %>

    <form action="managePatients"
          method="get"
          class="search-box">

        <div style="margin-bottom: 20px;">
            <a href="addPatient.jsp">
                Add New Patient
            </a>
        </div>

        <input type="text"
               name="keyword"
               placeholder="Search name, email or phone">

        <button type="submit">
            Search
        </button>

        <a href="managePatients">
            Show All
        </a>

    </form>

    <table>

        <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Gender</th>
            <th>Date of Birth</th>
            <th>Action</th>
        </tr>

        <% if (patients != null) { %>

            <% for (Patient patient : patients) { %>

                <tr>

                    <td>
                        <%= patient.getPatientId() %>
                    </td>

                    <td>
                        <%= patient.getFullName() %>
                    </td>

                    <td>
                        <%= patient.getEmail() %>
                    </td>

                    <td>
                        <%= patient.getPhone() %>
                    </td>

                    <td>
                        <%= patient.getGender() %>
                    </td>

                    <td>
                        <%= patient.getDateOfBirth() %>
                    </td>

                    <td>

                        <a class="action-link"
                           href="editPatient.jsp?id=<%= patient.getPatientId() %>&source=admin">
                            Edit
                        </a>

                        <a class="action-link"
                           href="deletePatient?id=<%= patient.getPatientId() %>"
                           onclick="return confirm('Are you sure you want to delete this patient?');">
                            Delete
                        </a>

                    </td>

                </tr>

            <% } %>

        <% } %>

    </table>

</div>

</body>
</html>