<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=3">
</head>
<body>
<div class="landing">
    <div class="landing-panel">
        <span class="eyebrow">Java EE Patient Appointment System</span>
        <h1>ClinicCare</h1>
        <p>A simple clinic portal for patient registration, doctor scheduling, appointment management and time-based consultation charges.</p>

        <div class="choice-grid">
            <div class="card">
                <div class="card-icon">P</div>
                <h3>Patient Portal</h3>
                <p>Register, sign in, view your profile, choose an available doctor schedule and manage appointments.</p>
                <div class="button-row">
                    <a class="btn btn-primary" href="login.jsp">Patient Login</a>
                    <a class="btn btn-secondary" href="register.jsp">Create Account</a>
                </div>
            </div>

            <div class="card">
                <div class="card-icon">A</div>
                <h3>Admin Portal</h3>
                <p>Manage patient records, doctors, doctor schedules, fees and appointment statuses from one place.</p>
                <div class="button-row">
                    <a class="btn btn-primary" href="adminLogin.jsp">Admin Login</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
