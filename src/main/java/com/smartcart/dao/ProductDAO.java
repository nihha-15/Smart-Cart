package com.smartcart.dao;

import com.smartcart.model.AlternativeRecommendation;
import com.smartcart.model.BoughtTogetherBundle;
import com.smartcart.model.Product;
import com.smartcart.model.SetupBundle;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Products, Specifications, Alternatives, and Setup Bundles.
 * Features 45 real-world branded consumer tech products in Indian Rupees (₹ / INR).
 */
public class ProductDAO {
    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    private static final Map<Integer, Product> IN_MEMORY_PRODUCTS = new LinkedHashMap<>();
    private static final List<AlternativeMapping> IN_MEMORY_ALTERNATIVES = new ArrayList<>();
    private static final List<SetupBundle> IN_MEMORY_BUNDLES = new ArrayList<>();

    static {
        seedInMemoryCatalogue();
    }

    public List<Product> getAllProducts() {
        if (DBUtil.isMySqlAvailable()) {
            List<Product> list = new ArrayList<>();
            String sql = "SELECT * FROM products ORDER BY id ASC";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = mapProduct(rs);
                    loadProductSpecs(conn, p);
                    list.add(p);
                }
                return list;
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to load products from MySQL. Falling back to in-memory store.", e);
            }
        }
        return new ArrayList<>(IN_MEMORY_PRODUCTS.values());
    }

    public Product getProductById(int id) {
        if (DBUtil.isMySqlAvailable()) {
            String sql = "SELECT * FROM products WHERE id = ?";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Product p = mapProduct(rs);
                        loadProductSpecs(conn, p);
                        return p;
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to get product by ID from MySQL.", e);
            }
        }
        return IN_MEMORY_PRODUCTS.get(id);
    }

    public List<Product> getProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty() || "all".equalsIgnoreCase(category)) {
            return getAllProducts();
        }
        if (DBUtil.isMySqlAvailable()) {
            List<Product> list = new ArrayList<>();
            String sql = "SELECT * FROM products WHERE LOWER(category) = LOWER(?) ORDER BY id ASC";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, category.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Product p = mapProduct(rs);
                        loadProductSpecs(conn, p);
                        list.add(p);
                    }
                }
                return list;
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to filter products by category from MySQL.", e);
            }
        }
        List<Product> filtered = new ArrayList<>();
        for (Product p : IN_MEMORY_PRODUCTS.values()) {
            if (p.getCategory().equalsIgnoreCase(category.trim())) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    public List<Product> searchProducts(String query, String category) {
        return searchProducts(query, category, null, null, null);
    }

    public List<Product> searchProducts(String query, String category, String brand, String dealer, String priceRange) {
        List<Product> baseList = getProductsByCategory(category);
        List<Product> results = new ArrayList<>();

        String q = (query != null && !query.trim().isEmpty()) ? query.trim().toLowerCase() : null;
        String b = (brand != null && !brand.trim().isEmpty() && !brand.equalsIgnoreCase("all")) ? brand.trim().toLowerCase() : null;
        String d = (dealer != null && !dealer.trim().isEmpty() && !dealer.equalsIgnoreCase("all")) ? dealer.trim().toLowerCase() : null;
        String pr = (priceRange != null && !priceRange.trim().isEmpty() && !priceRange.equalsIgnoreCase("all")) ? priceRange.trim().toLowerCase() : null;

        for (Product p : baseList) {
            if (q != null) {
                boolean matchQ = p.getName().toLowerCase().contains(q) || 
                                 p.getDescription().toLowerCase().contains(q) || 
                                 p.getCategory().toLowerCase().contains(q) ||
                                 p.getBrand().toLowerCase().contains(q);
                if (!matchQ) continue;
            }
            if (b != null) {
                if (!p.getBrand().toLowerCase().contains(b)) continue;
            }
            if (d != null) {
                if (!p.getDealer().toLowerCase().contains(d)) continue;
            }
            if (pr != null) {
                if ("budget".equals(pr) && p.getPrice() >= 2000) continue;
                if ("mid".equals(pr) && (p.getPrice() < 2000 || p.getPrice() >= 25000)) continue;
                if ("premium".equals(pr) && (p.getPrice() < 25000 || p.getPrice() >= 70000)) continue;
                if ("flagship".equals(pr) && p.getPrice() < 70000) continue;
            }
            results.add(p);
        }
        return results;
    }

    public List<Product> getFeaturedProducts() {
        if (DBUtil.isMySqlAvailable()) {
            List<Product> list = new ArrayList<>();
            String sql = "SELECT * FROM products WHERE featured = TRUE ORDER BY rating DESC LIMIT 12";
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = mapProduct(rs);
                    loadProductSpecs(conn, p);
                    list.add(p);
                }
                return list;
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to load featured products from MySQL.", e);
            }
        }
        // Curated multi-brand lineup across diverse price tiers & verified dealers
        int[] curatedIds = {2, 56, 7, 5, 23, 10, 1, 15, 18, 14, 46, 114};
        List<Product> featured = new ArrayList<>();
        for (int id : curatedIds) {
            Product p = IN_MEMORY_PRODUCTS.get(id);
            if (p != null) {
                featured.add(p);
            }
        }
        if (featured.size() < 12) {
            for (Product p : IN_MEMORY_PRODUCTS.values()) {
                if (p.isFeatured() && !featured.contains(p)) {
                    featured.add(p);
                    if (featured.size() >= 12) break;
                }
            }
        }
        return featured;
    }

    public List<AlternativeRecommendation> getAlternativesForCart(Set<Integer> productIds) {
        List<AlternativeRecommendation> recommendations = new ArrayList<>();
        for (int pid : productIds) {
            AlternativeRecommendation alt = getAlternativeForProduct(pid);
            if (alt != null) {
                recommendations.add(alt);
            }
        }
        return recommendations;
    }

    public AlternativeRecommendation getAlternativeForProduct(int productId) {
        for (AlternativeMapping m : IN_MEMORY_ALTERNATIVES) {
            if (m.originalId == productId) {
                Product orig = getProductById(m.originalId);
                Product alt = getProductById(m.alternativeId);
                if (orig != null && alt != null) {
                    return new AlternativeRecommendation(orig, alt, m.savings, m.reason);
                }
            }
        }
        return null;
    }

    /**
     * Finds intelligent Similar Products and Competitor Alternatives for a given product.
     * Discovers same-category models from competing brands with comparable price points and high ratings.
     */
    public List<Product> getSimilarProducts(Product current, int limit) {
        if (current == null) return Collections.emptyList();
        List<Product> categoryList = getProductsByCategory(current.getCategory());
        List<Product> candidates = new ArrayList<>();

        for (Product p : categoryList) {
            if (p.getId() != current.getId()) {
                candidates.add(p);
            }
        }

        final double curPrice = current.getPrice();
        final String curBrand = current.getBrand() != null ? current.getBrand() : "";

        candidates.sort((a, b) -> {
            boolean aDiffBrand = !curBrand.equalsIgnoreCase(a.getBrand());
            boolean bDiffBrand = !curBrand.equalsIgnoreCase(b.getBrand());
            if (aDiffBrand != bDiffBrand) {
                return aDiffBrand ? -1 : 1;
            }
            double diffA = Math.abs(a.getPrice() - curPrice);
            double diffB = Math.abs(b.getPrice() - curPrice);
            int priceComp = Double.compare(diffA, diffB);
            if (priceComp != 0) return priceComp;
            return Double.compare(b.getRating(), a.getRating());
        });

        if (candidates.size() > limit) {
            return new ArrayList<>(candidates.subList(0, limit));
        }
        return candidates;
    }

    // Feature F: Companion setup bundle recommendations
    public List<SetupBundle> getSetupBundlesForCart(Set<Integer> productIdsInCart) {
        List<SetupBundle> bundles = new ArrayList<>();
        for (SetupBundle b : IN_MEMORY_BUNDLES) {
            if (productIdsInCart.contains(b.getPrimaryProductId()) && 
                !productIdsInCart.contains(b.getAccessoryProduct().getId())) {
                bundles.add(b);
            }
        }
        return bundles;
    }

    // Frequently Bought Together (FBT) Engine
    public BoughtTogetherBundle getBoughtTogetherBundle(int productId) {
        Product primary = getProductById(productId);
        if (primary == null) return null;

        List<Product> companions = new ArrayList<>();
        double discount = 300.0;

        switch (productId) {
            case 1: // Apple MacBook Air 15" M3 -> Anker 65W GaN (15) + Logitech MX Master 3S (33)
                if (getProductById(15) != null) companions.add(getProductById(15));
                if (getProductById(33) != null) companions.add(getProductById(33));
                discount = 800.0;
                break;
            case 2: // ASUS ROG Zephyrus G16 -> Anker 150W GaN (16) + Keychron K2 Keyboard (32)
                if (getProductById(16) != null) companions.add(getProductById(16));
                if (getProductById(32) != null) companions.add(getProductById(32));
                discount = 1200.0;
                break;
            case 5: // Apple iPhone 15 Pro -> Anker 65W GaN (15) + Spigen Ultra Hybrid MagFit Case (27)
                if (getProductById(15) != null) companions.add(getProductById(15));
                if (getProductById(27) != null) companions.add(getProductById(27));
                discount = 500.0;
                break;
            case 6: // Apple iPhone 15 -> Spigen Liquid Crystal Case (29) + Apple AirPods Pro (24)
                if (getProductById(29) != null) companions.add(getProductById(29));
                if (getProductById(24) != null) companions.add(getProductById(24));
                discount = 1000.0;
                break;
            case 9: // Apple iPad Air 11" M2 -> Apple Pencil Pro (11) + Apple Magic Keyboard (12)
                if (getProductById(11) != null) companions.add(getProductById(11));
                if (getProductById(12) != null) companions.add(getProductById(12));
                discount = 2000.0;
                break;
            case 31: // LG UltraGear 4K Monitor -> Belkin 8K HDMI (21) + Amazon Basics Monitor Arm (39)
                if (getProductById(21) != null) companions.add(getProductById(21));
                if (getProductById(39) != null) companions.add(getProductById(39));
                discount = 800.0;
                break;
            default:
                // General smart pairing based on category
                String cat = primary.getCategory().toLowerCase();
                if ("laptops".equals(cat) || "monitors".equals(cat)) {
                    if (getProductById(33) != null) companions.add(getProductById(33));
                    if (getProductById(19) != null) companions.add(getProductById(19));
                    discount = 500.0;
                } else if ("smartphones".equals(cat) || "tablets".equals(cat)) {
                    if (getProductById(15) != null) companions.add(getProductById(15));
                    if (getProductById(19) != null) companions.add(getProductById(19));
                    discount = 400.0;
                } else {
                    if (getProductById(19) != null && productId != 19) companions.add(getProductById(19));
                    if (getProductById(15) != null && productId != 15) companions.add(getProductById(15));
                    discount = 250.0;
                }
                break;
        }

        if (companions.isEmpty()) return null;
        return new BoughtTogetherBundle(primary, companions, discount);
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setCategory(rs.getString("category"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        double origPrice = rs.getDouble("original_price");
        if (!rs.wasNull()) {
            p.setOriginalPrice(origPrice);
        }
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setRating(rs.getDouble("rating"));
        p.setReviewCount(rs.getInt("review_count"));
        p.setFeatured(rs.getBoolean("featured"));
        p.setBadge(rs.getString("badge"));
        return p;
    }

    private void loadProductSpecs(Connection conn, Product product) {
        String sql = "SELECT spec_key, spec_value FROM product_specs WHERE product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    product.addSpec(rs.getString("spec_key"), rs.getString("spec_value"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.FINE, "Failed to load specs for product " + product.getId(), e);
        }
    }

    public static class AlternativeMapping {
        int originalId;
        int alternativeId;
        double savings;
        String reason;

        public AlternativeMapping(int originalId, int alternativeId, double savings, String reason) {
            this.originalId = originalId;
            this.alternativeId = alternativeId;
            this.savings = savings;
            this.reason = reason;
        }
    }

    private static void seedInMemoryCatalogue() {
        // =====================================================================
        // CATEGORY 1: LAPTOPS
        // =====================================================================
        Product p1 = new Product(1, "Apple MacBook Air 15\" M3 (16GB, 512GB SSD)", "laptops", 
                "15.3-inch Liquid Retina Display with True Tone, Apple M3 chip (8-core CPU, 10-core GPU), 16GB Unified Memory, 512GB SSD. MagSafe 3 and Dual Thunderbolt / USB 4 ports. Requires min 65W/70W fast charging.", 
                134900.00, 144900.00, 15, "images/prod-1.png", 4.9, 142, true, "Best Seller");
        p1.setPowerWatts(65);
        p1.setAnnualMaintenanceCost(1500.0);
        p1.addSpec("device_type", "laptop");
        p1.addSpec("screen_size", "15.3");
        p1.addSpec("charging_port", "USB-C");
        p1.addSpec("min_charging_watts", "65");
        p1.addSpec("audio_jack", "true");
        p1.addSpec("includes_charger", "true");
        IN_MEMORY_PRODUCTS.put(1, p1);

        Product p2 = new Product(2, "ASUS ROG Zephyrus G16 (RTX 4080, i9-14900HX)", "laptops", 
                "16-inch 2.5K 240Hz OLED ROG Nebula Display, Intel Core i9-14900HX, NVIDIA GeForce RTX 4080 12GB GDDR6, 32GB LPDDR5X, 1TB PCIe 4.0 SSD. High-draw gaming requiring min 100W PD charging.", 
                169990.00, 189990.00, 8, "images/prod-2.png", 4.9, 88, true, "Extreme Performance");
        p2.setPowerWatts(180);
        p2.setAnnualMaintenanceCost(2500.0);
        p2.addSpec("device_type", "laptop");
        p2.addSpec("screen_size", "16.0");
        p2.addSpec("charging_port", "USB-C");
        p2.addSpec("min_charging_watts", "100");
        p2.addSpec("audio_jack", "true");
        p2.addSpec("includes_charger", "true");
        IN_MEMORY_PRODUCTS.put(2, p2);

        Product p3 = new Product(3, "Apple MacBook Air 13\" M2 (8GB, 256GB SSD)", "laptops", 
                "13.6-inch Liquid Retina Display, Apple M2 chip, 8GB Unified Memory, 256GB SSD, 1080p FaceTime HD camera, fanless silent thermal architecture, all-day 18hr battery.", 
                89900.00, 99900.00, 20, "images/prod-3.png", 4.8, 65, false, "Ultra Portable");
        p3.setPowerWatts(45);
        p3.setAnnualMaintenanceCost(1000.0);
        p3.addSpec("device_type", "laptop");
        p3.addSpec("screen_size", "13.6");
        p3.addSpec("charging_port", "USB-C");
        p3.addSpec("min_charging_watts", "45");
        p3.addSpec("audio_jack", "true");
        p3.addSpec("includes_charger", "true");
        IN_MEMORY_PRODUCTS.put(3, p3);

        Product p4 = new Product(4, "Lenovo IdeaPad Slim 3 (Ryzen 5 7520U, 16GB, 512GB)", "laptops", 
                "15.6-inch FHD Anti-Glare display, AMD Ryzen 5 7520U, 16GB LPDDR5, 512GB NVMe SSD, Dolby Audio, Rapid Charge Boost (2hr runtime in 15min). Student-friendly workhorse.", 
                44990.00, 51990.00, 25, "images/prod-4.png", 4.5, 95, false, "Student Favorite");
        p4.setPowerWatts(45);
        p4.setAnnualMaintenanceCost(600.0);
        p4.addSpec("device_type", "laptop");
        p4.addSpec("screen_size", "15.6");
        p4.addSpec("charging_port", "USB-C");
        p4.addSpec("min_charging_watts", "45");
        p4.addSpec("audio_jack", "true");
        p4.addSpec("includes_charger", "true");
        IN_MEMORY_PRODUCTS.put(4, p4);

        // =====================================================================
        // CATEGORY 2: SMARTPHONES
        // =====================================================================
        Product p5 = new Product(5, "Apple iPhone 15 Pro (128GB, Natural Titanium)", "smartphones", 
                "6.1-inch Super Retina XDR OLED with ProMotion 120Hz, A17 Pro Chip, Grade 5 Aerospace Titanium build, 48MP Pro camera system, USB-C 3.2 Gen 2 (10Gbps). Retail box contains cable only; NO wall adapter or 3.5mm jack.", 
                119900.00, 134900.00, 30, "images/prod-5.png", 4.9, 320, true, "Top Flagship");
        p5.setPowerWatts(20);
        p5.setAnnualMaintenanceCost(1500.0);
        p5.addSpec("device_type", "smartphone");
        p5.addSpec("model_family", "iPhone 15 Pro");
        p5.addSpec("screen_size", "6.1");
        p5.addSpec("charging_port", "USB-C");
        p5.addSpec("min_charging_watts", "20");
        p5.addSpec("audio_jack", "false");
        p5.addSpec("includes_charger", "false");
        IN_MEMORY_PRODUCTS.put(5, p5);

        Product p6 = new Product(6, "Apple iPhone 15 (128GB, Blue)", "smartphones", 
                "6.1-inch Super Retina XDR OLED, Dynamic Island, A16 Bionic chip, 48MP main camera with 2x Telephoto, USB-C. Special SmartCart promotional bundle includes an official 20W charger in-box.", 
                69900.00, 79900.00, 40, "images/prod-6.png", 4.7, 210, false, "Value Flagship");
        p6.setPowerWatts(20);
        p6.setAnnualMaintenanceCost(1000.0);
        p6.addSpec("device_type", "smartphone");
        p6.addSpec("model_family", "iPhone 15");
        p6.addSpec("screen_size", "6.1");
        p6.addSpec("charging_port", "USB-C");
        p6.addSpec("min_charging_watts", "20");
        p6.addSpec("audio_jack", "false");
        p6.addSpec("includes_charger", "true");
        IN_MEMORY_PRODUCTS.put(6, p6);

        Product p7 = new Product(7, "OnePlus 12 5G (16GB RAM, 512GB Storage)", "smartphones", 
                "Snapdragon 8 Gen 3, 6.82-inch 2K 120Hz ProXDR LTPO AMOLED, 4th Gen Hasselblad Camera, 5400mAh battery. Includes official 100W SuperVOOC charger in the retail box.", 
                64999.00, 69999.00, 35, "images/prod-7.png", 4.8, 175, true, "Value King");
        p7.setPowerWatts(100);
        p7.setAnnualMaintenanceCost(700.0);
        p7.addSpec("device_type", "smartphone");
        p7.addSpec("model_family", "OnePlus 12");
        p7.addSpec("screen_size", "6.82");
        p7.addSpec("charging_port", "USB-C");
        p7.addSpec("min_charging_watts", "100");
        p7.addSpec("audio_jack", "false");
        p7.addSpec("includes_charger", "true");
        IN_MEMORY_PRODUCTS.put(7, p7);

        Product p8 = new Product(8, "Xiaomi Redmi Note 13 5G (8GB RAM, 256GB)", "smartphones", 
                "6.67-inch 120Hz AMOLED, MediaTek Dimensity 6080, 108MP 3x in-sensor zoom camera, dedicated 3.5mm headphone jack, 5000mAh battery with 33W fast charger included in-box.", 
                16999.00, 19999.00, 50, "images/prod-8.png", 4.5, 98, false, "Budget Pick");
        p8.setPowerWatts(33);
        p8.setAnnualMaintenanceCost(300.0);
        p8.addSpec("device_type", "smartphone");
        p8.addSpec("model_family", "Redmi Note 13");
        p8.addSpec("screen_size", "6.67");
        p8.addSpec("charging_port", "USB-C");
        p8.addSpec("min_charging_watts", "33");
        p8.addSpec("audio_jack", "true");
        p8.addSpec("includes_charger", "true");
        IN_MEMORY_PRODUCTS.put(8, p8);

        // =====================================================================
        // CATEGORY 3: TABLETS & STYLUS
        // =====================================================================
        Product p9 = new Product(9, "Apple iPad Air 11\" M2 (128GB, WiFi)", "tablets", 
                "11-inch Liquid Retina Display with P3 wide color, Apple M2 chip, landscape 12MP Ultra Wide front camera with Center Stage, USB-C Thunderbolt. Apple Pencil NOT included in retail box.", 
                59900.00, 64900.00, 18, "images/prod-9.png", 4.9, 112, true, "Pro Tablet");
        p9.setPowerWatts(30);
        p9.setAnnualMaintenanceCost(800.0);
        p9.addSpec("device_type", "tablet");
        p9.addSpec("screen_size", "11.0");
        p9.addSpec("charging_port", "USB-C");
        p9.addSpec("min_charging_watts", "30");
        p9.addSpec("includes_stylus", "false");
        IN_MEMORY_PRODUCTS.put(9, p9);

        Product p10 = new Product(10, "Samsung Galaxy Tab S9 FE (128GB, S-Pen Included)", "tablets", 
                "10.9-inch 90Hz display, Exynos 1380, IP68 water & dust resistance, dual speakers tuned by AKG. Official Samsung S-Pen included free inside the retail box.", 
                34999.00, 39999.00, 22, "images/prod-10.png", 4.7, 85, false, "Stylus Included");
        p10.setPowerWatts(25);
        p10.setAnnualMaintenanceCost(600.0);
        p10.addSpec("device_type", "tablet");
        p10.addSpec("screen_size", "10.9");
        p10.addSpec("charging_port", "USB-C");
        p10.addSpec("min_charging_watts", "25");
        p10.addSpec("includes_stylus", "true");
        IN_MEMORY_PRODUCTS.put(10, p10);

        Product p11 = new Product(11, "Apple Pencil Pro (Magnetic Wireless Charging)", "tablets", 
                "Barrel roll gyroscope, squeeze gesture sensor, haptic feedback, pixel-perfect precision, tilt sensitivity, magnetic wireless pairing and charging on iPad Air M2 & iPad Pro.", 
                11900.00, 12900.00, 40, "images/prod-11.png", 4.9, 92, false, "Must-Have Stylus");
        p11.addSpec("accessory_type", "stylus");
        p11.addSpec("target_device", "Apple iPad Air 11\" M2");
        IN_MEMORY_PRODUCTS.put(11, p11);

        Product p12 = new Product(12, "Apple Magic Keyboard for iPad Air 11\"", "tablets", 
                "Floating cantilever magnetic design, full-size backlit scissor keys with 1mm travel, built-in trackpad for iPadOS gestures, and USB-C pass-through charging.", 
                14900.00, 17900.00, 25, "images/prod-12.png", 4.7, 54, false, "Productivity");
        p12.addSpec("accessory_type", "keyboard_case");
        p12.addSpec("target_device", "Apple iPad Air 11\" M2");
        IN_MEMORY_PRODUCTS.put(12, p12);

        // =====================================================================
        // CATEGORY 4: SMARTWATCHES & WEARABLES
        // =====================================================================
        Product p13 = new Product(13, "Apple Watch Ultra 2 (49mm Titanium, GPS + Cellular)", "wearables", 
                "49mm Grade 5 Aerospace Titanium case, 3000-nit sapphire crystal OLED, S9 SiP with Double Tap gesture, precision dual-frequency GPS, 36hr normal / 72hr low-power battery, 100m water resistance.", 
                89900.00, 94900.00, 20, "images/prod-13.png", 4.9, 130, true, "Rugged Pro");
        p13.setPowerWatts(5);
        p13.setAnnualMaintenanceCost(500.0);
        p13.addSpec("device_type", "smartwatch");
        p13.addSpec("charging_type", "magnetic_puck");
        IN_MEMORY_PRODUCTS.put(13, p13);

        Product p14 = new Product(14, "Samsung Galaxy Watch 6 (44mm Bluetooth)", "wearables", 
                "1.5-inch Super AMOLED Sapphire Crystal, Advanced Sleep Coaching, ECG & Blood Pressure monitoring, Body Composition BIA sensor, Wear OS powered by Samsung.", 
                19999.00, 23999.00, 60, "images/prod-14.png", 4.6, 210, false, "Fitness Best");
        p14.setPowerWatts(3);
        p14.setAnnualMaintenanceCost(200.0);
        p14.addSpec("device_type", "smartwatch");
        p14.addSpec("charging_type", "magnetic_puck");
        IN_MEMORY_PRODUCTS.put(14, p14);

        // =====================================================================
        // CATEGORY 5: FAST CHARGERS
        // =====================================================================
        Product p15 = new Product(15, "Anker 735 65W GaN Dual USB-C Fast Charger", "chargers", 
                "Anker GaNPrime technology with PowerIQ 4.0 dynamic power distribution: 2x USB-C + 1x USB-A (65W max). Charges laptops, iPhones, and tablets safely with ActiveShield 2.0 temperature protection.", 
                3499.00, 4499.00, 80, "images/prod-15.png", 4.8, 240, true, "Recommended");
        p15.addSpec("output_watts", "65");
        p15.addSpec("connector_type", "USB-C");
        p15.addSpec("device_compatibility", "laptop,smartphone,tablet");
        IN_MEMORY_PRODUCTS.put(15, p15);

        Product p16 = new Product(16, "Anker 747 150W 4-Port GaN Desktop Charger", "chargers", 
                "GaNPrime 150W desktop charger with 3x USB-C + 1x USB-A. Capable of powering demanding RTX 4080 gaming laptops, MacBooks, and high-performance workstation docks simultaneously.", 
                7999.00, 9999.00, 45, "images/prod-16.png", 4.9, 110, false, "Heavy Duty");
        p16.addSpec("output_watts", "150");
        p16.addSpec("connector_type", "USB-C");
        p16.addSpec("device_compatibility", "laptop,smartphone,tablet");
        IN_MEMORY_PRODUCTS.put(16, p16);

        Product p17 = new Product(17, "Apple 20W USB-C Power Adapter", "chargers", 
                "Official Apple 20W Power Delivery wall adapter for iPhone, iPad, and AirPods. Compact and energy-efficient. Note: NOT sufficient to charge laptops or high-wattage gaming machines.", 
                1900.00, 2100.00, 100, "images/prod-17.png", 4.6, 280, false, "Phone Charger");
        p17.addSpec("output_watts", "20");
        p17.addSpec("connector_type", "USB-C");
        p17.addSpec("device_compatibility", "smartphone,earbuds");
        IN_MEMORY_PRODUCTS.put(17, p17);

        Product p18 = new Product(18, "Mi 50W 20,000mAh Power Bank 3 Pro", "chargers", 
                "20,000mAh lithium-polymer capacity with 50W MAX Type-C two-way fast charging. Triple port output capable of powering MacBook Air, Nintendo Switch, and all smartphones on the move.", 
                3499.00, 4499.00, 50, "images/prod-18.png", 4.8, 145, true, "Travel Essential");
        p18.addSpec("output_watts", "50");
        p18.addSpec("connector_type", "USB-C");
        p18.addSpec("device_compatibility", "laptop,smartphone,tablet");
        IN_MEMORY_PRODUCTS.put(18, p18);

        // =====================================================================
        // CATEGORY 6: CABLES & ADAPTERS
        // =====================================================================
        Product p19 = new Product(19, "Spigen DuraPro 100W Braided USB-C Cable (2m)", "cables", 
                "Heavy-duty double-braided nylon exterior with E-Marker smart chip supporting 100W 5A Power Delivery and 480Mbps data transfer. Tested for 30,000+ bends.", 
                899.00, 1299.00, 150, "images/prod-19.png", 4.8, 310, false, "Best Cable");
        p19.addSpec("cable_from", "USB-C");
        p19.addSpec("cable_to", "USB-C");
        p19.addSpec("max_watts", "100");
        IN_MEMORY_PRODUCTS.put(19, p19);

        Product p20 = new Product(20, "Apple USB-C to Lightning Cable (1m)", "cables", 
                "Original Apple MFi-certified cable connecting your iPhone, iPad, or iPod with Lightning connector to your USB-C enabled Mac or iPad for syncing and fast charging.", 
                1900.00, 2100.00, 75, "images/prod-20.png", 4.7, 120, false, "Legacy Lightning");
        p20.addSpec("cable_from", "USB-C");
        p20.addSpec("cable_to", "Lightning");
        p20.addSpec("max_watts", "27");
        IN_MEMORY_PRODUCTS.put(20, p20);

        Product p21 = new Product(21, "Belkin Ultra HD High Speed 8K HDMI 2.1 Cable (2m)", "cables", 
                "Official Ultra High Speed HDMI certified, 48Gbps bandwidth supporting 8K@60Hz, 4K@120Hz, eARC, Dolby Vision HDR, and VRR for high-end gaming consoles and monitors.", 
                1499.00, 1999.00, 60, "images/prod-21.png", 4.8, 140, false, "4K Gaming");
        p21.addSpec("cable_from", "HDMI");
        p21.addSpec("cable_to", "HDMI");
        IN_MEMORY_PRODUCTS.put(21, p21);

        Product p22 = new Product(22, "Apple USB-C to 3.5mm Headphone Jack Adapter", "cables", 
                "Genuine Apple digital-to-analog converter (DAC) dongle allowing standard 3.5mm audio headphones and AUX cables to connect directly to USB-C iPhones, iPads, and MacBooks.", 
                900.00, 1100.00, 90, "images/prod-22.png", 4.7, 180, true, "DAC Adapter");
        p22.addSpec("cable_from", "USB-C");
        p22.addSpec("cable_to", "3.5mm");
        p22.addSpec("dac_enabled", "true");
        IN_MEMORY_PRODUCTS.put(22, p22);

        // =====================================================================
        // CATEGORY 7: AUDIO
        // =====================================================================
        Product p23 = new Product(23, "Sony WH-1000XM5 Wireless Noise Cancelling Headphones", "audio", 
                "Industry-leading noise cancellation with 8 microphones and Auto NC Optimizer, 30mm carbon-fiber driver unit, LDAC Hi-Res Wireless Audio, 30-hour battery life with 3-minute quick charge for 3 hours playback.", 
                29990.00, 34990.00, 25, "images/prod-23.png", 4.9, 230, true, "Audiophile");
        p23.setPowerWatts(4);
        p23.setAnnualMaintenanceCost(400.0);
        p23.addSpec("audio_type", "wireless_bluetooth");
        p23.addSpec("charging_port", "USB-C");
        IN_MEMORY_PRODUCTS.put(23, p23);

        Product p24 = new Product(24, "Apple AirPods Pro (2nd Gen, USB-C MagSafe Case)", "audio", 
                "Apple H2 headphone chip, up to 2x more Active Noise Cancellation, Adaptive Audio, Transparency mode, Personalized Spatial Audio with dynamic head tracking, dust/sweat/water resistant (IP54).", 
                24900.00, 26900.00, 70, "images/prod-24.png", 4.9, 280, true, "Top Value TWS");
        p24.setPowerWatts(2);
        p24.setAnnualMaintenanceCost(200.0);
        p24.addSpec("audio_type", "wireless_bluetooth");
        p24.addSpec("charging_port", "USB-C");
        IN_MEMORY_PRODUCTS.put(24, p24);

        Product p25 = new Product(25, "OnePlus Bullets Wireless Z2 ANC Bluetooth Neckband", "audio", 
                "45dB Hybrid Active Noise Cancellation, 12.4mm large dynamic drivers with anti-distortion audio technology, 28 hours battery life, 10-minute Warp Charge gives 20 hours playback.", 
                1999.00, 2499.00, 85, "images/prod-25.png", 4.5, 320, false, "Sports Neckband");
        p25.setPowerWatts(2);
        p25.setAnnualMaintenanceCost(150.0);
        p25.addSpec("audio_type", "wireless_bluetooth");
        p25.addSpec("charging_port", "USB-C");
        IN_MEMORY_PRODUCTS.put(25, p25);

        Product p26 = new Product(26, "Sony MDR-XB55AP 3.5mm Extra Bass Wired Earphones", "audio", 
                "12mm neodymium driver units delivering punchy club-like bass, inline remote microphone for hands-free calls, tangle-free serrated flat cable with standard gold-plated 3.5mm audio jack. Requires 3.5mm port or DAC dongle.", 
                1490.00, 1990.00, 45, "images/prod-26.png", 4.4, 85, false, "Zero Latency");
        p26.addSpec("audio_type", "wired_3.5mm");
        p26.addSpec("connector_type", "3.5mm");
        IN_MEMORY_PRODUCTS.put(26, p26);

        // =====================================================================
        // CATEGORY 8: PROTECTIVE CASES
        // =====================================================================
        Product p27 = new Product(27, "Spigen Ultra Hybrid MagFit Case (iPhone 15 Pro, 6.1\")", "cases", 
                "Clear shock-absorbent TPU bumper with durable PC back, Air Cushion Technology on all corners, integrated strong MagSafe magnet ring designed exclusively for the 6.1-inch iPhone 15 Pro.", 
                1699.00, 2199.00, 60, "images/prod-27.png", 4.8, 140, false, "Shockproof");
        p27.addSpec("target_model", "iPhone 15 Pro");
        p27.addSpec("target_screen_size", "6.1");
        IN_MEMORY_PRODUCTS.put(27, p27);

        Product p28 = new Product(28, "Spigen Tough Armor MagFit Case (iPhone 15 Pro Max, 6.7\")", "cases", 
                "Heavy-duty dual-layer kickstand case with extreme impact foam protection and MagSafe compatibility, engineered specifically for the larger 6.7-inch iPhone 15 Pro Max.", 
                1999.00, 2499.00, 40, "images/prod-28.png", 4.7, 95, false, "Oversized 6.7\"");
        p28.addSpec("target_model", "iPhone 15 Pro Max");
        p28.addSpec("target_screen_size", "6.7");
        IN_MEMORY_PRODUCTS.put(28, p28);

        Product p29 = new Product(29, "Spigen Liquid Crystal Case (iPhone 15, 6.1\")", "cases", 
                "Lightweight flexible TPU case with anti-watermark microdot pattern and reinforced raised bezels around camera and screen, precision tailored for the standard 6.1-inch iPhone 15 dual-camera layout.", 
                1199.00, 1499.00, 70, "images/prod-29.png", 4.6, 110, false, "Crystal Clear");
        p29.addSpec("target_model", "iPhone 15");
        p29.addSpec("target_screen_size", "6.1");
        IN_MEMORY_PRODUCTS.put(29, p29);

        Product p30 = new Product(30, "Spigen Glas.tR AlignMaster Tempered Glass for iPhone 15 Pro (2-Pack)", "cases", 
                "9H surface hardness oleophobic tempered glass screen protector with auto-alignment installation frame tray for bubble-free application on iPhone 15 Pro.", 
                999.00, 1399.00, 95, "images/prod-30.png", 4.8, 160, false, "Screen Shield");
        p30.addSpec("target_model", "iPhone 15 Pro");
        p30.addSpec("accessory_type", "screen_protector");
        IN_MEMORY_PRODUCTS.put(30, p30);

        // =====================================================================
        // CATEGORY 9: MONITORS & WORKSTATION ACCESSORIES
        // =====================================================================
        Product p31 = new Product(31, "LG UltraGear 27\" 4K UHD 144Hz Nano IPS Gaming Monitor (27GR93U)", "monitors", 
                "27-inch 4K UHD (3840x2160) Nano IPS display, 144Hz refresh rate, 1ms GtG response time, VESA DisplayHDR 400, AMD FreeSync Premium & NVIDIA G-Sync Compatible, HDMI 2.1 x2, DP 1.4. Certified high-speed HDMI cable included in-box.", 
                39999.00, 49999.00, 15, "images/prod-31.png", 4.8, 88, true, "4K 144Hz");
        p31.setPowerWatts(48);
        p31.setAnnualMaintenanceCost(500.0);
        p31.addSpec("device_type", "monitor");
        p31.addSpec("screen_size", "27.0");
        p31.addSpec("resolution", "4K UHD");
        p31.addSpec("video_port", "HDMI,DisplayPort,USB-C");
        p31.addSpec("includes_hdmi", "true");
        IN_MEMORY_PRODUCTS.put(31, p31);

        Product p32 = new Product(32, "Keychron K2 V2 Wireless Mechanical Keyboard (RGB Hot-Swap)", "monitors", 
                "75% compact 84-key wireless mechanical keyboard, Gateron G Pro Brown switches, hot-swappable PCB, Mac/Windows layout switchable, 4000mAh battery, connects up to 3 devices via Bluetooth 5.1 + Type-C wired mode.", 
                7999.00, 9999.00, 35, "images/prod-32.png", 4.8, 125, false, "Mechanical");
        p32.addSpec("device_type", "keyboard");
        p32.addSpec("connector_type", "USB-C");
        IN_MEMORY_PRODUCTS.put(32, p32);

        Product p33 = new Product(33, "Logitech MX Master 3S Wireless Performance Mouse", "monitors", 
                "Quiet clicks with 90% noise reduction, 8000 DPI Darkfield sensor tracks on glass, MagSpeed electromagnetic scroll wheel (1000 lines/sec), ergonomic sculpted thumb rest, USB-C quick charging.", 
                8995.00, 10995.00, 50, "images/prod-33.png", 4.9, 92, false, "Ergonomic");
        p33.addSpec("device_type", "mouse");
        p33.addSpec("connector_type", "USB-C");
        IN_MEMORY_PRODUCTS.put(33, p33);

        // =====================================================================
        // CATEGORY 10: SMART HOME & IOT
        // =====================================================================
        Product p34 = new Product(34, "Wipro 16A Smart WiFi Plug with Energy Monitoring", "smart-home",
                "Heavy-duty 16A smart plug for geysers, ACs, and microwaves. Real-time energy consumption telemetry, voice control via Alexa & Google Assistant, programmable timer schedules via Wipro Smart App.",
                999.00, 1499.00, 60, "images/prod-34.png", 4.6, 140, true, "Smart Home");
        p34.setPowerWatts(1);
        p34.setAnnualMaintenanceCost(0.0);
        p34.addSpec("connectivity", "WiFi 2.4GHz");
        p34.addSpec("max_current", "16A (3680W)");
        IN_MEMORY_PRODUCTS.put(34, p34);

        Product p35 = new Product(35, "Philips Hue Play Smart RGB Light Bar with Power Supply", "smart-home",
                "Versatile smart light bar delivering 16 million colors and tunable white light. Mounts behind TV/monitor for immersive gaming & entertainment sync. Controllable via Hue Bridge, Bluetooth, or voice.",
                4999.00, 5999.00, 45, "images/prod-35.png", 4.7, 95, false, "RGB Ambience");
        p35.setPowerWatts(18);
        p35.setAnnualMaintenanceCost(100.0);
        p35.addSpec("length", "Compact Bar");
        p35.addSpec("connectivity", "Zigbee + Bluetooth");
        IN_MEMORY_PRODUCTS.put(35, p35);

        Product p36 = new Product(36, "Qubo Smart WiFi Video Doorbell (Hero Electronix)", "smart-home",
                "2K QHD video resolution, AI Person Detection with instant intruder siren alarm, crystal-clear 2-way talk, full-color night vision, IP65 weatherproof, 6-month rechargeable battery life.",
                5490.00, 6990.00, 25, "images/prod-36.png", 4.7, 78, true, "Home Security");
        p36.setPowerWatts(4);
        p36.setAnnualMaintenanceCost(300.0);
        p36.addSpec("resolution", "2K QHD");
        p36.addSpec("battery_life", "180 Days");
        IN_MEMORY_PRODUCTS.put(36, p36);

        // =====================================================================
        // CATEGORY 11: DESK SETUP & STREAMING
        // =====================================================================
        Product p37 = new Product(37, "Logitech Brio 4K Ultra HD Pro Streaming Webcam", "desk-setup",
                "Ultra 4K HD video at 30 fps or 1080p at 60 fps, RightLight 3 with HDR auto-exposure, dual omnidirectional noise-cancelling mics, 65°/78°/90° adjustable field of view, Windows Hello facial recognition.",
                16995.00, 19995.00, 30, "images/prod-37.png", 4.8, 110, true, "Pro Streamer");
        p37.setPowerWatts(5);
        p37.setAnnualMaintenanceCost(0.0);
        p37.addSpec("sensor", "Sony 4K");
        p37.addSpec("connector_type", "USB-C");
        IN_MEMORY_PRODUCTS.put(37, p37);

        Product p38 = new Product(38, "Blue Yeti USB Condenser Microphone for Streaming", "desk-setup",
                "Legendary tri-capsule array with 4 pickup patterns (Cardioid, Omnidirectional, Bidirectional, Stereo). Built-in headphone output with volume control, mic gain control, and instant mute button.",
                9999.00, 12999.00, 40, "images/prod-38.png", 4.8, 130, false, "Hi-Res Audio");
        p38.setPowerWatts(3);
        p38.setAnnualMaintenanceCost(0.0);
        p38.addSpec("polar_pattern", "Multi-Pattern");
        p38.addSpec("sample_rate", "16-bit/48kHz");
        p38.addSpec("connector_type", "USB");
        IN_MEMORY_PRODUCTS.put(38, p38);

        Product p39 = new Product(39, "Amazon Basics Gas Spring Single Monitor Arm Mount", "desk-setup",
                "Premium aircraft-grade aluminum gas-spring arm holding monitors from 13 to 32 inches up to 9kg. Full motion 360° rotation, +75°/-45° tilt, 180° swivel with integrated cable management channels.",
                2499.00, 3499.00, 20, "images/prod-39.png", 4.7, 85, true, "Ergonomic Desk");
        p39.setPowerWatts(0);
        p39.setAnnualMaintenanceCost(0.0);
        p39.addSpec("vesa_compatibility", "75x75, 100x100");
        p39.addSpec("max_screen_size", "32 inches");
        IN_MEMORY_PRODUCTS.put(39, p39);

        Product p40 = new Product(40, "Anker 575 10-in-1 Dual 4K USB-C Docking Station (100W PD)", "desk-setup",
                "Equipped with 2x HDMI 4K@60Hz, 1x DisplayPort, 1x USB-C 10Gbps data, 1x USB-A 10Gbps, 2x USB-A 480Mbps, 100W USB-C Power Delivery passthrough, Ethernet 1Gbps, and SD/TF card slots.",
                8499.00, 10999.00, 35, "images/prod-40.png", 4.8, 92, true, "Workstation Hub");
        p40.setPowerWatts(15);
        p40.setAnnualMaintenanceCost(0.0);
        p40.addSpec("max_pd_pass", "100W");
        p40.addSpec("video_outputs", "2x HDMI, 1x DP");
        p40.addSpec("ethernet", "1000Mbps");
        IN_MEMORY_PRODUCTS.put(40, p40);

        // =====================================================================
        // CATEGORY 12: GAMING GEAR
        // =====================================================================
        Product p41 = new Product(41, "Microsoft Xbox Wireless Controller (Carbon Black)", "gaming",
                "Sculpted surfaces and refined geometry, hybrid D-pad, textured grip on triggers and bumpers, dedicated Share button, 3.5mm audio jack, AA battery power for up to 40 hours of gameplay.",
                5390.00, 5990.00, 50, "images/prod-41.png", 4.8, 160, true, "RGB Controller");
        p41.setPowerWatts(3);
        p41.setAnnualMaintenanceCost(100.0);
        p41.addSpec("battery_life", "40 Hours");
        p41.addSpec("connector_type", "USB-C");
        IN_MEMORY_PRODUCTS.put(41, p41);

        Product p42 = new Product(42, "SteelSeries QcK Heavy XXL Gaming Mouse Pad (900x400mm)", "gaming",
                "Extra thick 4mm non-slip rubber base eliminates unwanted movement, exclusive micro-woven QcK cloth optimized for low and high DPI mouse tracking movements.",
                2499.00, 3199.00, 80, "images/prod-42.png", 4.9, 220, false, "Waterproof");
        p42.setPowerWatts(0);
        p42.setAnnualMaintenanceCost(0.0);
        p42.addSpec("dimensions", "900 x 400 x 4mm");
        IN_MEMORY_PRODUCTS.put(42, p42);

        Product p43 = new Product(43, "Samsung 990 PRO 2TB PCIe 4.0 NVMe SSD with Heatsink", "gaming",
                "Blistering sequential read/write speeds up to 7,450 / 6,900 MB/s, integrated slim heatsink designed to fit PlayStation 5 and gaming PCs for sustained peak thermal control.",
                16999.00, 19999.00, 40, "images/prod-43.png", 4.9, 145, true, "Ultra Fast");
        p43.setPowerWatts(6);
        p43.setAnnualMaintenanceCost(0.0);
        p43.addSpec("interface", "PCIe 4.0 x4 NVMe 2.0");
        p43.addSpec("read_speed", "7450 MB/s");
        p43.addSpec("ps5_compatible", "true");
        IN_MEMORY_PRODUCTS.put(43, p43);

        // =====================================================================
        // CATEGORY 13: POWER & CHARGING PROTECTION
        // =====================================================================
        Product p44 = new Product(44, "Belkin 6-Socket Surge Protector with 30W USB-C PD Fast Port", "power",
                "Heavy-duty 6 universal AC sockets with 650 Joules surge protection, 30W USB-C Power Delivery fast charging port, 1x USB-A port, 2m heavy-duty cord, and connected equipment warranty.",
                2299.00, 2999.00, 55, "images/prod-44.png", 4.7, 185, true, "Surge Protection");
        p44.setPowerWatts(2);
        p44.setAnnualMaintenanceCost(0.0);
        p44.addSpec("outlets", "6 AC Sockets + 2 USB");
        p44.addSpec("surge_joules", "650 Joules");
        IN_MEMORY_PRODUCTS.put(44, p44);

        Product p45 = new Product(45, "ESR 3-in-1 MagSafe Foldable Travel Wireless Charging Stand", "power",
                "Officially certified Apple 15W Made for MagSafe fast wireless charging for iPhone 15/14, 5W Apple Watch fast charger, and 5W AirPods magnetic charging pad in an ultra-compact folding aluminum travel form.",
                4499.00, 5999.00, 45, "images/prod-45.png", 4.8, 120, true, "MagSafe 15W");
        p45.setPowerWatts(15);
        p45.setAnnualMaintenanceCost(0.0);
        p45.addSpec("magsafe_output", "15W Fast Charge");
        p45.addSpec("watch_output", "5W");
        p45.addSpec("earbuds_output", "5W");
        IN_MEMORY_PRODUCTS.put(45, p45);

        // Seed 130 additional branded products across all 13 categories (10 new per section, IDs 46 to 175)
        ExtendedCatalogueSeeder.seed(IN_MEMORY_PRODUCTS, IN_MEMORY_ALTERNATIVES, IN_MEMORY_BUNDLES);

        // =====================================================================
        // BUDGET ALTERNATIVES (Budget Guardian Data in INR)
        // =====================================================================
        IN_MEMORY_ALTERNATIVES.add(new AlternativeMapping(2, 3, 80090.00, 
                "Switch from ASUS ROG Zephyrus G16 (₹1,69,990) to Apple MacBook Air 13\" M2 (₹89,900). You save ₹80,090 while gaining 18-hour silent battery life and exceptional build quality."));
        IN_MEMORY_ALTERNATIVES.add(new AlternativeMapping(5, 6, 50000.00, 
                "Switch from Apple iPhone 15 Pro (₹1,19,900) to Apple iPhone 15 (₹69,900). You save ₹50,000 and receive an in-box 20W charger with the same Dynamic Island experience."));
        IN_MEMORY_ALTERNATIVES.add(new AlternativeMapping(9, 10, 24901.00, 
                "Switch from Apple iPad Air 11\" M2 (₹59,900) to Samsung Galaxy Tab S9 FE (₹34,999). You save ₹24,901 and get the official S-Pen included free in-box."));
        IN_MEMORY_ALTERNATIVES.add(new AlternativeMapping(23, 24, 5090.00, 
                "Switch from Sony WH-1000XM5 (₹29,990) to Apple AirPods Pro (₹24,900). You save ₹5,090 with pocketable active noise cancellation and spatial audio."));
        IN_MEMORY_ALTERNATIVES.add(new AlternativeMapping(16, 15, 4500.00, 
                "Switch from Anker 747 150W Charger (₹7,999) to Anker 735 65W GaN Charger (₹3,499). You save ₹4,500 while still charging laptops and phones rapidly."));

        // =====================================================================
        // FEATURE F: SETUP BUNDLES ("Complete Your Setup")
        // =====================================================================
        IN_MEMORY_BUNDLES.add(new SetupBundle("BUNDLE_LAPTOP_MOUSE", 1, "Apple MacBook Air 15\" M3", p33, 500.00,
                "Mobile Productivity Bundle", "Pair your Apple MacBook Air 15\" M3 with the Logitech MX Master 3S Wireless Performance Mouse (Save ₹500)."));

        IN_MEMORY_BUNDLES.add(new SetupBundle("BUNDLE_GAMING_PERIPHERALS", 2, "ASUS ROG Zephyrus G16", p32, 800.00,
                "Pro Esports Bundle", "Level up your ASUS ROG Zephyrus G16 with the Keychron K2 V2 Wireless Mechanical Keyboard (Save ₹800)."));

        IN_MEMORY_BUNDLES.add(new SetupBundle("BUNDLE_PHONE_CHARGER", 5, "Apple iPhone 15 Pro", p15, 400.00,
                "Power Setup Bundle", "Apple iPhone 15 Pro does not include a wall adapter in-box. Add the Anker 735 65W GaN Fast Charger and save ₹400!"));
        
        IN_MEMORY_BUNDLES.add(new SetupBundle("BUNDLE_TABLET_PEN", 9, "Apple iPad Air 11\" M2", p11, 600.00,
                "Creator Sketch Bundle", "Unlock digital sketching and note-taking on your Apple iPad Air 11\" M2 with the Apple Pencil Pro (Save ₹600)."));

        IN_MEMORY_BUNDLES.add(new SetupBundle("BUNDLE_DESKTOP_PERIPHERALS", 31, "LG UltraGear 27\" 4K UHD 144Hz Gaming Monitor", p32, 600.00,
                "Complete Workstation Bundle", "Pair your new LG UltraGear 4K Gaming Monitor with the Keychron K2 V2 Mechanical Keyboard (Save ₹600)."));
    }
}
