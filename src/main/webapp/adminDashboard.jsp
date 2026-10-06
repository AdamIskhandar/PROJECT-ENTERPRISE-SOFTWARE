<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<title>Admin Dashboard - ClinicCare</title>
		<link
			rel="stylesheet"
			href="<%= request.getContextPath() %>/css/clinic.css?v=3"
		/>
	</head>
	<body>
		<%@ include file="nav.jsp" %>
		<div class="page">
			<div class="page-header">
				<div>
					<span class="eyebrow">Administration</span>
					<h1>Admin Dashboard</h1>
					<p>Manage ClinicCare records and daily appointment operations.</p>
				</div>
				<a class="btn btn-secondary" href="index.jsp">Main Home</a>
			</div>

			<div class="dashboard-cards">
				<div class="card">
					<div class="card-icon">P</div>
					<h3>Manage Patients</h3>
					<p>Add, view, update, delete and search patient records.</p>
					<a class="btn btn-primary" href="managePatients">Open Patients</a>
				</div>

				<div class="card">
					<div class="card-icon">A</div>
					<h3>Manage Appointments</h3>
					<p>
						Review bookings, update appointment status and monitor appointment
						charges.
					</p>
					<a class="btn btn-primary" href="manageAppointments"
						>Open Appointments</a
					>
				</div>

				<div class="card">
					<div class="card-icon">D</div>
					<h3>Manage Doctors</h3>
					<p>
						Maintain doctor information, morning/night rates and available
						schedules.
					</p>
					<a class="btn btn-primary" href="manageDoctors">Open Doctors</a>
				</div>
			</div>

			<div class="info" style="margin-top: 24px">
				Doctor pricing uses separate morning and night hourly rates, while
				appointments use the selected schedule duration to calculate the final
				charge.
			</div>
		</div>
	</body>
</html>
