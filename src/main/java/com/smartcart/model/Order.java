package com.smartcart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a customer purchase order with Indian GST invoice breakdown.
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Integer userId; // Optional customer account link
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private String paymentMethod;
    private double budgetLimit;
    private double subtotal;
    private double cgst;
    private double sgst;
    private double tax;
    private double shippingCost;
    private double discount;
    private double totalAmount;
    private int readinessScore;
    private String gstin = "29AABCU9603R1ZM"; // Simulated Indian GSTIN
    private String orderStatus = "CONFIRMED";
    private Date createdAt;
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
        this.createdAt = new Date();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getBudgetLimit() { return budgetLimit; }
    public void setBudgetLimit(double budgetLimit) { this.budgetLimit = budgetLimit; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { 
        this.subtotal = subtotal;
        this.cgst = Math.round((subtotal * 0.09) * 100.0) / 100.0;
        this.sgst = Math.round((subtotal * 0.09) * 100.0) / 100.0;
        this.tax = Math.round((cgst + sgst) * 100.0) / 100.0;
    }

    public double getCgst() { return cgst; }
    public void setCgst(double cgst) { this.cgst = cgst; }

    public double getSgst() { return sgst; }
    public void setSgst(double sgst) { this.sgst = sgst; }

    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }

    public double getShippingCost() { return shippingCost; }
    public void setShippingCost(double shippingCost) { this.shippingCost = shippingCost; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public int getReadinessScore() { return readinessScore; }
    public void setReadinessScore(int readinessScore) { this.readinessScore = readinessScore; }

    public String getGstin() { return gstin; }
    public void setGstin(String gstin) { this.gstin = gstin; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public void addItem(OrderItem item) {
        if (this.items == null) this.items = new ArrayList<>();
        this.items.add(item);
    }

    public String getFormattedSubtotal() { return String.format("₹%,.2f", subtotal); }
    public String getFormattedCGST() { return String.format("₹%,.2f", cgst); }
    public String getFormattedSGST() { return String.format("₹%,.2f", sgst); }
    public String getFormattedTax() { return String.format("₹%,.2f", tax); }
    public String getFormattedShipping() { return shippingCost == 0 ? "FREE" : String.format("₹%,.2f", shippingCost); }
    public String getFormattedTotal() { return String.format("₹%,.2f", totalAmount); }

    public String getFormattedCreatedAt() {
        if (createdAt == null) return "Just now";
        return new java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a").format(createdAt);
    }

    public boolean hasAnyPriceDrop() {
        if (items == null) return false;
        for (OrderItem item : items) {
            if (item.hasPriceDrop()) return true;
        }
        return false;
    }

    public double getTotalPriceDropSavings() {
        double total = 0.0;
        if (items != null) {
            for (OrderItem item : items) {
                total += item.getPriceDrop() * item.getQuantity();
            }
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public String getFormattedTotalPriceDropSavings() {
        return String.format("₹%,.2f", getTotalPriceDropSavings());
    }
}
