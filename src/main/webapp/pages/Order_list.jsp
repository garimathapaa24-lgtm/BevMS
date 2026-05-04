<%@ page contentType="text/html;charset=UTF-8" import="java.util.*, model.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>BevServe Admin — Orders</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body class="admin-body">
<%@ include file="admin_sidebar.jsp" %>

<div class="admin-content">
    <div class="admin-header">
        <h2>Order Management</h2>
        <span>Welcome, Admin &nbsp;<%= session.getAttribute("userID") %></span>
    </div>

    <%-- KPI Cards --%>
    <div class="kpi-grid">
        <div class="kpi-card"><div class="kpi-value"><%= request.getAttribute("totalOrders") %></div><div class="kpi-label">Total Orders</div></div>
        <div class="kpi-card kpi-warning"><div class="kpi-value"><%= request.getAttribute("pendingOrders") %></div><div class="kpi-label">Pending</div></div>
        <div class="kpi-card kpi-success"><div class="kpi-value"><%= request.getAttribute("deliveredOrders") %></div><div class="kpi-label">Delivered</div></div>
        <div class="kpi-card kpi-info"><div class="kpi-value"><%= request.getAttribute("totalUsers") %></div><div class="kpi-label">Total Users</div></div>
    </div>

    <%-- Orders Table --%>
    <div class="table-card">
        <table class="data-table">
            <thead>
                <tr><th>Order ID</th><th>Customer</th><th>Beverage</th><th>Amount</th><th>Status</th><th>Update</th></tr>
            </thead>
            <tbody>
            <% List<CustomerTransactionView> orders = (List<CustomerTransactionView>) request.getAttribute("orderList"); %>
            <% if (orders != null) { for (CustomerTransactionView o : orders) { %>
            <tr>
                <td><%= o.getOrderId() %></td>
                <td><%= o.getCustomerName() %></td>
                <td><%= o.getBeverageName() %></td>
                <td>Rs. <%= o.getTotalAmount() %></td>
                <td><span class="status-badge status-<%= o.getStatus().toLowerCase() %>"><%= o.getStatus() %></span></td>
                <td>
                    <form action="<%= request.getContextPath() %>/updateStatus" method="post" style="display:flex;gap:6px">
                        <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
                        <select name="status" class="form-select-sm">
                            <option value="Pending"    <%= "Pending".equals(o.getStatus())    ? "selected" : "" %>>Pending</option>
                            <option value="Processing" <%= "Processing".equals(o.getStatus()) ? "selected" : "" %>>Processing</option>
                            <option value="Delivered"  <%= "Delivered".equals(o.getStatus())  ? "selected" : "" %>>Delivered</option>
                            <option value="Cancelled"  <%= "Cancelled".equals(o.getStatus())  ? "selected" : "" %>>Cancelled</option>
                        </select>
                        <button type="submit" class="btn btn-primary btn-sm">Update</button>
                    </form>
                </td>
            </tr>
            <% }} %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>