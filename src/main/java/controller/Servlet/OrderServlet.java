package controller.servlets;

import controller.DatabaseController;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        DatabaseController dao = new DatabaseController();
        req.setAttribute("orderList",       dao.getAllOrderDetails());
        req.setAttribute("totalOrders",     dao.getOrderCount());
        req.setAttribute("pendingOrders",   dao.getPendingCount());
        req.setAttribute("deliveredOrders", dao.getDeliveredCount());
        req.setAttribute("totalUsers",      dao.getUserCount());
        req.getRequestDispatcher(StringUtils.PAGE_ADMIN_DASH).forward(req, res);
    }
}