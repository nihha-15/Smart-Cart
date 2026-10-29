<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<main class="main-content">

    <div class="section-header">
        <div>
            <h1 class="section-title">
                <c:if test="${not empty searchQuery}">Search Results for "${searchQuery}"</c:if>
                <c:if test="${empty searchQuery}">
                    <c:if test="${empty selectedCategory || selectedCategory == 'all'}">All Products</c:if>
                    <c:if test="${selectedCategory == 'laptops'}">Laptops &amp; Computers</c:if>
                    <c:if test="${selectedCategory == 'smartphones'}">Smartphones</c:if>
                    <c:if test="${selectedCategory == 'tablets'}">Tablets &amp; Stylus</c:if>
                    <c:if test="${selectedCategory == 'wearables'}">Smartwatches &amp; Wearables</c:if>
                    <c:if test="${selectedCategory == 'chargers'}">Fast GaN Chargers</c:if>
                    <c:if test="${selectedCategory == 'cables'}">High-Speed Cables &amp; Adapters</c:if>
                    <c:if test="${selectedCategory == 'audio'}">Audio &amp; Earbuds</c:if>
                    <c:if test="${selectedCategory == 'cases'}">Protective Cases</c:if>
                    <c:if test="${selectedCategory == 'monitors'}">4K Gaming Monitors &amp; Workstation Gear</c:if>
                    <c:if test="${selectedCategory == 'smart-home'}">Smart Home Automation &amp; IoT</c:if>
                    <c:if test="${selectedCategory == 'desk-setup'}">Desk Setup, Docks &amp; Streaming Gear</c:if>
                    <c:if test="${selectedCategory == 'gaming'}">High-Performance Gaming Gear</c:if>
                    <c:if test="${selectedCategory == 'power'}">Power Management &amp; Surge Protection</c:if>
                </c:if>
            </h1>
            <p class="section-desc">Showing verified electronics and accessories ready for intelligent cart evaluation.</p>
        </div>
    </div>

    <!-- Filter Bar: Brands and Price Range Tiers -->
    <div style="background: #ffffff; border: 1px solid #e2e8f0; border-radius: 10px; padding: 1rem 1.25rem; margin-bottom: 2rem; box-shadow: 0 1px 3px rgba(0,0,0,0.04);">
        <div style="display: flex; flex-direction: column; gap: 0.75rem;">
            <!-- Brand Filter Row -->
            <div style="display: flex; align-items: center; gap: 0.5rem; flex-wrap: wrap;">
                <span style="font-size: 0.82rem; font-weight: 800; color: #475569; min-width: 75px;">BRANDS:</span>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&q=${searchQuery}" class="cat-chip ${empty selectedBrand || selectedBrand == 'all' ? 'active' : ''}">All Brands</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=Samsung&q=${searchQuery}" class="cat-chip ${selectedBrand == 'Samsung' ? 'active' : ''}">Samsung</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=Apple&q=${searchQuery}" class="cat-chip ${selectedBrand == 'Apple' ? 'active' : ''}">Apple</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=OnePlus&q=${searchQuery}" class="cat-chip ${selectedBrand == 'OnePlus' ? 'active' : ''}">OnePlus</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=ASUS&q=${searchQuery}" class="cat-chip ${selectedBrand == 'ASUS' ? 'active' : ''}">ASUS</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=Xiaomi&q=${searchQuery}" class="cat-chip ${selectedBrand == 'Xiaomi' ? 'active' : ''}">Xiaomi / Redmi</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=Sony&q=${searchQuery}" class="cat-chip ${selectedBrand == 'Sony' ? 'active' : ''}">Sony</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=Dell&q=${searchQuery}" class="cat-chip ${selectedBrand == 'Dell' ? 'active' : ''}">Dell</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=Lenovo&q=${searchQuery}" class="cat-chip ${selectedBrand == 'Lenovo' ? 'active' : ''}">Lenovo</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=Anker&q=${searchQuery}" class="cat-chip ${selectedBrand == 'Anker' ? 'active' : ''}">Anker</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=Logitech&q=${searchQuery}" class="cat-chip ${selectedBrand == 'Logitech' ? 'active' : ''}">Logitech</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=boAt&q=${searchQuery}" class="cat-chip ${selectedBrand == 'boAt' ? 'active' : ''}">boAt</a>
            </div>

            <!-- Price Range Filter Row -->
            <div style="display: flex; align-items: center; gap: 0.5rem; flex-wrap: wrap;">
                <span style="font-size: 0.82rem; font-weight: 800; color: #475569; min-width: 75px;">BUDGET:</span>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=${selectedBrand}&q=${searchQuery}" class="cat-chip ${empty selectedPriceRange || selectedPriceRange == 'all' ? 'active' : ''}">All Prices</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=${selectedBrand}&priceRange=budget&q=${searchQuery}" class="cat-chip ${selectedPriceRange == 'budget' ? 'active' : ''}">Under ₹2,000 (Budget)</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=${selectedBrand}&priceRange=mid&q=${searchQuery}" class="cat-chip ${selectedPriceRange == 'mid' ? 'active' : ''}">₹2,000 - ₹25,000 (Mid-Range)</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=${selectedBrand}&priceRange=premium&q=${searchQuery}" class="cat-chip ${selectedPriceRange == 'premium' ? 'active' : ''}">₹25,000 - ₹70,000 (Flagship)</a>
                <a href="${pageContext.request.contextPath}/products?category=${selectedCategory}&brand=${selectedBrand}&priceRange=flagship&q=${searchQuery}" class="cat-chip ${selectedPriceRange == 'flagship' ? 'active' : ''}">₹70,000+ (Ultra High-End)</a>
            </div>
        </div>
    </div>

    <!-- Product Grid -->
    <div class="product-grid">
        <c:forEach items="${products}" var="product">
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

                    <!-- Verified Dealer Line -->
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

    <c:if test="${empty products}">
        <div style="text-align: center; padding: 4rem 1rem; background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md);">
            <div style="font-size: 3rem; margin-bottom: 1rem;">🔍</div>
            <h3 style="font-size: 1.3rem; margin-bottom: 0.5rem;">No matching products found</h3>
            <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">Try adjusting your search terms or view all products in the catalogue.</p>
            <a href="${pageContext.request.contextPath}/products" class="btn-primary">View All Products</a>
        </div>
    </c:if>

</main>

<%@ include file="footer.jsp" %>
