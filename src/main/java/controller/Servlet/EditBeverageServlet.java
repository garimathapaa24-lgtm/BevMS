package controller.servlets;

import controller.DatabaseController;
import model.BeverageModel;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.UUID;

@WebServlet("/editBeverage")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class EditBeverageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String id = req.getParameter("beverageId");
        DatabaseController dao = new DatabaseController();
        req.setAttribute("beverage",   dao.getBeverageById(id));
        req.setAttribute("categories", dao.getAllCategories());
        req.getRequestDispatcher(StringUtils.PAGE_EDIT_BEV).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String bevId    = req.getParameter("beverageId").trim();
        String name     = req.getParameter("beverageName").trim();
        String desc     = req.getParameter("description").trim();
        double price    = Double.parseDouble(req.getParameter("price"));
        double discount = Double.parseDouble(req.getParameter("discount"));
        double discAmt  = price * discount / 100;
        String stock    = req.getParameter("stockQty").trim();
        String catId    = req.getParameter("categoryId");

        Part imgPart = req.getPart("image");
        String imgPath = req.getParameter("existingImage");

        if (imgPart != null && imgPart.getSize() > 0) {
            String fileName  = UUID.randomUUID().toString() + "_" + imgPart.getSubmittedFileName();
            String uploadDir = getServletContext().getRealPath("/resources/images/");
            new File(uploadDir).mkdirs();
            imgPart.write(uploadDir + File.separator + fileName);
            imgPath = "resources/images/" + fileName;
        }

        BeverageModel b = new BeverageModel(bevId, name, desc, price, discount, discAmt, stock, catId, imgPath);
        new DatabaseController().updateBeverage(b);
        res.sendRedirect(req.getContextPath() + "/pages/beverage_list.jsp");
    }
}