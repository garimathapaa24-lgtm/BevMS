package controller.servlets;

import controller.DatabaseController;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/deleteBeverage")
public class DeleteBeverageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        new DatabaseController().deleteBeverage(req.getParameter("beverageId"));
        res.sendRedirect(req.getContextPath() + "/pages/beverage_list.jsp");
    }
}