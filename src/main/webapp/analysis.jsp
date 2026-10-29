<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<main class="main-content">

    <div class="section-header">
        <div>
            <h1 class="section-title">📊 Smart Cart Analysis &amp; Savings Report</h1>
            <p class="section-desc">Comprehensive pre-checkout decision check for hardware compatibility, shopping advice, and money-saving alternatives.</p>
        </div>
        <a href="${pageContext.request.contextPath}/cart" class="section-link">← Return to Cart</a>
    </div>

    <!-- Executive Readiness Hero Card -->
    <div class="report-hero">
        <div class="readiness-dial-card">
            <div class="score-circle ${report.scoreColorClass}">
                <span class="score-num">${report.readinessScore}</span>
                <span class="score-max">/ 100</span>
            </div>
            <div class="readiness-info">
                <h2>${report.statusTitle}</h2>
                <p>${report.executiveSummary}</p>
            </div>
        </div>

        <div style="display: flex; gap: 1rem; align-items: center; flex-wrap: wrap;">
            <a href="${pageContext.request.contextPath}/checkout" class="btn-primary" style="padding: 0.85rem 2rem;">
                Proceed to Checkout →
            </a>
        </div>
    </div>

    <!-- Diagnostic Metrics Grid -->
    <div class="report-stats-grid">
        <div class="report-stat-card">
            <div class="stat-label">Cart Total vs Budget</div>
            <div class="stat-value" style="${report.budgetExceeded ? 'color: var(--danger-red);' : ''}">
                ${sessionScope.cart.formattedTotal}
            </div>
            <div class="stat-sub">Planned Budget: ${sessionScope.cart.formattedBudget} (${report.budgetUsagePercentage}% used)</div>
        </div>

        <div class="report-stat-card">
            <div class="stat-label">Estimated 3-Yr Cost</div>
            <div class="stat-value" style="color: #0284c7;">
                ${report.formattedThreeYearTCO}
            </div>
            <div class="stat-sub">Includes 3-yr electricity &amp; battery upkeep</div>
        </div>

        <div class="report-stat-card">
            <div class="stat-label">Setup Completion Score</div>
            <div class="stat-value" style="${report.setupCompletionScore >= 80 ? 'color: var(--accent-emerald-dark);' : 'color: var(--warning-amber);'}">
                ${report.setupCompletionScore}%
            </div>
            <div class="stat-sub">Companion compatibility and bundle coverage</div>
        </div>

        <div class="report-stat-card">
            <div class="stat-label">Critical Compatibilities</div>
            <div class="stat-value" style="${report.criticalIssueCount > 0 ? 'color: var(--danger-red);' : 'color: var(--accent-emerald-dark);'}">
                ${report.criticalIssueCount} Issues
            </div>
            <div class="stat-sub">${report.criticalIssueCount == 0 ? 'All hardware specs verified' : 'Action recommended'}</div>
        </div>

        <div class="report-stat-card">
            <div class="stat-label">Cart Mistakes Detected</div>
            <div class="stat-value" style="${report.mistakeCount > 0 ? 'color: var(--warning-amber);' : 'color: var(--accent-emerald-dark);'}">
                ${report.mistakeCount} Warnings
            </div>
            <div class="stat-sub">Duplicates, bulk quantities, redundancies</div>
        </div>

        <div class="report-stat-card">
            <div class="stat-label">Potential Alternative Savings</div>
            <div class="stat-value" style="color: var(--accent-emerald-dark);">
                ${report.formattedTotalPotentialSavings}
            </div>
            <div class="stat-sub">${report.alternativeCount} alternative options found</div>
        </div>
    </div>

    <!-- SECTION 1: Hardware Compatibility Checker (Feature C) -->
    <section style="margin-bottom: 3rem;">
        <h3 style="font-size: 1.3rem; font-weight: 800; color: var(--text-primary); margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem;">
            <span>🔌 Specification &amp; Compatibility Analysis</span>
        </h3>

        <c:if test="${empty report.compatibilityIssues}">
            <div style="background: var(--accent-emerald-light); border: 1px solid rgba(16, 185, 129, 0.4); border-radius: var(--radius-md); padding: 1.5rem; display: flex; align-items: center; gap: 1rem;">
                <span style="font-size: 1.75rem;">✓</span>
                <div>
                    <h4 style="font-size: 1.05rem; font-weight: 700; color: #065f46;">Full Hardware Interoperability Verified</h4>
                    <p style="color: #065f46; font-size: 0.9rem; margin-top: 0.2rem;">All power requirements, connector types (USB-C), audio jack ports, and device dimensions are fully compatible.</p>
                </div>
            </div>
        </c:if>

        <c:forEach items="${report.compatibilityIssues}" var="issue">
            <div class="alert-card ${issue.severity == 'CRITICAL' ? 'alert-critical' : 'alert-warning'}">
                <div class="alert-icon">${issue.severity == 'CRITICAL' ? '⛔' : '⚠️'}</div>
                <div class="alert-content">
                    <div class="alert-header">
                        <h4 class="alert-title">${issue.title}</h4>
                        <span class="alert-severity-badge ${issue.severity == 'CRITICAL' ? 'badge-critical' : 'badge-warning'}">
                            ${issue.severity}
                        </span>
                    </div>
                    <p class="alert-msg">${issue.reason}</p>
                    <div class="alert-actions">
                        <span style="font-size: 0.88rem; font-weight: 600; color: var(--text-primary);">Resolution: ${issue.suggestedAction}</span>
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
    </section>

    <!-- SECTION 2: Cart Mistake Detector (Feature B) -->
    <section style="margin-bottom: 3rem;">
        <h3 style="font-size: 1.3rem; font-weight: 800; color: var(--text-primary); margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem;">
            <span>⚠️ Cart Mistake &amp; Redundancy Audit</span>
        </h3>

        <c:if test="${empty report.mistakeAlerts}">
            <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.5rem; display: flex; align-items: center; gap: 1rem;">
                <span style="font-size: 1.75rem;">✓</span>
                <div>
                    <h4 style="font-size: 1.05rem; font-weight: 700; color: var(--text-primary);">No Accidental Mistakes Detected</h4>
                    <p style="color: var(--text-secondary); font-size: 0.9rem; margin-top: 0.2rem;">No duplicate accessories, unusual quantities, or redundant in-box items were flagged.</p>
                </div>
            </div>
        </c:if>

        <c:forEach items="${report.mistakeAlerts}" var="alert">
            <div class="alert-card alert-warning">
                <div class="alert-icon">⚠️</div>
                <div class="alert-content">
                    <div class="alert-header">
                        <h4 class="alert-title">${alert.title}</h4>
                        <span class="alert-severity-badge badge-warning">Review Flag</span>
                    </div>
                    <p class="alert-msg">${alert.message}</p>
                    <div class="alert-explanation">
                        <strong>Why did this trigger?</strong> ${alert.explanation}
                    </div>
                    <div class="alert-actions" style="margin-top: 0.75rem;">
                        <form action="${pageContext.request.contextPath}/cart/dismissAlert" method="post">
                            <input type="hidden" name="alertId" value="${alert.id}">
                            <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/cart/analysis">
                            <button type="submit" class="btn-dismiss-alert">✓ Dismiss Warning</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>
    </section>

    <!-- SECTION 3: Cost-Saving Alternatives (Feature A & D in INR) -->
    <section style="margin-bottom: 3.5rem;">
        <h3 style="font-size: 1.3rem; font-weight: 800; color: var(--text-primary); margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem;">
            <span>💡 Budget Guardian Value Recommendations (INR)</span>
            <c:if test="${report.totalPotentialSavings > 0}">
                <span class="alt-savings-badge">Save Up to ${report.formattedTotalPotentialSavings}</span>
            </c:if>
        </h3>

        <c:if test="${empty report.alternativeRecommendations}">
            <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.5rem;">
                <p style="color: var(--text-secondary); font-size: 0.95rem;">You have selected the optimal value products for each category in your cart. No lower-cost alternatives are currently needed.</p>
            </div>
        </c:if>

        <c:if test="${not empty report.alternativeRecommendations}">
            <div class="alternatives-container">
                <p style="color: var(--text-secondary); font-size: 0.92rem; margin-bottom: 0.5rem;">
                    The Budget Guardian identified products in your cart where an alternative model offers equivalent functionality at a lower price point:
                </p>

                <c:forEach items="${report.alternativeRecommendations}" var="rec">
                    <div class="alt-card">
                        <div class="alt-info" style="flex: 1;">
                            <div style="display: flex; align-items: center; gap: 0.6rem; margin-bottom: 0.35rem;">
                                <h4>${rec.originalProduct.name} (${rec.originalProduct.formattedPrice}) → ${rec.alternativeProduct.name} (${rec.alternativeProduct.formattedPrice})</h4>
                                <span class="alt-savings-badge">Save ${rec.formattedSavings}</span>
                            </div>
                            <p>${rec.reason}</p>
                        </div>

                        <!-- One-Click Swap Form -->
                        <form action="${pageContext.request.contextPath}/cart/replace" method="post" style="display: flex; align-items: center;">
                            <input type="hidden" name="originalId" value="${rec.originalProduct.id}">
                            <input type="hidden" name="alternativeId" value="${rec.alternativeProduct.id}">
                            <input type="hidden" name="redirect" value="${pageContext.request.contextPath}/cart/analysis">
                            <button type="submit" class="btn-swap-alt">⚡ Apply One-Click Swap</button>
                        </form>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </section>

    <!-- SECTION 4: Setup Completion & Recommended Bundles (Feature F) -->
    <c:if test="${not empty report.recommendedBundles}">
        <section style="margin-bottom: 3.5rem;">
            <h3 style="font-size: 1.3rem; font-weight: 800; color: var(--text-primary); margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem;">
                <span>🎁 Complete Your Setup Bundles</span>
                <span class="bundle-savings-badge">Setup Score: ${report.setupCompletionScore}%</span>
            </h3>

            <div class="setup-bundles-container" style="margin: 0;">
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
        </section>
    </c:if>

    <!-- Final Checkout CTA Banner -->
    <div style="background: var(--bg-subtle); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 2rem; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1.5rem;">
        <div>
            <h3 style="font-size: 1.25rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.35rem;">Ready to complete your order?</h3>
            <p style="color: var(--text-secondary); font-size: 0.92rem;">Audit complete. GST and subtotals calculated server-side.</p>
        </div>
        <div style="display: flex; gap: 1rem;">
            <a href="${pageContext.request.contextPath}/cart" class="btn-secondary" style="color: var(--text-primary); border-color: var(--border-color);">Review Cart</a>
            <a href="${pageContext.request.contextPath}/checkout" class="btn-primary" style="padding: 0.85rem 2rem;">Proceed to Checkout (${sessionScope.cart.formattedTotal}) →</a>
        </div>
    </div>

</main>

<%@ include file="footer.jsp" %>
