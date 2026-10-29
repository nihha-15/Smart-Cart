package com.smartcart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a Frequently Bought Together (FBT) bundle.
 */
public class BoughtTogetherBundle implements Serializable {
    private static final long serialVersionUID = 1L;

    private Product primaryProduct;
    private List<Product> companionProducts = new ArrayList<>();
    private double bundleDiscount = 0.0; // Extra discount when bought together

    public BoughtTogetherBundle() {}

    public BoughtTogetherBundle(Product primaryProduct, List<Product> companionProducts, double bundleDiscount) {
        this.primaryProduct = primaryProduct;
        this.companionProducts = companionProducts != null ? companionProducts : new ArrayList<>();
        this.bundleDiscount = bundleDiscount;
    }

    public Product getPrimaryProduct() { return primaryProduct; }
    public void setPrimaryProduct(Product primaryProduct) { this.primaryProduct = primaryProduct; }

    public List<Product> getCompanionProducts() { return companionProducts; }
    public void setCompanionProducts(List<Product> companionProducts) { this.companionProducts = companionProducts; }

    public List<Product> getAllProducts() {
        List<Product> all = new ArrayList<>();
        if (primaryProduct != null) all.add(primaryProduct);
        if (companionProducts != null) all.addAll(companionProducts);
        return all;
    }

    public int getItemCount() {
        return getAllProducts().size();
    }

    public double getTotalRegularPrice() {
        double total = 0.0;
        for (Product p : getAllProducts()) {
            total += p.getPrice();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public double getBundlePrice() {
        return Math.max(0.0, Math.round((getTotalRegularPrice() - bundleDiscount) * 100.0) / 100.0);
    }

    public double getBundleDiscount() { return bundleDiscount; }
    public void setBundleDiscount(double bundleDiscount) { this.bundleDiscount = bundleDiscount; }

    public String getFormattedTotalRegularPrice() {
        return String.format("₹%,.2f", getTotalRegularPrice());
    }

    public String getFormattedBundlePrice() {
        return String.format("₹%,.2f", getBundlePrice());
    }

    public String getFormattedBundleDiscount() {
        return String.format("₹%,.2f", bundleDiscount);
    }

    public String getProductIdsCsv() {
        StringBuilder sb = new StringBuilder();
        for (Product p : getAllProducts()) {
            if (sb.length() > 0) sb.append(",");
            sb.append(p.getId());
        }
        return sb.toString();
    }
}
