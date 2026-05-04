<%@ page contentType="text/html;charset=UTF-8" import="java.util.*, model.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>BevServe — Checkout</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/user.css">
</head>
<body>
<nav class="navbar">
    <div class="nav-brand">BevServe</div>
    <div class="nav-links"><a href="<%= request.getContextPath() %>/cart">← Back to Cart</a></div>
</nav>
<div class="container">
    <h2 class="page-title">Checkout</h2>
    <div class="checkout-layout">

        <div class="checkout-form-section">
            <h3>Delivery Details</h3>
            <form action="<%= request.getContextPath() %>/checkout" method="post">
                <input type="hidden" name="grandTotal" value="<%= request.getAttribute("grandTotal") %>">
                <div class="form-group">
                    <label>City</label>
                    <input type="text" name="city" required placeholder="Enter your city">
                </div>
                <div class="form-group">
                    <label>Full Address</label>
                    <textarea name="address" rows="3" required placeholder="Street, Area, Postal Code"></textarea>
                </div>
                <div class="form-group">
                    <label>Payment Method</label>
                    <select name="payment" required>
                        <option value="Cash on Delivery">Cash on Delivery</option>
                        <option value="Card">Card Payment</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary btn-full">Place Order</button>
            </form>
        </div>

        <div class="order-summary-section">
            <h3>Order Summary</h3>
            <% List<BeverageModel> items = (List<BeverageModel>) request.getAttribute("cartItems"); %>
            <% if (items != null) { for (BeverageModel b : items) {
                int qty = Integer.parseInt(b.getQuantity() != null ? b.getQuantity() : "1"); %>
                <div class="summary-row">
                    <span><%= b.getBeverageName() %> × <%= qty %></span>
                    <span>Rs. <%= String.format("%.2f", b.getPrice() * qty) %></span>
                </div>
            <% }} %>
            <hr>
            <div class="summary-row total-row">
                <strong>Total</strong>
                <strong>Rs. <%= request.getAttribute("grandTotal") %></strong>
            </div>
        </div>
    </div>
</div>
</body>
</html>