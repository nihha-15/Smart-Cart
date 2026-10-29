<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<main class="main-content">

    <div style="margin-bottom: 1.5rem;">
        <a href="${pageContext.request.contextPath}/products" style="color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; font-weight: 600;">
            ← Back to Products
        </a>
    </div>

    <!-- Product Details Main Layout -->
    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 3rem; margin-bottom: 3.5rem; align-items: flex-start;">
        
        <!-- Left: Product Image Box -->
        <div style="background: var(--bg-subtle); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 3rem; display: flex; align-items: center; justify-content: center; position: relative;">
            <c:if test="${not empty product.badge}">
                <span class="product-badge">${product.badge}</span>
            </c:if>
            <c:if test="${product.discountPercentage > 0}">
                <span class="discount-tag">-${product.discountPercentage}% OFF</span>
            </c:if>
            <img src="${pageContext.request.contextPath}/${product.imageUrl}" alt="${product.name}" style="max-height: 280px; max-width: 100%; width: auto; height: auto; object-fit: contain; display: block; margin: 0 auto;" onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/placeholder-product.png';">
        </div>

        <!-- Right: Info & Actions -->
        <div>
            <span class="product-cat">${product.category}</span>
            <h1 style="font-size: 2.2rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.75rem; line-height: 1.2;">
                ${product.name}
            </h1>

            <div class="product-rating" style="margin-bottom: 1.25rem;">
                <span style="font-size: 1.1rem;">★</span>
                <strong style="font-size: 1rem;">${product.rating}</strong>
                <span class="rating-count" style="font-size: 0.9rem;">(${product.reviewCount} customer reviews)</span>
                <span style="color: var(--border-color); margin: 0 0.5rem;">|</span>
                <span style="color: var(--accent-emerald-dark); font-weight: 700; font-size: 0.88rem;">
                    ✓ In Stock (${product.stock} units available)
                </span>
            </div>

            <div style="margin-bottom: 1.5rem; display: flex; align-items: baseline; gap: 0.75rem; flex-wrap: wrap;">
                <span style="font-size: 2rem; font-weight: 800; color: var(--text-primary);">${product.formattedPrice}</span>
                <c:if test="${not empty product.formattedOriginalPrice}">
                    <span style="font-size: 1.1rem; color: var(--text-muted); text-decoration: line-through;">${product.formattedOriginalPrice}</span>
                </c:if>
                <c:if test="${product.lowestPriceNow}">
                    <span class="price-drop-badge" style="background: #ecfdf5; color: #047857; border: 1px solid #a7f3d0; padding: 0.35rem 0.75rem; border-radius: 999px; font-size: 0.82rem; font-weight: 800; display: inline-flex; align-items: center; gap: 0.35rem;">
                        📉 30-Day Lowest Price Guarantee (Save ${product.formattedPriceDropAmount})
                    </span>
                </c:if>
            </div>

            <!-- Verified Merchant & Dealer Box -->
            <div style="margin-bottom: 1.5rem; background: #f8fafc; border: 1px solid #e2e8f0; border-radius: var(--radius-md); padding: 1rem 1.25rem; display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 1rem;">
                <div>
                    <div style="font-size: 0.75rem; color: #64748b; font-weight: 700; text-transform: uppercase;">Sold &amp; Dispatched By</div>
                    <div style="font-size: 0.95rem; font-weight: 800; color: #1e293b; margin-top: 3px; display: flex; align-items: center; gap: 6px;">
                        <span>🏬 ${product.dealer}</span>
                    </div>
                    <div style="margin-top: 4px;">
                        <span style="background: #ecfdf5; color: #047857; font-size: 0.75rem; padding: 2px 7px; border-radius: 999px; font-weight: 700; border: 1px solid #a7f3d0;">★ ${product.dealerRating} Merchant Rating</span>
                    </div>
                </div>
                <div>
                    <div style="font-size: 0.75rem; color: #64748b; font-weight: 700; text-transform: uppercase;">Delivery Promise</div>
                    <div style="font-size: 0.95rem; font-weight: 800; color: #0d9488; margin-top: 3px;">
                        ⚡ ${product.deliveryInfo}
                    </div>
                    <div style="font-size: 0.8rem; color: #64748b; margin-top: 4px;">Standard courier tracking included</div>
                </div>
                <div>
                    <div style="font-size: 0.75rem; color: #64748b; font-weight: 700; text-transform: uppercase;">Assurance &amp; Warranty</div>
                    <div style="font-size: 0.88rem; font-weight: 700; color: #3b82f6; margin-top: 3px;">
                        🛡️ ${product.warrantyInfo}
                    </div>
                </div>
            </div>

            <p style="font-size: 1rem; color: var(--text-secondary); line-height: 1.6; margin-bottom: 2rem;">
                ${product.description}
            </p>

            <!-- Add to Cart & Wishlist Form -->
            <div style="display: flex; gap: 0.75rem; align-items: center; margin-bottom: 1rem; flex-wrap: wrap;">
                <form action="${pageContext.request.contextPath}/cart/add" method="post" style="display: flex; gap: 0.75rem; align-items: center; flex: 1; min-width: 250px; margin: 0;">
                    <input type="hidden" name="productId" value="${product.id}">
                    <div style="display: flex; align-items: center; border: 1px solid var(--border-color); border-radius: var(--radius-sm); background: #ffffff;">
                        <label for="pQty" style="padding: 0.5rem 0.75rem; font-size: 0.85rem; font-weight: 700; color: var(--text-muted);">QTY</label>
                        <input type="number" id="pQty" name="quantity" value="1" min="1" max="${product.stock}" style="width: 60px; border: none; padding: 0.5rem; font-weight: 700; font-size: 1rem; outline: none;">
                    </div>
                    <button type="submit" class="btn-primary" style="flex: 1; justify-content: center; padding: 0.85rem 1.5rem;">
                        🛒 Add to SmartCart
                    </button>
                </form>

                <button type="button" class="btn-wishlist-hero" id="hero-wishlist-btn" data-id="${product.id}" onclick="toggleWishlist(${product.id}, this, event)" title="Add to Wishlist & Price Tracker">
                    <span class="heart-icon">🤍</span>
                    <span class="wishlist-btn-text">Add to Wishlist & Price Watch</span>
                </button>
            </div>

            <!-- Quick Compare Link to Similar Products -->
            <div style="margin-bottom: 2rem;">
                <a href="#similar-products" style="display: inline-flex; align-items: center; gap: 6px; font-size: 0.88rem; font-weight: 700; color: #4338ca; text-decoration: none; background: #e0e7ff; padding: 0.4rem 0.85rem; border-radius: var(--radius-full);">
                    <span>🔍</span>
                    <span>Compare with similar ${product.category} alternatives ↓</span>
                </a>
            </div>

            <!-- Technical Specifications Table (Used by Compatibility Engine) -->
            <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); overflow: hidden;">
                <div style="background: var(--bg-subtle); padding: 0.85rem 1.25rem; border-bottom: 1px solid var(--border-color); font-weight: 700; font-size: 0.95rem;">
                    🔬 Verified Hardware Specifications
                </div>
                <div style="padding: 1rem 1.25rem;">
                    <c:forEach items="${product.specs}" var="spec">
                        <div style="display: flex; justify-content: space-between; padding: 0.45rem 0; border-bottom: 1px solid var(--border-subtle); font-size: 0.9rem;">
                            <span style="color: var(--text-secondary); text-transform: capitalize;">${spec.key.replace('_', ' ')}</span>
                            <strong style="color: var(--text-primary);">${spec.value}</strong>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Feature E: Lifetime TCO & Power Rating Profile -->
            <c:if test="${product.powerWatts > 0 || product.annualMaintenanceCost > 0}">
                <div style="margin-top: 1.5rem; background: #f8fafc; border: 1px solid #e2e8f0; border-radius: var(--radius-md); padding: 1.25rem;">
                    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 0.5rem;">
                        <span style="font-weight: 800; font-size: 0.95rem; color: var(--text-primary);">⚡ Energy &amp; Upkeep Profile</span>
                        <span class="tco-badge">Estimated Upkeep</span>
                    </div>
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 0.75rem; font-size: 0.88rem;">
                        <div>
                            <span style="color: var(--text-muted);">Rated Power Draw:</span>
                            <div><strong>${product.powerWatts} Watts</strong></div>
                        </div>
                        <div>
                            <span style="color: var(--text-muted);">Est. Annual Running Cost:</span>
                            <div><strong>${product.formattedAnnualRunningCost}</strong> <span style="font-size: 0.75rem; color: #64748b;">(@ ₹8/kWh)</span></div>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Budget Alternative Suggestion Callout -->
            <c:if test="${not empty alternative}">
                <div style="margin-top: 1.5rem; background: var(--accent-emerald-light); border: 1px solid rgba(16, 185, 129, 0.4); border-radius: var(--radius-md); padding: 1.25rem;">
                    <div style="display: flex; align-items: center; gap: 0.5rem; font-weight: 800; color: var(--accent-emerald-dark); margin-bottom: 0.35rem;">
                        <span>💡 Budget Guardian Suggestion</span>
                        <span class="alt-savings-badge">Save ${alternative.formattedSavings}</span>
                    </div>
                    <p style="font-size: 0.9rem; color: #065f46; margin-bottom: 0.75rem;">
                        ${alternative.reason}
                    </p>
                    <a href="${pageContext.request.contextPath}/product?id=${alternative.alternativeProduct.id}" class="btn-primary" style="font-size: 0.85rem; padding: 0.4rem 1rem;">
                        View Alternative (${alternative.alternativeProduct.name}) →
                    </a>
                </div>
            </c:if>

        </div>
    </div>

    <!-- Frequently Bought Together Bundle (Feature) -->
    <c:if test="${not empty boughtTogether && not empty boughtTogether.companionProducts}">
        <section class="bought-together-section" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 2rem; margin-bottom: 3.5rem; box-shadow: var(--shadow-sm);">
            <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 1rem;">
                <div>
                    <h2 style="font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.25rem;">
                        🤝 Frequently Bought Together
                    </h2>
                    <p style="font-size: 0.9rem; color: var(--text-secondary);">
                        Customers who purchased this item also bought these verified accessories. Save extra when buying as a bundle!
                    </p>
                </div>
                <c:if test="${boughtTogether.bundleDiscount > 0}">
                    <span style="background: #fef3c7; color: #b45309; border: 1px solid #fde68a; padding: 0.4rem 0.85rem; border-radius: 999px; font-size: 0.82rem; font-weight: 800;">
                        🏷️ Instant Bundle Discount: ${boughtTogether.formattedBundleDiscount} OFF
                    </span>
                </c:if>
            </div>

            <!-- Bundle Items Grid -->
            <div style="display: flex; align-items: center; gap: 1.5rem; flex-wrap: wrap; margin-bottom: 1.5rem;">
                <!-- Primary Product Card -->
                <div style="display: flex; align-items: center; gap: 1rem; background: var(--bg-subtle); border: 2px solid var(--accent-emerald); border-radius: var(--radius-md); padding: 1rem 1.25rem; min-width: 240px;">
                    <img src="${pageContext.request.contextPath}/${boughtTogether.primaryProduct.imageUrl}" alt="${boughtTogether.primaryProduct.name}" style="width: 55px; height: 55px; object-fit: contain;" onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/placeholder-product.png';">
                    <div>
                        <span style="font-size: 0.75rem; font-weight: 700; color: var(--accent-emerald); text-transform: uppercase;">This Item</span>
                        <div style="font-size: 0.9rem; font-weight: 700; color: var(--text-primary); line-height: 1.3;">${boughtTogether.primaryProduct.name}</div>
                        <strong style="font-size: 0.95rem; color: var(--text-primary);">${boughtTogether.primaryProduct.formattedPrice}</strong>
                    </div>
                </div>

                <c:forEach items="${boughtTogether.companionProducts}" var="companion">
                    <div style="font-size: 1.5rem; font-weight: 800; color: var(--text-muted);">+</div>
                    <!-- Companion Product Card -->
                    <div style="display: flex; align-items: center; gap: 1rem; background: var(--bg-subtle); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1rem 1.25rem; min-width: 240px;">
                        <img src="${pageContext.request.contextPath}/${companion.imageUrl}" alt="${companion.name}" style="width: 55px; height: 55px; object-fit: contain;" onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/placeholder-product.png';">
                        <div>
                            <span style="font-size: 0.75rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Companion</span>
                            <div style="font-size: 0.9rem; font-weight: 700; color: var(--text-primary); line-height: 1.3;">${companion.name}</div>
                            <strong style="font-size: 0.95rem; color: var(--text-primary);">${companion.formattedPrice}</strong>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Total Bundle Pricing & 1-Click Action -->
            <div style="display: flex; align-items: center; justify-content: space-between; background: var(--bg-subtle); border-radius: var(--radius-md); padding: 1.25rem 1.75rem; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <div style="font-size: 0.85rem; color: var(--text-muted);">Total Bundle Price for all ${boughtTogether.itemCount} items:</div>
                    <div style="display: flex; align-items: baseline; gap: 0.75rem;">
                        <span style="font-size: 1.7rem; font-weight: 800; color: var(--text-primary);">${boughtTogether.formattedBundlePrice}</span>
                        <c:if test="${boughtTogether.bundleDiscount > 0}">
                            <span style="font-size: 1rem; color: var(--text-muted); text-decoration: line-through;">${boughtTogether.formattedTotalRegularPrice}</span>
                            <span style="font-size: 0.85rem; font-weight: 700; color: var(--accent-emerald-dark);">Save ${boughtTogether.formattedBundleDiscount}</span>
                        </c:if>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/cart/addBoughtTogether" method="post">
                    <input type="hidden" name="productIds" value="${boughtTogether.productIdsCsv}">
                    <button type="submit" class="btn-primary" style="padding: 0.85rem 1.75rem; font-size: 0.95rem; display: inline-flex; align-items: center; gap: 0.5rem;">
                        <span>🛒</span> Add All ${boughtTogether.itemCount} to Cart
                    </button>
                </form>
            </div>
        </section>
    </c:if>

    <!-- Section: Similar Products & Competitor Alternatives -->
    <c:if test="${not empty similarProducts}">
        <section id="similar-products" class="similar-products-section">
            <div style="display: flex; justify-content: space-between; align-items: flex-end; flex-wrap: wrap; gap: 1rem; margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
                <div>
                    <div style="display: inline-flex; align-items: center; gap: 0.4rem; background: #e0e7ff; color: #4338ca; font-size: 0.75rem; font-weight: 800; padding: 0.25rem 0.65rem; border-radius: var(--radius-full); text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 0.4rem;">
                        <span>🔍 Smart Discovery</span>
                    </div>
                    <h2 style="font-size: 1.6rem; font-weight: 800; color: var(--text-primary); margin: 0;">
                        Similar Products & Competitor Alternatives
                    </h2>
                    <p style="color: var(--text-muted); font-size: 0.92rem; margin: 0.25rem 0 0 0;">
                        Looking for options? Compare rival models in <strong>${product.category}</strong> with matching specs and competitive pricing.
                    </p>
                </div>
                <a href="${pageContext.request.contextPath}/products?category=${product.category}" class="section-link" style="font-weight: 700; color: #4338ca; text-decoration: none;">
                    View all in ${product.category} →
                </a>
            </div>

            <div class="similar-grid">
                <c:forEach items="${similarProducts}" var="sim">
                    <div class="similar-card">
                        <div>
                            <div class="product-image-container" style="position: relative; height: 180px;">
                                <button type="button" class="wishlist-toggle-btn" data-id="${sim.id}" onclick="toggleWishlist(${sim.id}, this, event)" title="Add to Wishlist & Price Tracker" aria-label="Add to Wishlist">
                                    <span class="heart-icon">🤍</span>
                                </button>
                                <a href="${pageContext.request.contextPath}/product?id=${sim.id}">
                                    <img src="${pageContext.request.contextPath}/${sim.imageUrl}" alt="${sim.name}" width="180" height="140" loading="lazy" onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/placeholder-product.png';">
                                </a>
                            </div>

                            <div style="padding: 1rem 1.15rem;">
                                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px;">
                                    <span style="font-size: 0.72rem; font-weight: 800; color: #4338ca; background: #e0e7ff; padding: 1px 6px; border-radius: 4px; text-transform: uppercase;">${sim.brand}</span>
                                    <span style="font-size: 0.78rem; font-weight: 700; color: #059669;">★ ${sim.rating}</span>
                                </div>

                                <a href="${pageContext.request.contextPath}/product?id=${sim.id}" class="product-name" style="font-size: 0.95rem; font-weight: 700; line-height: 1.3; margin-bottom: 0.4rem; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; text-decoration: none; color: var(--text-primary);">
                                    ${sim.name}
                                </a>

                                <div style="font-size: 0.78rem; color: #64748b; margin-bottom: 0.6rem;">
                                    Dealer: <strong>${sim.dealer}</strong>
                                </div>

                                <div style="display: flex; align-items: baseline; gap: 0.5rem;">
                                    <span style="font-size: 1.15rem; font-weight: 800; color: var(--text-primary);">${sim.formattedPrice}</span>
                                    <c:if test="${sim.discountPercentage > 0}">
                                        <span style="font-size: 0.8rem; color: var(--text-muted); text-decoration: line-through;">${sim.formattedOriginalPrice}</span>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <div style="padding: 0 1.15rem 1.15rem 1.15rem; display: flex; gap: 0.5rem;">
                            <a href="${pageContext.request.contextPath}/product?id=${sim.id}" class="btn-secondary" style="flex: 1; text-align: center; text-decoration: none; padding: 0.55rem 0.5rem; font-size: 0.85rem; font-weight: 700;">
                                View Details
                            </a>
                            <form action="${pageContext.request.contextPath}/cart/add" method="post" style="margin: 0;">
                                <input type="hidden" name="productId" value="${sim.id}">
                                <input type="hidden" name="quantity" value="1">
                                <button type="submit" class="btn-primary" style="padding: 0.55rem 0.85rem; font-size: 0.85rem;" title="Add to Cart">
                                    🛒 Add
                                </button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </c:if>

</main>

<%@ include file="footer.jsp" %>
