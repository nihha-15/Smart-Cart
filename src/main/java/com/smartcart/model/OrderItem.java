package com.smartcart.model;

import java.io.Serializable;

/**
 * Represents a saved line item in a finalized order with INR formatting and Price Drop tracking.
 */
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int orderId;
    private int productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double itemTotal;
    private double currentPrice = 0.0; // Current catalog price for price-drop comparison

    public OrderItem() {}

    public OrderItem(int productId, String productName, double unitPrice, int quantity, double itemTotal) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.itemTotal = itemTotal;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getItemTotal() { return itemTotal; }
    public void setItemTotal(double itemTotal) { this.itemTotal = itemTotal; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public boolean hasPriceDrop() {
        return currentPrice > 0.0 && currentPrice < unitPrice;
    }

    public double getPriceDrop() {
        return hasPriceDrop() ? Math.round((unitPrice - currentPrice) * 100.0) / 100.0 : 0.0;
    }

    public String getFormattedPriceDrop() {
        return String.format("₹%,.2f", getPriceDrop());
    }

    public String getFormattedCurrentPrice() {
        return String.format("₹%,.2f", currentPrice);
    }

    public String getFormattedUnitPrice() { return String.format("₹%,.2f", unitPrice); }
    public String getFormattedItemTotal() { return String.format("₹%,.2f", itemTotal); }
}
