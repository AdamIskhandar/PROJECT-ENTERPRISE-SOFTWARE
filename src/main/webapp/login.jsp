<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Login - ClinicCare</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
        }

        .container {
            width: 400px;
            margin: 80px auto;
            background: white;
            padding: 25px;
            border-radius: 8px;
        }

        h2 {
            text-align: center;
        }

        label {
            display: block;
            margin-top: 12px;
        }

        input {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            padding: 10px;
            margin-top: 20px;
            cursor: pointer;
        }

        .error {
            color: red;
            text-align: center;
        }

        .success {
            color: green;
            text-align: center;
        }

        .link {
            text-align: center;
            margin-top: 15px;
        }
    </style>
</head>

<body>

<div class="container">

    <h2>ClinicCare Login</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p class="error">
            <%= request.getAttribute("error") %>
        </p>
    <% } %>

    <% if (request.getParameter("logout") != null) { %>
        <p class="success">
            You have logged out successfully.
        </p>
    <% } %>

    <form action="login" method="post">

        <label>Email</label>
        <input type="email"
               name="email"
               required>

        <label>Password</label>
        <input type="password"
               name="password"
               required>

        <button type="submit">
            Login
        </button>

    </form>

    <div class="link">
        Don't have an account?
        <a href="register.jsp">Register</a>
    </div>

</div>

</body>
</html>