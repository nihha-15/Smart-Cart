<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<main class="main-content">

    <!-- Top Breadcrumb & Navigation -->
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 0.75rem;">
        <a href="${pageContext.request.contextPath}/products" style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; font-weight: 600; display: inline-flex; align-items: center; gap: 6px;">
            <span>←</span> Back to Catalogue
        </a>
        <div style="font-size: 0.85rem; color: #64748b;">
            <a href="${pageContext.request.contextPath}/" style="color: #64748b; text-decoration: none;">Home</a>
            <span style="color: #cbd5e1; margin: 0 5px;">/</span>
            <strong style="color: #0f172a;">Smart Wishlist & Price Tracker</strong>
        </div>
    </div>

    <!-- Section Header -->
    <div class="section-header">
        <div>
            <div style="display: inline-flex; align-items: center; gap: 6px; background: #fee2e2; border: 1px solid #fecaca; color: #dc2626; font-size: 0.75rem; font-weight: 800; padding: 3px 10px; border-radius: var(--radius-full); text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 0.5rem;">
                <span>🎯 Price Tracker &amp; Device Pairing</span>
            </div>
            <h1 class="section-title">My Wishlist</h1>
            <p class="section-desc">Save products you love, set automated <strong>Price Drop Alerts</strong>, and get smart device pairing suggestions.</p>
        </div>

        <c:if test="${not empty wishlist && not wishlist.empty}">
            <div style="display: flex; gap: 0.75rem; align-items: center; flex-wrap: wrap;">
                <form action="${pageContext.request.contextPath}/wishlist/moveAllToCart" method="post" style="margin: 0;">
                    <button type="submit" class="btn-primary" style="padding: 0.65rem 1.25rem; font-size: 0.9rem; display: inline-flex; align-items: center; gap: 0.4rem;">
                        <span>🛒</span> Move All to Cart
                    </button>
                </form>
                <form action="${pageContext.request.contextPath}/wishlist/clear" method="post" style="margin: 0;" onsubmit="return confirm('Clear all items from your Wishlist?');">
                    <button type="submit" class="btn-dismiss" style="color: var(--danger-red); cursor: pointer;">Clear Wishlist</button>
                </form>
            </div>
        </c:if>
    </div>

    <!-- Empty State -->
    <c:if test="${empty wishlist || wishlist.empty}">
        <div style="text-align: center; padding: 4.5rem 1.5rem; background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm); max-width: 640px; margin: 2rem auto;">
            <div style="width: 76px; height: 76px; background: #fee2e2; border: 2px solid #fecaca; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 1.25rem auto; font-size: 2.2rem; color: #ef4444;">
                ♡
            </div>
            <h2 style="font-size: 1.5rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.5rem;">Your Wishlist is Empty</h2>
            <p style="color: var(--text-secondary); max-width: 480px; margin: 0 auto 2rem; line-height: 1.5; font-size: 0.95rem;">
                Browse our catalogue of 175 genuine gadgets across 13 tech categories. Click the heart icon on any product card to save it here and activate automated price drop alerts.
            </p>
            <a href="${pageContext.request.contextPath}/products" class="btn-primary" style="display: inline-flex; align-items: center; gap: 0.5rem; text-decoration: none; padding: 0.75rem 1.75rem;">Browse Catalogue →</a>
        </div>
    </c:if>

    <!-- Active Wishlist Layout -->
    <c:if test="${not empty wishlist && not wishlist.empty}">
        <!-- Control & Summary Strip -->
        <div style="background: #ffffff; border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1rem 1.5rem; margin-bottom: 1.5rem; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 1rem; box-shadow: var(--shadow-sm);">
            <div style="display: flex; align-items: center; gap: 1.5rem;">
                <div>
                    <span style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; letter-spacing: 0.5px;">Saved Items</span>
                    <div style="font-size: 1.3rem; font-weight: 800; color: var(--text-primary);">${wishlist.itemCount} Products</div>
                </div>
                <div style="height: 36px; width: 1px; background: var(--border-color);"></div>
                <div>
                    <span style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; letter-spacing: 0.5px;">Total Value</span>
                    <div style="font-size: 1.3rem; font-weight: 800; color: #4338ca;">${wishlist.formattedTotalValue}</div>
                </div>
            </div>

            <div style="font-size: 0.85rem; color: #475569; background: #f8fafc; border: 1px solid #e2e8f0; padding: 0.4rem 0.85rem; border-radius: var(--radius-sm);">
                <span>🎯 Automated Price Watcher: <strong style="color: #059669;">Active</strong></span>
            </div>
        </div>

        <!-- Smart Synergy & Ecosystem Alerts -->
        <c:if test="${not empty wishlist.synergyAlerts}">
            <div style="background: #f0fdf4; border: 1px solid #bbf7d0; border-radius: var(--radius-md); padding: 1.15rem 1.35rem; margin-bottom: 2rem;">
                <div style="display: flex; align-items: center; gap: 0.5rem; font-weight: 800; font-size: 0.95rem; color: #166534; margin-bottom: 0.5rem;">
                    <span>✨</span> SmartCart Synergy & Pairing Insights
                </div>
                <ul style="margin: 0; padding-left: 1.25rem; color: #15803d; font-size: 0.88rem; line-height: 1.6;">
                    <c:forEach items="${wishlist.synergyAlerts}" var="alert">
                        <li>${alert}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <!-- Wishlist Grid -->
        <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(310px, 1fr)); gap: 1.5rem;">
            <c:forEach items="${wishlist.itemList}" var="item">
                <div class="product-card" style="display: flex; flex-direction: column; justify-content: space-between; position: relative;">
                    
                    <div>
                        <!-- Image Container with Heart Active Toggle -->
                        <div class="product-image-container" style="position: relative;">
                            <button type="button" class="wishlist-toggle-btn active" data-id="${item.product.id}"
                                    onclick="toggleWishlist(${item.product.id}, this, event)" 
                                    title="Remove from Wishlist" 
                                    aria-label="Wishlist Toggle">
                                <span class="heart-icon">❤️</span>
                            </button>
                            <a href="${pageContext.request.contextPath}/product?id=${item.product.id}">
                                <img src="${pageContext.request.contextPath}/${item.product.imageUrl}" alt="${item.product.name}" width="180" height="140" loading="eager" onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/placeholder-product.png';">
                            </a>
                        </div>

                        <!-- Card Body -->
                        <div class="product-body" style="padding: 1.25rem;">
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;">
                                <span class="product-cat">${item.product.category}</span>
                                <span style="font-size: 0.72rem; font-weight: 800; color: #4338ca; background: #e0e7ff; padding: 2px 7px; border-radius: 6px; letter-spacing: 0.3px; text-transform: uppercase;">${item.product.brand}</span>
                            </div>

                            <a href="${pageContext.request.contextPath}/product?id=${item.product.id}" class="product-name" style="font-size: 1rem; line-height: 1.35; margin-bottom: 0.5rem; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; text-decoration: none; color: var(--text-primary);">
                                ${item.product.name}
                            </a>

                            <!-- Dealer & Rating -->
                            <div style="display: flex; align-items: center; justify-content: space-between; font-size: 0.78rem; color: #475569; margin: 4px 0 8px 0; background: #f8fafc; padding: 4px 8px; border-radius: 6px; border: 1px solid #f1f5f9;">
                                <span style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 170px;">🏬 ${item.product.dealer}</span>
                                <span style="color: #059669; font-weight: 700;">★ ${item.product.dealerRating}</span>
                            </div>

                            <!-- Pricing -->
                            <div style="display: flex; align-items: baseline; gap: 0.5rem; margin-bottom: 0.75rem;">
                                <span style="font-size: 1.25rem; font-weight: 800; color: var(--text-primary);">${item.product.formattedPrice}</span>
                                <c:if test="${item.product.discountPercentage > 0}">
                                    <span style="font-size: 0.85rem; color: var(--text-muted); text-decoration: line-through;">${item.product.formattedOriginalPrice}</span>
                                    <span style="font-size: 0.75rem; font-weight: 700; color: var(--accent-emerald-dark);">-${item.product.discountPercentage}%</span>
                                </c:if>
                            </div>

                            <!-- Unique Feature: Target Price Drop Alert Box -->
                            <div style="background: #f8fafc; border: 1px solid #e2e8f0; border-radius: var(--radius-sm); padding: 0.85rem 1rem; margin-bottom: 1rem;">
                                <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 4px;">
                                    <span style="font-size: 0.75rem; font-weight: 700; color: #475569; text-transform: uppercase; letter-spacing: 0.3px;">🎯 Target Price Alert</span>
                                    <span style="font-size: 0.72rem; color: #64748b;">Added: ${item.formattedAddedDate}</span>
                                </div>

                                <div style="font-size: 0.85rem; color: #1e293b; margin-bottom: 6px;">
                                    Alert threshold: <strong style="color: #4338ca;">${item.formattedTargetPrice}</strong>
                                    <c:if test="${item.targetMet}">
                                        <span style="display: inline-block; background: #dcfce7; color: #166534; font-size: 0.7rem; font-weight: 800; padding: 1px 6px; border-radius: 4px; margin-left: 4px;">TARGET REACHED</span>
                                    </c:if>
                                </div>

                                <div style="font-size: 0.75rem; color: #64748b; margin-bottom: 8px;">
                                    Est. Historical Low: <strong>${item.formattedHistoricalLow}</strong>
                                </div>

                                <form action="${pageContext.request.contextPath}/wishlist/updateTarget" method="post" style="display: flex; gap: 6px; align-items: center; margin: 0;">
                                    <input type="hidden" name="productId" value="${item.product.id}">
                                    <input type="number" name="targetPrice" value="${item.targetPrice}" step="100" min="100" style="padding: 0.35rem 0.5rem; font-size: 0.82rem; border: 1px solid #cbd5e1; border-radius: var(--radius-sm); width: 110px;">
                                    <button type="submit" style="padding: 0.35rem 0.75rem; font-size: 0.78rem; font-weight: 700; background: #e0e7ff; color: #4338ca; border: 1px solid #c7d2fe; border-radius: var(--radius-sm); cursor: pointer;">Update Alert</button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Bottom Actions -->
                    <div style="padding: 0 1.25rem 1.25rem 1.25rem; display: flex; gap: 0.5rem; align-items: center;">
                        <form action="${pageContext.request.contextPath}/wishlist/moveToCart" method="post" style="flex: 1; margin: 0;">
                            <input type="hidden" name="productId" value="${item.product.id}">
                            <button type="submit" class="btn-primary" style="width: 100%; padding: 0.65rem 0.75rem; font-size: 0.88rem; display: inline-flex; align-items: center; justify-content: center; gap: 0.4rem;">
                                <span>🛒</span> Move to Cart
                            </button>
                        </form>

                        <form action="${pageContext.request.contextPath}/wishlist/remove" method="post" style="margin: 0;">
                            <input type="hidden" name="productId" value="${item.product.id}">
                            <button type="submit" class="btn-secondary" style="padding: 0.65rem 0.75rem; font-size: 0.88rem; color: #94a3b8; border-color: #e2e8f0; cursor: pointer;" title="Remove from Wishlist">
                                🗑️
                            </button>
                        </form>
                    </div>

                </div>
            </c:forEach>
        </div>
    </c:if>

</main>

<%@ include file="footer.jsp" %>
