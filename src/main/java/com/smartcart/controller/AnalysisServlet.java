package com.smartcart.controller;

import com.smartcart.dao.ProductDAO;
import com.smartcart.model.AnalysisReport;
import com.smartcart.model.Cart;
import com.smartcart.service.SmartAnalysisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for Feature D: Cart Risk & Value Report.
 * Displays pre-checkout diagnostic analysis and allows dismissing non-critical mistake alerts.
 */
@WebServlet(urlPatterns = {"/cart/analysis", "/cart/analysis/dismiss"})
public class AnalysisServlet extends HttpServlet {
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

        AnalysisReport report = analysisService.generateReport(cart);
        request.setAttribute("report", report);

        request.getRequestDispatcher("/analysis.jsp").forward(request, response);
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

        String alertId = request.getParameter("alertId");
        if (alertId != null && !alertId.trim().isEmpty()) {
            cart.dismissAlert(alertId.trim());
            session.setAttribute("toastMessage", "Alert dismissed. It will not block purchase readiness.");
        }

        String referer = request.getParameter("redirect");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/cart/analysis");
        }
    }
}
