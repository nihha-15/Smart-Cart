package com.smartcart.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model representing a registered customer in SmartCart.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private double savedBudget = 75000.00;
    private Date createdAt;

    public User() {
        this.createdAt = new Date();
    }

    public User(int id, String name, String email, String password, String phone, String address, double savedBudget) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.savedBudget = savedBudget;
        this.createdAt = new Date();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFirstName() {
        if (name == null || name.trim().isEmpty()) return "Shopper";
        String[] parts = name.trim().split("\\s+");
        return parts[0];
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getSavedBudget() { return savedBudget; }
    public void setSavedBudget(double savedBudget) { this.savedBudget = savedBudget; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getFormattedSavedBudget() {
        return String.format("₹%,.2f", savedBudget);
    }
}
