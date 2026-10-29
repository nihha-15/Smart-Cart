/**
 * SmartCart — Intelligent E-Commerce Cart System
 * Frontend Interactions & Dynamic Budget Guardian sync
 */

document.addEventListener("DOMContentLoaded", () => {
    initBudgetSync();
    initToasts();
});

/**
 * Real-time Budget Guardian synchronization & interactive feedback
 */
function initBudgetSync() {
    const budgetInput = document.getElementById("budgetInput");
    const budgetProgressBar = document.getElementById("budgetProgressBar");
    const remainingBudgetText = document.getElementById("remainingBudgetText");
    const cartTotalElement = document.getElementById("cartTotalValue");

    if (budgetInput && budgetProgressBar && cartTotalElement) {
        budgetInput.addEventListener("input", (e) => {
            const rawVal = e.target.value.replace(/[^0-9.]/g, "");
            const newBudget = parseFloat(rawVal) || 0;
            const currentTotal = parseFloat(cartTotalElement.dataset.total || "0");

            if (newBudget > 0) {
                const percentage = Math.min(100, Math.round((currentTotal / newBudget) * 100));
                budgetProgressBar.style.width = percentage + "%";

                budgetProgressBar.className = "progress-bar-fill";
                if (currentTotal > newBudget) {
                    budgetProgressBar.classList.add("budget-exceeded");
                } else if (percentage >= 70) {
                    budgetProgressBar.classList.add("budget-warning");
                } else {
                    budgetProgressBar.classList.add("budget-safe");
                }

                if (remainingBudgetText) {
                    const diff = newBudget - currentTotal;
                    if (diff < 0) {
                        remainingBudgetText.textContent = "-₹" + Math.abs(diff).toLocaleString('en-IN', {minimumFractionDigits: 2, maximumFractionDigits: 2}) + " (Over Budget)";
                        remainingBudgetText.style.color = "#ef4444";
                    } else {
                        remainingBudgetText.textContent = "₹" + diff.toLocaleString('en-IN', {minimumFractionDigits: 2, maximumFractionDigits: 2});
                        remainingBudgetText.style.color = "#059669";
                    }
                }
            }
        });
    }
}

/**
 * Automatically fades and cleans up toast notifications
 */
function initToasts() {
    const toasts = document.querySelectorAll(".toast");
    toasts.forEach((toast) => {
        setTimeout(() => {
            toast.style.transition = "opacity 0.4s ease, transform 0.4s ease";
            toast.style.opacity = "0";
            toast.style.transform = "translateY(10px)";
            setTimeout(() => toast.remove(), 400);
        }, 4000);
    });
}

/**
 * Confirmation dialog for dismissing mistake warnings
 */
function confirmDismiss(alertTitle) {
    return confirm("Are you sure you want to dismiss the warning for \"" + alertTitle + "\"? SmartCart will no longer count this against your purchase readiness.");
}
