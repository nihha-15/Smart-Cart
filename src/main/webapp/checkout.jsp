<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<main class="main-content">

    <div class="section-header">
        <div>
            <h1 class="section-title">Checkout &amp; Tax Invoice</h1>
            <p class="section-desc">Simulated purchase with official 18% GST (9% CGST + 9% SGST) invoice breakdown.</p>
        </div>
        <a href="${pageContext.request.contextPath}/cart" class="section-link">← Return to Cart</a>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="budget-warning-banner banner-danger" style="margin-bottom: 1.5rem;">
            <span>⚠️</span>
            <span>${errorMessage}</span>
        </div>
    </c:if>

    <div class="checkout-grid">
        
        <!-- Left Column: Delivery & Payment Details Form -->
        <div class="form-card">
            <h2 style="font-size: 1.25rem; font-weight: 800; color: var(--text-primary); margin-bottom: 1.5rem;">
                1. Delivery &amp; Contact Information
            </h2>

            <form action="${pageContext.request.contextPath}/order/place" method="post" id="checkoutForm">
                
                <div class="form-group">
                    <label for="cName" class="form-label">Full Name *</label>
                    <input type="text" id="cName" name="customerName" value="Aditya Sharma" required class="form-control" placeholder="Enter recipient full name">
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <div class="form-group">
                        <label for="cEmail" class="form-label">Email Address *</label>
                        <input type="email" id="cEmail" name="customerEmail" value="aditya.sharma@college.ac.in" required class="form-control" placeholder="aditya@example.com">
                    </div>

                    <div class="form-group">
                        <label for="cPhone" class="form-label">Phone Number *</label>
                        <input type="tel" id="cPhone" name="customerPhone" value="+91 98765 43210" required class="form-control" placeholder="+91 98765 00000">
                    </div>
                </div>

                <div class="form-group">
                    <label for="cAddress" class="form-label">Complete Shipping Address *</label>
                    <textarea id="cAddress" name="shippingAddress" rows="3" required class="form-control" placeholder="Street Address, City, State, PIN Code">Flat 402, Prestige Tech Park, Outer Ring Road, Kadubeesanahalli, Bengaluru, Karnataka 560103</textarea>
                </div>

                <h2 style="font-size: 1.25rem; font-weight: 800; color: var(--text-primary); margin: 2rem 0 1rem;">
                    2. Payment Method
                </h2>

                <div class="payment-modes-grid">
                    <label class="payment-mode-label">
                        <input type="radio" name="paymentMethod" value="UPI (Google Pay / PhonePe)" checked>
                        <span class="payment-mode-text">📱 UPI / QR</span>
                    </label>

                    <label class="payment-mode-label">
                        <input type="radio" name="paymentMethod" value="Debit / Credit Card (RuPay / Visa)">
                        <span class="payment-mode-text">💳 RuPay / Cards</span>
                    </label>

                    <label class="payment-mode-label">
                        <input type="radio" name="paymentMethod" value="NetBanking (All Major Banks)">
                        <span class="payment-mode-text">🏛️ NetBanking</span>
                    </label>

                    <label class="payment-mode-label">
                        <input type="radio" name="paymentMethod" value="Cash on Delivery (Simulated)">
                        <span class="payment-mode-text">💵 Pay on Delivery</span>
                    </label>
                </div>

                <div style="margin-top: 2rem;">
                    <button type="submit" class="btn-checkout" style="font-size: 1.05rem;">
                        🔒 Place Verified Order (${sessionScope.cart.formattedTotal})
                    </button>
                    <p style="text-align: center; font-size: 0.8rem; color: var(--text-muted); margin-top: 0.75rem;">
                        Educational microproject simulation. Orders are recorded in MySQL. No actual card charged.
                    </p>
                </div>

            </form>
        </div>

        <!-- Right Column: Order Summary & Tax Invoice Recap -->
        <div>
            <div class="order-summary-box" style="position: static; margin-bottom: 1.5rem;">
                <div class="gst-invoice-header">
                    <span>GSTIN: <strong>29AABCU9603R1ZM</strong></span>
                    <span>State: <strong>Karnataka (29)</strong></span>
                </div>

                <h3 class="summary-title">Order Recap (${sessionScope.cart.itemCount} items)</h3>

                <div style="max-height: 240px; overflow-y: auto; margin-bottom: 1rem; padding-right: 0.5rem;">
                    <c:forEach items="${sessionScope.cart.itemList}" var="item">
                        <div style="display: flex; justify-content: space-between; margin-bottom: 0.75rem; font-size: 0.9rem;">
                            <span style="color: var(--text-primary); font-weight: 600;">
                                ${item.quantity}x ${item.product.name}
                            </span>
                            <span style="font-weight: 700;">${item.formattedItemTotal}</span>
                        </div>
                    </c:forEach>
                </div>

                <div class="summary-row">
                    <span>Taxable Subtotal</span>
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
                    <span>Total Tax (18% GST)</span>
                    <span>${sessionScope.cart.formattedTax}</span>
                </div>
                <div class="summary-row">
                    <span>Express Delivery</span>
                    <span>${sessionScope.cart.formattedShipping}</span>
                </div>

                <div class="summary-row total-row">
                    <span>Final Amount (INR)</span>
                    <span>${sessionScope.cart.formattedTotal}</span>
                </div>
            </div>

            <!-- Smart Audit Badge Box -->
            <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.25rem;">
                <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 0.75rem;">
                    <span style="font-size: 0.85rem; font-weight: 700; color: var(--text-secondary);">READINESS SCORE</span>
                    <span class="badge-success" style="font-size: 0.8rem; font-weight: 700; padding: 0.2rem 0.5rem; border-radius: var(--radius-full);">
                        Score: ${report.readinessScore}/100
                    </span>
                </div>
                <div style="font-size: 0.85rem; color: var(--text-secondary); line-height: 1.45;">
                    🛡️ <strong>Budget Status:</strong> ${sessionScope.cart.overBudget ? 'Over Target Budget' : 'Within Target Budget'}<br>
                    ⚡ <strong>Estimated 3-Yr Cost:</strong> ${report.formattedThreeYearTCO}<br>
                    🔬 <strong>Specifications:</strong> ${report.criticalIssueCount == 0 ? 'Verified Compatible' : 'Warning Detected'}
                </div>
                <div style="margin-top: 0.75rem;">
                    <a href="${pageContext.request.contextPath}/cart/analysis" style="font-size: 0.82rem; font-weight: 600; color: var(--accent-emerald-dark); text-decoration: none;">
                        Review Full Analysis Report →
                    </a>
                </div>
            </div>

        </div>

    </div>

</main>

<%@ include file="footer.jsp" %>
