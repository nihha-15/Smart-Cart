<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<main class="main-content" style="max-width: 900px; margin: 2rem auto;">

    <div style="text-align: center; margin-bottom: 2.5rem;">
        <span class="brand-badge" style="margin-bottom: 0.5rem; display: inline-block;">Customer Portal</span>
        <h1 style="font-size: 2.2rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.5rem;">
            Sign in to SmartCart
        </h1>
        <p style="color: var(--text-secondary); font-size: 1rem;">
            Access your order history, track price drops on past purchases, and sync your personalized budget limit.
        </p>
    </div>

    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2.5rem; align-items: stretch;">

        <!-- Regular Sign In Form -->
        <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 2.5rem; box-shadow: var(--shadow-sm);">
            <h2 style="font-size: 1.3rem; font-weight: 800; margin-bottom: 1.5rem; color: var(--text-primary);">
                Standard Account Login
            </h2>

            <c:if test="${not empty errorMessage}">
                <div style="background: #fef2f2; border: 1px solid #fecaca; color: #b91c1c; padding: 0.75rem 1rem; border-radius: var(--radius-sm); font-size: 0.88rem; margin-bottom: 1.25rem;">
                    ⚠️ ${errorMessage}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group" style="margin-bottom: 1.25rem;">
                    <label class="form-label" for="loginEmail">Email Address</label>
                    <input type="email" id="loginEmail" name="email" class="form-control" placeholder="name@example.com" required>
                </div>

                <div class="form-group" style="margin-bottom: 1.5rem;">
                    <label class="form-label" for="loginPassword">Password</label>
                    <input type="password" id="loginPassword" name="password" class="form-control" placeholder="••••••••" required>
                </div>

                <button type="submit" class="btn-primary" style="width: 100%; justify-content: center; padding: 0.85rem; font-size: 1rem;">
                    Sign In
                </button>
            </form>

            <div style="text-align: center; margin-top: 1.5rem; padding-top: 1.25rem; border-top: 1px solid var(--border-subtle); font-size: 0.9rem;">
                Don't have an account yet? 
                <a href="${pageContext.request.contextPath}/register" style="color: var(--accent-emerald-dark); font-weight: 700; text-decoration: none;">Create an account</a>
            </div>
        </div>

        <!-- 1-Click Demo Accounts for College & Viva Evaluation -->
        <div style="background: linear-gradient(135deg, #f8fafc 0%, #ecfdf5 100%); border: 1.5px solid #a7f3d0; border-radius: var(--radius-lg); padding: 2.5rem; display: flex; flex-direction: column; justify-content: space-between;">
            <div>
                <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 0.75rem;">
                    <span style="font-weight: 800; font-size: 1.1rem; color: #065f46;">⚡ Quick Evaluation</span>
                    <span style="background: #10b981; color: #ffffff; padding: 0.2rem 0.6rem; border-radius: 999px; font-size: 0.75rem; font-weight: 800;">1-Click</span>
                </div>
                <h3 style="font-size: 1.2rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.5rem;">
                    Demo Examiner Accounts
                </h3>
                <p style="font-size: 0.88rem; color: #047857; margin-bottom: 1.5rem; line-height: 1.5;">
                    Skip typing! Click any test profile below to instantly log in and inspect live past orders with real-time <strong>Price Drop Alerts</strong>.
                </p>

                <!-- Demo Account 1 -->
                <form action="${pageContext.request.contextPath}/login" method="post" style="margin-bottom: 1rem;">
                    <input type="hidden" name="email" value="aditya@smartcart.in">
                    <input type="hidden" name="password" value="smartcart123">
                    <button type="submit" class="btn-secondary" style="width: 100%; text-align: left; padding: 1rem; border-color: #6ee7b7; background: #ffffff; display: flex; justify-content: space-between; align-items: center; cursor: pointer;">
                        <div>
                            <div style="font-weight: 800; color: var(--text-primary); font-size: 0.95rem;">Aditya Sharma (Demo Buyer)</div>
                            <div style="font-size: 0.8rem; color: var(--text-secondary);">aditya@smartcart.in &bull; Budget ₹85,000</div>
                            <div style="font-size: 0.75rem; font-weight: 700; color: #059669; margin-top: 0.25rem;">✨ Contains past orders with ₹8,000 in Price Drops!</div>
                        </div>
                        <span style="font-size: 1.2rem; color: var(--accent-emerald);">➔</span>
                    </button>
                </form>

                <!-- Demo Account 2 -->
                <form action="${pageContext.request.contextPath}/login" method="post">
                    <input type="hidden" name="email" value="alex@college.edu">
                    <input type="hidden" name="password" value="password123">
                    <button type="submit" class="btn-secondary" style="width: 100%; text-align: left; padding: 1rem; border-color: #cbd5e1; background: #ffffff; display: flex; justify-content: space-between; align-items: center; cursor: pointer;">
                        <div>
                            <div style="font-weight: 800; color: var(--text-primary); font-size: 0.95rem;">Alex Morgan (Student Profile)</div>
                            <div style="font-size: 0.8rem; color: var(--text-secondary);">alex@college.edu &bull; Budget ₹50,000</div>
                            <div style="font-size: 0.75rem; font-weight: 700; color: #64748b; margin-top: 0.25rem;">Clean budget profile for fresh shopping tests</div>
                        </div>
                        <span style="font-size: 1.2rem; color: var(--text-muted);">➔</span>
                    </button>
                </form>
            </div>

            <div style="margin-top: 1.5rem; font-size: 0.78rem; color: #065f46; text-align: center;">
                🔒 Both profiles work whether MySQL is running or fallback in-memory store is active.
            </div>
        </div>

    </div>

</main>

<%@ include file="footer.jsp" %>