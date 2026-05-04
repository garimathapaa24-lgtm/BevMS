package controller.servlets;

import controller.DatabaseController;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher(StringUtils.PAGE_LOGIN).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String userId   = req.getParameter("userID").trim();
        String password = req.getParameter("password");

        int result = new DatabaseController().getUserLoginInfo(userId, password);

        switch (result) {
            case 0:
                req.setAttribute(StringUtils.ATTR_ERROR, StringUtils.ERR_NO_USER);
                req.getRequestDispatcher(StringUtils.PAGE_LOGIN).forward(req, res);
                break;
            case 4:
                req.setAttribute(StringUtils.ATTR_ERROR, StringUtils.ERR_LOGIN);
                req.getRequestDispatcher(StringUtils.PAGE_LOGIN).forward(req, res);
                break;
            case 1: // admin
                req.getSession().setAttribute(StringUtils.SESSION_USER_ID,  userId);
                req.getSession().setAttribute(StringUtils.SESSION_IS_ADMIN, true);
                res.sendRedirect(req.getContextPath() + "/orders");
                break;
            case 2: // user
                req.getSession().setAttribute(StringUtils.SESSION_USER_ID,  userId);
                req.getSession().setAttribute(StringUtils.SESSION_IS_ADMIN, false);
                res.sendRedirect(req.getContextPath() + "/home");
                break;
            default:
                req.setAttribute(StringUtils.ATTR_ERROR, StringUtils.ERR_SERVER);
                req.getRequestDispatcher(StringUtils.PAGE_LOGIN).forward(req, res);
        }
    }
}