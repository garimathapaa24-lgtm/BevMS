package controller.servlets;

import controller.DatabaseController;
import model.BeverageModel;
import model.CartModel;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String userId = (String) req.getSession().getAttribute(StringUtils.SESSION_USER_ID);
        DatabaseController dao = new DatabaseController();
        List<BeverageModel> items = dao.getCartItems(userId);

        // Calculate grand total
        double total = 0;
        for (BeverageModel b : items) {
            int qty = Integer.parseInt(b.getQuantity() != null ? b.getQuantity() : "1");
            total += b.getPrice() * qty;
        }

        req.setAttribute("cartItems",  items);
        req.setAttribute("grandTotal", String.format("%.2f", total));
        req.setAttribute("cartCount",  dao.getCartCount(userId));
        req.getRequestDispatcher(StringUtils.PAGE_CART).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String userId     = (String) req.getSession().getAttribute(StringUtils.SESSION_USER_ID);
        String beverageId = req.getParameter("beverageId");
        String quantity   = req.getParameter("quantity") != null ? req.getParameter("quantity") : "1";

        DatabaseController dao = new DatabaseController();
        if (dao.checkCartDuplicate(userId, beverageId)) {
            req.setAttribute(StringUtils.ATTR_ERROR, StringUtils.ERR_CART_DUP);
            req.setAttribute("beverageList", dao.getAllBeverages());
            req.setAttribute("categories",   dao.getAllCategories());
            req.setAttribute("cartCount",    dao.getCartCount(userId));
            req.getRequestDispatcher(StringUtils.PAGE_HOME).forward(req, res);
            return;
        }

        String cartId = "CRT" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        dao.addToCart(new CartModel(cartId, userId, beverageId, quantity));
        res.sendRedirect(req.getContextPath() + "/cart");
    }
}