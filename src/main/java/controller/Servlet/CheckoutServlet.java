package controller.servlets;

import controller.DatabaseController;
import model.*;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/checkout")
public class CheckOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String userId = (String) req.getSession().getAttribute(StringUtils.SESSION_USER_ID);
        DatabaseController dao   = new DatabaseController();
        List<BeverageModel> cart = dao.getCartItems(userId);

        if (cart.isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/cart"); return;
        }

        double total = 0;
        for (BeverageModel b : cart) {
            int qty = Integer.parseInt(b.getQuantity() != null ? b.getQuantity() : "1");
            total += b.getPrice() * qty;
        }

        req.setAttribute("cartItems",  cart);
        req.setAttribute("grandTotal", String.format("%.2f", total));
        req.getRequestDispatcher(StringUtils.PAGE_CHECKOUT).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String userId  = (String) req.getSession().getAttribute(StringUtils.SESSION_USER_ID);
        String city    = req.getParameter("city").trim();
        String address = req.getParameter("address").trim();
        String payment = req.getParameter("payment");
        String total   = req.getParameter("grandTotal");

        DatabaseController  dao   = new DatabaseController();
        List<BeverageModel> cart  = dao.getCartItems(userId);

        String orderId = "ORD" + UUID.randomUUID().toString().substring(0, 7).toUpperCase();
        OrderModel order = new OrderModel(orderId, userId, total, "Pending", city, address, payment);
        dao.addOrder(order);

        for (BeverageModel b : cart) {
            String qty = b.getQuantity() != null ? b.getQuantity() : "1";
            dao.addOrderItem(new OrderItemModel(orderId, b.getBeverageId(), qty));
        }

        dao.clearCart(userId);
        req.getSession().setAttribute("lastOrderId", orderId);
        res.sendRedirect(req.getContextPath() + StringUtils.PAGE_ORDER_CONF);
    }
}