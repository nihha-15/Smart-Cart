package com.smartcart.model;

import java.io.Serializable;

/**
 * Represents a smart alternative recommendation to save budget or enhance value.
 */
public class AlternativeRecommendation implements Serializable {
    private static final long serialVersionUID = 1L;

    private Product originalProduct;
    private Product alternativeProduct;
    private double savingsAmount;
    private String reason;

    public AlternativeRecommendation() {}

    public AlternativeRecommendation(Product originalProduct, Product alternativeProduct, 
                                     double savingsAmount, String reason) {
        this.originalProduct = originalProduct;
        this.alternativeProduct = alternativeProduct;
        this.savingsAmount = savingsAmount;
        this.reason = reason;
    }

    public Product getOriginalProduct() { return originalProduct; }
    public void setOriginalProduct(Product originalProduct) { this.originalProduct = originalProduct; }

    public Product getAlternativeProduct() { return alternativeProduct; }
    public void setAlternativeProduct(Product alternativeProduct) { this.alternativeProduct = alternativeProduct; }

    public double getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(double savingsAmount) { this.savingsAmount = savingsAmount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getFormattedSavings() {
        return String.format("₹%,.2f", savingsAmount);
    }
}
