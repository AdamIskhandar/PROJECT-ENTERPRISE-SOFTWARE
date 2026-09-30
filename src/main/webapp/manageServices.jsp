<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="com.cliniccare.model.MedicalService" %>
<%@ page import="com.cliniccare.util.HtmlUtil" %>

<%
    List<MedicalService> services =
            (List<MedicalService>) request.getAttribute("services");

    // Opened the JSP directly? Go through the servlet so the list is loaded.
    if (services == null) {
        response.sendRedirect("manageServices");
        return;
    }

    String message = request.getParameter("message");
    String error = request.getParameter("error");
    String keyword = request.getParameter("keyword");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Services - ClinicCare</title>
    <link rel="stylesheet" href="css/clinic.css">
</head>

<body>

<%@ include file="nav.jspf" %>

<div class="container">

    <h2>Manage Medical Services</h2>

    <% if ("added".equals(message)) { %>
        <p class="success">Service added successfully.</p>
    <% } %>

    <% if ("updated".equals(message)) { %>
        <p class="success">Service updated successfully.</p>
    <% } %>

    <% if ("deleted".equals(message)) { %>
        <p class="success">Service deleted successfully.</p>
    <% } %>

    <% if ("delete".equals(error)) { %>
        <p class="error">
            Failed to delete service. It may still be used by appointments.
        </p>
    <% } %>

    <% if ("notfound".equals(error)) { %>
        <p class="error">Service not found.</p>
    <% } %>

    <div style="margin-bottom: 20px;">
        <a href="addService.jsp">Add New Service</a>
    </div>

    <form action="manageServices" method="get" class="search-box">

        <input type="text"
               name="keyword"
               value="<%= HtmlUtil.escape(keyword) %>"
               placeholder="Search service name or description">

        <button type="submit">Search</button>

        <a href="manageServices">Show All</a>

    </form>

    <table>

        <tr>
            <th>ID</th>
            <th>Service Name</th>
            <th>Description</th>
            <th>Price (RM)</th>
            <th>Action</th>
        </tr>

        <% if (services.isEmpty()) { %>

            <tr>
                <td colspan="5" class="empty">No services found.</td>
            </tr>

        <% } %>

        <% for (MedicalService service : services) { %>

            <tr>
                <td><%= service.getServiceId() %></td>
                <td><%= HtmlUtil.escape(service.getServiceName()) %></td>
                <td><%= HtmlUtil.escape(service.getDescription()) %></td>
                <td><%= String.format("%.2f", service.getPrice()) %></td>

                <td>
                    <a class="action-link"
                       href="editService?id=<%= service.getServiceId() %>">
                        Edit
                    </a>

                    <form action="deleteService"
                          method="post"
                          class="inline-form"
                          onsubmit="return confirm('Are you sure you want to delete this service?');">

                        <input type="hidden" name="id"
                               value="<%= service.getServiceId() %>">

                        <button type="submit" class="link-button">Delete</button>
                    </form>
                </td>
            </tr>

        <% } %>

    </table>

</div>

</body>
</html>
