<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Login - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=3">
</head>
<body class="auth-page">
<div class="auth-shell">
    <div class="auth-brand">
        <div class="brand-mark">ClinicCare</div>
        <div>
            <span class="eyebrow" style="color:#bfe5ef;">Patient Portal</span>
            <h1>Welcome back.</h1>
            <p>Sign in to view your patient profile, book a doctor schedule and manage your appointments.</p>
        </div>
        <small>ClinicCare Patient Appointment System</small>
    </div>

    <div class="auth-card">
        <h2>Patient Login</h2>
        <p class="subtitle">Enter your registered email and password.</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>
        <% if (request.getParameter("logout") != null) { %>
            <div class="success">You have logged out successfully.</div>
        <% } %>

        <form action="login" method="post">
            <div class="form-group">
                <label>Email Address</label>
                <input type="email" name="email" placeholder="you@example.com" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" placeholder="Enter your password" required>
            </div>
            <button type="submit">Sign In</button>
        </form>

        <div class="auth-links">
            Don't have an account? <a href="register.jsp">Register here</a><br>
            <a href="index.jsp">Back to home</a>
        </div>
    </div>
</div>
</body>
</html>
