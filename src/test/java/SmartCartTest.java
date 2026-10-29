import com.smartcart.dao.OrderDAO;
import com.smartcart.dao.ProductDAO;
import com.smartcart.dao.UserDAO;
import com.smartcart.model.*;
import com.smartcart.service.SmartAnalysisService;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Automated Verification Suite for SmartCart (INR / ₹ Edition).
 * Tests all 6 features (A-F), financial calculations with 18% GST, and DAO operations with real-world products.
 */
public class SmartCartTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("     RUNNING SMARTCART AUTOMATED TEST SUITE (REAL-TIME PRODUCTS)  ");
        System.out.println("==================================================================");

        testProductDAOLoading();
        testFeatureABudgetGuardian();
        testFeatureBMistakeDetector();
        testFeatureCCompatibilityChecker();
        testFeatureDRiskAndValueReport();
        testFeatureELifetimeTCO();
        testFeatureFSetupBundles();
        testUserAuthenticationAndRegistration();
        testFrequentlyBoughtTogether();
        testOrderHistoryAndPriceDropAlerts();
        testOrderPersistenceAndIntegrity();

        System.out.println("\n==================================================================");
        System.out.println("TEST RESULTS: Passed: " + testsPassed + " | Failed: " + testsFailed);
        System.out.println("==================================================================");

        if (testsFailed > 0) {
            System.exit(1);
        }
    }

    private static void assertEquals(String testName, Object expected, Object actual) {
        if (expected == null && actual == null) {
            System.out.println("  [PASS] " + testName);
            testsPassed++;
        } else if (expected != null && expected.equals(actual)) {
            System.out.println("  [PASS] " + testName + " => " + actual);
            testsPassed++;
        } else {
            System.err.println("  [FAIL] " + testName + " => Expected: " + expected + ", Actual: " + actual);
            testsFailed++;
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            testsPassed++;
        } else {
            System.err.println("  [FAIL] " + testName + " => Expected true but was false");
            testsFailed++;
        }
    }

    private static void testProductDAOLoading() {
        System.out.println("\n--- Testing Product DAO Catalogue (Real-World INR Products) ---");
        ProductDAO dao = new ProductDAO();
        List<Product> all = dao.getAllProducts();
        assertTrue("Product catalogue loaded", all != null && !all.isEmpty());
        assertTrue("Contains 175 seeded products across 13 categories", all.size() == 175);

        Product p1 = dao.getProductById(1);
        assertEquals("Product 1 is Apple MacBook Air 15\" M3", "Apple MacBook Air 15\" M3 (16GB, 512GB SSD)", p1.getName());
        assertEquals("Product 1 category is laptops", "laptops", p1.getCategory());
        assertEquals("Product 1 price is ₹1,34,900", 134900.00, p1.getPrice());
        assertEquals("Product 1 charging port spec", "USB-C", p1.getSpec("charging_port"));
        assertEquals("Product 1 min wattage spec", "65", p1.getSpec("min_charging_watts"));

        Product p9 = dao.getProductById(9);
        assertEquals("Product 9 is Apple iPad Air 11\" M2", "Apple iPad Air 11\" M2 (128GB, WiFi)", p9.getName());
        assertEquals("Product 9 category is tablets", "tablets", p9.getCategory());

        Product p13 = dao.getProductById(13);
        assertEquals("Product 13 is Apple Watch Ultra 2", "Apple Watch Ultra 2 (49mm Titanium, GPS + Cellular)", p13.getName());
        assertEquals("Product 13 category is wearables", "wearables", p13.getCategory());

        Product p31 = dao.getProductById(31);
        assertEquals("Product 31 is LG UltraGear 27\" 4K UHD", "LG UltraGear 27\" 4K UHD 144Hz Nano IPS Gaming Monitor (27GR93U)", p31.getName());
        assertEquals("Product 31 category is monitors", "monitors", p31.getCategory());

        // --- Verifying 10 New Products Added from Different Brands to Each Section ---
        Product p46 = dao.getProductById(46);
        assertEquals("Product 46 is Dell XPS 15", "Dell XPS 15 (Core i7-13700H, 16GB, 512GB, RTX 4050)", p46.getName());
        assertEquals("Product 46 category is laptops", "laptops", p46.getCategory());

        Product p56 = dao.getProductById(56);
        assertEquals("Product 56 is Samsung Galaxy S24 Ultra", "Samsung Galaxy S24 Ultra (12GB, 256GB, Titanium Gray, S-Pen)", p56.getName());
        assertEquals("Product 56 category is smartphones", "smartphones", p56.getCategory());

        Product p66 = dao.getProductById(66);
        assertEquals("Product 66 is Apple iPad Pro 13\" M4", "Apple iPad Pro 13\" M4 (Ultra Retina XDR OLED, 256GB)", p66.getName());
        assertEquals("Product 66 category is tablets", "tablets", p66.getCategory());

        Product p76 = dao.getProductById(76);
        assertEquals("Product 76 is Garmin Fenix 7 Pro Solar", "Garmin Fenix 7 Pro Solar (Multi-band GPS, Sapphire Titanium)", p76.getName());
        assertEquals("Product 76 category is wearables", "wearables", p76.getCategory());

        Product p86 = dao.getProductById(86);
        assertEquals("Product 86 is UGREEN Nexode 100W", "UGREEN Nexode 100W 4-Port GaN Fast Desktop Charger", p86.getName());
        assertEquals("Product 86 category is chargers", "chargers", p86.getCategory());

        Product p96 = dao.getProductById(96);
        assertEquals("Product 96 is Anker PowerLine III Flow", "Anker PowerLine III Flow Silicone Type-C 100W Cable (1.8m)", p96.getName());
        assertEquals("Product 96 category is cables", "cables", p96.getCategory());

        Product p106 = dao.getProductById(106);
        assertEquals("Product 106 is Bose QuietComfort Ultra", "Bose QuietComfort Ultra Wireless Noise Cancelling Headphones", p106.getName());
        assertEquals("Product 106 category is audio", "audio", p106.getCategory());

        Product p116 = dao.getProductById(116);
        assertEquals("Product 116 is Ringke Fusion-X S24 Ultra Case", "Ringke Fusion-X Rugged Bumper Case for Galaxy S24 Ultra", p116.getName());
        assertEquals("Product 116 category is cases", "cases", p116.getCategory());

        Product p126 = dao.getProductById(126);
        assertEquals("Product 126 is Samsung Odyssey Neo G8 32\"", "Samsung Odyssey Neo G8 32\" 4K 240Hz Curved Gaming Monitor", p126.getName());
        assertEquals("Product 126 category is monitors", "monitors", p126.getCategory());

        Product p136 = dao.getProductById(136);
        assertEquals("Product 136 is TP-Link Tapo C210 2K Cam", "TP-Link Tapo C210 2K 3MP Pan/Tilt Smart Security Wi-Fi Camera", p136.getName());
        assertEquals("Product 136 category is smart-home", "smart-home", p136.getCategory());

        Product p146 = dao.getProductById(146);
        assertEquals("Product 146 is Elgato Stream Deck MK.2", "Elgato Stream Deck MK.2 Studio Controller (15 LCD Keys)", p146.getName());
        assertEquals("Product 146 category is desk-setup", "desk-setup", p146.getCategory());

        Product p156 = dao.getProductById(156);
        assertEquals("Product 156 is Sony DualSense Edge", "Sony DualSense Edge Wireless Controller for PS5 & PC", p156.getName());
        assertEquals("Product 156 category is gaming", "gaming", p156.getCategory());

        Product p166 = dao.getProductById(166);
        assertEquals("Product 166 is APC Back-UPS BX600C-IN", "APC Back-UPS BX600C-IN 600VA / 360W 230V Desktop UPS", p166.getName());
        assertEquals("Product 166 category is power", "power", p166.getCategory());

        // --- Verifying Section Depth: Every category has at least 12 distinct branded products ---
        String[] categories = {"laptops", "smartphones", "tablets", "wearables", "chargers", "cables",
                               "audio", "cases", "monitors", "smart-home", "desk-setup", "gaming", "power"};
        for (String cat : categories) {
            List<Product> catProducts = dao.getProductsByCategory(cat);
            assertTrue("Section '" + cat + "' has at least 12 products (found " + catProducts.size() + ")", catProducts.size() >= 12);
        }

        // --- Verifying Image Integrity: All 175 products have non-blank PNG images on disk ---
        int validImageCount = 0;
        for (Product p : all) {
            String imgUrl = p.getImageUrl();
            assertTrue("Product " + p.getId() + " has image URL", imgUrl != null && !imgUrl.isEmpty());
            java.io.File imgFile = new java.io.File("src/main/webapp", imgUrl);
            if (imgFile.exists() && imgFile.length() > 1000) {
                validImageCount++;
            }
        }
        assertEquals("All 175 products have valid high-resolution studio images on disk", 175, validImageCount);
    }

    private static void testFeatureABudgetGuardian() {
        System.out.println("\n--- Testing Feature A: Budget Guardian & 18% GST (INR) ---");
        ProductDAO dao = new ProductDAO();
        Cart cart = new Cart();
        cart.setBudget(75000.00); // Default ₹75,000

        Product phone = dao.getProductById(6); // Apple iPhone 15: ₹69,900.00
        cart.addItem(phone, 1);

        assertEquals("Subtotal matches product price", 69900.00, cart.getSubtotal());
        // 9% CGST = 6291.00, 9% SGST = 6291.00, Total GST (18%) = 12582.00
        assertEquals("CGST is 9% of subtotal", 6291.00, cart.getCgst());
        assertEquals("SGST is 9% of subtotal", 6291.00, cart.getSgst());
        assertEquals("Total Tax is 18% GST", 12582.00, cart.getTax());
        assertEquals("Free delivery on subtotal >= ₹10,000", 0.00, cart.getShipping());
        assertEquals("Total is subtotal + tax", 82482.00, cart.getTotal());

        // Total 82,482.00 > Budget 75,000
        assertTrue("Over budget flag triggered when total exceeds budget", cart.isOverBudget());
        assertTrue("Formatted total has ₹ symbol", cart.getFormattedTotal().startsWith("₹"));
        assertTrue("Formatted remaining budget has proper format", cart.getFormattedRemainingBudget() != null);

        // Increase budget to ₹1,00,000
        cart.setBudget(100000.00);
        assertTrue("No longer over budget with ₹1,00,000 limit", !cart.isOverBudget());
        assertTrue("Approaching budget flag active (82.5% used)", cart.isApproachingBudget());
    }

    private static void testFeatureBMistakeDetector() {
        System.out.println("\n--- Testing Feature B: Cart Mistake Detector ---");
        ProductDAO dao = new ProductDAO();
        SmartAnalysisService service = new SmartAnalysisService(dao);

        Cart cart = new Cart();

        // 1. High quantity test (add 3 MacBook laptops)
        Product laptop = dao.getProductById(1);
        cart.addItem(laptop, 3);
        List<MistakeAlert> alerts1 = service.detectMistakes(cart);
        assertTrue("Detects unusually high laptop quantity (>=3)", 
                alerts1.stream().anyMatch(a -> a.getType() == MistakeAlert.AlertType.HIGH_QUANTITY));

        // 2. Unnecessary charger test (Apple iPhone 15 includes 20W charger in-box + 20W charger added)
        cart.clear();
        Product phoneWithCharger = dao.getProductById(6); // Apple iPhone 15 includes charger
        Product extraCharger = dao.getProductById(17);    // Apple 20W USB-C Charger
        cart.addItem(phoneWithCharger, 1);
        cart.addItem(extraCharger, 1);
        List<MistakeAlert> alerts2 = service.detectMistakes(cart);
        assertTrue("Detects unnecessary charger purchase for phone with in-box charger",
                alerts2.stream().anyMatch(a -> a.getType() == MistakeAlert.AlertType.UNNECESSARY_PURCHASE));

        // 3. Unnecessary stylus test (Samsung Galaxy Tab S9 FE includes S-Pen + Apple Pencil Pro added)
        cart.clear();
        Product tabWithStylus = dao.getProductById(10); // Samsung Galaxy Tab S9 FE
        Product extraStylus = dao.getProductById(11);   // Apple Pencil Pro
        cart.addItem(tabWithStylus, 1);
        cart.addItem(extraStylus, 1);
        List<MistakeAlert> alerts3 = service.detectMistakes(cart);
        assertTrue("Detects unnecessary stylus purchase for tablet with bundled S-Pen",
                alerts3.stream().anyMatch(a -> a.getId().equals("MISTAKE_UNNECESSARY_STYLUS")));

        // 4. User dismissal test
        MistakeAlert unnecAlert = alerts3.stream().findFirst().orElse(null);
        assertTrue("Alert is present before dismissal", unnecAlert != null);
        cart.dismissAlert(unnecAlert.getId());
        List<MistakeAlert> alertsAfterDismissal = service.detectMistakes(cart);
        assertTrue("Alert is suppressed after user dismisses it",
                alertsAfterDismissal.stream().noneMatch(a -> a.getId().equals(unnecAlert.getId())));
    }

    private static void testFeatureCCompatibilityChecker() {
        System.out.println("\n--- Testing Feature C: Compatibility Checker ---");
        ProductDAO dao = new ProductDAO();
        SmartAnalysisService service = new SmartAnalysisService(dao);

        Cart cart = new Cart();

        // 1. Charger Wattage Test: Apple MacBook Air 15" M3 (min 65W) + 20W Charger
        Product laptop = dao.getProductById(1);      // requires 65W
        Product weakCharger = dao.getProductById(17); // outputs 20W
        cart.addItem(laptop, 1);
        cart.addItem(weakCharger, 1);

        List<CompatibilityIssue> issues1 = service.checkCompatibility(cart);
        assertTrue("Detects insufficient charger wattage conflict",
                issues1.stream().anyMatch(i -> "RULE_CHARGER_WATTAGE".equals(i.getRuleCode()) && "CRITICAL".equals(i.getSeverity())));

        // 2. Audio Jack Test: Apple iPhone 15 Pro (no 3.5mm) + 3.5mm Wired Earphones
        cart.clear();
        Product phoneNoJack = dao.getProductById(5); // Apple iPhone 15 Pro (no jack)
        Product wiredEarphones = dao.getProductById(26); // Sony 3.5mm wired earphones
        cart.addItem(phoneNoJack, 1);
        cart.addItem(wiredEarphones, 1);

        List<CompatibilityIssue> issues2 = service.checkCompatibility(cart);
        assertTrue("Detects missing 3.5mm audio jack conflict",
                issues2.stream().anyMatch(i -> "RULE_AUDIO_JACK_MISSING".equals(i.getRuleCode())));

        // 3. Case Dimension Mismatch: Apple iPhone 15 Pro (6.1") + Pro Max Case (6.7")
        cart.clear();
        Product wrongCase = dao.getProductById(28); // 6.7" Pro Max case
        cart.addItem(phoneNoJack, 1);
        cart.addItem(wrongCase, 1);

        List<CompatibilityIssue> issues3 = service.checkCompatibility(cart);
        assertTrue("Detects phone case dimension/model mismatch",
                issues3.stream().anyMatch(i -> "RULE_CASE_DIMENSION_MISMATCH".equals(i.getRuleCode()) && "CRITICAL".equals(i.getSeverity())));

        // 4. Missing In-Box Charger for Phone
        cart.clear();
        cart.addItem(phoneNoJack, 1);
        List<CompatibilityIssue> issues4 = service.checkCompatibility(cart);
        assertTrue("Alerts that Apple iPhone 15 Pro does not include a wall charger",
                issues4.stream().anyMatch(i -> "RULE_SMARTPHONE_NO_CHARGER".equals(i.getRuleCode())));
    }

    private static void testFeatureDRiskAndValueReport() {
        System.out.println("\n--- Testing Feature D: Cart Risk & Value Report ---");
        ProductDAO dao = new ProductDAO();
        SmartAnalysisService service = new SmartAnalysisService(dao);

        Cart cart = new Cart();
        cart.setBudget(100000.00); // ₹1,00,000

        // Add ASUS ROG Zephyrus G16 (₹1,69,990) -> triggers over-budget and gives ₹80,090 alternative
        Product gamingLaptop = dao.getProductById(2);
        cart.addItem(gamingLaptop, 1);

        AnalysisReport report = service.generateReport(cart);
        assertTrue("Report identifies budget exceeded", report.isBudgetExceeded());
        assertTrue("Readiness score deducted for budget overrun", report.getReadinessScore() < 100);
        assertTrue("Alternative recommendation provided for gaming laptop", 
                report.getAlternativeRecommendations().size() > 0);
        assertEquals("Potential savings is ₹80,090.00", 80090.00, report.getTotalPotentialSavings());
    }

    private static void testFeatureELifetimeTCO() {
        System.out.println("\n--- Testing Feature E: 3-Year Lifetime TCO Estimator ---");
        ProductDAO dao = new ProductDAO();
        Cart cart = new Cart();

        Product laptop = dao.getProductById(1); // 65W, maintenance ₹1500/yr
        cart.addItem(laptop, 1);

        double energyCost = cart.getThreeYearEnergyCost();
        double maintCost = cart.getThreeYearMaintenanceCost();
        double tco = cart.getEstimatedThreeYearTCO();

        assertTrue("3-Year energy cost is positive", energyCost > 0);
        assertTrue("3-Year maintenance cost is positive (₹4,500 for laptop)", maintCost == 4500.00);
        assertTrue("3-Year TCO is greater than initial purchase price", tco > cart.getTotal());
        assertTrue("Formatted TCO has ₹ symbol", cart.getFormattedEstimatedThreeYearTCO().startsWith("₹"));
    }

    private static void testFeatureFSetupBundles() {
        System.out.println("\n--- Testing Feature F: Smart Setup Bundle Recommender ---");
        ProductDAO dao = new ProductDAO();
        Set<Integer> productIds = new HashSet<>();
        productIds.add(1); // Apple MacBook Air 15" M3

        List<SetupBundle> bundles = dao.getSetupBundlesForCart(productIds);
        assertTrue("Generates companion setup bundles for MacBook Air", bundles != null && !bundles.isEmpty());
        assertTrue("Bundle offers discount on companion product", bundles.get(0).getDiscountAmount() > 0);
        assertTrue("Bundle includes formatted pricing in INR", bundles.get(0).getFormattedBundlePrice().startsWith("₹"));
    }

    private static void testUserAuthenticationAndRegistration() {
        System.out.println("\n--- Testing Customer Authentication & Registration ---");
        UserDAO userDAO = new UserDAO();

        // 1. Authenticate pre-seeded demo user Aditya
        User aditya = userDAO.authenticate("aditya@smartcart.in", "smartcart123");
        assertTrue("Aditya demo authentication successful", aditya != null);
        assertEquals("Aditya name matches", "Aditya Sharma", aditya.getName());
        assertEquals("Aditya saved budget is ₹85,000", 85000.00, aditya.getSavedBudget());

        // 2. Authenticate pre-seeded demo user Alex
        User alex = userDAO.authenticate("alex@college.edu", "password123");
        assertTrue("Alex demo authentication successful", alex != null);
        assertEquals("Alex name matches", "Alex Morgan", alex.getName());

        // 3. Reject invalid credentials
        User invalid = userDAO.authenticate("aditya@smartcart.in", "wrongpassword");
        assertTrue("Invalid password rejected", invalid == null);

        // 4. Register a new customer
        User newUser = new User(0, "Pooja Verma", "pooja.test@smartcart.in", "pass456", "+91 91234 56789", "Indiranagar, Bengaluru", 60000.00);
        boolean registered = userDAO.register(newUser);
        assertTrue("New user registered successfully", registered);

        User authenticatedNewUser = userDAO.authenticate("pooja.test@smartcart.in", "pass456");
        assertTrue("Newly registered user can log in immediately", authenticatedNewUser != null);
        assertEquals("New user name matches", "Pooja Verma", authenticatedNewUser.getName());
    }

    private static void testFrequentlyBoughtTogether() {
        System.out.println("\n--- Testing Frequently Bought Together (FBT) Bundles ---");
        ProductDAO dao = new ProductDAO();

        // Primary product 1 (Apple MacBook Air 15" M3)
        BoughtTogetherBundle bundle = dao.getBoughtTogetherBundle(1);
        assertTrue("FBT bundle generated for Apple MacBook Air 15\" M3", bundle != null);
        assertEquals("Primary product is Apple MacBook Air 15\" M3", 1, bundle.getPrimaryProduct().getId());
        assertTrue("Contains companion products", bundle.getCompanionProducts().size() > 0);
        assertTrue("Item count includes primary and companions", bundle.getItemCount() >= 2);
        assertTrue("Bundle price has discount applied", bundle.getBundlePrice() < bundle.getTotalRegularPrice());
        assertTrue("Product IDs CSV contains primary product id", bundle.getProductIdsCsv().contains("1"));
    }

    private static void testOrderHistoryAndPriceDropAlerts() {
        System.out.println("\n--- Testing Order History & Live Price Drop Alerts ---");
        OrderDAO orderDAO = new OrderDAO();

        // Retrieve past orders for Aditya (User ID 1)
        List<Order> adityaOrders = orderDAO.getOrdersByUserId(1);
        assertTrue("Aditya has past orders in system", adityaOrders != null && !adityaOrders.isEmpty());

        Order pastOrder = adityaOrders.get(0);
        assertTrue("Past order has price drops detected", pastOrder.hasAnyPriceDrop());
        assertTrue("Savings from price drop is positive (> ₹0)", pastOrder.getTotalPriceDropSavings() > 0);

        // Verify specific item price drop: MacBook Air 15" M3 was bought at ₹1,44,900, now ₹1,34,900 -> ₹10,000 drop!
        OrderItem laptopItem = adityaOrders.stream()
                .flatMap(o -> o.getItems().stream())
                .filter(i -> i.getProductId() == 1)
                .findFirst().orElse(null);
        assertTrue("Past order contains MacBook Air 15\" M3", laptopItem != null);
        assertTrue("Laptop item detects price drop", laptopItem.hasPriceDrop());
        assertEquals("Price drop is exactly ₹10,000.00", 10000.00, laptopItem.getPriceDrop());
        assertEquals("Current catalog price is ₹1,34,900.00", 134900.00, laptopItem.getCurrentPrice());
    }

    private static void testOrderPersistenceAndIntegrity() {
        System.out.println("\n--- Testing Order DAO Persistence & Indian GST Breakdown ---");
        OrderDAO orderDAO = new OrderDAO();
        Order order = new Order();
        order.setCustomerName("Aditya Sharma");
        order.setCustomerEmail("aditya.sharma@college.ac.in");
        order.setCustomerPhone("+91 98765 43210");
        order.setShippingAddress("Flat 402, Prestige Tech Park, Bengaluru, Karnataka");
        order.setPaymentMethod("UPI (Google Pay)");
        order.setBudgetLimit(160000.00);
        order.setSubtotal(134900.00);
        order.setCgst(12141.00);
        order.setSgst(12141.00);
        order.setTax(24282.00);
        order.setShippingCost(0.00);
        order.setDiscount(0.00);
        order.setTotalAmount(159182.00);
        order.setReadinessScore(95);

        order.addItem(new OrderItem(1, "Apple MacBook Air 15\" M3 (16GB, 512GB SSD)", 134900.00, 1, 134900.00));

        boolean saved = orderDAO.saveOrder(order);
        assertTrue("Order saved successfully", saved);
        assertTrue("Order number generated with SC- prefix", order.getOrderNumber() != null && order.getOrderNumber().startsWith("SC-"));
        assertEquals("Indian GSTIN assigned", "29AABCU9603R1ZM", order.getGstin());

        Order fetched = orderDAO.getOrderByNumber(order.getOrderNumber());
        assertTrue("Fetched order by order number", fetched != null);
        assertEquals("Customer name preserved", "Aditya Sharma", fetched.getCustomerName());
        assertEquals("Total amount matches exactly", 159182.00, fetched.getTotalAmount());
        assertEquals("CGST matches", 12141.00, fetched.getCgst());
        assertEquals("SGST matches", 12141.00, fetched.getSgst());
    }
}
