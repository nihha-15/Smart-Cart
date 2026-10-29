package com.smartcart.controller;

import com.smartcart.dao.OrderDAO;
import com.smartcart.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller for rendering order confirmation receipt.
 */
@WebServlet(urlPatterns = {"/order/confirmation"})
public class OrderConfirmationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String orderNumber = request.getParameter("orderNumber");
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Order order = orderDAO.getOrderByNumber(orderNumber.trim());
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.setAttribute("order", order);
        request.getRequestDispatcher("/order-confirmation.jsp").forward(request, response);
    }
}
