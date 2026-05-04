package controller.servlets;

import controller.DatabaseController;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/manageUsers")
public class AdminUserManageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("userList", new DatabaseController().getAllUsers());
        req.getRequestDispatcher(StringUtils.PAGE_USER_MANAGE).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            new DatabaseController().deleteUser(req.getParameter("userId"));
        }
        res.sendRedirect(req.getContextPath() + "/manageUsers");
    }
}