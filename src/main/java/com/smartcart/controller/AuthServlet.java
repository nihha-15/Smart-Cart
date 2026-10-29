package com.smartcart.controller;

import com.smartcart.dao.UserDAO;
import com.smartcart.model.Cart;
import com.smartcart.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for Customer Authentication (Login, Register, Logout, Profile).
 */
@WebServlet(urlPatterns = {"/login", "/register", "/logout", "/profile"})
public class AuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession(true);

        if ("/logout".equals(servletPath)) {
            session.removeAttribute("user");
            session.setAttribute("toastMessage", "You have been safely signed out.");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        if ("/register".equals(servletPath)) {
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if ("/profile".equals(servletPath)) {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login?redirect=/profile");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        // Default: /login
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession(true);

        if ("/login".equals(servletPath)) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String demoAction = request.getParameter("demoAction");

            // Instant 1-Click Demo Login support for quick evaluation
            if ("aditya".equalsIgnoreCase(demoAction)) {
                email = "aditya@smartcart.in";
                password = "smartcart123";
            } else if ("alex".equalsIgnoreCase(demoAction)) {
                email = "alex@college.edu";
                password = "password123";
            }

            User user = userDAO.authenticate(email, password);
            if (user != null) {
                session.setAttribute("user", user);
                session.setAttribute("toastMessage", "Welcome back, " + user.getFirstName() + "!");

                // Sync cart budget with user profile
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart != null && user.getSavedBudget() > 0) {
                    cart.setBudget(user.getSavedBudget());
                }

                String redirect = request.getParameter("redirect");
                if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("/login")) {
                    response.sendRedirect(request.getContextPath() + redirect);
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            } else {
                request.setAttribute("errorMessage", "Invalid email or password. Please try again or use 1-Click Demo Login.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } else if ("/register".equals(servletPath)) {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String budgetStr = request.getParameter("budget");

            if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Please complete all required fields.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            double budget = 75000.00;
            if (budgetStr != null && !budgetStr.trim().isEmpty()) {
                try {
                    budget = Double.parseDouble(budgetStr.replaceAll("[^0-9.]", ""));
                } catch (NumberFormatException ignored) {}
            }

            User user = new User(0, name.trim(), email.trim(), password.trim(), 
                    phone != null ? phone.trim() : "", 
                    address != null ? address.trim() : "", 
                    budget);

            boolean registered = userDAO.register(user);
            if (registered) {
                session.setAttribute("user", user);
                session.setAttribute("toastMessage", "Welcome to SmartCart, " + user.getFirstName() + "!");

                Cart cart = (Cart) session.getAttribute("cart");
                if (cart != null) {
                    cart.setBudget(budget);
                }

                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                request.setAttribute("errorMessage", "An account with that email already exists. Please sign in.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        }
    }
}
