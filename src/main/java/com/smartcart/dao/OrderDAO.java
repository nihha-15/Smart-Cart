package com.smartcart.dao;

import com.smartcart.model.Order;
import com.smartcart.model.OrderItem;
import com.smartcart.model.Product;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for persisting Orders and OrderItems into MySQL
 * and fetching Order History with Price Drop tracking.
 */
public class OrderDAO {
    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());

    private static final Map<Integer, Order> IN_MEMORY_ORDERS = new ConcurrentHashMap<>();
    private static final Map<String, Order> IN_MEMORY_ORDERS_BY_NO = new ConcurrentHashMap<>();
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1001);

    static {
        // Pre-seed realistic past order history for demo account (Aditya Sharma)
        // Showcase: "history which is at lower price show" (Price Drop Guarantee)
        try {
            Order pastOrder1 = new Order();
            pastOrder1.setId(101);
            pastOrder1.setUserId(1);
            pastOrder1.setOrderNumber("SC-20260915-4821");
            pastOrder1.setCustomerName("Aditya Sharma");
            pastOrder1.setCustomerEmail("aditya@smartcart.in");
            pastOrder1.setCustomerPhone("+91 98765 43210");
            pastOrder1.setShippingAddress("Flat 402, Prestige Tech Park, Outer Ring Road, Kadubeesanahalli, Bengaluru, Karnataka 560103");
            pastOrder1.setPaymentMethod("UPI (Google Pay)");
            pastOrder1.setBudgetLimit(160000.00);
            pastOrder1.setSubtotal(144900.00); // Purchased at earlier launch price of ₹1,44,900 (now ₹1,34,900 -> ₹10,000 drop!)
            pastOrder1.setCgst(13041.00);
            pastOrder1.setSgst(13041.00);
            pastOrder1.setTax(26082.00);
            pastOrder1.setShippingCost(0.00);
            pastOrder1.setDiscount(0.00);
            pastOrder1.setTotalAmount(170982.00);
            pastOrder1.setReadinessScore(95);
            pastOrder1.setOrderStatus("DELIVERED");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -42);
            pastOrder1.setCreatedAt(cal.getTime());

            OrderItem item1 = new OrderItem(1, "Apple MacBook Air 15\" M3 (16GB, 512GB SSD)", 144900.00, 1, 144900.00);
            item1.setCurrentPrice(134900.00); // Current price is ₹10,000 lower!
            pastOrder1.addItem(item1);

            IN_MEMORY_ORDERS.put(pastOrder1.getId(), pastOrder1);
            IN_MEMORY_ORDERS_BY_NO.put(pastOrder1.getOrderNumber(), pastOrder1);

            Order pastOrder2 = new Order();
            pastOrder2.setId(102);
            pastOrder2.setUserId(1);
            pastOrder2.setOrderNumber("SC-20261010-8912");
            pastOrder2.setCustomerName("Aditya Sharma");
            pastOrder2.setCustomerEmail("aditya@smartcart.in");
            pastOrder2.setCustomerPhone("+91 98765 43210");
            pastOrder2.setShippingAddress("Flat 402, Prestige Tech Park, Outer Ring Road, Kadubeesanahalli, Bengaluru, Karnataka 560103");
            pastOrder2.setPaymentMethod("Debit Card (RuPay)");
            pastOrder2.setBudgetLimit(50000.00);
            pastOrder2.setSubtotal(34990.00); // Purchased Sony WH-1000XM5 at ₹34,990 (now ₹29,990 -> ₹5,000 drop!)
            pastOrder2.setCgst(3149.10);
            pastOrder2.setSgst(3149.10);
            pastOrder2.setTax(6298.20);
            pastOrder2.setShippingCost(0.00);
            pastOrder2.setDiscount(0.00);
            pastOrder2.setTotalAmount(41288.20);
            pastOrder2.setReadinessScore(100);
            pastOrder2.setOrderStatus("DELIVERED");
            Calendar cal2 = Calendar.getInstance();
            cal2.add(Calendar.DATE, -16);
            pastOrder2.setCreatedAt(cal2.getTime());

            OrderItem item2 = new OrderItem(23, "Sony WH-1000XM5 Wireless Noise Cancelling Headphones", 34990.00, 1, 34990.00);
            item2.setCurrentPrice(29990.00); // Current price is ₹5,000 lower!
            pastOrder2.addItem(item2);

            IN_MEMORY_ORDERS.put(pastOrder2.getId(), pastOrder2);
            IN_MEMORY_ORDERS_BY_NO.put(pastOrder2.getOrderNumber(), pastOrder2);
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Failed to seed past demo orders.", e);
        }
    }

    public synchronized boolean saveOrder(Order order) {
        if (order == null) return false;

        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            order.setOrderNumber(generateOrderNumber());
        }

        if (DBUtil.isMySqlAvailable()) {
            String insertOrderSql = "INSERT INTO orders (user_id, order_number, customer_name, customer_email, " +
                    "customer_phone, shipping_address, payment_method, budget_limit, subtotal, cgst, sgst, tax, " +
                    "shipping_cost, discount, total_amount, readiness_score, gstin, order_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            String insertItemSql = "INSERT INTO order_items (order_id, product_id, product_name, " +
                    "unit_price, quantity, item_total) VALUES (?, ?, ?, ?, ?, ?)";

            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                conn.setAutoCommit(false);

                int orderId = 0;
                try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                    if (order.getUserId() != null) psOrder.setInt(1, order.getUserId());
                    else psOrder.setNull(1, Types.INTEGER);

                    psOrder.setString(2, order.getOrderNumber());
                    psOrder.setString(3, order.getCustomerName());
                    psOrder.setString(4, order.getCustomerEmail());
                    psOrder.setString(5, order.getCustomerPhone());
                    psOrder.setString(6, order.getShippingAddress());
                    psOrder.setString(7, order.getPaymentMethod());
                    psOrder.setDouble(8, order.getBudgetLimit());
                    psOrder.setDouble(9, order.getSubtotal());
                    psOrder.setDouble(10, order.getCgst());
                    psOrder.setDouble(11, order.getSgst());
                    psOrder.setDouble(12, order.getTax());
                    psOrder.setDouble(13, order.getShippingCost());
                    psOrder.setDouble(14, order.getDiscount());
                    psOrder.setDouble(15, order.getTotalAmount());
                    psOrder.setInt(16, order.getReadinessScore());
                    psOrder.setString(17, order.getGstin());
                    psOrder.setString(18, order.getOrderStatus());

                    psOrder.executeUpdate();
                    try (ResultSet rs = psOrder.getGeneratedKeys()) {
                        if (rs.next()) {
                            orderId = rs.getInt(1);
                            order.setId(orderId);
                        }
                    }
                }

                if (orderId > 0 && order.getItems() != null) {
                    try (PreparedStatement psItem = conn.prepareStatement(insertItemSql)) {
                        for (OrderItem item : order.getItems()) {
                            psItem.setInt(1, orderId);
                            psItem.setInt(2, item.getProductId());
                            psItem.setString(3, item.getProductName());
                            psItem.setDouble(4, item.getUnitPrice());
                            psItem.setInt(5, item.getQuantity());
                            psItem.setDouble(6, item.getItemTotal());
                            psItem.addBatch();
                        }
                        psItem.executeBatch();
                    }
                }

                conn.commit();
                LOGGER.info("Order #" + order.getOrderNumber() + " successfully persisted to MySQL.");
                IN_MEMORY_ORDERS.put(order.getId(), order);
                IN_MEMORY_ORDERS_BY_NO.put(order.getOrderNumber(), order);
                return true;
            } catch (SQLException e) {
                if (conn != null) {
                    try { conn.rollback(); } catch (SQLException ex) { /* ignored */ }
                }
                LOGGER.log(Level.WARNING, "Failed to persist order to MySQL. Falling back to in-memory store.", e);
            } finally {
                if (conn != null) {
                    try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { /* ignored */ }
                }
            }
        }

        // In-memory fallback
        int id = ID_GENERATOR.getAndIncrement();
        order.setId(id);
        IN_MEMORY_ORDERS.put(id, order);
        IN_MEMORY_ORDERS_BY_NO.put(order.getOrderNumber(), order);
        LOGGER.info("Order #" + order.getOrderNumber() + " stored in in-memory repository.");
        return true;
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        if (DBUtil.isMySqlAvailable()) {
            String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY id DESC";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Order o = mapOrder(rs);
                        loadOrderItems(conn, o);
                        list.add(o);
                    }
                }
                attachCurrentPrices(list);
                return list;
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to get orders by user ID from MySQL.", e);
            }
        }

        for (Order o : IN_MEMORY_ORDERS.values()) {
            if (o.getUserId() != null && o.getUserId() == userId) {
                list.add(o);
            }
        }
        list.sort((a, b) -> Integer.compare(b.getId(), a.getId()));
        attachCurrentPrices(list);
        return list;
    }

    public List<Order> getOrdersByEmail(String email) {
        if (email == null) return Collections.emptyList();
        String normalized = email.trim().toLowerCase();
        List<Order> list = new ArrayList<>();

        if (DBUtil.isMySqlAvailable()) {
            String sql = "SELECT * FROM orders WHERE LOWER(customer_email) = ? ORDER BY id DESC";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, normalized);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Order o = mapOrder(rs);
                        loadOrderItems(conn, o);
                        list.add(o);
                    }
                }
                attachCurrentPrices(list);
                return list;
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to get orders by email from MySQL.", e);
            }
        }

        for (Order o : IN_MEMORY_ORDERS.values()) {
            if (o.getCustomerEmail() != null && o.getCustomerEmail().equalsIgnoreCase(normalized)) {
                list.add(o);
            }
        }
        list.sort((a, b) -> Integer.compare(b.getId(), a.getId()));
        attachCurrentPrices(list);
        return list;
    }

    public Order getOrderByNumber(String orderNumber) {
        if (orderNumber == null) return null;
        if (DBUtil.isMySqlAvailable()) {
            String sql = "SELECT * FROM orders WHERE order_number = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, orderNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Order o = mapOrder(rs);
                        loadOrderItems(conn, o);
                        attachCurrentPrices(Collections.singletonList(o));
                        return o;
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to get order from MySQL.", e);
            }
        }
        Order o = IN_MEMORY_ORDERS_BY_NO.get(orderNumber);
        if (o != null) attachCurrentPrices(Collections.singletonList(o));
        return o;
    }

    public Order getOrderById(int id) {
        if (DBUtil.isMySqlAvailable()) {
            String sql = "SELECT * FROM orders WHERE id = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Order o = mapOrder(rs);
                        loadOrderItems(conn, o);
                        attachCurrentPrices(Collections.singletonList(o));
                        return o;
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to get order by ID from MySQL.", e);
            }
        }
        Order o = IN_MEMORY_ORDERS.get(id);
        if (o != null) attachCurrentPrices(Collections.singletonList(o));
        return o;
    }

    private void attachCurrentPrices(List<Order> orders) {
        ProductDAO pdao = new ProductDAO();
        for (Order o : orders) {
            if (o.getItems() != null) {
                for (OrderItem item : o.getItems()) {
                    Product p = pdao.getProductById(item.getProductId());
                    if (p != null) {
                        item.setCurrentPrice(p.getPrice());
                    }
                }
            }
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        int uid = rs.getInt("user_id");
        if (!rs.wasNull()) o.setUserId(uid);
        o.setOrderNumber(rs.getString("order_number"));
        o.setCustomerName(rs.getString("customer_name"));
        o.setCustomerEmail(rs.getString("customer_email"));
        o.setCustomerPhone(rs.getString("customer_phone"));
        o.setShippingAddress(rs.getString("shipping_address"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setBudgetLimit(rs.getDouble("budget_limit"));
        o.setSubtotal(rs.getDouble("subtotal"));
        o.setCgst(rs.getDouble("cgst"));
        o.setSgst(rs.getDouble("sgst"));
        o.setTax(rs.getDouble("tax"));
        o.setShippingCost(rs.getDouble("shipping_cost"));
        o.setDiscount(rs.getDouble("discount"));
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setReadinessScore(rs.getInt("readiness_score"));
        o.setGstin(rs.getString("gstin"));
        o.setOrderStatus(rs.getString("order_status"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        return o;
    }

    private void loadOrderItems(Connection conn, Order order) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getInt("id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setItemTotal(rs.getDouble("item_total"));
                    order.addItem(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.FINE, "Failed to load order items for order " + order.getId(), e);
        }
    }

    private String generateOrderNumber() {
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        int randomPart = 1000 + new Random().nextInt(9000);
        return "SC-" + datePart + "-" + randomPart;
    }
}
