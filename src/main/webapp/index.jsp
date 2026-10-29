<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<main class="main-content">

    <!-- Hero Banner -->
    <section class="hero-banner">
        <div class="hero-content">
            <span class="hero-pill">⚡ Powered by Java Servlets &amp; Rule Engine</span>
            <h1 class="hero-title">Shop Smarter with <span>Intelligent Cart</span> Protection</h1>
            <p class="hero-subtitle">SmartCart monitors your spending budget, identifies accidental duplicate purchases, and validates hardware compatibility between your devices and accessories before you pay.</p>
            <div class="hero-actions">
                <a href="${pageContext.request.contextPath}/products" class="btn-primary">Explore Products →</a>
                <a href="${pageContext.request.contextPath}/cart" class="btn-secondary">View Smart Cart</a>
            </div>
        </div>
    </section>

    <!-- Four Distinctive Smart Features -->
    <section class="section-container" style="margin-bottom: 3.5rem;">
        <div class="section-header">
            <div>
                <h2 class="section-title">Why SmartCart is Different</h2>
                <p class="section-desc">Traditional shopping carts just add and total items. SmartCart actively protects your decisions.</p>
            </div>
        </div>

        <div class="features-grid">
            <!-- Feature 1: Budget Tracking -->
            <div class="feature-box">
                <div class="feature-icon-wrapper">🛡️</div>
                <h3>Smart Budget Tracker</h3>
                <p>Set a target spending limit in ₹ (INR). SmartCart monitors your total in real-time, alerts you before you overspend, and suggests money-saving alternatives.</p>
            </div>

            <!-- Feature 2: Mistake Prevention -->
            <div class="feature-box">
                <div class="feature-icon-wrapper">💡</div>
                <h3>Mistake Prevention</h3>
                <p>Avoid accidental duplicate orders, wrong quantities, or purchasing redundant accessories when the device already includes them in the retail box.</p>
            </div>

            <!-- Feature 3: Hardware Compatibility -->
            <div class="feature-box">
                <div class="feature-icon-wrapper">🔌</div>
                <h3>Hardware Compatibility</h3>
                <p>Automatically verifies that chargers supply enough wattage, phone cases fit exact device models, and audio gear matches your headphone/USB ports.</p>
            </div>

            <!-- Feature 4: Cart Analysis & Savings -->
            <div class="feature-box">
                <div class="feature-icon-wrapper">📊</div>
                <h3>Smart Cart Analysis</h3>
                <p>Generates an instant purchase scorecard (0–100) before checkout with transparent recommendations to save money and get better value.</p>
            </div>

            <!-- Feature 5: Long-Term Cost Estimator -->
            <div class="feature-box">
                <div class="feature-icon-wrapper">⚡</div>
                <h3>Long-Term Cost Estimator</h3>
                <p>Know what your electronics will cost over 3 years, calculating estimated electricity power bills and battery maintenance upfront.</p>
            </div>

            <!-- Feature 6: Companion Bundles -->
            <div class="feature-box">
                <div class="feature-icon-wrapper">🎁</div>
                <h3>Complete Your Setup</h3>
                <p>Discovers verified companion accessories tailored for your chosen devices (cables, GaN chargers, stylus, sleeves) with exclusive bundle discounts.</p>
            </div>
        </div>
    </section>

    <!-- Featured Products Catalogue -->
    <section class="section-container">
        <div class="section-header">
            <div>
                <h2 class="section-title">Featured Products</h2>
                <p class="section-desc">Hand-picked premium electronics and accessories with verified specifications.</p>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="section-link">View All Products →</a>
        </div>

        <div class="product-grid">
            <c:forEach items="${featuredProducts}" var="product">
                <div class="product-card">
                    <c:if test="${not empty product.badge}">
                        <span class="product-badge">${product.badge}</span>
                    </c:if>
                    <c:if test="${product.discountPercentage > 0}">
                        <span class="discount-tag">-${product.discountPercentage}%</span>
                    </c:if>

                    <div class="product-image-container">
                        <button type="button" class="wishlist-toggle-btn" data-id="${product.id}" onclick="toggleWishlist(${product.id}, this, event)" title="Add to Wishlist & Price Tracker" aria-label="Add to Wishlist">
                            <span class="heart-icon">🤍</span>
                        </button>
                        <a href="${pageContext.request.contextPath}/product?id=${product.id}">
                            <img src="${pageContext.request.contextPath}/${product.imageUrl}" alt="${product.name}" width="180" height="140" loading="eager" onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/placeholder-product.png';">
                        </a>
                    </div>

                    <div class="product-body">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px;">
                            <span class="product-cat">${product.category}</span>
                            <span style="font-size: 0.72rem; font-weight: 800; color: #4338ca; background: #e0e7ff; padding: 2px 7px; border-radius: 6px; letter-spacing: 0.3px; text-transform: uppercase;">${product.brand}</span>
                        </div>
                        <a href="${pageContext.request.contextPath}/product?id=${product.id}" class="product-name">${product.name}</a>
                        
                        <!-- Verified Dealer & Delivery Line -->
                        <div style="display: flex; align-items: center; justify-content: space-between; font-size: 0.78rem; color: #475569; margin: 4px 0 6px 0; background: #f8fafc; padding: 4px 8px; border-radius: 6px; border: 1px solid #f1f5f9;">
                            <span style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 170px;" title="Authorized Dealer">🏬 ${product.dealer}</span>
                            <span style="color: #059669; font-weight: 700; flex-shrink: 0;">★ ${product.dealerRating}</span>
                        </div>

                        <p class="product-desc">${product.description}</p>
                        
                        <div style="font-size: 0.75rem; color: #0d9488; font-weight: 700; margin-bottom: 6px; display: flex; align-items: center; gap: 4px;">
                            <span>⚡</span><span>${product.deliveryInfo}</span>
                        </div>

                        <div class="product-rating">
                            <span>★</span>
                            <strong>${product.rating}</strong>
                            <span class="rating-count">(${product.reviewCount} reviews)</span>
                        </div>

                        <div class="product-footer">
                            <div class="price-wrap">
                                <span class="current-price">${product.formattedPrice}</span>
                                <c:if test="${not empty product.formattedOriginalPrice}">
                                    <span class="original-price">${product.formattedOriginalPrice}</span>
                                </c:if>
                            </div>

                            <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                <input type="hidden" name="productId" value="${product.id}">
                                <input type="hidden" name="quantity" value="1">
                                <button type="submit" class="btn-add-cart">+ Add to Cart</button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>

</main>

<%@ include file="footer.jsp" %>
