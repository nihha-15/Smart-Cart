package com.smartcart.controller;

import com.smartcart.dao.ProductDAO;
import com.smartcart.model.Cart;
import com.smartcart.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the SmartCart Home page.
 */
@WebServlet(urlPatterns = {"", "/home", "/index"})
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", new Cart());
        }

        List<Product> featuredProducts = productDAO.getFeaturedProducts();
        request.setAttribute("featuredProducts", featuredProducts);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
