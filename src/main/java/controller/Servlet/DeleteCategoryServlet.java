package controller.servlets;

import controller.DatabaseController;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/deleteCategory")
public class DeleteCategoryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        new DatabaseController().deleteCategory(req.getParameter("categoryId"));
        res.sendRedirect(req.getContextPath() + "/addCategory");
    }
}