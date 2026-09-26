<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>

    <title>Admin Login - ClinicCare</title>

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

        input {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            margin-bottom: 15px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            padding: 10px;
            cursor: pointer;
        }

        .error {
            color: red;
            text-align: center;
        }

    </style>

</head>

<body>

<div class="container">

    <h2>Admin Login</h2>

    <% if (request.getAttribute("error") != null) { %>

        <p class="error">
            <%= request.getAttribute("error") %>
        </p>

    <% } %>

    <form action="adminLogin" method="post">

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

</div>

</body>
</html>