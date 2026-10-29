package com.smartcart.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a product in the SmartCart catalogue.
 * Priced in Indian Rupees (₹ / INR) with hardware specs and TCO metadata.
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String category;
    private String description;
    private double price;
    private Double originalPrice;
    private int stock;
    private String imageUrl;
    private double rating;
    private int reviewCount;
    private boolean featured;
    private String badge;
    private int powerWatts = 0; // For Lifetime Cost / Energy Calculation
    private double annualMaintenanceCost = 0.0;
    private double lowestPrice30Days = 0.0;
    private double highestPrice30Days = 0.0;
    private String brand = null;
    private String dealer = null;
    private double dealerRating = 4.8;
    private String deliveryInfo = "FREE Delivery Tomorrow by 11:00 AM";
    private String warrantyInfo = "1 Year Official Brand Warranty • 7-Day Replacement Guarantee";
    private Map<String, String> specs = new HashMap<>();

    public Product() {}

    public Product(int id, String name, String category, String description, double price, 
                   Double originalPrice, int stock, String imageUrl, double rating, 
                   int reviewCount, boolean featured, String badge) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.originalPrice = originalPrice;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.featured = featured;
        this.badge = badge;
        this.lowestPrice30Days = price;
        this.highestPrice30Days = originalPrice != null ? originalPrice : Math.round(price * 1.12 * 100.0) / 100.0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }

    public int getPowerWatts() { return powerWatts; }
    public void setPowerWatts(int powerWatts) { this.powerWatts = powerWatts; }

    public double getAnnualMaintenanceCost() { return annualMaintenanceCost; }
    public void setAnnualMaintenanceCost(double annualMaintenanceCost) { this.annualMaintenanceCost = annualMaintenanceCost; }

    public Map<String, String> getSpecs() { return specs; }
    public void setSpecs(Map<String, String> specs) { this.specs = specs; }

    public void addSpec(String key, String value) {
        if (this.specs == null) {
            this.specs = new HashMap<>();
        }
        this.specs.put(key, value);
    }

    public String getSpec(String key) {
        return specs != null ? specs.get(key) : null;
    }

    // Formatted in Indian Rupees (₹)
    public String getFormattedPrice() {
        return String.format("₹%,.2f", price);
    }

    public String getFormattedOriginalPrice() {
        if (originalPrice != null && originalPrice > price) {
            return String.format("₹%,.2f", originalPrice);
        }
        return "";
    }

    public int getDiscountPercentage() {
        if (originalPrice != null && originalPrice > price) {
            return (int) Math.round(((originalPrice - price) / originalPrice) * 100);
        }
        return 0;
    }

    public boolean isInStock() {
        return stock > 0;
    }

    // Price History & Lowest Price Guarantee
    public double getLowestPrice30Days() {
        return lowestPrice30Days > 0 ? lowestPrice30Days : price;
    }

    public void setLowestPrice30Days(double lowestPrice30Days) {
        this.lowestPrice30Days = lowestPrice30Days;
    }

    public double getHighestPrice30Days() {
        return highestPrice30Days > 0 ? highestPrice30Days : (originalPrice != null ? originalPrice : price * 1.15);
    }

    public void setHighestPrice30Days(double highestPrice30Days) {
        this.highestPrice30Days = highestPrice30Days;
    }

    public boolean isLowestPriceNow() {
        return price <= getLowestPrice30Days() || (originalPrice != null && price < originalPrice);
    }

    public double getPriceDropAmount() {
        double baseline = originalPrice != null ? originalPrice : getHighestPrice30Days();
        return Math.max(0.0, Math.round((baseline - price) * 100.0) / 100.0);
    }

    public String getFormattedLowestPrice30Days() {
        return String.format("₹%,.2f", getLowestPrice30Days());
    }

    public String getFormattedHighestPrice30Days() {
        return String.format("₹%,.2f", getHighestPrice30Days());
    }

    public String getFormattedPriceDropAmount() {
        return String.format("₹%,.2f", getPriceDropAmount());
    }

    public String getFormattedAnnualRunningCost() {
        // (Watts * 6 hrs/day * 365 days / 1000) * ₹8/kWh
        double kwh = (powerWatts * 6.0 * 365.0) / 1000.0;
        double cost = (kwh * 8.0) + annualMaintenanceCost;
        return String.format("₹%,.2f/yr", cost);
    }

    // Brand, Dealer, and Price Range Metdata
    public String getBrand() {
        if (brand != null && !brand.trim().isEmpty()) return brand;
        if (name == null) return "Generic";
        String n = name.toLowerCase();
        if (n.contains("apple") || n.contains("macbook") || n.contains("iphone") || n.contains("ipad") || n.contains("airpods")) return "Apple";
        if (n.contains("samsung") || n.contains("galaxy")) return "Samsung";
        if (n.contains("oneplus")) return "OnePlus";
        if (n.contains("asus") || n.contains("rog") || n.contains("zephyrus")) return "ASUS";
        if (n.contains("lenovo") || n.contains("thinkpad") || n.contains("ideapad") || n.contains("legion")) return "Lenovo";
        if (n.contains("dell") || n.contains("alienware") || n.contains("xps")) return "Dell";
        if (n.contains("hp ") || n.contains("omen") || n.contains("spectre") || n.contains("victus")) return "HP";
        if (n.contains("xiaomi") || n.contains("redmi") || n.contains("mi ")) return "Xiaomi";
        if (n.contains("sony") || n.contains("dualsense") || n.contains("playstation")) return "Sony";
        if (n.contains("bose")) return "Bose";
        if (n.contains("logitech")) return "Logitech";
        if (n.contains("anker")) return "Anker";
        if (n.contains("boat") || n.contains("boAt")) return "boAt";
        if (n.contains("noise")) return "Noise";
        if (n.contains("zebronics")) return "Zebronics";
        if (n.contains("keychron")) return "Keychron";
        if (n.contains("spigen")) return "Spigen";
        if (n.contains("belkin")) return "Belkin";
        if (n.contains("nothing")) return "Nothing";
        if (n.contains("realme")) return "Realme";
        if (n.contains("lg ")) return "LG";
        if (n.contains("philips")) return "Philips";
        if (n.contains("tp-link")) return "TP-Link";
        if (n.contains("corsair")) return "Corsair";
        if (n.contains("razer")) return "Razer";
        return "SmartTech";
    }

    public void setBrand(String brand) { this.brand = brand; }

    public String getDealer() {
        if (dealer != null && !dealer.trim().isEmpty()) return dealer;
        String b = getBrand();
        switch (b) {
            case "Apple": return "Appario Retail Pvt Ltd (Apple Authorized)";
            case "Samsung": return "RetailNet India (Samsung Authorized)";
            case "OnePlus": return "OnePlus Flagship E-Store";
            case "ASUS": return "Asus ROG Hub India";
            case "Dell": return "Dell Authorized Direct Store";
            case "Lenovo": return "Croma Electronics Official";
            case "HP": return "Vijay Sales Digital Direct";
            case "Xiaomi": return "Clicktech Retail (Xiaomi Official)";
            case "Sony": return "Sony Center Official Dealer";
            case "Bose": return "Audiophile Hub Direct";
            case "Logitech": return "Logitech Official Partner";
            case "Anker": case "Belkin": return "Anker Innovations Direct";
            case "boAt": return "boAt Lifestyle Official";
            case "Noise": return "Noise Official E-Store";
            default: return "RetailNet India";
        }
    }

    public void setDealer(String dealer) { this.dealer = dealer; }

    public double getDealerRating() { return dealerRating; }
    public void setDealerRating(double dealerRating) { this.dealerRating = dealerRating; }

    public String getDeliveryInfo() {
        if (deliveryInfo != null) return deliveryInfo;
        return price >= 50000 ? "⚡ FREE Delivery Tomorrow by 11:00 AM" : "FREE Delivery in 2 Days";
    }
    public void setDeliveryInfo(String deliveryInfo) { this.deliveryInfo = deliveryInfo; }

    public String getWarrantyInfo() { return warrantyInfo; }
    public void setWarrantyInfo(String warrantyInfo) { this.warrantyInfo = warrantyInfo; }

    public String getPriceRangeTier() {
        if (price < 2000) return "Budget (Under ₹2,000)";
        if (price < 25000) return "Mid-Range (₹2,000 - ₹25,000)";
        if (price < 70000) return "Premium (₹25,000 - ₹70,000)";
        return "Ultra Flagship (Above ₹70,000)";
    }
}
