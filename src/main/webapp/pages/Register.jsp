<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BevServe — Register</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body class="auth-body">
<div class="auth-card">
    <div class="auth-brand"><h1>BevServe</h1><p>Create your account</p></div>

    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("errorMessage") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/register" method="post">
        <div class="form-group">
            <label>User ID</label>
            <input type="text" name="userID" required placeholder="Min 6 characters">
        </div>
        <div class="form-group">
            <label>Full Name</label>
            <input type="text" name="fullName" required placeholder="Letters and spaces only">
        </div>
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" required placeholder="you@example.com">
        </div>
        <div class="form-group">
            <label>Phone Number</label>
            <input type="text" name="phoneNumber" required placeholder="10 digits">
        </div>
        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" required placeholder="Min 8 chars, uppercase, digit, special">
        </div>
        <div class="form-group">
            <label>Retype Password</label>
            <input type="password" name="retypePassword" required placeholder="Confirm password">
        </div>
        <button type="submit" class="btn btn-primary btn-full">Register</button>
    </form>
    <p class="auth-link">Already have an account? <a href="<%= request.getContextPath() %>/login">Log in</a></p>
</div>
</body>
</html>