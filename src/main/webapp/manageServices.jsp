<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<title>Services - ClinicCare</title>
		<link
			rel="stylesheet"
			href="<%= request.getContextPath() %>/css/clinic.css?v=3"
		/>
	</head>
	<body>
		<%@ include file="nav.jsp" %>
		<div class="page" style="max-width: 780px">
			<div class="page-header">
				<div>
					<span class="eyebrow">Database Alignment</span>
					<h1>Medical Services</h1>
					<p>This module is not enabled in the current ClinicCare database.</p>
				</div>
			</div>
			<div class="warning">
				The supplied database contains Admin, Patient, Doctor, Fees, Scheduling
				and Appointment tables, but no Medical Service table. This legacy page
				has therefore been disabled to prevent the interface from showing data
				that cannot be stored by the current schema.
			</div>
			<a class="button" href="adminDashboard.jsp">Back to Dashboard</a>
		</div>
	</body>
</html>
