<%@ page contentType="text/html;charset=UTF-8" import="java.util.*, model.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>BevServe — My Cart</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/user.css">
</head>
<body>
<nav class="navbar">
    <div class="nav-brand"><a href="<%= request.getContextPath() %>/home">BevServe</a></div>
    <div class="nav-links">
        <a href="<%= request.getContextPath() %>/home">← Continue Shopping</a>
        <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline">Logout</a>
    </div>
</nav>

<div class="container">
    <h2 class="page-title">My Cart</h2>

    <% List<BeverageModel> items = (List<BeverageModel>) request.getAttribute("cartItems"); %>
    <% if (items != null && !items.isEmpty()) { %>
    <div class="cart-layout">
        <div class="cart-items">
            <table class="data-table">
                <thead>
                    <tr><th>Item</th><th>Price</th><th>Qty</th><th>Total</th><th>Action</th></tr>
                </thead>
                <tbody>
                <% double grandTotal = 0;
                   for (BeverageModel b : items) {
                       int qty = Integer.parseInt(b.getQuantity() != null ? b.getQuantity() : "1");
                       double lineTotal = b.getPrice() * qty;
                       grandTotal += lineTotal; %>
                <tr>
                    <td>
                        <div class="cart-item-name">
                            <img src="<%= request.getContextPath() %>/<%= b.getImageUrl() %>" width="48" height="48" style="border-radius:6px;object-fit:cover">
                            <span><%= b.getBeverageName() %></span>
                        </div>
                    </td>
                    <td>Rs. <%= String.format("%.2f", b.getPrice()) %></td>
                    <td><%= qty %></td>
                    <td>Rs. <%= String.format("%.2f", lineTotal) %></td>
                    <td>
                        <form action="<%= request.getContextPath() %>/removeCart" method="post">
                            <input type="hidden" name="cartId" value="<%= b.getStockQuantity() %>">
                            <button type="submit" class="btn btn-danger btn-sm">Remove</button>
                        </form>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <div class="cart-summary">
            <h3>Order Summary</h3>
            <div class="summary-row"><span>Grand Total</span><strong>Rs. <%= String.format("%.2f", grandTotal) %></strong></div>
            <form action="<%= request.getContextPath() %>/checkout" method="get">
                <input type="hidden" name="grandTotal" value="<%= String.format("%.2f", grandTotal) %>">
                <button type="submit" class="btn btn-primary btn-full">Proceed to Checkout</button>
            </form>
        </div>
    </div>
    <% } else { %>
        <div class="empty-state">
            <p>Your cart is empty.</p>
            <a href="<%= request.getContextPath() %>/home" class="btn btn-primary">Browse Beverages</a>
        </div>
    <% } %>
</div>
</body>
</html>