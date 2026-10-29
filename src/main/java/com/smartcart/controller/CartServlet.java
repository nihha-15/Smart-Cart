package com.smartcart.controller;

import com.smartcart.dao.ProductDAO;
import com.smartcart.model.*;
import com.smartcart.service.SmartAnalysisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Shopping Cart management, Budget Guardian operations,
 * Smart Setup Bundle addition, and Mistake Alert dismissal.
 */
@WebServlet(urlPatterns = {
    "/cart", 
    "/cart/add", 
    "/cart/update", 
    "/cart/remove", 
    "/cart/budget", 
    "/cart/clear", 
    "/cart/replace",
    "/cart/addBundle",
    "/cart/addBoughtTogether",
    "/cart/dismissAlert"
})
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductDAO productDAO = new ProductDAO();
    private final SmartAnalysisService analysisService = new SmartAnalysisService(productDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        // Run smart analysis so cart page displays real-time warnings & budget progress
        AnalysisReport report = analysisService.generateReport(cart);
        request.setAttribute("report", report);

        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        String path = request.getServletPath();
        String action = request.getParameter("action");
        if (action == null && path != null) {
            if (path.endsWith("/add")) action = "add";
            else if (path.endsWith("/update")) action = "update";
            else if (path.endsWith("/remove")) action = "remove";
            else if (path.endsWith("/budget")) action = "budget";
            else if (path.endsWith("/clear")) action = "clear";
            else if (path.endsWith("/replace")) action = "replace";
            else if (path.endsWith("/addBundle")) action = "addBundle";
            else if (path.endsWith("/addBoughtTogether")) action = "addBoughtTogether";
            else if (path.endsWith("/dismissAlert")) action = "dismissAlert";
        }

        try {
            if ("add".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = 1;
                String qtyStr = request.getParameter("quantity");
                if (qtyStr != null && !qtyStr.trim().isEmpty()) {
                    quantity = Integer.parseInt(qtyStr.trim());
                }

                Product p = productDAO.getProductById(productId);
                if (p != null) {
                    cart.addItem(p, quantity);
                    session.setAttribute("toastMessage", "Added " + p.getName() + " to your cart.");
                }
            } else if ("addBundle".equalsIgnoreCase(action)) {
                // Feature F: Complete Your Setup companion bundle addition
                int companionId = Integer.parseInt(request.getParameter("companionId"));
                Product companion = productDAO.getProductById(companionId);
                if (companion != null) {
                    cart.addItem(companion, 1);
                    session.setAttribute("toastMessage", "Bundle item added: " + companion.getName() + "!");
                }
            } else if ("addBoughtTogether".equalsIgnoreCase(action)) {
                // Frequently Bought Together Bundle: adds all selected items in bundle
                String idsStr = request.getParameter("productIds");
                if (idsStr != null && !idsStr.trim().isEmpty()) {
                    String[] ids = idsStr.split(",");
                    int count = 0;
                    for (String idPart : ids) {
                        try {
                            int pId = Integer.parseInt(idPart.trim());
                            Product prod = productDAO.getProductById(pId);
                            if (prod != null) {
                                cart.addItem(prod, 1);
                                count++;
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                    session.setAttribute("toastMessage", "Added " + count + " Frequently Bought Together items to your cart!");
                }
            } else if ("dismissAlert".equalsIgnoreCase(action)) {
                // Feature B: Allow users to review and dismiss mistake alerts
                String alertId = request.getParameter("alertId");
                if (alertId != null && !alertId.trim().isEmpty()) {
                    cart.dismissAlert(alertId.trim());
                    session.setAttribute("toastMessage", "Mistake alert dismissed.");
                }
            } else if ("update".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                cart.updateQuantity(productId, quantity);
            } else if ("remove".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                cart.removeItem(productId);
                session.setAttribute("toastMessage", "Item removed from cart.");
            } else if ("budget".equalsIgnoreCase(action)) {
                String budgetStr = request.getParameter("budget");
                if (budgetStr != null) {
                    double budget = Double.parseDouble(budgetStr.replaceAll("[^0-9.]", ""));
                    cart.setBudget(budget);
                    session.setAttribute("toastMessage", "Budget updated to ₹" + String.format("%,.2f", budget));
                }
            } else if ("replace".equalsIgnoreCase(action)) {
                // One-click alternative swap for Budget Guardian!
                int originalId = Integer.parseInt(request.getParameter("originalId"));
                int alternativeId = Integer.parseInt(request.getParameter("alternativeId"));
                CartItem existing = cart.getItem(originalId);
                int qty = (existing != null) ? existing.getQuantity() : 1;
                cart.removeItem(originalId);
                Product altProduct = productDAO.getProductById(alternativeId);
                if (altProduct != null) {
                    cart.addItem(altProduct, qty);
                    session.setAttribute("toastMessage", "Swapped to " + altProduct.getName() + "! Savings applied.");
                }
            } else if ("clear".equalsIgnoreCase(action)) {
                cart.clear();
                session.setAttribute("toastMessage", "Cart cleared.");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error processing cart request: " + e.getMessage());
        }

        String redirectUrl = request.getParameter("redirect");
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}
