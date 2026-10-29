package com.smartcart.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Represents a single item in the user's Smart Wishlist.
 * Includes unique smart features:
 * - Target Price Alert tracking (notifies when price drops to desired threshold)
 * - Historical Lowest Price estimation
 * - Timestamp of when the item was added
 */
public class WishlistItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Product product;
    private Date addedDate;
    private double targetPrice;
    private boolean priceDropAlertEnabled;
    private double estimatedHistoricalLow;

    public WishlistItem() {
        this.addedDate = new Date();
        this.priceDropAlertEnabled = true;
    }

    public WishlistItem(Product product) {
        this.product = product;
        this.addedDate = new Date();
        this.priceDropAlertEnabled = true;
        if (product != null) {
            // Default target price set to ~10% discount threshold as smart recommendation
            this.targetPrice = Math.round(product.getPrice() * 0.90 / 100.0) * 100.0;
            // Historical low estimate (~12% below current price)
            this.estimatedHistoricalLow = Math.round(product.getPrice() * 0.88 / 100.0) * 100.0;
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public String getFormattedAddedDate() {
        if (addedDate == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        return sdf.format(addedDate);
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public String getFormattedTargetPrice() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return nf.format(targetPrice);
    }

    public boolean isPriceDropAlertEnabled() {
        return priceDropAlertEnabled;
    }

    public void setPriceDropAlertEnabled(boolean priceDropAlertEnabled) {
        this.priceDropAlertEnabled = priceDropAlertEnabled;
    }

    public double getEstimatedHistoricalLow() {
        return estimatedHistoricalLow;
    }

    public void setEstimatedHistoricalLow(double estimatedHistoricalLow) {
        this.estimatedHistoricalLow = estimatedHistoricalLow;
    }

    public String getFormattedHistoricalLow() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return nf.format(estimatedHistoricalLow);
    }

    /**
     * Checks if current product price has hit or dropped below target price threshold.
     */
    public boolean isTargetMet() {
        if (product == null || targetPrice <= 0) return false;
        return product.getPrice() <= targetPrice;
    }

    public double getSavingsToTarget() {
        if (product == null) return 0.0;
        return Math.max(0.0, product.getPrice() - targetPrice);
    }

    public String getFormattedSavingsToTarget() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return nf.format(getSavingsToTarget());
    }
}
