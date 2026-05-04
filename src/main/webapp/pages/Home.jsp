<%@ page contentType="text/html;charset=UTF-8" import="java.util.*, model.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BevServe — Home</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/user.css">
</head>
<body>
<%-- Navigation --%>
<nav class="navbar">
    <div class="nav-brand">BevServe</div>
    <form class="nav-search" action="<%= request.getContextPath() %>/home" method="get">
        <input type="text" name="search" placeholder="Search beverages..." value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
        <button type="submit">🔍</button>
    </form>
    <div class="nav-links">
        <a href="<%= request.getContextPath() %>/cart" class="cart-link">
            🛒 <span class="cart-badge"><%= request.getAttribute("cartCount") != null ? request.getAttribute("cartCount") : 0 %></span>
        </a>
        <a href="<%= request.getContextPath() %>/profile"><%= session.getAttribute("userID") %></a>
        <a href="<%= request.getContextPath() %>/inquiry">Contact</a>
        <a href="<%= request.getContextPath() %>/pages/about.jsp">About</a>
        <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline">Logout</a>
    </div>
</nav>

<div class="container">
    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("errorMessage") %></div>
    <% } %>
    <% if (request.getAttribute("successMessage") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("successMessage") %></div>
    <% } %>

    <%-- Category Filters --%>
    <div class="category-filters">
        <a href="<%= request.getContextPath() %>/home" class="filter-btn">All</a>
        <% List<CategoryModel> cats = (List<CategoryModel>) request.getAttribute("categories"); %>
        <% if (cats != null) { for (CategoryModel c : cats) { %>
            <a href="<%= request.getContextPath() %>/home?category=<%= c.getCategoryId() %>" class="filter-btn"><%= c.getCategoryName() %></a>
        <% }} %>
    </div>

    <%-- Beverage Grid --%>
    <div class="bev-grid">
    <% List<BeverageModel> bevs = (List<BeverageModel>) request.getAttribute("beverageList"); %>
    <% if (bevs != null && !bevs.isEmpty()) {
        for (BeverageModel b : bevs) { %>
        <div class="bev-card">
            <div class="bev-img-wrap">
                <img src="<%= request.getContextPath() %>/<%= b.getImageUrl() %>"
                     alt="<%= b.getBeverageName() %>" class="bev-img"
                     onerror="this.src='<%= request.getContextPath() %>/resources/images/default.png'">
            </div>
            <div class="bev-info">
                <span class="bev-category"><%= b.getCategoryName() %></span>
                <h3 class="bev-name"><%= b.getBeverageName() %></h3>
                <p class="bev-desc"><%= b.getDescription() != null ? b.getDescription() : "" %></p>
                <div class="bev-footer">
                    <span class="bev-price">Rs. <%= String.format("%.2f", b.getPrice()) %></span>
                    <form action="<%= request.getContextPath() %>/cart" method="post">
                        <input type="hidden" name="beverageId" value="<%= b.getBeverageId() %>">
                        <input type="hidden" name="quantity"   value="1">
                        <button type="submit" class="btn btn-primary btn-sm">Add to Cart</button>
                    </form>
                </div>
            </div>
        </div>
    <% }} else { %>
        <div class="empty-state"><p>No beverages found.</p></div>
    <% } %>
    </div>
</div>
</body>
</html>