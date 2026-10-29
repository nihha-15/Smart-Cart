<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<main class="main-content">

    <div class="section-header">
        <div>
            <h1 class="section-title">Your Shopping Cart</h1>
            <p class="section-desc">Review selected electronics, track your shopping budget in real-time, and get smart compatibility advice.</p>
        </div>
        <c:if test="${not empty sessionScope.cart && not sessionScope.cart.empty}">
            <form action="${pageContext.request.contextPath}/cart/clear" method="post" onsubmit="return confirm('Clear all items from your cart?');">
                <button type="submit" class="btn-dismiss" style="color: var(--danger-red);">Clear Cart</button>
            </form>
        </c:if>
    </div>

    <!-- Empty Cart State -->
    <c:if test="${empty sessionScope.cart || sessionScope.cart.empty}">
        <div style="text-align: center; padding: 4.5rem 1.5rem; background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); box-shadow: var(--shadow-sm);">
            <div style="font-size: 3.5rem; margin-bottom: 1rem;">🛒</div>
            <h2 style="font-size: 1.5rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.5rem;">Your Cart is Empty</h2>
            <p style="color: var(--text-secondary); max-width: 520px; margin: 0 auto 2rem;">
                Explore our catalogue of laptops, smartphones, tablets, GaN fast chargers, and accessories to see the smart shopping assistant in action.
            </p>
            <a href="${pageContext.request.contextPath}/products" class="btn-primary">Browse Catalogue →</a>
        </div>
    </c:if>

    <!-- Active Cart Layout -->
    <c:if test="${not empty sessionScope.cart && not sessionScope.cart.empty}">
        
        <!-- ===================================================================
             SMART BUDGET TRACKER
             =================================================================== -->
        <div class="budget-guardian-widget">
            <div class="budget-header">
                <div class="budget-title">
                    <span>🛡️ Shopping Budget Tracker</span>
                    <span class="brand-badge">Live Budget Monitor</span>
                </div>

                <!-- Update Budget Form -->
                <form action="${pageContext.request.contextPath}/cart/budget" method="post" class="budget-form-inline">
                    <span class="currency-symbol">₹</span>
                    <div class="budget-input-group">
                        <input type="number" id="budgetInput" name="budget" value="${sessionScope.cart.budgetAsInt}" min="500" step="500" class="budget-input" title="Enter your maximum target budget in INR">
                    </div>
                    <button type="submit" class="btn-update-budget">Set Budget</button>
                </form>
            </div>

            <!-- Progress Bar -->
            <div class="budget-progress-container">
                <div class="progress-track">
                    <div id="budgetProgressBar" class="progress-bar-fill ${sessionScope.cart.budgetStatusClass}" 
                         style="width: ${sessionScope.cart.clampedBudgetPercentage}%;"></div>
                </div>
            </div>

            <!-- Metrics Row -->
            <div class="budget-metrics-row">
                <div>
                    <span style="color: #64748b; font-size: 0.85rem;">Cart Total: </span>
                    <strong id="cartTotalValue" data-total="${sessionScope.cart.total}">${sessionScope.cart.formattedTotal}</strong>
                </div>
                <div>
                    <span style="color: #64748b; font-size: 0.85rem;">Your Budget Limit: </span>
                    <strong>${sessionScope.cart.formattedBudget}</strong>
                </div>
                <div>
                    <span style="color: #64748b; font-size: 0.85rem;">Status: </span>
                    <strong id="remainingBudgetText">
                        <c:if test="${sessionScope.cart.overBudget}">
                            <span style="color: var(--danger-red); font-weight: 800;">Over Budget by ${sessionScope.cart.formattedRemainingBudget}</span>
                        </c:if>
                        <c:if test="${not sessionScope.cart.overBudget}">
                            <span style="color: var(--accent-emerald-dark); font-weight: 800;">${sessionScope.cart.formattedRemainingBudget} Remaining</span>
                        </c:if>
                    </strong>
                </div>
            </div>

            <!-- Budget Warning / Danger Banners -->
            <c:if test="${sessionScope.cart.overBudget}">
                <div class="budget-warning-banner banner-danger">
                    <span>🚨</span>
                    <span><strong>Budget Limit Exceeded:</strong> Your cart total is <strong>${sessionScope.cart.formattedRemainingBudget}</strong> over your set budget limit. You can adjust your limit above or view money-saving alternatives in the Smart Analysis report.</span>
                </div>
            </c:if>
            <c:if test="${sessionScope.cart.approachingBudget}">
                <div class="budget-warning-banner banner-warning">
                    <span>⚠️</span>
                    <span><strong>Approaching Budget Limit:</strong> You have used <strong>${sessionScope.cart.budgetUsagePercentage}%</strong> of your set budget (${sessionScope.cart.formattedRemainingBudget} remaining). Spend carefully on optional accessories.</span>
                </div>
            </c:if>
            <c:if test="${not sessionScope.cart.overBudget and not sessionScope.cart.approachingBudget}">
                <div class="budget-warning-banner banner-safe">
                    <span>✓</span>
                    <span><strong>Within Budget:</strong> You are safely within your budget limit (${sessionScope.cart.formattedRemainingBudget} remaining).</span>
                </div>
            </c:if>
        </div>

        <!-- ===================================================================
             3-YEAR LONG-TERM COST ESTIMATOR
             =================================================================== -->
        <div class="tco-card">
            <div class="tco-header">
                <div class="tco-title">
                    <span>⚡ 3-Year Ownership Cost Estimate</span>
                </div>
                <span class="tco-badge">Electricity &amp; Upkeep Estimate</span>
            </div>
            
            <div class="tco-metrics-grid">
                <div class="tco-stat-box">
                    <div class="tco-stat-label">Purchase Price (Today)</div>
                    <div class="tco-stat-val">${sessionScope.cart.formattedTotal}</div>
                    <div class="tco-stat-sub">Cart total including 18% GST</div>
                </div>
                <div class="tco-stat-box">
                    <div class="tco-stat-label">Estimated 3-Yr Electricity</div>
                    <div class="tco-stat-val" style="color: #0284c7;">${sessionScope.cart.formattedThreeYearEnergyCost}</div>
                    <div class="tco-stat-sub">Power cost calculated from device wattage</div>
                </div>
                <div class="tco-stat-box">
                    <div class="tco-stat-label">Estimated 3-Yr Maintenance</div>
                    <div class="tco-stat-val" style="color: #f59e0b;">${sessionScope.cart.formattedThreeYearMaintenanceCost}</div>
                    <div class="tco-stat-sub">Expected battery health upkeep &amp; servicing</div>
                </div>
                <div class="tco-stat-box" style="background: #f0fdf4; border-color: #86efac;">
                    <div class="tco-stat-label" style="color: #059669;">Total 3-Year Cost</div>
                    <div class="tco-stat-val" style="color: #059669;">${sessionScope.cart.formattedEstimatedThreeYearTCO}</div>
                    <div class="tco-stat-sub">Total estimated cost to own this setup</div>
                </div>
            </div>
        </div>

        <!-- ===================================================================
             SMART SHOPPING ADVICE & COMPATIBILITY WARNINGS
             =================================================================== -->
        <c:if test="${not empty report && report.hasAnyRisks()}">
            <div style="margin-bottom: 2rem;">
                <h3 style="font-size: 1.15rem; font-weight: 800; color: var(--text-primary); margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem;">
                    <span>💡 Smart Shopping Advice &amp; Warnings</span>
                    <span style="font-size: 0.82rem; font-weight: 600; color: var(--text-secondary);">(Automated check before checkout)</span>
                </h3>

                <!-- Compatibility Warnings -->
                <c:forEach items="${report.compatibilityIssues}" var="issue">
                    <div class="alert-card ${issue.severity == 'CRITICAL' ? 'alert-critical' : 'alert-warning'}">
                        <div class="alert-icon">${issue.severity == 'CRITICAL' ? '⛔' : '⚠️'}</div>
                        <div class="alert-content">
                            <div class="alert-header">
                                <h4 class="alert-title">${issue.title}</h4>
                                <span class="alert-severity-badge ${issue.severity == 'CRITICAL' ? 'badge-critical' : 'badge-warning'}">
                                    ${issue.severity == 'CRITICAL' ? 'Hardware Conflict' : 'Recommendation'}
                                </span>
                            </div>
                            <p class="alert-msg">${issue.reason}</p>
                            <div class="alert-actions">
                                <span style="font-size: 0.85rem; font-weight: 600; color: var(--text-primary);">Recommendation: ${issue.suggestedAction}</span>
                                <c:if test="${issue.recommendedProductId > 0}">
                                    <form action="${pageContext.request.contextPath}/cart/add" method="post" style="display: inline;">
                                        <input type="hidden" name="productId" value="${issue.recommendedProductId}">
                                        <input type="hidden" name="quantity" value="1">
                                        <button type="submit" class="btn-solve-action">+ Add ${issue.recommendedProductName}</button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <!-- Smart Shopping Tips (Mistake Prevention) -->
                <c:forEach items="${report.mistakeAlerts}" var="alert">
                    <div class="alert-card alert-warning">
                        <div class="alert-icon">💡</div>
                        <div class="alert-content">
                            <div class="alert-header">
                                <h4 class="alert-title">${alert.title}</h4>
                                <span class="alert-severity-badge badge-warning">Shopping Advice</span>
                            </div>
                            <p class="alert-msg">${alert.message}</p>
                            <div class="alert-explanation">
                                <strong>Why you see this:</strong> ${alert.explanation}
                            </div>
                            <div class="alert-actions" style="margin-top: 0.75rem;">
                                <form action="${pageContext.request.contextPath}/cart/dismissAlert" method="post">
                                    <input type="hidden" name="alertId" value="${alert.id}">
                                    <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/cart">
                                    <button type="submit" class="btn-dismiss-alert">✓ I understand, keep item</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <!-- ===================================================================
             SETUP BUNDLES ("COMPLETE YOUR SETUP")
             =================================================================== -->
        <c:if test="${not empty report.recommendedBundles}">
            <div class="setup-bundles-container">
                <div class="setup-bundles-header">
                    <div class="setup-bundles-title">
                        <span>🎁 Recommended Setup Bundles</span>
                    </div>
                    <span class="bundle-savings-badge">Bundle &amp; Save</span>
                </div>
                <p style="font-size: 0.88rem; color: var(--text-secondary); margin-bottom: 1.25rem;">
                    We found compatible companion accessories designed for your chosen devices at discounted bundle prices.
                </p>

                <c:forEach items="${report.recommendedBundles}" var="bundle">
                    <div class="bundle-card">
                        <div class="bundle-info">
                            <div class="bundle-title">${bundle.bundleName}</div>
                            <div class="bundle-desc">${bundle.description}</div>
                        </div>
                        <div class="bundle-pricing">
                            <div class="bundle-price">${bundle.formattedBundlePrice}</div>
                            <div style="font-size: 0.8rem; color: var(--text-muted); text-decoration: line-through;">
                                ${bundle.formattedOriginalPrice}
                            </div>
                            <span class="bundle-savings-badge">Save ${bundle.formattedDiscountAmount}</span>
                            <form action="${pageContext.request.contextPath}/cart/addBundle" method="post" style="margin-top: 0.5rem;">
                                <input type="hidden" name="primaryId" value="${bundle.primaryProduct.id}">
                                <input type="hidden" name="companionId" value="${bundle.companionProduct.id}">
                                <button type="submit" class="btn-add-bundle">+ Add to Cart</button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <!-- Cart Items Table and Order Summary -->
        <div class="cart-layout">
            
            <!-- Left: Items List -->
            <div class="cart-items-card">
                <div class="cart-items-header">
                    <h2 style="font-size: 1.15rem; font-weight: 800;">Items in Cart (${sessionScope.cart.uniqueItemCount})</h2>
                    <span style="font-size: 0.88rem; color: var(--text-secondary); font-weight: 600;">Server-side verified pricing</span>
                </div>

                <c:forEach items="${sessionScope.cart.itemList}" var="item">
                    <div class="cart-item-row">
                        <div class="cart-item-thumb">
                            <img src="${pageContext.request.contextPath}/${item.product.imageUrl}" alt="${item.product.name}" onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/placeholder-product.png';">
                        </div>

                        <div class="cart-item-info">
                            <div style="display: flex; gap: 6px; align-items: center; margin-bottom: 2px;">
                                <span class="cart-item-category">${item.product.category}</span>
                                <span style="font-size: 0.72rem; font-weight: 700; color: #4338ca; background: #e0e7ff; padding: 1px 6px; border-radius: 4px;">${item.product.brand}</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/product?id=${item.product.id}" class="cart-item-title">
                                ${item.product.name}
                            </a>
                            <div style="font-size: 0.78rem; color: #64748b; margin-top: 2px;">
                                Sold by: <strong>${item.product.dealer}</strong> (★ ${item.product.dealerRating})
                            </div>
                            <span class="cart-item-price">${item.product.formattedPrice} each</span>
                        </div>

                        <!-- Quantity Stepper -->
                        <div class="qty-control-form">
                            <form action="${pageContext.request.contextPath}/cart/update" method="post" style="display: inline;">
                                <input type="hidden" name="productId" value="${item.product.id}">
                                <input type="hidden" name="quantity" value="${item.quantity - 1}">
                                <button type="submit" class="btn-qty" title="Decrease Quantity">-</button>
                            </form>
                            
                            <span class="qty-display">${item.quantity}</span>

                            <form action="${pageContext.request.contextPath}/cart/update" method="post" style="display: inline;">
                                <input type="hidden" name="productId" value="${item.product.id}">
                                <input type="hidden" name="quantity" value="${item.quantity + 1}">
                                <button type="submit" class="btn-qty" title="Increase Quantity">+</button>
                            </form>
                        </div>

                        <div class="cart-item-total">
                            ${item.formattedItemTotal}
                        </div>

                        <!-- Remove Button -->
                        <form action="${pageContext.request.contextPath}/cart/remove" method="post" style="display: inline;">
                            <input type="hidden" name="productId" value="${item.product.id}">
                            <button type="submit" class="btn-remove-item" title="Remove Item">✕</button>
                        </form>
                    </div>
                </c:forEach>
            </div>

            <!-- Right: Order Summary Box with 18% GST -->
            <div class="order-summary-box">
                <h3 class="summary-title">Bill Summary (INR)</h3>

                <div class="summary-row">
                    <span>Taxable Value (Subtotal)</span>
                    <span>${sessionScope.cart.formattedSubtotal}</span>
                </div>
                <div class="summary-row bill-row-gst">
                    <span>Central GST (9% CGST)</span>
                    <span>${sessionScope.cart.formattedCGST}</span>
                </div>
                <div class="summary-row bill-row-gst">
                    <span>State GST (9% SGST)</span>
                    <span>${sessionScope.cart.formattedSGST}</span>
                </div>
                <div class="summary-row">
                    <span>Total GST (18%)</span>
                    <span>${sessionScope.cart.formattedTax}</span>
                </div>
                <div class="summary-row">
                    <span>Express Shipping</span>
                    <span>${sessionScope.cart.formattedShipping}</span>
                </div>

                <div class="summary-row total-row">
                    <span>Total Amount Payable</span>
                    <span>${sessionScope.cart.formattedTotal}</span>
                </div>

                <!-- CTA: Smart Analysis Report -->
                <a href="${pageContext.request.contextPath}/cart/analysis" class="btn-smart-analysis">
                    📊 View SmartCart Analysis Report →
                </a>

                <!-- CTA: Checkout -->
                <a href="${pageContext.request.contextPath}/checkout" class="btn-checkout">
                    Proceed to Checkout →
                </a>

                <p style="text-align: center; font-size: 0.8rem; color: var(--text-muted); margin-top: 1rem;">
                    🔒 100% Genuine electronics • Verified dealers • GST invoice included (GSTIN: 29AABCU9603R1ZM)
                </p>
            </div>

        </div>

    </c:if>

</main>

<%@ include file="footer.jsp" %>
