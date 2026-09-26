<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Add Patient - ClinicCare</title>

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

        h2 {
            text-align: center;
        }

        label {
            display: block;
            margin-top: 12px;
        }

        input,
        select {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            padding: 10px;
            margin-top: 20px;
            cursor: pointer;
        }

        .error {
            color: red;
            text-align: center;
        }

        .success {
            color: green;
            text-align: center;
        }

        .back {
            text-align: center;
            margin-top: 15px;
        }
    </style>
</head>

<body>

<div class="container">

    <h2>Add New Patient</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p class="error">
            <%= request.getAttribute("error") %>
        </p>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
        <p class="success">
            <%= request.getAttribute("success") %>
        </p>
    <% } %>

    <form action="addPatient" method="post">

        <label>Full Name</label>
        <input type="text"
               name="fullName"
               required>

        <label>Email</label>
        <input type="email"
               name="email"
               required>

        <label>Phone Number</label>
        <input type="text"
            name="phone"
            pattern="[0-9]{10,12}"
            title="Phone number must contain 10 to 12 digits">

        <label>Password</label>
        <input type="password"
            name="password"
            minlength="6"
            required>

        <label>Gender</label>
        <select name="gender" required>
            <option value="">Select Gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
        </select>

        <label>Date of Birth</label>
        <input type="date"
            name="dateOfBirth"
            max="<%= java.time.LocalDate.now() %>">

        <button type="submit">
            Add Patient
        </button>

    </form>

    <div class="back">
        <a href="managePatients">
            Back to Manage Patients
        </a>
    </div>

</div>

</body>
</html>