<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Dashboard - ClinicCare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/clinic.css?v=3">
</head>
<body>
<div class="page" style="max-width:900px;">
    <div class="page-header">
        <div><span class="eyebrow">Patient Portal</span><h1>Patient Dashboard</h1><p>Choose what you want to do in ClinicCare.</p></div>
    </div>
    <div class="card-grid">
        <div class="card"><div class="card-icon">P</div><h3>Patient Profile</h3><p>View or update your registered information.</p><a class="btn btn-primary" href="login.jsp">Open Profile</a></div>
        <div class="card"><div class="card-icon">A</div><h3>Appointments</h3><p>Book and review your clinic appointments.</p><a class="btn btn-secondary" href="login.jsp">Continue</a></div>
    </div>
</div>
</body>
</html>
