package com.smartcart.controller;

import com.smartcart.dao.ProductDAO;
import com.smartcart.model.Cart;
import com.smartcart.model.Product;
import com.smartcart.model.Wishlist;
import com.smartcart.model.WishlistItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for SmartCart Wishlist & Price Drop Watcher.
 * Handles adding/removing products, toggling heart state, target price alerts,
 * and 1-click moves to shopping cart.
 */
@WebServlet(urlPatterns = {
    "/wishlist",
    "/wishlist/add",
    "/wishlist/remove",
    "/wishlist/toggle",
    "/wishlist/clear",
    "/wishlist/moveToCart",
    "/wishlist/moveAllToCart",
    "/wishlist/updateTarget"
})
public class WishlistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        Wishlist wishlist = getOrCreateWishlist(session);

        request.setAttribute("wishlist", wishlist);
        request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        Wishlist wishlist = getOrCreateWishlist(session);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        String path = request.getServletPath();
        if (path == null) path = "/wishlist";

        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
                || "application/json".equalsIgnoreCase(request.getHeader("Accept"));

        if (path.endsWith("/add")) {
            handleAdd(request, response, wishlist, isAjax);
        } else if (path.endsWith("/toggle")) {
            handleToggle(request, response, wishlist, isAjax);
        } else if (path.endsWith("/remove")) {
            handleRemove(request, response, wishlist, isAjax);
        } else if (path.endsWith("/moveToCart")) {
            handleMoveToCart(request, response, wishlist, cart);
        } else if (path.endsWith("/moveAllToCart")) {
            handleMoveAllToCart(request, response, wishlist, cart);
        } else if (path.endsWith("/updateTarget")) {
            handleUpdateTarget(request, response, wishlist);
        } else if (path.endsWith("/clear")) {
            wishlist.clear();
            session.setAttribute("toastMessage", "Wishlist cleared.");
            response.sendRedirect(request.getContextPath() + "/wishlist");
        } else {
            response.sendRedirect(request.getContextPath() + "/wishlist");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp, Wishlist wishlist, boolean isAjax) 
            throws IOException {
        String idStr = req.getParameter("productId");
        try {
            int pid = Integer.parseInt(idStr);
            Product p = productDAO.getProductById(pid);
            if (p != null) {
                boolean added = wishlist.addItem(p);
                if (isAjax) {
                    sendJsonResponse(resp, true, true, wishlist.getItemCount(), 
                        "❤️ Added " + p.getName() + " to Wishlist!");
                    return;
                }
                req.getSession().setAttribute("toastMessage", "Added " + p.getName() + " to your Wishlist!");
            }
        } catch (Exception ignored) {}

        if (isAjax) {
            sendJsonResponse(resp, false, false, wishlist.getItemCount(), "Could not add to Wishlist.");
        } else {
            redirectToReferer(req, resp);
        }
    }

    private void handleToggle(HttpServletRequest req, HttpServletResponse resp, Wishlist wishlist, boolean isAjax) 
            throws IOException {
        String idStr = req.getParameter("productId");
        try {
            int pid = Integer.parseInt(idStr);
            Product p = productDAO.getProductById(pid);
            if (p != null) {
                boolean inWishlistNow;
                String msg;
                if (wishlist.contains(pid)) {
                    wishlist.removeItem(pid);
                    inWishlistNow = false;
                    msg = "Removed from Wishlist";
                } else {
                    wishlist.addItem(p);
                    inWishlistNow = true;
                    msg = "❤️ Added " + p.getName() + " to Wishlist!";
                }

                if (isAjax) {
                    sendJsonResponse(resp, true, inWishlistNow, wishlist.getItemCount(), msg);
                    return;
                }
                req.getSession().setAttribute("toastMessage", msg);
            }
        } catch (Exception ignored) {}

        if (isAjax) {
            sendJsonResponse(resp, false, false, wishlist.getItemCount(), "Unable to toggle item.");
        } else {
            redirectToReferer(req, resp);
        }
    }

    private void handleRemove(HttpServletRequest req, HttpServletResponse resp, Wishlist wishlist, boolean isAjax) 
            throws IOException {
        String idStr = req.getParameter("productId");
        try {
            int pid = Integer.parseInt(idStr);
            wishlist.removeItem(pid);
            if (isAjax) {
                sendJsonResponse(resp, true, false, wishlist.getItemCount(), "Item removed from Wishlist.");
                return;
            }
            req.getSession().setAttribute("toastMessage", "Item removed from Wishlist.");
        } catch (Exception ignored) {}

        if (isAjax) {
            sendJsonResponse(resp, false, false, wishlist.getItemCount(), "Error removing item.");
        } else {
            redirectToReferer(req, resp);
        }
    }

    private void handleMoveToCart(HttpServletRequest req, HttpServletResponse resp, Wishlist wishlist, Cart cart) 
            throws IOException {
        String idStr = req.getParameter("productId");
        try {
            int pid = Integer.parseInt(idStr);
            Product p = productDAO.getProductById(pid);
            if (p != null) {
                cart.addItem(p, 1);
                wishlist.removeItem(pid);
                req.getSession().setAttribute("toastMessage", "🛒 Moved " + p.getName() + " to your Shopping Cart!");
            }
        } catch (Exception ignored) {}
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleMoveAllToCart(HttpServletRequest req, HttpServletResponse resp, Wishlist wishlist, Cart cart) 
            throws IOException {
        int count = 0;
        for (WishlistItem item : wishlist.getItemList()) {
            if (item.getProduct() != null) {
                cart.addItem(item.getProduct(), 1);
                count++;
            }
        }
        wishlist.clear();
        req.getSession().setAttribute("toastMessage", "🛒 Successfully moved " + count + " items from Wishlist to Cart!");
        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleUpdateTarget(HttpServletRequest req, HttpServletResponse resp, Wishlist wishlist) 
            throws IOException {
        String idStr = req.getParameter("productId");
        String targetStr = req.getParameter("targetPrice");
        try {
            int pid = Integer.parseInt(idStr);
            double target = Double.parseDouble(targetStr);
            WishlistItem item = wishlist.getItem(pid);
            if (item != null) {
                item.setTargetPrice(target);
                req.getSession().setAttribute("toastMessage", "🎯 Target Price Alert updated to " + item.getFormattedTargetPrice());
            }
        } catch (Exception ignored) {}
        resp.sendRedirect(req.getContextPath() + "/wishlist");
    }

    private Wishlist getOrCreateWishlist(HttpSession session) {
        Wishlist w = (Wishlist) session.getAttribute("wishlist");
        if (w == null) {
            w = new Wishlist();
            session.setAttribute("wishlist", w);
        }
        return w;
    }

    private void sendJsonResponse(HttpServletResponse resp, boolean success, boolean inWishlist, int count, String message) 
            throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String json = String.format("{\"success\":%b,\"inWishlist\":%b,\"count\":%d,\"message\":\"%s\"}",
                success, inWishlist, count, escapeJson(message));
        resp.getWriter().write(json);
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"").replace("\n", " ");
    }

    private void redirectToReferer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String referer = req.getHeader("Referer");
        if (referer != null && !referer.trim().isEmpty() && referer.contains(req.getServerName())) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/wishlist");
        }
    }
}
