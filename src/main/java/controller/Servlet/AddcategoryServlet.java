package controller.servlets;

import controller.DatabaseController;
import model.CategoryModel;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/addCategory")
public class AddCategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("categories", new DatabaseController().getAllCategories());
        req.getRequestDispatcher(StringUtils.PAGE_CAT_LIST).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String id   = req.getParameter("categoryId").trim();
        String name = req.getParameter("categoryName").trim();
        String desc = req.getParameter("categoryDesc").trim();
        new DatabaseController().addCategory(new CategoryModel(id, name, desc));
        res.sendRedirect(req.getContextPath() + "/addCategory");
    }
}