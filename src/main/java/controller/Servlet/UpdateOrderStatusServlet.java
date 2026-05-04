package controller.servlets;

import controller.DatabaseController;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/updateStatus")
public class UpdateOrderStatusServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String orderId = req.getParameter("orderId");
        String status  = req.getParameter("status");
        new DatabaseController().updateOrderStatus(orderId, status);
        res.sendRedirect(req.getContextPath() + "/orders");
    }
}