package controller.servlets;

import controller.DatabaseController;
import model.BeverageModel;
import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.UUID;

@WebServlet("/addBeverage")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class AddBeverageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("categories", new DatabaseController().getAllCategories());
        req.getRequestDispatcher(StringUtils.PAGE_ADD_BEV).forward(req, res);
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

        // Handle image upload
        Part   imgPart  = req.getPart("image");
        String fileName = UUID.randomUUID().toString() + "_" + imgPart.getSubmittedFileName();
        String uploadDir = getServletContext().getRealPath("/resources/images/");
        new File(uploadDir).mkdirs();
        imgPart.write(uploadDir + File.separator + fileName);
        String imgPath = "resources/images/" + fileName;

        BeverageModel b = new BeverageModel(bevId, name, desc, price, discount, discAmt, stock, catId, imgPath);
        int result      = new DatabaseController().addBeverage(b);

        if (result == 1) {
            req.setAttribute(StringUtils.ATTR_SUCCESS, "Beverage added successfully!");
        } else {
            req.setAttribute(StringUtils.ATTR_ERROR, "Failed to add beverage.");
        }
        req.setAttribute("categories", new DatabaseController().getAllCategories());
        req.getRequestDispatcher(StringUtils.PAGE_ADD_BEV).forward(req, res);
    }
}