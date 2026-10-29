package com.smartcart.controller;

import com.smartcart.dao.OrderDAO;
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

/**
 * Controller for the Checkout flow, order placement, and 18% GST bill calculation.
 */
@WebServlet(urlPatterns = {"/checkout", "/order/place"})
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final SmartAnalysisService analysisService = new SmartAnalysisService(productDAO);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        AnalysisReport report = analysisService.generateReport(cart);
        request.setAttribute("report", report);

        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // Server-side validation of customer inputs
        String name = request.getParameter("customerName");
        String email = request.getParameter("customerEmail");
        String phone = request.getParameter("customerPhone");
        String address = request.getParameter("shippingAddress");
        String paymentMethod = request.getParameter("paymentMethod");

        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            address == null || address.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please provide complete shipping and contact details.");
            AnalysisReport report = analysisService.generateReport(cart);
            request.setAttribute("report", report);
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
            return;
        }

        // Server-side calculation of financial amounts (never trusting client inputs)
        double subtotal = cart.getSubtotal();
        double cgst = cart.getCgst();
        double sgst = cart.getSgst();
        double tax = cart.getTax();
        double shipping = cart.getShipping();
        double total = cart.getTotal();
        AnalysisReport report = analysisService.generateReport(cart);

        Order order = new Order();
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            order.setUserId(currentUser.getId());
        }
        order.setCustomerName(name.trim());
        order.setCustomerEmail(email.trim());
        order.setCustomerPhone(phone != null ? phone.trim() : "");
        order.setShippingAddress(address.trim());
        order.setPaymentMethod(paymentMethod != null ? paymentMethod : "UPI / NetBanking");
        order.setBudgetLimit(cart.getBudget());
        order.setSubtotal(subtotal);
        order.setCgst(cgst);
        order.setSgst(sgst);
        order.setTax(tax);
        order.setShippingCost(shipping);
        order.setDiscount(0.00);
        order.setTotalAmount(total);
        order.setReadinessScore(report.getReadinessScore());
        order.setOrderStatus("CONFIRMED");

        // Convert cart items to order items
        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem(
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getProduct().getPrice(),
                    item.getQuantity(),
                    item.getItemTotal()
            );
            order.addItem(orderItem);
        }

        // Persist order via OrderDAO into MySQL / Data Store
        boolean saved = orderDAO.saveOrder(order);

        if (saved) {
            // Clear cart upon successful order
            cart.clear();
            response.sendRedirect(request.getContextPath() + "/order/confirmation?orderNumber=" + order.getOrderNumber());
        } else {
            request.setAttribute("errorMessage", "Failed to finalize order. Please try again.");
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        }
    }
}
