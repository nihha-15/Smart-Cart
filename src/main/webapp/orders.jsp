<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<main class="main-content" style="max-width: 1050px; margin: 2rem auto;">

    <!-- Page Header -->
    <div style="display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 2rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
        <div>
            <span class="brand-badge" style="margin-bottom: 0.5rem; display: inline-block;">Purchase History &amp; Price Protection</span>
            <h1 style="font-size: 2.2rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.25rem;">
                My Orders &amp; Price Drop Tracker
            </h1>
            <p style="color: var(--text-secondary); font-size: 0.95rem;">
                Review all past purchases. SmartCart continuously monitors catalog prices to notify you of lower reorder prices.
            </p>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/products" class="btn-primary" style="padding: 0.75rem 1.5rem;">
                Browse More Products →
            </a>
        </div>
    </div>

    <!-- Feature Highlight Banner: Price Drop Savings Detected -->
    <c:if test="${priceDropCount > 0}">
        <div style="background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%); border: 2px solid #10b981; border-radius: var(--radius-lg); padding: 1.75rem; margin-bottom: 2.5rem; box-shadow: 0 4px 15px rgba(16, 185, 129, 0.15); display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 1.25rem;">
            <div style="display: flex; align-items: center; gap: 1.25rem;">
                <div style="font-size: 2.8rem; background: #ffffff; width: 64px; height: 64px; border-radius: 50%; display: flex; align-items: center; justify-content: center; box-shadow: var(--shadow-sm);">
                    📉
                </div>
                <div>
                    <div style="display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.25rem;">
                        <span style="font-size: 0.75rem; font-weight: 800; background: #059669; color: #ffffff; padding: 0.15rem 0.5rem; border-radius: 999px; text-transform: uppercase;">SmartCart Price Watch</span>
                        <strong style="color: #065f46; font-size: 1.15rem;">Lower Prices Detected in Your History!</strong>
                    </div>
                    <p style="color: #047857; font-size: 0.92rem; margin: 0; line-height: 1.4;">
                        We identified <strong>${priceDropCount} item(s)</strong> in your past orders that are now listed at a cheaper rate in our live catalogue.
                    </p>
                </div>
            </div>
            <div style="text-align: right; background: #ffffff; padding: 0.85rem 1.5rem; border-radius: var(--radius-md); border: 1px solid #a7f3d0;">
                <div style="font-size: 0.75rem; color: #64748b; font-weight: 700; text-transform: uppercase;">Total Reorder Savings</div>
                <div style="font-size: 1.6rem; font-weight: 800; color: #059669;">${formattedHistoricalSavings}</div>
            </div>
        </div>
    </c:if>

    <!-- Orders List -->
    <c:if test="${not empty orders}">
        <div style="display: flex; flex-direction: column; gap: 2rem;">
            <c:forEach items="${orders}" var="order">
                <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); overflow: hidden; box-shadow: var(--shadow-sm);">
                    
                    <!-- Order Header -->
                    <div style="background: var(--bg-subtle); padding: 1.25rem 1.75rem; border-bottom: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
                        <div style="display: flex; gap: 2rem; flex-wrap: wrap;">
                            <div>
                                <span style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; display: block;">Order Placed</span>
                                <strong style="font-size: 0.92rem; color: var(--text-primary);">${order.formattedCreatedAt}</strong>
                            </div>
                            <div>
                                <span style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; display: block;">Order Number</span>
                                <strong style="font-size: 0.92rem; color: var(--text-primary); font-family: monospace;">${order.orderNumber}</strong>
                            </div>
                            <div>
                                <span style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; display: block;">Recipient</span>
                                <span style="font-size: 0.92rem; color: var(--text-primary); font-weight: 600;">${order.customerName}</span>
                            </div>
                            <div>
                                <span style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; display: block;">Total Paid (Incl. 18% GST)</span>
                                <strong style="font-size: 0.92rem; color: var(--accent-emerald-dark);">${order.formattedTotal}</strong>
                            </div>
                        </div>

                        <div>
                            <span style="background: #ecfdf5; color: #047857; border: 1px solid #a7f3d0; padding: 0.35rem 0.85rem; border-radius: 999px; font-size: 0.82rem; font-weight: 800;">
                                ✓ ${order.orderStatus}
                            </span>
                        </div>
                    </div>

                    <!-- Items in this Order -->
                    <div style="padding: 1.5rem 1.75rem;">
                        <table style="width: 100%; border-collapse: collapse;">
                            <thead>
                                <tr style="border-bottom: 1.5px solid var(--border-subtle); text-align: left; font-size: 0.82rem; color: var(--text-muted); text-transform: uppercase;">
                                    <th style="padding-bottom: 0.75rem;">Item Description</th>
                                    <th style="padding-bottom: 0.75rem; text-align: center;">Qty</th>
                                    <th style="padding-bottom: 0.75rem; text-align: right;">Price Paid</th>
                                    <th style="padding-bottom: 0.75rem; text-align: right;">Current Live Price</th>
                                    <th style="padding-bottom: 0.75rem; text-align: right;">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${order.items}" var="item">
                                    <tr style="border-bottom: 1px solid var(--border-subtle); font-size: 0.92rem;">
                                        
                                        <!-- Product Info -->
                                        <td style="padding: 1rem 0;">
                                            <a href="${pageContext.request.contextPath}/product?id=${item.productId}" style="font-weight: 700; color: var(--text-primary); text-decoration: none;">
                                                ${item.productName}
                                            </a>
                                            <!-- Price Drop Alert Tag if current price is lower! -->
                                            <c:if test="${item.hasPriceDrop()}">
                                                <div style="margin-top: 0.5rem; display: inline-flex; align-items: center; gap: 0.4rem; background: #ecfdf5; border: 1px solid #6ee7b7; padding: 0.35rem 0.75rem; border-radius: var(--radius-sm); font-size: 0.82rem; color: #065f46; font-weight: 700;">
                                                    <span>🎉 Lower Price Alert:</span>
                                                    <span>Price dropped by <strong>${item.formattedPriceDrop}</strong> since you ordered!</span>
                                                </div>
                                            </c:if>
                                        </td>

                                        <!-- Quantity -->
                                        <td style="padding: 1rem 0; text-align: center; color: var(--text-secondary); font-weight: 600;">
                                            ${item.quantity}
                                        </td>

                                        <!-- Price Paid -->
                                        <td style="padding: 1rem 0; text-align: right; color: var(--text-secondary);">
                                            ${item.formattedUnitPrice}
                                        </td>

                                        <!-- Current Price -->
                                        <td style="padding: 1rem 0; text-align: right;">
                                            <c:if test="${item.hasPriceDrop()}">
                                                <strong style="color: #059669; font-size: 1rem;">${item.formattedCurrentPrice}</strong>
                                                <div style="font-size: 0.75rem; color: #10b981; font-weight: 700;">Cheaper now!</div>
                                            </c:if>
                                            <c:if test="${not item.hasPriceDrop()}">
                                                <span style="color: var(--text-primary); font-weight: 600;">${item.formattedCurrentPrice}</span>
                                            </c:if>
                                        </td>

                                        <!-- Reorder Action -->
                                        <td style="padding: 1rem 0; text-align: right;">
                                            <form action="${pageContext.request.contextPath}/cart/add" method="post" style="display: inline;">
                                                <input type="hidden" name="productId" value="${item.productId}">
                                                <input type="hidden" name="quantity" value="1">
                                                <c:if test="${item.hasPriceDrop()}">
                                                    <button type="submit" class="btn-primary" style="padding: 0.4rem 0.85rem; font-size: 0.82rem; background: #059669; border-color: #059669;">
                                                        Reorder at Lower Price ➔
                                                    </button>
                                                </c:if>
                                                <c:if test="${not item.hasPriceDrop()}">
                                                    <button type="submit" class="btn-secondary" style="padding: 0.4rem 0.85rem; font-size: 0.82rem;">
                                                        Buy Again
                                                    </button>
                                                </c:if>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <!-- Order Financial Breakdown Footer -->
                        <div style="margin-top: 1.25rem; display: flex; justify-content: flex-end; font-size: 0.85rem; color: var(--text-secondary);">
                            <div style="min-width: 250px; line-height: 1.8;">
                                <div style="display: flex; justify-content: space-between;">
                                    <span>Subtotal:</span>
                                    <span>${order.formattedSubtotal}</span>
                                </div>
                                <div style="display: flex; justify-content: space-between;">
                                    <span>CGST (9%):</span>
                                    <span>${order.formattedCgst}</span>
                                </div>
                                <div style="display: flex; justify-content: space-between;">
                                    <span>SGST (9%):</span>
                                    <span>${order.formattedSgst}</span>
                                </div>
                                <div style="display: flex; justify-content: space-between;">
                                    <span>Delivery:</span>
                                    <span>${order.formattedShipping}</span>
                                </div>
                                <div style="display: flex; justify-content: space-between; border-top: 1.5px solid var(--border-color); padding-top: 0.35rem; font-size: 0.95rem; font-weight: 800; color: var(--text-primary);">
                                    <span>Total Amount:</span>
                                    <span>${order.formattedTotal}</span>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${empty orders}">
        <div style="text-align: center; padding: 4rem 1rem; background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md);">
            <div style="font-size: 3rem; margin-bottom: 1rem;">📦</div>
            <h3 style="font-size: 1.3rem; margin-bottom: 0.5rem; color: var(--text-primary);">No Past Orders Yet</h3>
            <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">
                When you make a purchase on SmartCart, your order items and live price-drop tracking will appear here.
            </p>
            <a href="${pageContext.request.contextPath}/products" class="btn-primary">Start Shopping Now</a>
        </div>
    </c:if>

</main>

<%@ include file="footer.jsp" %>