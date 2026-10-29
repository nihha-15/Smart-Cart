package com.smartcart.dao;

import com.smartcart.model.User;

import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Customer Accounts with MySQL & In-Memory Fallback.
 */
public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    private static final Map<Integer, User> IN_MEMORY_USERS = new ConcurrentHashMap<>();
    private static final Map<String, User> IN_MEMORY_BY_EMAIL = new ConcurrentHashMap<>();
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(101);

    static {
        // Seed default demo accounts for instant 1-click evaluation
        User demo1 = new User(1, "Aditya Sharma", "aditya@smartcart.in", "smartcart123", 
                "+91 98765 43210", 
                "Flat 402, Prestige Tech Park, Outer Ring Road, Kadubeesanahalli, Bengaluru, Karnataka 560103", 
                85000.00);
        IN_MEMORY_USERS.put(demo1.getId(), demo1);
        IN_MEMORY_BY_EMAIL.put(demo1.getEmail().toLowerCase(), demo1);

        User demo2 = new User(2, "Alex Morgan", "alex@college.edu", "password123", 
                "+91 91234 56789", 
                "Tech Campus Hostel 3, Room 204, Pune, Maharashtra 411007", 
                50000.00);
        IN_MEMORY_USERS.put(demo2.getId(), demo2);
        IN_MEMORY_BY_EMAIL.put(demo2.getEmail().toLowerCase(), demo2);
    }

    public User authenticate(String email, String password) {
        if (email == null || password == null) return null;
        String normalizedEmail = email.trim().toLowerCase();

        if (DBUtil.isMySqlAvailable()) {
            String sql = "SELECT * FROM users WHERE LOWER(email) = ? AND password = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, normalizedEmail);
                ps.setString(2, password.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapUser(rs);
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to authenticate against MySQL. Checking memory store.", e);
            }
        }

        User user = IN_MEMORY_BY_EMAIL.get(normalizedEmail);
        if (user != null && user.getPassword().equals(password.trim())) {
            return user;
        }
        return null;
    }

    public synchronized boolean register(User user) {
        if (user == null || user.getEmail() == null) return false;
        String normalizedEmail = user.getEmail().trim().toLowerCase();

        if (getUserByEmail(normalizedEmail) != null) {
            return false; // Email already in use
        }

        if (DBUtil.isMySqlAvailable()) {
            String sql = "INSERT INTO users (name, email, password, phone, address, saved_budget) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getName());
                ps.setString(2, normalizedEmail);
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getPhone());
                ps.setString(5, user.getAddress());
                ps.setDouble(6, user.getSavedBudget());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }
                IN_MEMORY_USERS.put(user.getId(), user);
                IN_MEMORY_BY_EMAIL.put(normalizedEmail, user);
                return true;
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to register user to MySQL. Storing in memory.", e);
            }
        }

        int id = ID_GENERATOR.getAndIncrement();
        user.setId(id);
        IN_MEMORY_USERS.put(id, user);
        IN_MEMORY_BY_EMAIL.put(normalizedEmail, user);
        return true;
    }

    public User getUserByEmail(String email) {
        if (email == null) return null;
        String normalized = email.trim().toLowerCase();

        if (DBUtil.isMySqlAvailable()) {
            String sql = "SELECT * FROM users WHERE LOWER(email) = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, normalized);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapUser(rs);
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to get user by email from MySQL.", e);
            }
        }
        return IN_MEMORY_BY_EMAIL.get(normalized);
    }

    public User getUserById(int id) {
        if (DBUtil.isMySqlAvailable()) {
            String sql = "SELECT * FROM users WHERE id = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapUser(rs);
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to get user by ID from MySQL.", e);
            }
        }
        return IN_MEMORY_USERS.get(id);
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setPhone(rs.getString("phone"));
        u.setAddress(rs.getString("address"));
        u.setSavedBudget(rs.getDouble("saved_budget"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }
}
