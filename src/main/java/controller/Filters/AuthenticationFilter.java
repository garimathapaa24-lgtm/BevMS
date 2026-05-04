package controller.filters;

import util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {
        "/pages/login.jsp", "/pages/register.jsp", "/login", "/register"
    };

    private static final String[] ADMIN_PATHS = {
        "/pages/order_list.jsp", "/pages/beverage_list.jsp",
        "/pages/add_beverage.jsp", "/pages/edit_beverage.jsp",
        "/pages/category_list.jsp", "/pages/manage_users.jsp",
        "/orders", "/addBeverage", "/editBeverage", "/deleteBeverage",
        "/addCategory", "/editCategory", "/deleteCategory",
        "/updateStatus", "/manageUsers"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest)  request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getServletPath();

        // Allow static resources always
        if (path.startsWith("/css")       || path.startsWith("/js")
         || path.startsWith("/resources") || path.endsWith(".ico")) {
            chain.doFilter(request, response); return;
        }

        // Allow public pages
        for (String p : PUBLIC_PATHS) {
            if (path.startsWith(p)) { chain.doFilter(request, response); return; }
        }

        HttpSession session  = req.getSession(false);
        boolean loggedIn     = session != null && session.getAttribute(StringUtils.SESSION_USER_ID) != null;

        if (!loggedIn) {
            res.sendRedirect(req.getContextPath() + StringUtils.PAGE_LOGIN);
            return;
        }

        // Role check: block non-admins from admin pages
        Boolean isAdmin = (Boolean) session.getAttribute(StringUtils.SESSION_IS_ADMIN);
        if (isAdmin == null || !isAdmin) {
            for (String adminPath : ADMIN_PATHS) {
                if (path.startsWith(adminPath)) {
                    res.sendRedirect(req.getContextPath() + "/home");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override public void init(FilterConfig cfg) {}
    @Override public void destroy() {}
}