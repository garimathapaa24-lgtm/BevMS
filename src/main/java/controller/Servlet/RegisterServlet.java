package controller.servlets;

import controller.DatabaseController;
import model.UsersModel;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/pages/register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String userId   = req.getParameter("userID").trim();
        String fullName = req.getParameter("fullName").trim();
        String email    = req.getParameter("email").trim();
        String phone    = req.getParameter("phoneNumber").trim();
        String password = req.getParameter("password");
        String repass   = req.getParameter("retypePassword");

        String error = null;
        if (userId.length() < 6)                             error = StringUtils.ERR_USER_ID;
        else if (!fullName.matches("[a-zA-Z ]+"))            error = StringUtils.ERR_NAME;
        else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) error = StringUtils.ERR_EMAIL;
        else if (!phone.matches("\\d{10}"))                  error = StringUtils.ERR_PHONE;
        else if (!password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$"))
                                                              error = StringUtils.ERR_PASS;
        else if (!password.equals(repass))                   error = StringUtils.ERR_MISMATCH;

        if (error != null) {
            req.setAttribute(StringUtils.ATTR_ERROR, error);
            req.getRequestDispatcher("/pages/register.jsp").forward(req, res);
            return;
        }

        DatabaseController dao = new DatabaseController();
        if (dao.checkDuplicacy(StringUtils.SQL_CHECK_USER_ID, userId) ||
            dao.checkDuplicacy(StringUtils.SQL_CHECK_EMAIL, email)    ||
            dao.checkDuplicacy(StringUtils.SQL_CHECK_PHONE, phone)) {
            req.setAttribute(StringUtils.ATTR_ERROR, StringUtils.ERR_DUPLICATE);
            req.getRequestDispatcher("/pages/register.jsp").forward(req, res);
            return;
        }

        UsersModel u = new UsersModel(userId, fullName, email, password, phone, StringUtils.ROLE_USER);
        int result   = dao.addUser(u);

        if (result == 1) {
            res.sendRedirect(req.getContextPath() + "/login?registered=true");
        } else {
            req.setAttribute(StringUtils.ATTR_ERROR, StringUtils.ERR_SERVER);
            req.getRequestDispatcher("/pages/register.jsp").forward(req, res);
        }
    }
}
