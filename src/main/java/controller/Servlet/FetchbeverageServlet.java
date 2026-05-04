package controller.servlets;

import controller.DatabaseController;
import model.BeverageModel;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class FetchBeveragesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String userId   = (String) req.getSession().getAttribute(StringUtils.SESSION_USER_ID);
        String category = req.getParameter("category");
        String search   = req.getParameter("search");

        DatabaseController dao    = new DatabaseController();
        List<BeverageModel> bevs;

        if (search != null && !search.trim().isEmpty()) {
            bevs = dao.searchBeverages(search.trim());
        } else if (category != null && !category.trim().isEmpty()) {
            bevs = dao.getBeveragesByCategory(category.trim());
        } else {
            bevs = dao.getAllBeverages();
        }

        req.setAttribute("beverageList", bevs);
        req.setAttribute("categories",   dao.getAllCategories());
        req.setAttribute("cartCount",    dao.getCartCount(userId));
        req.getRequestDispatcher(StringUtils.PAGE_HOME).forward(req, res);
    }
}