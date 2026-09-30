<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.cliniccare.model.MedicalService" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>

<%
    MedicalService service = (MedicalService) request.getAttribute("service");

    // Opened directly without an id? Go back to the list.
    if (service == null) {
        response.sendRedirect("manageServices");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Service - ClinicCare</title>
    <link rel="stylesheet" href="css/clinic.css">
</head>

<body>

<%@ include file="nav.jspf" %>

<div class="form-container">

    <h2>Edit Service</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= HtmlUtil.escape(request.getAttribute("error")) %></p>
    <% } %>

    <form action="updateService" method="post">

        <input type="hidden" name="serviceId" value="<%= service.getServiceId() %>">

        <label>Service Name</label>
        <input type="text" name="serviceName" maxlength="100"
               value="<%= HtmlUtil.escape(service.getServiceName()) %>" required>

        <label>Description</label>
        <textarea name="description" rows="3"
                  maxlength="255"><%= HtmlUtil.escape(service.getDescription()) %></textarea>

        <label>Price (RM)</label>
        <input type="number" name="price" min="0" step="0.01"
               value="<%= String.format("%.2f", service.getPrice()) %>" required>

        <button type="submit">Update Service</button>

    </form>

    <p class="back"><a href="manageServices">Back to Manage Services</a></p>

</div>

</body>
</html>
