<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Registration - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=3">
</head>
<body class="auth-page">
<div class="auth-shell">
    <div class="auth-brand">
        <div class="brand-mark">ClinicCare</div>
        <div>
            <span class="eyebrow" style="color:#bfe5ef;">New Patient</span>
            <h1>Create your account.</h1>
            <p>Register once to access doctor schedules, appointment booking and your patient information.</p>
        </div>
        <small>Your password is stored securely using password hashing.</small>
    </div>

    <div class="auth-card">
        <h2>Patient Registration</h2>
        <p class="subtitle">Complete the details below to get started.</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="success"><%= request.getAttribute("success") %></div>
        <% } %>

        <form action="register" method="post">
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
                    <label>Password</label>
                    <input type="password" name="password" minlength="6" required>
                </div>
            </div>
            <button type="submit" style="width:100%; margin-top:18px;">Create Account</button>
        </form>
        <div class="auth-links">Already registered? <a href="login.jsp">Patient login</a></div>
    </div>
</div>
</body>
</html>
