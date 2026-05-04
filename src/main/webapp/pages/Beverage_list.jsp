<%@ page contentType="text/html;charset=UTF-8" import="java.util.*, model.*" %>
<%
    List<BeverageModel> bevs = new controller.DatabaseController().getAllBeverages();
    pageContext.setAttribute("bevs", bevs);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin — Beverages</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body class="admin-body">
<%@ include file="admin_sidebar.jsp" %>
<div class="admin-content">
    <div class="admin-header">
        <h2>Beverage Management</h2>
        <a href="<%= request.getContextPath() %>/addBeverage" class="btn btn-primary">+ Add Beverage</a>
    </div>

    <% if (request.getAttribute("successMessage") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("successMessage") %></div>
    <% } %>

    <div class="table-card">
        <table class="data-table">
            <thead>
                <tr><th>Image</th><th>ID</th><th>Name</th><th>Category</th><th>Price</th><th>Stock</th><th>Actions</th></tr>
            </thead>
            <tbody>
            <% for (BeverageModel b : bevs) { %>
            <tr>
                <td><img src="<%= request.getContextPath() %>/<%= b.getImageUrl() %>" width="50" height="50" style="border-radius:6px;object-fit:cover"></td>
                <td><%= b.getBeverageId() %></td>
                <td><%= b.getBeverageName() %></td>
                <td><%= b.getCategoryName() %></td>
                <td>Rs. <%= b.getPrice() %></td>
                <td><%= b.getStockQuantity() %></td>
                <td>
                    <a href="<%= request.getContextPath() %>/editBeverage?beverageId=<%= b.getBeverageId() %>" class="btn btn-outline btn-sm">Edit</a>
                    <form action="<%= request.getContextPath() %>/deleteBeverage" method="post" style="display:inline"
                          onsubmit="return confirm('Delete this beverage?')">
                        <input type="hidden" name="beverageId" value="<%= b.getBeverageId() %>">
                        <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                    </form>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>\