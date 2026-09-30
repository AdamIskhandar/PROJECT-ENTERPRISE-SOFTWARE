<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.cliniccare.model.MedicalService" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>

<%
    // Filled only when the servlet sends the form back after an error
    MedicalService service = (MedicalService) request.getAttribute("service");

    String name = service != null ? service.getServiceName() : "";
    String description = service != null ? service.getDescription() : "";
    String price = service != null
            ? String.format("%.2f", service.getPrice()) : "";
%>

<!DOCTYPE html>
<html>
<head>
    <title>Add Service - ClinicCare</title>
    <link rel="stylesheet" href="css/clinic.css">
</head>

<body>

<%@ include file="nav.jspf" %>

<div class="form-container">

    <h2>Add New Service</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= HtmlUtil.escape(request.getAttribute("error")) %></p>
    <% } %>

    <form action="addService" method="post">

        <label>Service Name</label>
        <input type="text" name="serviceName" maxlength="100"
               value="<%= HtmlUtil.escape(name) %>" required>

        <label>Description</label>
        <textarea name="description" rows="3"
                  maxlength="255"><%= HtmlUtil.escape(description) %></textarea>

        <label>Price (RM)</label>
        <input type="number" name="price" min="0" step="0.01"
               value="<%= HtmlUtil.escape(price) %>" required>

        <button type="submit">Add Service</button>

    </form>

    <p class="back"><a href="manageServices">Back to Manage Services</a></p>

</div>

</body>
</html>
