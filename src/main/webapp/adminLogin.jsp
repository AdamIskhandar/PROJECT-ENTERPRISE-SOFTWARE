<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=3">
</head>
<body class="auth-page">
<div class="auth-shell">
    <div class="auth-brand">
        <div class="brand-mark">ClinicCare</div>
        <div>
            <span class="eyebrow" style="color:#bfe5ef;">Administration</span>
            <h1>Clinic operations, organised.</h1>
            <p>Access patient management, doctor information, doctor schedules, fees and appointment records.</p>
        </div>
        <small>Authorised staff access only</small>
    </div>

    <div class="auth-card">
        <h2>Admin Login</h2>
        <p class="subtitle">Use your administrator account to continue.</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form action="adminLogin" method="post">
            <div class="form-group">
                <label>Email Address</label>
                <input type="email" name="email" placeholder="admin@cliniccare.com" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" placeholder="Enter your password" required>
            </div>
            <button type="submit">Sign In as Admin</button>
        </form>

        <div class="auth-links"><a href="index.jsp">Back to home</a></div>
    </div>
</div>
</body>
</html>
