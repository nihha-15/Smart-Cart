package com.smartcart.model;

import java.io.Serializable;

/**
 * Feature F: Represents a companion accessory bundle to complete a device setup
 * with an intelligent bundle discount.
 */
public class SetupBundle implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bundleId;
    private int primaryProductId;
    private String primaryProductName;
    private Product accessoryProduct;
    private double bundleDiscount;
    private String title;
    private String rationale;

    public SetupBundle() {}

    public SetupBundle(String bundleId, int primaryProductId, String primaryProductName, 
                       Product accessoryProduct, double bundleDiscount, String title, String rationale) {
        this.bundleId = bundleId;
        this.primaryProductId = primaryProductId;
        this.primaryProductName = primaryProductName;
        this.accessoryProduct = accessoryProduct;
        this.bundleDiscount = bundleDiscount;
        this.title = title;
        this.rationale = rationale;
    }

    public String getBundleId() { return bundleId; }
    public void setBundleId(String bundleId) { this.bundleId = bundleId; }

    public int getPrimaryProductId() { return primaryProductId; }
    public void setPrimaryProductId(int primaryProductId) { this.primaryProductId = primaryProductId; }

    public String getPrimaryProductName() { return primaryProductName; }
    public void setPrimaryProductName(String primaryProductName) { this.primaryProductName = primaryProductName; }

    public Product getAccessoryProduct() { return accessoryProduct; }
    public void setAccessoryProduct(Product accessoryProduct) { this.accessoryProduct = accessoryProduct; }

    public Product getCompanionProduct() { return accessoryProduct; }

    public Product getPrimaryProduct() {
        Product p = new Product();
        p.setId(primaryProductId);
        p.setName(primaryProductName);
        return p;
    }

    public double getBundleDiscount() { return bundleDiscount; }
    public void setBundleDiscount(double bundleDiscount) { this.bundleDiscount = bundleDiscount; }

    public double getDiscountAmount() { return bundleDiscount; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBundleName() { return title; }

    public String getRationale() { return rationale; }
    public void setRationale(String rationale) { this.rationale = rationale; }

    public String getDescription() { return rationale; }

    public double getOriginalPrice() {
        return accessoryProduct != null ? accessoryProduct.getPrice() : 0.0;
    }

    public double getBundlePrice() {
        return Math.max(0.0, getOriginalPrice() - bundleDiscount);
    }

    public String getFormattedBundleDiscount() {
        return String.format("₹%,.2f", bundleDiscount);
    }

    public String getFormattedDiscountAmount() {
        return String.format("₹%,.2f", bundleDiscount);
    }

    public String getFormattedOriginalPrice() {
        return String.format("₹%,.2f", getOriginalPrice());
    }

    public String getFormattedBundlePrice() {
        return String.format("₹%,.2f", getBundlePrice());
    }
}
