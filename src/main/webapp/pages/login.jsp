<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BevServe — Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body class="auth-body">
<div class="auth-card">
    <div class="auth-brand">
        <h1>BevServe</h1>
        <p>Beverage Management System</p>
    </div>

    <% if (request.getParameter("registered") != null) { %>
        <div class="alert alert-success">Registration successful! Please log in.</div>
    <% } %>
    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("errorMessage") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/login" method="post">
        <div class="form-group">
            <label for="userID">User ID</label>
            <input type="text" id="userID" name="userID" required placeholder="Enter your User ID">
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required placeholder="Enter your password">
        </div>
        <button type="submit" class="btn btn-primary btn-full">Log In</button>
    </form>
    <p class="auth-link">New to BevServe? <a href="<%= request.getContextPath() %>/register">Create an account</a></p>
</div>
</body>
</html>