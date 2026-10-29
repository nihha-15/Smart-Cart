package com.smartcart.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;

/**
 * Intelligent Wishlist management for SmartCart.
 * Features:
 * - Real-time item tracking & state management
 * - Target Price Alert aggregation
 * - Cross-item ecosystem synergy detection (SmartCart Synergy Engine)
 * - 1-Click transfer to Shopping Cart
 */
public class Wishlist implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<Integer, WishlistItem> items = new LinkedHashMap<>();

    public Wishlist() {}

    public synchronized boolean addItem(Product product) {
        if (product == null) return false;
        int pid = product.getId();
        if (!items.containsKey(pid)) {
            items.put(pid, new WishlistItem(product));
            return true;
        }
        return false;
    }

    public synchronized boolean removeItem(int productId) {
        return items.remove(productId) != null;
    }

    public synchronized void clear() {
        items.clear();
    }

    public boolean contains(int productId) {
        return items.containsKey(productId);
    }

    public Set<Integer> getItemIds() {
        return items.keySet();
    }

    public String getItemIdsCsv() {
        StringBuilder sb = new StringBuilder();
        for (Integer id : items.keySet()) {
            if (sb.length() > 0) sb.append(",");
            sb.append(id);
        }
        return sb.toString();
    }

    public WishlistItem getItem(int productId) {
        return items.get(productId);
    }

    public Collection<WishlistItem> getItems() {
        return items.values();
    }

    public List<WishlistItem> getItemList() {
        return new ArrayList<>(items.values());
    }

    public int getItemCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getTotalValue() {
        double sum = 0.0;
        for (WishlistItem item : items.values()) {
            if (item.getProduct() != null) {
                sum += item.getProduct().getPrice();
            }
        }
        return sum;
    }

    public String getFormattedTotalValue() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return nf.format(getTotalValue());
    }

    /**
     * SmartCart Ecosystem Synergy Detection for Wishlist items.
     * Evaluates compatibility and pairing synergies among saved products.
     */
    public List<String> getSynergyAlerts() {
        List<String> alerts = new ArrayList<>();
        if (items.size() < 2) return alerts;

        boolean hasIpad = false;
        boolean hasApplePencil = false;
        boolean hasMagicKeyboard = false;
        boolean hasLaptop = false;
        boolean hasHighWattCharger = false;
        boolean hasPhone = false;
        boolean hasPowerBank = false;
        boolean hasMonitor = false;
        boolean hasMonitorArm = false;
        boolean hasMechanicalKeyboard = false;
        boolean hasErgoMouse = false;

        Map<String, Integer> brandCounts = new HashMap<>();

        for (WishlistItem item : items.values()) {
            Product p = item.getProduct();
            if (p == null) continue;

            String brand = p.getBrand();
            if (brand != null && !brand.isEmpty()) {
                brandCounts.put(brand, brandCounts.getOrDefault(brand, 0) + 1);
            }

            String cat = p.getCategory().toLowerCase();
            String name = p.getName().toLowerCase();

            if (cat.contains("tablet") && name.contains("ipad")) hasIpad = true;
            if (name.contains("pencil") || name.contains("stylus")) hasApplePencil = true;
            if (name.contains("magic keyboard") || name.contains("keyboard folio")) hasMagicKeyboard = true;

            if (cat.contains("laptop")) hasLaptop = true;
            if (cat.contains("charger") && (name.contains("65w") || name.contains("100w") || name.contains("gan"))) hasHighWattCharger = true;

            if (cat.contains("smartphone") || cat.contains("phone")) hasPhone = true;
            if (cat.contains("power") || name.contains("power bank") || name.contains("powerbank")) hasPowerBank = true;

            if (cat.contains("monitor") && !name.contains("arm")) hasMonitor = true;
            if (name.contains("monitor arm") || name.contains("stand")) hasMonitorArm = true;

            if (name.contains("mechanical keyboard") || name.contains("k2") || name.contains("k70")) hasMechanicalKeyboard = true;
            if (name.contains("mx master") || name.contains("mouse")) hasErgoMouse = true;
        }

        if (hasIpad && (hasApplePencil || hasMagicKeyboard)) {
            alerts.add("✨ Productivity Setup Match: Your saved iPad and Apple Stylus / Keyboard accessories create an optimal creative workspace.");
        }
        if (hasLaptop && hasHighWattCharger) {
            alerts.add("⚡ High-Speed Power Pairing: Your saved Laptop and GaN Fast Charger support universal Type-C rapid charging.");
        }
        if (hasPhone && hasPowerBank) {
            alerts.add("🔋 Commuter Ready: Your saved Smartphone and high-capacity Power Bank ensure uninterrupted battery on the go.");
        }
        if (hasMonitor && hasMonitorArm) {
            alerts.add("🖥️ Ergonomic Desk Match: Your saved Monitor and Gas Spring Arm mount free up desk space and promote better posture.");
        }
        if (hasMechanicalKeyboard && hasErgoMouse) {
            alerts.add("⌨️ Desk Pro Suite: Your saved mechanical keyboard and precision mouse form an elite coding & typing station.");
        }

        for (Map.Entry<String, Integer> e : brandCounts.entrySet()) {
            if (e.getValue() >= 2 && !alerts.isEmpty()) {
                alerts.add("🔗 " + e.getKey() + " Brand Synergy: You have " + e.getValue() + " " + e.getKey() + " devices saved with unified ecosystem features.");
                break;
            }
        }

        return alerts;
    }
}
