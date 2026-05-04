package controller.servlets;

import controller.DatabaseController;
import model.InquiryModel;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/inquiry")
public class InquiryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher(StringUtils.PAGE_CONTACT).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String userId  = (String) req.getSession().getAttribute(StringUtils.SESSION_USER_ID);
        String subject = req.getParameter("subject").trim();
        String message = req.getParameter("message").trim();
        String inqId   = "INQ" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        int result = new DatabaseController().addInquiry(new InquiryModel(inqId, userId, subject, null, message));
        if (result == 1) req.setAttribute(StringUtils.ATTR_SUCCESS, "Your inquiry has been submitted. Thank you!");
        else             req.setAttribute(StringUtils.ATTR_ERROR, StringUtils.ERR_SERVER);

        req.getRequestDispatcher(StringUtils.PAGE_CONTACT).forward(req, res);
    }
}