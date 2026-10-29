<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SmartCart — Intelligent E-Commerce Cart System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <!-- Professional SmartCart Navigation Bar -->
    <header class="smart-navbar">
        <div class="nav-container">
            <!-- Brand Logo -->
            <a href="${pageContext.request.contextPath}/home" class="brand-logo">
                <svg viewBox="0 0 24 24"><path d="M7 18c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2-.9-2-2-2zM1 2v2h2l3.6 7.59-1.35 2.45c-.16.28-.25.61-.25.96 0 1.1.9 2 2 2h12v-2H7.42c-.14 0-.25-.11-.25-.25l.03-.12.9-1.63h7.45c.75 0 1.41-.41 1.75-1.03l3.58-6.49c.08-.14.12-.31.12-.48 0-.55-.45-1-1-1H5.21l-.94-2H1zm16 16c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2 2-.9 2-2-.9-2-2-2z"/></svg>
                <span>SmartCart</span>
                <span class="brand-badge">Intelligent</span>
            </a>

            <!-- Search Form -->
            <div class="nav-search">
                <form action="${pageContext.request.contextPath}/products" method="get" class="search-form">
                    <input type="text" name="q" value="${searchQuery}" placeholder="Search laptops, smartphones, chargers, audio..." class="search-input" autocomplete="off">
                    <button type="submit" class="search-btn">Search</button>
                </form>
            </div>

            <!-- Navigation Links & Status Pills -->
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/home" class="nav-link">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>

                <!-- Budget Guardian Status Pill -->
                <c:if test="${not empty sessionScope.cart}">
                    <li>
                        <a href="${pageContext.request.contextPath}/cart" class="nav-budget-pill ${sessionScope.cart.budgetStatusClass}">
                            <span>🛡️ Budget:</span>
                            <strong>${sessionScope.cart.formattedBudget}</strong>
                        </a>
                    </li>
                </c:if>

                <!-- Smart Analysis Link -->
                <li>
                    <a href="${pageContext.request.contextPath}/cart/analysis" class="nav-link">
                        <span>📊 Analysis</span>
                    </a>
                </li>

                <!-- Order History -->
                <li>
                    <a href="${pageContext.request.contextPath}/orders" class="nav-link">
                        <span>📦 Orders</span>
                    </a>
                </li>

                <!-- Customer Account / Auth -->
                <c:if test="${not empty sessionScope.user}">
                    <li class="nav-user-item">
                        <a href="${pageContext.request.contextPath}/profile" class="nav-user-pill">
                            <span>👤 ${sessionScope.user.firstName}</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/logout" class="nav-link nav-logout-btn">
                            <span>Logout</span>
                        </a>
                    </li>
                </c:if>
                <c:if test="${empty sessionScope.user}">
                    <li>
                        <a href="${pageContext.request.contextPath}/login" class="nav-link nav-login-btn">
                            <span>👤 Sign In</span>
                        </a>
                    </li>
                </c:if>

                <!-- Smart Wishlist Button with Dynamic Count -->
                <li>
                    <a href="${pageContext.request.contextPath}/wishlist" class="nav-wishlist-btn" title="Wishlist & Price Tracker">
                        <span>❤️ Wishlist</span>
                        <span class="wishlist-count-badge" id="nav-wishlist-count"><c:if test="${not empty sessionScope.wishlist}">${sessionScope.wishlist.itemCount}</c:if><c:if test="${empty sessionScope.wishlist}">0</c:if></span>
                    </a>
                </li>

                <!-- My Cart Button with Count Badge -->
                <li>
                    <a href="${pageContext.request.contextPath}/cart" class="nav-cart-btn">
                        <span>🛒 Cart</span>
                        <c:if test="${not empty sessionScope.cart}">
                            <span class="cart-count-badge">${sessionScope.cart.itemCount}</span>
                        </c:if>
                    </a>
                </li>
            </ul>
        </div>

        <!-- Category Sub-Navigation -->
        <div class="category-subnav">
            <div class="category-subnav-container">
                <a href="${pageContext.request.contextPath}/products" class="cat-chip ${empty selectedCategory || selectedCategory == 'all' ? 'active' : ''}">All Products</a>
                <a href="${pageContext.request.contextPath}/products?category=laptops" class="cat-chip ${selectedCategory == 'laptops' ? 'active' : ''}">Laptops</a>
                <a href="${pageContext.request.contextPath}/products?category=smartphones" class="cat-chip ${selectedCategory == 'smartphones' ? 'active' : ''}">Smartphones</a>
                <a href="${pageContext.request.contextPath}/products?category=tablets" class="cat-chip ${selectedCategory == 'tablets' ? 'active' : ''}">Tablets & Stylus</a>
                <a href="${pageContext.request.contextPath}/products?category=wearables" class="cat-chip ${selectedCategory == 'wearables' ? 'active' : ''}">Smartwatches</a>
                <a href="${pageContext.request.contextPath}/products?category=chargers" class="cat-chip ${selectedCategory == 'chargers' ? 'active' : ''}">Fast Chargers</a>
                <a href="${pageContext.request.contextPath}/products?category=cables" class="cat-chip ${selectedCategory == 'cables' ? 'active' : ''}">Cables & Adapters</a>
                <a href="${pageContext.request.contextPath}/products?category=audio" class="cat-chip ${selectedCategory == 'audio' ? 'active' : ''}">Audio</a>
                <a href="${pageContext.request.contextPath}/products?category=cases" class="cat-chip ${selectedCategory == 'cases' ? 'active' : ''}">Cases</a>
                <a href="${pageContext.request.contextPath}/products?category=monitors" class="cat-chip ${selectedCategory == 'monitors' ? 'active' : ''}">4K Monitors & Gear</a>
                <a href="${pageContext.request.contextPath}/products?category=smart-home" class="cat-chip ${selectedCategory == 'smart-home' ? 'active' : ''}">Smart Home</a>
                <a href="${pageContext.request.contextPath}/products?category=desk-setup" class="cat-chip ${selectedCategory == 'desk-setup' ? 'active' : ''}">Desk & Streaming</a>
                <a href="${pageContext.request.contextPath}/products?category=gaming" class="cat-chip ${selectedCategory == 'gaming' ? 'active' : ''}">Gaming Gear</a>
                <a href="${pageContext.request.contextPath}/products?category=power" class="cat-chip ${selectedCategory == 'power' ? 'active' : ''}">Power Protection</a>
            </div>
        </div>
    </header>

    <!-- Global Toast Notifications -->
    <div class="toast-container">
        <c:if test="${not empty sessionScope.toastMessage}">
            <div class="toast">
                <span>✓</span>
                <span>${sessionScope.toastMessage}</span>
            </div>
        </c:if>
        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="toast" style="border-left-color: #ef4444; background: #1e1b4b;">
                <span>⚠️</span>
                <span>${sessionScope.errorMessage}</span>
            </div>
        </c:if>
    </div>

    <!-- Smart Wishlist Client Engine -->
    <script>
        window.SMARTCART_WISHLIST = new Set([${sessionScope.wishlist != null ? sessionScope.wishlist.itemIdsCsv : ''}]);

        function toggleWishlist(productId, btnEl, evt) {
            if (evt) {
                evt.preventDefault();
                evt.stopPropagation();
            }

            btnEl.classList.add('heart-pulse');
            setTimeout(() => btnEl.classList.remove('heart-pulse'), 450);

            const contextPath = '${pageContext.request.contextPath}';
            const params = new URLSearchParams();
            params.append('productId', productId);

            fetch(contextPath + '/wishlist/toggle', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-Requested-With': 'XMLHttpRequest',
                    'Accept': 'application/json'
                },
                body: params.toString()
            })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    if (data.inWishlist) {
                        window.SMARTCART_WISHLIST.add(productId);
                    } else {
                        window.SMARTCART_WISHLIST.delete(productId);
                    }

                    // Sync all buttons on the page for this product
                    document.querySelectorAll('.wishlist-toggle-btn[data-id="' + productId + '"]').forEach(btn => {
                        const heart = btn.querySelector('.heart-icon');
                        if (data.inWishlist) {
                            btn.classList.add('active');
                            if (heart) heart.textContent = '❤️';
                            btn.setAttribute('title', 'Saved in Wishlist (Click to remove)');
                        } else {
                            btn.classList.remove('active');
                            if (heart) heart.textContent = '🤍';
                            btn.setAttribute('title', 'Add to Wishlist & Price Tracker');
                        }
                    });

                    // Sync product-details hero button if present
                    const heroBtn = document.getElementById('hero-wishlist-btn');
                    if (heroBtn && heroBtn.getAttribute('data-id') == productId) {
                        const heroHeart = heroBtn.querySelector('.heart-icon');
                        const heroText = heroBtn.querySelector('.wishlist-btn-text');
                        if (data.inWishlist) {
                            heroBtn.classList.add('active');
                            if (heroHeart) heroHeart.textContent = '❤️';
                            if (heroText) heroText.textContent = 'In Wishlist (Price Tracker Active)';
                        } else {
                            heroBtn.classList.remove('active');
                            if (heroHeart) heroHeart.textContent = '🤍';
                            if (heroText) heroText.textContent = 'Add to Wishlist & Price Watch';
                        }
                    }

                    // Update header wishlist counter badge
                    const navBadge = document.getElementById('nav-wishlist-count');
                    if (navBadge) {
                        navBadge.textContent = data.count;
                        navBadge.style.transform = 'scale(1.35)';
                        setTimeout(() => navBadge.style.transform = 'scale(1)', 220);
                    }

                    showToastNotification(data.message);

                    // If on wishlist page and removed an item, animate removal
                    if (window.location.pathname.includes('/wishlist') && !data.inWishlist) {
                        const card = btnEl.closest('.product-card');
                        if (card) {
                            card.style.transition = 'all 0.3s ease';
                            card.style.opacity = '0';
                            card.style.transform = 'scale(0.92)';
                            setTimeout(() => window.location.reload(), 320);
                        }
                    }
                }
            })
            .catch(err => console.error('Wishlist sync error:', err));
        }

        function showToastNotification(msg) {
            let container = document.querySelector('.toast-container');
            if (!container) {
                container = document.createElement('div');
                container.className = 'toast-container';
                document.body.appendChild(container);
            }
            const toast = document.createElement('div');
            toast.className = 'toast';
            toast.style.borderColor = '#ef4444';
            toast.innerHTML = '<span>❤️</span><span>' + msg + '</span>';
            container.appendChild(toast);
            setTimeout(() => {
                toast.style.opacity = '0';
                toast.style.transform = 'translateY(12px)';
                setTimeout(() => toast.remove(), 300);
            }, 3200);
        }

        // On DOMContentLoaded, ensure all wishlist toggle buttons reflect current state
        document.addEventListener('DOMContentLoaded', function() {
            document.querySelectorAll('.wishlist-toggle-btn').forEach(btn => {
                const id = parseInt(btn.getAttribute('data-id'), 10);
                if (window.SMARTCART_WISHLIST && window.SMARTCART_WISHLIST.has(id)) {
                    btn.classList.add('active');
                    const heart = btn.querySelector('.heart-icon');
                    if (heart) heart.textContent = '❤️';
                }
            });
            const heroBtn = document.getElementById('hero-wishlist-btn');
            if (heroBtn) {
                const id = parseInt(heroBtn.getAttribute('data-id'), 10);
                if (window.SMARTCART_WISHLIST && window.SMARTCART_WISHLIST.has(id)) {
                    heroBtn.classList.add('active');
                    const heroHeart = heroBtn.querySelector('.heart-icon');
                    const heroText = heroBtn.querySelector('.wishlist-btn-text');
                    if (heroHeart) heroHeart.textContent = '❤️';
                    if (heroText) heroText.textContent = 'In Wishlist (Price Tracker Active)';
                }
            }
        });
    </script>
