package controller;

import model.*;
import util.StringUtils;

import java.sql.*;
import java.util.*;

/**
 * Central DAO class. All database operations go through here.
 * Uses PreparedStatement to prevent SQL injection.
 */
public class DatabaseController {


    public Connection getConnection() throws Exception {
        Class.forName(StringUtils.DRIVER);
        return DriverManager.getConnection(
            StringUtils.CONN_LINK, StringUtils.DB_USER, StringUtils.DB_PASS);
    }

  
    public boolean checkDuplicacy(String sql, String value) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            return ps.executeQuery().next();
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean checkCartDuplicate(String userId, String beverageId) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_CHECK_CART_DUP)) {
            ps.setString(1, userId);
            ps.setString(2, beverageId);
            return ps.executeQuery().next();
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

   

    /** Returns 1=admin, 2=user, 0=not found, 4=wrong password, -1=error */
    public int getUserLoginInfo(String userId, String password) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_GET_LOGIN)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return 0;
            String stored = rs.getString("Password");
            String plain  = PasswordEncryptionWithAes.decrypt(stored, userId);
            if (!plain.equals(password)) return 4;
            return StringUtils.ROLE_ADMIN.equals(rs.getString("Role")) ? 1 : 2;
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public int addUser(UsersModel u) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_INSERT_USER)) {
            ps.setString(1, u.getUserId());
            ps.setString(2, u.getFullName());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getPhoneNumber());
            ps.setString(5, PasswordEncryptionWithAes.encrypt(u.getUserId(), u.getPassword()));
            ps.setString(6, StringUtils.ROLE_USER);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public UsersModel getProfileInfo(String userId) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_GET_PROFILE)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UsersModel u = new UsersModel();
                u.setUserId(rs.getString("User_ID"));
                u.setFullName(rs.getString("Full_Name"));
                u.setEmail(rs.getString("Email"));
                u.setPhoneNumber(rs.getString("Phone_Number"));
                u.setRole(rs.getString("Role"));
                u.setFullAddress(rs.getString("ADDRESS"));
                u.setImgLink(rs.getString("IMG_LINK"));
                return u;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public int userProfileUpdate(UsersModel u) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_UPDATE_PROFILE)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getFullAddress());
            ps.setString(3, u.getUserId());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public int updateUserImage(String userId, String imgPath) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_UPDATE_USER_IMG)) {
            ps.setString(1, imgPath);
            ps.setString(2, userId);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    /** Returns 1 on success, 4 if current password wrong, -1 on error */
    public int changePassword(String userId, String currentPass, String newPass) {
        try {
            // Verify current password
            int check = getUserLoginInfo(userId, currentPass);
            if (check == 4 || check == 0) return 4;
            String encrypted = PasswordEncryptionWithAes.encrypt(userId, newPass);
            try (Connection c = getConnection();
                 PreparedStatement ps = c.prepareStatement(StringUtils.SQL_UPDATE_PASSWORD)) {
                ps.setString(1, encrypted);
                ps.setString(2, userId);
                return ps.executeUpdate();
            }
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public List<UsersModel> getAllUsers() {
        List<UsersModel> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_GET_ALL_USERS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UsersModel u = new UsersModel();
                u.setUserId(rs.getString("User_ID"));
                u.setFullName(rs.getString("Full_Name"));
                u.setEmail(rs.getString("Email"));
                u.setPhoneNumber(rs.getString("Phone_Number"));
                u.setRole(rs.getString("Role"));
                list.add(u);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public int deleteUser(String userId) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_DELETE_USER)) {
            ps.setString(1, userId);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

  

    public List<CategoryModel> getAllCategories() {
        List<CategoryModel> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_GET_ALL_CATS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new CategoryModel(
                    rs.getString("Category_ID"),
                    rs.getString("Category_Name"),
                    rs.getString("Category_Desc")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public CategoryModel getCategoryById(String id) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_GET_CAT_BY_ID)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new CategoryModel(
                rs.getString("Category_ID"),
                rs.getString("Category_Name"),
                rs.getString("Category_Desc"));
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public int addCategory(CategoryModel cat) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_INSERT_CAT)) {
            ps.setString(1, cat.getCategoryId());
            ps.setString(2, cat.getCategoryName());
            ps.setString(3, cat.getCategoryDesc());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public int updateCategory(CategoryModel cat) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_UPDATE_CAT)) {
            ps.setString(1, cat.getCategoryName());
            ps.setString(2, cat.getCategoryDesc());
            ps.setString(3, cat.getCategoryId());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public int deleteCategory(String id) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_DELETE_CAT)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

  

    private BeverageModel mapBeverage(ResultSet rs) throws SQLException {
        BeverageModel b = new BeverageModel();
        b.setBeverageId(rs.getString("Beverage_ID"));
        b.setBeverageName(rs.getString("Beverage_Name"));
        b.setDescription(rs.getString("Description"));
        b.setPrice(rs.getDouble("Price"));
        b.setDiscount(rs.getDouble("Discount"));
        b.setDiscountAmount(rs.getDouble("Discount_Amt"));
        b.setStockQuantity(rs.getString("Stock_Qty"));
        b.setCategoryId(rs.getString("Category_ID"));
        b.setCategoryName(rs.getString("Category_Name"));
        b.setImageUrl(rs.getString("Image"));
        return b;
    }

    public List<BeverageModel> getAllBeverages() {
        List<BeverageModel> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_GET_ALL_BEVS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapBeverage(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public BeverageModel getBeverageById(String id) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_GET_BEV_BY_ID)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapBeverage(rs);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<BeverageModel> getBeveragesByCategory(String catId) {
        List<BeverageModel> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_GET_BEVS_BY_CAT)) {
            ps.setString(1, catId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBeverage(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<BeverageModel> searchBeverages(String keyword) {
        List<BeverageModel> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_SEARCH_BEVS)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBeverage(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public int addBeverage(BeverageModel b) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_INSERT_BEV)) {
            ps.setString(1, b.getBeverageId());
            ps.setString(2, b.getBeverageName());
            ps.setString(3, b.getDescription());
            ps.setDouble(4, b.getPrice());
            ps.setDouble(5, b.getDiscount());
            ps.setDouble(6, b.getDiscountAmount());
            ps.setString(7, b.getStockQuantity());
            ps.setString(8, b.getCategoryId());
            ps.setString(9, b.getImageUrl());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public int updateBeverage(BeverageModel b) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_UPDATE_BEV)) {
            ps.setString(1, b.getBeverageName());
            ps.setString(2, b.getDescription());
            ps.setDouble(3, b.getPrice());
            ps.setDouble(4, b.getDiscount());
            ps.setDouble(5, b.getDiscountAmount());
            ps.setString(6, b.getStockQuantity());
            ps.setString(7, b.getCategoryId());
            ps.setString(8, b.getImageUrl());
            ps.setString(9, b.getBeverageId());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public int deleteBeverage(String id) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_DELETE_BEV)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    // ── CART ───────────────────────────────────────────────────────────────────

    public int addToCart(CartModel cart) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_ADD_CART)) {
            ps.setString(1, cart.getCartId());
            ps.setString(2, cart.getUserId());
            ps.setString(3, cart.getBeverageId());
            ps.setString(4, cart.getQuantity());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public List<BeverageModel> getCartItems(String userId) {
        List<BeverageModel> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_GET_CART)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BeverageModel b = new BeverageModel();
                b.setBeverageId(rs.getString("Beverage_ID"));
                b.setBeverageName(rs.getString("Beverage_Name"));
                b.setPrice(rs.getDouble("Price"));
                b.setImageUrl(rs.getString("Image"));
                b.setQuantity(rs.getString("Quantity"));
                // store cartId in stockQuantity field for removal
                b.setStockQuantity(rs.getString("Cart_ID"));
                list.add(b);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public int removeFromCart(String cartId) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_REMOVE_CART)) {
            ps.setString(1, cartId);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public int clearCart(String userId) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_CLEAR_CART)) {
            ps.setString(1, userId);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public int getCartCount(String userId) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_CART_COUNT)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("cnt") : 0;
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }

    // ── ORDERS ─────────────────────────────────────────────────────────────────

    public int addOrder(OrderModel order) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_INSERT_ORDER)) {
            ps.setString(1, order.getOrderId());
            ps.setString(2, order.getUserId());
            ps.setString(3, order.getTotalAmount());
            ps.setString(4, "Pending");
            ps.setString(5, order.getCity());
            ps.setString(6, order.getAddress());
            ps.setString(7, order.getPayment());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public int addOrderItem(OrderItemModel item) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_INSERT_ORDER_ITEM)) {
            ps.setString(1, item.getOrderId());
            ps.setString(2, item.getBeverageId());
            ps.setString(3, item.getLineQuantity());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public List<CustomerTransactionView> getAllOrderDetails() {
        List<CustomerTransactionView> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_GET_ORDER_DETAILS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new CustomerTransactionView(
                    rs.getString("Order_ID"),
                    rs.getString("User_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("Beverage_ID"),
                    rs.getString("Beverage_Name"),
                    rs.getString("Total_Amount"),
                    rs.getString("Status"),
                    rs.getString("IMG_LINK")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public int updateOrderStatus(String orderId, String status) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_UPDATE_STATUS)) {
            ps.setString(1, status);
            ps.setString(2, orderId);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    private int getCount(String sql) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("cnt") : 0;
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }

    public int getOrderCount()     { return getCount(StringUtils.SQL_COUNT_ORDERS); }
    public int getPendingCount()   { return getCount(StringUtils.SQL_COUNT_PENDING); }
    public int getDeliveredCount() { return getCount(StringUtils.SQL_COUNT_DELIVERED); }
    public int getUserCount()      { return getCount(StringUtils.SQL_COUNT_USERS); }



    public int addInquiry(InquiryModel inq) {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(StringUtils.SQL_INSERT_INQUIRY)) {
            ps.setString(1, inq.getInquiryId());
            ps.setString(2, inq.getUserId());
            ps.setString(3, inq.getSubject());
            ps.setString(4, inq.getMessage());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return -1; }
    }
}