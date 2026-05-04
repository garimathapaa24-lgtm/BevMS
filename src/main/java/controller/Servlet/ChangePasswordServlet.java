package controller.servlets;

import controller.DatabaseController;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/changePassword")
public class ChangePasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String userId  = (String) req.getSession().getAttribute(StringUtils.SESSION_USER_ID);
        String current = req.getParameter("currentPassword");
        String newPass = req.getParameter("newPassword");
        String confirm = req.getParameter("confirmPassword");

        if (!newPass.equals(confirm)) {
            req.setAttribute(StringUtils.ATTR_ERROR, StringUtils.ERR_MISMATCH);
        } else {
            int result = new DatabaseController().changePassword(userId, current, newPass);
            if (result == 1) req.setAttribute(StringUtils.ATTR_SUCCESS, "Password changed successfully!");
            else if (result == 4) req.setAttribute(StringUtils.ATTR_ERROR, "Current password is incorrect.");
            else req.setAttribute(StringUtils.ATTR_ERROR, StringUtils.ERR_SERVER);
        }

        DatabaseController dao = new DatabaseController();
        req.setAttribute("profile", dao.getProfileInfo(userId));
        req.getRequestDispatcher(StringUtils.PAGE_PROFILE).forward(req, res);
    }
}