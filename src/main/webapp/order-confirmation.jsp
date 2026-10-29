<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<main class="main-content">

    <div class="confirmation-container">
        
        <!-- Animated Success Circle -->
        <div class="conf-check-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="20 6 9 17 4 12"></polyline>
            </svg>
        </div>

        <h1 class="conf-title">Tax Invoice &amp; Order Confirmed!</h1>
        <p style="color: var(--text-secondary); font-size: 1.05rem; margin-bottom: 1.25rem;">
            Thank you for shopping with <strong>SmartCart</strong>. Your order has been verified and registered in the database.
        </p>

        <div class="conf-order-no">
            Tax Invoice / Order Ref: <strong>${order.orderNumber}</strong>
        </div>

        <div class="gst-invoice-header" style="max-width: 600px; margin: 1rem auto 1.5rem;">
            <span>GSTIN: <strong>${order.gstin}</strong></span>
            <span>State: <strong>Karnataka (29)</strong></span>
            <span>Status: <strong style="color: var(--accent-emerald-dark);">${order.orderStatus}</strong></span>
        </div>

        <!-- Customer & Delivery Summary Card -->
        <div style="background: var(--bg-subtle); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.5rem; text-align: left; margin-bottom: 2rem; display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem;">
            <div>
                <span style="font-size: 0.78rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Delivery Recipient</span>
                <div style="font-weight: 700; font-size: 1rem; color: var(--text-primary); margin: 0.2rem 0;">${order.customerName}</div>
                <div style="font-size: 0.88rem; color: var(--text-secondary);">${order.customerEmail}</div>
                <div style="font-size: 0.88rem; color: var(--text-secondary);">${order.customerPhone}</div>
            </div>

            <div>
                <span style="font-size: 0.78rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">Shipping &amp; Payment</span>
                <div style="font-size: 0.88rem; color: var(--text-secondary); margin-top: 0.2rem;">${order.shippingAddress}</div>
                <div style="font-size: 0.88rem; font-weight: 600; color: var(--accent-emerald-dark); margin-top: 0.4rem;">
                    Mode: ${order.paymentMethod}
                </div>
            </div>
        </div>

        <!-- Itemized Receipt Table -->
        <h3 style="font-size: 1.15rem; font-weight: 800; text-align: left; margin-bottom: 0.75rem; color: var(--text-primary);">
            Tax Invoice Itemized Breakdown
        </h3>

        <table class="receipt-details-table">
            <thead>
                <tr>
                    <th>Product</th>
                    <th style="text-align: center;">Qty</th>
                    <th style="text-align: right;">Unit Price (INR)</th>
                    <th style="text-align: right;">Total (INR)</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${order.items}" var="item">
                    <tr>
                        <td style="font-weight: 600; color: var(--text-primary);">${item.productName}</td>
                        <td style="text-align: center;">${item.quantity}</td>
                        <td style="text-align: right;">${item.formattedUnitPrice}</td>
                        <td style="text-align: right; font-weight: 700;">${item.formattedItemTotal}</td>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="3" style="text-align: right; color: var(--text-secondary); padding-top: 1rem;">Taxable Subtotal:</td>
                    <td style="text-align: right; font-weight: 600; padding-top: 1rem;">${order.formattedSubtotal}</td>
                </tr>
                <tr>
                    <td colspan="3" style="text-align: right; color: var(--text-secondary);">Central GST (9% CGST):</td>
                    <td style="text-align: right; font-weight: 600;">${order.formattedCGST}</td>
                </tr>
                <tr>
                    <td colspan="3" style="text-align: right; color: var(--text-secondary);">State GST (9% SGST):</td>
                    <td style="text-align: right; font-weight: 600;">${order.formattedSGST}</td>
                </tr>
                <tr>
                    <td colspan="3" style="text-align: right; color: var(--text-secondary);">Express Delivery:</td>
                    <td style="text-align: right; font-weight: 600;">${order.formattedShipping}</td>
                </tr>
                <tr>
                    <td colspan="3" style="text-align: right; font-size: 1.15rem; font-weight: 800; color: var(--text-primary); border-top: 2px solid var(--border-color); padding-top: 0.75rem;">Total Paid:</td>
                    <td style="text-align: right; font-size: 1.25rem; font-weight: 800; color: var(--accent-emerald-dark); border-top: 2px solid var(--border-color); padding-top: 0.75rem;">${order.formattedTotal}</td>
                </tr>
            </tfoot>
        </table>

        <!-- Purchase Readiness Audit Badge -->
        <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 0.75rem 1.25rem; display: flex; align-items: center; justify-content: space-between; margin-bottom: 2rem;">
            <span style="font-size: 0.85rem; color: var(--text-secondary);">SmartCart Decision Engine Verification:</span>
            <span style="background: var(--accent-emerald-light); color: var(--accent-emerald-dark); font-weight: 700; padding: 0.2rem 0.6rem; border-radius: var(--radius-full); font-size: 0.85rem;">
                Verified (Readiness: ${order.readinessScore}/100)
            </span>
        </div>

        <div style="display: flex; gap: 1rem; justify-content: center;">
            <a href="${pageContext.request.contextPath}/products" class="btn-primary" style="padding: 0.85rem 2rem;">
                Continue Shopping →
            </a>
            <button onclick="window.print();" class="btn-secondary" style="color: var(--text-primary); border-color: var(--border-color);">
                🖨️ Print Tax Invoice
            </button>
        </div>

    </div>

</main>

<%@ include file="footer.jsp" %>
