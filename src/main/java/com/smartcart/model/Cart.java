package com.smartcart.model;

import java.io.Serializable;
import java.util.*;

/**
 * Manages cart items, Indian GST calculations (18% = 9% CGST + 9% SGST),
 * Budget Guardian state in Indian Rupees (₹), and 3-Year Lifetime Cost of Ownership (TCO).
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<Integer, CartItem> items = new LinkedHashMap<>();
    private double budget = 75000.00; // Sensible default budget in INR (₹75,000)
    private final Set<String> dismissedAlertIds = new HashSet<>();

    public Cart() {}

    public synchronized void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) return;
        int pid = product.getId();
        if (items.containsKey(pid)) {
            CartItem existing = items.get(pid);
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            items.put(pid, new CartItem(product, quantity));
        }
    }

    public synchronized void updateQuantity(int productId, int quantity) {
        if (items.containsKey(productId)) {
            if (quantity <= 0) {
                items.remove(productId);
            } else {
                items.get(productId).setQuantity(quantity);
            }
        }
    }

    public synchronized void removeItem(int productId) {
        items.remove(productId);
    }

    public synchronized void clear() {
        items.clear();
        dismissedAlertIds.clear();
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public List<CartItem> getItemList() {
        return new ArrayList<>(items.values());
    }

    public CartItem getItem(int productId) {
        return items.get(productId);
    }

    public boolean containsProduct(int productId) {
        return items.containsKey(productId);
    }

    public int getItemCount() {
        int count = 0;
        for (CartItem item : items.values()) {
            count += item.getQuantity();
        }
        return count;
    }

    public int getUniqueItemCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Calculations in INR
    public double getSubtotal() {
        double subtotal = 0.0;
        for (CartItem item : items.values()) {
            subtotal += item.getItemTotal();
        }
        return Math.round(subtotal * 100.0) / 100.0;
    }

    // Indian GST Breakdown (18% Total = 9% CGST + 9% SGST)
    public double getCGST() {
        return Math.round((getSubtotal() * 0.09) * 100.0) / 100.0;
    }
    public double getCgst() { return getCGST(); }

    public double getSGST() {
        return Math.round((getSubtotal() * 0.09) * 100.0) / 100.0;
    }
    public double getSgst() { return getSGST(); }

    public double getTax() {
        return Math.round((getCGST() + getSGST()) * 100.0) / 100.0;
    }

    public double getShipping() {
        // Free standard shipping in India for orders over ₹1,000, otherwise ₹99
        if (isEmpty() || getSubtotal() >= 1000.0) {
            return 0.00;
        }
        return 99.00;
    }

    public double getTotal() {
        return Math.round((getSubtotal() + getTax() + getShipping()) * 100.0) / 100.0;
    }

    // Feature E: 3-Year Lifetime Cost of Ownership (TCO)
    public double getThreeYearEnergyCost() {
        if (isEmpty()) return 0.0;
        double cost = 0.0;
        for (CartItem item : items.values()) {
            Product p = item.getProduct();
            int qty = item.getQuantity();
            // (Watts * 6 hrs/day * 365 days / 1000) * ₹8/kWh * 3 years
            double kwhPerYear = (p.getPowerWatts() * 6.0 * 365.0) / 1000.0;
            cost += (kwhPerYear * 3.0 * 8.0) * qty;
        }
        return Math.round(cost * 100.0) / 100.0;
    }

    public double getThreeYearMaintenanceCost() {
        if (isEmpty()) return 0.0;
        double cost = 0.0;
        for (CartItem item : items.values()) {
            Product p = item.getProduct();
            int qty = item.getQuantity();
            cost += (p.getAnnualMaintenanceCost() * 3.0) * qty;
        }
        return Math.round(cost * 100.0) / 100.0;
    }

    public double getEstimatedThreeYearTCO() {
        if (isEmpty()) return 0.0;
        return Math.round((getTotal() + getThreeYearEnergyCost() + getThreeYearMaintenanceCost()) * 100.0) / 100.0;
    }

    // Budget Guardian methods
    public double getBudget() {
        return budget;
    }

    public long getBudgetAsInt() {
        return Math.round(budget);
    }

    public void setBudget(double budget) {
        this.budget = Math.max(0.0, Math.round(budget * 100.0) / 100.0);
    }

    public double getRemainingBudget() {
        return Math.round((budget - getTotal()) * 100.0) / 100.0;
    }

    public double getBudgetUsagePercentage() {
        if (budget <= 0) return 100.0;
        double pct = (getTotal() / budget) * 100.0;
        return Math.round(pct * 10.0) / 10.0;
    }

    public double getClampedBudgetPercentage() {
        return Math.min(100.0, Math.max(0.0, getBudgetUsagePercentage()));
    }

    public boolean isOverBudget() {
        return budget > 0 && getTotal() > budget;
    }

    public boolean isApproachingBudget() {
        if (budget <= 0) return false;
        double pct = getBudgetUsagePercentage();
        return pct >= 70.0 && pct <= 100.0;
    }

    public String getBudgetStatusClass() {
        if (isOverBudget()) return "budget-exceeded";
        if (isApproachingBudget()) return "budget-warning";
        return "budget-safe";
    }

    // Formatted outputs in INR (₹)
    public String getFormattedSubtotal() { return String.format("₹%,.2f", getSubtotal()); }
    public String getFormattedCGST() { return String.format("₹%,.2f", getCGST()); }
    public String getFormattedSGST() { return String.format("₹%,.2f", getSGST()); }
    public String getFormattedTax() { return String.format("₹%,.2f", getTax()); }
    public String getFormattedShipping() { 
        return getShipping() == 0 ? "FREE" : String.format("₹%,.2f", getShipping()); 
    }
    public String getFormattedTotal() { return String.format("₹%,.2f", getTotal()); }
    public String getFormattedBudget() { return String.format("₹%,.2f", budget); }
    public String getFormattedRemainingBudget() { 
        return String.format("₹%,.2f", Math.abs(getRemainingBudget())); 
    }
    public String getFormattedThreeYearTCO() {
        return String.format("₹%,.2f", getEstimatedThreeYearTCO());
    }
    public String getFormattedEstimatedThreeYearTCO() {
        return String.format("₹%,.2f", getEstimatedThreeYearTCO());
    }
    public String getFormattedThreeYearEnergyCost() {
        return String.format("₹%,.2f", getThreeYearEnergyCost());
    }
    public String getFormattedThreeYearMaintenanceCost() {
        return String.format("₹%,.2f", getThreeYearMaintenanceCost());
    }

    // Dismissed alerts tracking
    public Set<String> getDismissedAlertIds() { return dismissedAlertIds; }
    public void dismissAlert(String alertId) {
        if (alertId != null) dismissedAlertIds.add(alertId);
    }
    public boolean isAlertDismissed(String alertId) {
        return dismissedAlertIds.contains(alertId);
    }
}
