package controller.servlets;

import controller.DatabaseController;
import model.UsersModel;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.UUID;

@WebServlet("/profile")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024)
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String userId = (String) req.getSession().getAttribute(StringUtils.SESSION_USER_ID);
        req.setAttribute("profile", new DatabaseController().getProfileInfo(userId));
        req.getRequestDispatcher(StringUtils.PAGE_PROFILE).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String userId  = (String) req.getSession().getAttribute(StringUtils.SESSION_USER_ID);
        String name    = req.getParameter("fullName").trim();
        String address = req.getParameter("fullAddress") != null ? req.getParameter("fullAddress").trim() : "";

        DatabaseController dao = new DatabaseController();

        // Handle profile photo upload
        Part imgPart = req.getPart("profileImage");
        if (imgPart != null && imgPart.getSize() > 0) {
            String fileName  = UUID.randomUUID() + "_" + imgPart.getSubmittedFileName();
            String uploadDir = getServletContext().getRealPath("/resources/images/");
            new File(uploadDir).mkdirs();
            imgPart.write(uploadDir + File.separator + fileName);
            dao.updateUserImage(userId, "resources/images/" + fileName);
        }

        dao.userProfileUpdate(new UsersModel(userId, name, address));
        req.setAttribute(StringUtils.ATTR_SUCCESS, "Profile updated successfully!");
        req.setAttribute("profile", dao.getProfileInfo(userId));
        req.getRequestDispatcher(StringUtils.PAGE_PROFILE).forward(req, res);
    }
}