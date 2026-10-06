<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Patient - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=3">
</head>
<body>
<%@ include file="nav.jsp" %>
<div class="form-container">
    <span class="eyebrow">Patient Management</span>
    <h2>Add New Patient</h2>
    <p class="subtitle">Create a patient account from the admin portal.</p>

    <% if (request.getAttribute("error") != null) { %><div class="error"><%= request.getAttribute("error") %></div><% } %>
    <% if (request.getAttribute("success") != null) { %><div class="success"><%= request.getAttribute("success") %></div><% } %>

    <form action="addPatient" method="post">
        <div class="form-grid">
            <div class="form-group full">
                <label>Full Name</label>
                <input type="text" name="fullName" maxlength="100" required>
            </div>
            <div class="form-group full">
                <label>Email Address</label>
                <input type="email" name="email" maxlength="100" required>
            </div>
            <div class="form-group">
                <label>Phone Number</label>
                <input type="text" name="phone" pattern="[0-9]{10,12}" title="Phone number must contain 10 to 12 digits">
            </div>
            <div class="form-group">
                <label>Gender</label>
                <select name="gender" required>
                    <option value="">Select Gender</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                </select>
            </div>
            <div class="form-group">
                <label>Date of Birth</label>
                <input type="date" name="dateOfBirth" max="<%= java.time.LocalDate.now() %>">
            </div>
            <div class="form-group">
                <label>Temporary Password</label>
                <input type="password" name="password" minlength="6" required>
            </div>
        </div>
        <div class="form-actions">
            <button type="submit">Add Patient</button>
            <a class="button" href="managePatients">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>
