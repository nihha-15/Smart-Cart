<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<main class="main-content" style="max-width: 650px; margin: 2rem auto;">

    <div style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); padding: 2.5rem; box-shadow: var(--shadow-sm);">
        
        <div style="text-align: center; margin-bottom: 2rem;">
            <span class="brand-badge" style="margin-bottom: 0.5rem; display: inline-block;">Join SmartCart</span>
            <h1 style="font-size: 1.8rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.5rem;">
                Create Your Account
            </h1>
            <p style="color: var(--text-secondary); font-size: 0.95rem;">
                Set your monthly shopping budget and let SmartCart safeguard your purchases.
            </p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div style="background: #fef2f2; border: 1px solid #fecaca; color: #b91c1c; padding: 0.75rem 1rem; border-radius: var(--radius-sm); font-size: 0.88rem; margin-bottom: 1.25rem;">
                ⚠️ ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            
            <div class="form-group" style="margin-bottom: 1.25rem;">
                <label class="form-label" for="regName">Full Name</label>
                <input type="text" id="regName" name="name" class="form-control" placeholder="e.g. Ramesh Kumar" required>
            </div>

            <div class="form-group" style="margin-bottom: 1.25rem;">
                <label class="form-label" for="regEmail">Email Address</label>
                <input type="email" id="regEmail" name="email" class="form-control" placeholder="name@example.com" required>
            </div>

            <div class="form-group" style="margin-bottom: 1.25rem;">
                <label class="form-label" for="regPassword">Password</label>
                <input type="password" id="regPassword" name="password" class="form-control" placeholder="Minimum 6 characters" required>
            </div>

            <div class="form-group" style="margin-bottom: 1.25rem;">
                <label class="form-label" for="regPhone">Phone Number</label>
                <input type="tel" id="regPhone" name="phone" class="form-control" placeholder="+91 98765 43210" required>
            </div>

            <div class="form-group" style="margin-bottom: 1.25rem;">
                <label class="form-label" for="regAddress">Shipping Address</label>
                <textarea id="regAddress" name="address" class="form-control" rows="2" placeholder="House/Flat No, Street, Landmark, City, State, PIN" required></textarea>
            </div>

            <div class="form-group" style="margin-bottom: 1.75rem;">
                <label class="form-label" for="regBudget">Default Shopping Budget (INR ₹)</label>
                <input type="number" id="regBudget" name="budget" class="form-control" value="75000" min="1000" step="500" required>
                <small style="color: var(--text-muted); font-size: 0.8rem; display: block; margin-top: 0.25rem;">
                    Budget Guardian will automatically activate using this threshold whenever you shop.
                </small>
            </div>

            <button type="submit" class="btn-primary" style="width: 100%; justify-content: center; padding: 0.85rem; font-size: 1rem;">
                Create Account &amp; Start Shopping
            </button>
        </form>

        <div style="text-align: center; margin-top: 1.5rem; padding-top: 1.25rem; border-top: 1px solid var(--border-subtle); font-size: 0.9rem;">
            Already have an account? 
            <a href="${pageContext.request.contextPath}/login" style="color: var(--accent-emerald-dark); font-weight: 700; text-decoration: none;">Sign In here</a>
        </div>

    </div>

</main>

<%@ include file="footer.jsp" %>