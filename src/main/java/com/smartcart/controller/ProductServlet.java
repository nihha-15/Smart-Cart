package com.smartcart.controller;

import com.smartcart.dao.ProductDAO;
import com.smartcart.model.AlternativeRecommendation;
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
 * Controller for Product Listing and Details views.
 */
@WebServlet(urlPatterns = {"/products", "/product"})
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", new Cart());
        }

        String servletPath = request.getServletPath();

        if ("/product".equals(servletPath)) {
            handleProductDetails(request, response);
        } else {
            handleProductList(request, response);
        }
    }

    private void handleProductList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String category = request.getParameter("category");
        String query = request.getParameter("q");
        String brand = request.getParameter("brand");
        String dealer = request.getParameter("dealer");
        String priceRange = request.getParameter("priceRange");

        List<Product> products = productDAO.searchProducts(query, category, brand, dealer, priceRange);

        request.setAttribute("products", products);
        request.setAttribute("selectedCategory", category != null ? category : "all");
        request.setAttribute("searchQuery", query != null ? query : "");
        request.setAttribute("selectedBrand", brand != null ? brand : "all");
        request.setAttribute("selectedDealer", dealer != null ? dealer : "all");
        request.setAttribute("selectedPriceRange", priceRange != null ? priceRange : "all");

        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }

    private void handleProductDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            int id = Integer.parseInt(idParam.trim());
            Product product = productDAO.getProductById(id);
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }

            AlternativeRecommendation alternative = productDAO.getAlternativeForProduct(id);
            List<Product> relatedProducts = productDAO.getProductsByCategory(product.getCategory());
            List<Product> similarProducts = productDAO.getSimilarProducts(product, 6);
            com.smartcart.model.BoughtTogetherBundle boughtTogether = productDAO.getBoughtTogetherBundle(id);

            request.setAttribute("product", product);
            request.setAttribute("alternative", alternative);
            request.setAttribute("relatedProducts", relatedProducts);
            request.setAttribute("similarProducts", similarProducts);
            request.setAttribute("boughtTogether", boughtTogether);

            request.getRequestDispatcher("/product-details.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
}
