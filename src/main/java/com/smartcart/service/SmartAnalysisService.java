package com.smartcart.service;

import com.smartcart.dao.ProductDAO;
import com.smartcart.model.*;

import java.util.*;

/**
 * Intelligent Decision Engine for SmartCart (INR / ₹ Edition).
 * Implements the distinctive smart features:
 *  - Feature A: Budget Guardian
 *  - Feature B: Cart Mistake Detector
 *  - Feature C: Compatibility Checker
 *  - Feature D: Cart Risk & Value Report
 *  - Feature E: 3-Year Lifetime Cost of Ownership (TCO)
 *  - Feature F: Smart Setup Bundle Recommender ("Complete Your Setup")
 */
public class SmartAnalysisService {

    private final ProductDAO productDAO;

    public SmartAnalysisService() {
        this.productDAO = new ProductDAO();
    }

    public SmartAnalysisService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Feature D: Generates the comprehensive Cart Risk & Value Report before checkout.
     */
    public AnalysisReport generateReport(Cart cart) {
        AnalysisReport report = new AnalysisReport();
        if (cart == null || cart.isEmpty()) {
            report.setReadinessScore(100);
            report.setStatusTitle("Cart is Empty");
            report.setStatusBadgeClass("badge-secondary");
            report.setExecutiveSummary("Add products to your cart to see intelligent compatibility, budget, lifetime TCO, and mistake analysis.");
            return report;
        }

        // 1. Budget Guardian Metrics (Feature A)
        double total = cart.getTotal();
        double budget = cart.getBudget();
        double remaining = cart.getRemainingBudget();
        double usagePct = cart.getBudgetUsagePercentage();
        boolean overBudget = cart.isOverBudget();

        report.setCartTotal(total);
        report.setBudget(budget);
        report.setRemainingBudget(remaining);
        report.setBudgetUsagePercentage(usagePct);
        report.setBudgetExceeded(overBudget);

        // 2. Feature E: 3-Year Lifetime Cost of Ownership (TCO)
        double threeYearTCO = cart.getEstimatedThreeYearTCO();
        double energyAndMaintenance = cart.getThreeYearEnergyCost() + cart.getThreeYearMaintenanceCost();
        report.setEstimatedThreeYearTCO(threeYearTCO);
        report.setEnergyAndMaintenanceTotal(energyAndMaintenance);

        // 3. Cart Mistake Detector (Feature B)
        List<MistakeAlert> mistakes = detectMistakes(cart);
        report.setMistakeAlerts(mistakes);

        // 4. Compatibility Checker (Feature C)
        List<CompatibilityIssue> compatibilityIssues = checkCompatibility(cart);
        report.setCompatibilityIssues(compatibilityIssues);

        // 5. Budget Alternatives & Savings (Feature A & D in INR)
        Set<Integer> productIdsInCart = new HashSet<>();
        for (CartItem item : cart.getItems()) {
            productIdsInCart.add(item.getProduct().getId());
        }
        List<AlternativeRecommendation> alternatives = productDAO.getAlternativesForCart(productIdsInCart);
        report.setAlternativeRecommendations(alternatives);

        double totalSavings = 0.0;
        for (AlternativeRecommendation alt : alternatives) {
            totalSavings += alt.getSavingsAmount();
        }
        report.setTotalPotentialSavings(Math.round(totalSavings * 100.0) / 100.0);

        // 6. Feature F: Smart Setup Bundles & Setup Completion Score
        List<SetupBundle> bundles = productDAO.getSetupBundlesForCart(productIdsInCart);
        report.setRecommendedBundles(bundles);
        int setupScore = calculateSetupScore(cart);
        report.setSetupCompletionScore(setupScore);

        // 7. Calculate Purchase Readiness Score (0 - 100)
        int score = 100;

        // Deductions for critical issues
        int criticalCount = report.getCriticalIssueCount();
        score -= (criticalCount * 25);

        // Deductions for over-budget
        if (overBudget) {
            score -= 20;
        } else if (cart.isApproachingBudget()) {
            score -= 5;
        }

        // Deductions for non-critical mistakes
        int mistakeCount = mistakes.size();
        score -= (mistakeCount * 10);

        // Deductions for non-critical compatibility warnings
        int warningCount = report.getWarningCount() - mistakeCount - (overBudget ? 1 : 0);
        score -= (Math.max(0, warningCount) * 10);

        score = Math.max(10, Math.min(100, score));
        report.setReadinessScore(score);

        // Set status titles and badges
        if (criticalCount > 0 || score < 60) {
            report.setStatusTitle("Critical Issues Detected");
            report.setStatusBadgeClass("badge-danger");
            report.setExecutiveSummary("Action Required: Severe specification conflicts or budget overruns were identified. Review the issues below before proceeding.");
        } else if (mistakeCount > 0 || overBudget || score < 85) {
            report.setStatusTitle("Review Recommended");
            report.setStatusBadgeClass("badge-warning");
            report.setExecutiveSummary("Advisory: Your cart contains warnings or potential cost-saving opportunities. Please verify before completing checkout.");
        } else {
            report.setStatusTitle("Safe to Purchase");
            report.setStatusBadgeClass("badge-success");
            report.setExecutiveSummary("Optimal: All items are mutually compatible, purchase quantities are standard, and totals remain within your defined budget.");
        }

        return report;
    }

    /**
     * Calculates the Setup Completion Score based on essential companions.
     */
    private int calculateSetupScore(Cart cart) {
        if (cart == null || cart.isEmpty()) return 100;
        int score = 100;
        boolean hasLaptop = false;
        boolean hasPhone = false;
        boolean hasTablet = false;
        boolean hasMouseOrKeyboard = false;
        boolean hasPhoneAccessory = false;
        boolean hasStylus = false;

        for (CartItem item : cart.getItems()) {
            String cat = item.getProduct().getCategory().toLowerCase();
            int pid = item.getProduct().getId();
            if ("laptops".equals(cat)) hasLaptop = true;
            if ("smartphones".equals(cat)) hasPhone = true;
            if ("tablets".equals(cat) && (pid == 9 || pid == 10)) hasTablet = true;
            if (pid == 32 || pid == 33) hasMouseOrKeyboard = true;
            if (pid == 27 || pid == 28 || pid == 29 || pid == 30 || pid == 24 || pid == 25) hasPhoneAccessory = true;
            if (pid == 11 || pid == 12) hasStylus = true;
        }

        if (hasLaptop && !hasMouseOrKeyboard) score -= 15;
        if (hasPhone && !hasPhoneAccessory) score -= 15;
        if (hasTablet && !hasStylus) score -= 15;

        return Math.max(40, score);
    }

    /**
     * Feature B: Cart Mistake Detector
     * Detects high quantities, redundant items, and unnecessary purchases.
     */
    public List<MistakeAlert> detectMistakes(Cart cart) {
        List<MistakeAlert> alerts = new ArrayList<>();
        if (cart == null || cart.isEmpty()) return alerts;

        List<CartItem> items = cart.getItemList();

        // 1. High Quantity Detection
        for (CartItem item : items) {
            Product p = item.getProduct();
            int qty = item.getQuantity();
            String cat = p.getCategory().toLowerCase();

            if (("laptops".equals(cat) || "smartphones".equals(cat) || "tablets".equals(cat)) && qty >= 3) {
                String alertId = "MISTAKE_HIGH_QTY_" + p.getId();
                if (!cart.isAlertDismissed(alertId)) {
                    alerts.add(new MistakeAlert(
                            alertId,
                            MistakeAlert.AlertType.HIGH_QUANTITY,
                            "Unusually High Quantity for " + p.getName(),
                            "You have added " + qty + " units of " + p.getName() + " to your cart.",
                            "Purchasing " + qty + " major electronics in a single consumer order is uncommon. Please verify this was not an accidental double-click.",
                            p.getId(),
                            p.getName(),
                            "HIGH"
                    ));
                }
            } else if (qty >= 5) {
                String alertId = "MISTAKE_HIGH_QTY_ACC_" + p.getId();
                if (!cart.isAlertDismissed(alertId)) {
                    alerts.add(new MistakeAlert(
                            alertId,
                            MistakeAlert.AlertType.HIGH_QUANTITY,
                            "Bulk Accessory Quantity for " + p.getName(),
                            "You have selected " + qty + " units of this accessory.",
                            "High item count detected. Please confirm whether multiple units are needed for your setup.",
                            p.getId(),
                            p.getName(),
                            "MEDIUM"
                    ));
                }
            }
        }

        // 2. Unnecessary Purchases: Apple iPhone 15 (id 6) includes 20W charger in-box
        CartItem phone15 = cart.getItem(6);
        CartItem charger20w = cart.getItem(17);
        if (phone15 != null && charger20w != null) {
            String alertId = "MISTAKE_UNNECESSARY_CHARGER_20W";
            if (!cart.isAlertDismissed(alertId)) {
                alerts.add(new MistakeAlert(
                        alertId,
                        MistakeAlert.AlertType.UNNECESSARY_PURCHASE,
                        "Potentially Redundant Charger Purchase",
                        "You are buying a 20W Charger while purchasing the " + phone15.getProduct().getName() + ".",
                        "The " + phone15.getProduct().getName() + " retail box already includes an official 20W fast wall charger. You may not need to spend an extra " + charger20w.getProduct().getFormattedPrice() + " on a second one.",
                        charger20w.getProduct().getId(),
                        charger20w.getProduct().getName(),
                        "MEDIUM"
                ));
            }
        }

        // 3. Unnecessary Purchases: Samsung Galaxy Tab S9 FE (id 10) includes S-Pen in-box
        CartItem tabS9 = cart.getItem(10);
        CartItem stylus = cart.getItem(11);
        if (tabS9 != null && stylus != null) {
            String alertId = "MISTAKE_UNNECESSARY_STYLUS";
            if (!cart.isAlertDismissed(alertId)) {
                alerts.add(new MistakeAlert(
                        alertId,
                        MistakeAlert.AlertType.UNNECESSARY_PURCHASE,
                        "Redundant Stylus Pen Purchase",
                        "You are purchasing a standalone " + stylus.getProduct().getName() + " alongside the " + tabS9.getProduct().getName() + ".",
                        "The " + tabS9.getProduct().getName() + " retail package already includes the official Samsung S-Pen free in the box. You can save " + stylus.getProduct().getFormattedPrice() + " by removing this accessory.",
                        stylus.getProduct().getId(),
                        stylus.getProduct().getName(),
                        "MEDIUM"
                ));
            }
        }

        // 4. Unnecessary Purchases: 4K Gaming Monitor (id 31) includes HDMI cable in-box
        CartItem monitor = cart.getItem(31);
        CartItem hdmiCable = cart.getItem(21);
        if (monitor != null && hdmiCable != null) {
            String alertId = "MISTAKE_UNNECESSARY_HDMI";
            if (!cart.isAlertDismissed(alertId)) {
                alerts.add(new MistakeAlert(
                        alertId,
                        MistakeAlert.AlertType.UNNECESSARY_PURCHASE,
                        "Redundant HDMI 2.1 Cable Purchase",
                        "You are buying an 8K HDMI cable while ordering the " + monitor.getProduct().getName() + ".",
                        "The " + monitor.getProduct().getName() + " already comes bundled with a certified high-speed HDMI 2.1 cable in the box. You can save " + hdmiCable.getProduct().getFormattedPrice() + ".",
                        hdmiCable.getProduct().getId(),
                        hdmiCable.getProduct().getName(),
                        "LOW"
                ));
            }
        }

        // 5. Redundant Cases for the Same Phone Model
        List<Product> casesInCart = new ArrayList<>();
        for (CartItem item : items) {
            if ("cases".equalsIgnoreCase(item.getProduct().getCategory())) {
                casesInCart.add(item.getProduct());
            }
        }
        if (casesInCart.size() > 1) {
            String alertId = "MISTAKE_REDUNDANT_CASES";
            if (!cart.isAlertDismissed(alertId)) {
                alerts.add(new MistakeAlert(
                        alertId,
                        MistakeAlert.AlertType.REDUNDANT_ACCESSORY,
                        "Multiple Cases in Cart (" + casesInCart.size() + " items)",
                        "You have multiple protective cases in your cart.",
                        "Shoppers typically only need one protective case per phone. Review whether all selected cases are necessary.",
                        casesInCart.get(0).getId(),
                        "Protective Cases",
                        "LOW"
                ));
            }
        }

        return alerts;
    }

    /**
     * Feature C: Compatibility Checker
     * Rule-based engine validating specs between devices and accessories.
     */
    public List<CompatibilityIssue> checkCompatibility(Cart cart) {
        List<CompatibilityIssue> issues = new ArrayList<>();
        if (cart == null || cart.isEmpty()) return issues;

        List<CartItem> items = cart.getItemList();

        List<Product> laptops = new ArrayList<>();
        List<Product> smartphones = new ArrayList<>();
        List<Product> tablets = new ArrayList<>();
        List<Product> chargers = new ArrayList<>();
        List<Product> cables = new ArrayList<>();
        List<Product> audio = new ArrayList<>();
        List<Product> cases = new ArrayList<>();

        for (CartItem item : items) {
            Product p = item.getProduct();
            String cat = p.getCategory().toLowerCase();
            switch (cat) {
                case "laptops": laptops.add(p); break;
                case "smartphones": smartphones.add(p); break;
                case "tablets": tablets.add(p); break;
                case "chargers": chargers.add(p); break;
                case "cables": cables.add(p); break;
                case "audio": audio.add(p); break;
                case "cases": cases.add(p); break;
            }
        }

        // Rule 1: Charger Wattage vs Laptop Requirement
        for (Product laptop : laptops) {
            int minWatts = 65;
            try {
                String mwStr = laptop.getSpec("min_charging_watts");
                if (mwStr != null) minWatts = Integer.parseInt(mwStr);
            } catch (NumberFormatException ignored) {}

            for (Product charger : chargers) {
                int outputWatts = 0;
                try {
                    String owStr = charger.getSpec("output_watts");
                    if (owStr != null) outputWatts = Integer.parseInt(owStr);
                } catch (NumberFormatException ignored) {}

                if (outputWatts > 0 && outputWatts < minWatts) {
                    issues.add(new CompatibilityIssue(
                            "COMPAT_WATT_" + laptop.getId() + "_" + charger.getId(),
                            "RULE_CHARGER_WATTAGE",
                            laptop.getId(),
                            laptop.getName(),
                            charger.getId(),
                            charger.getName(),
                            "CRITICAL",
                            "Insufficient Charger Power (" + outputWatts + "W vs " + minWatts + "W Required)",
                            charger.getName() + " only outputs " + outputWatts + "W, but " + laptop.getName() + 
                            " requires at least " + minWatts + "W. The laptop will charge very slowly, fail to charge, or lose power under load.",
                            "Replace with an Anker 65W GaN Charger or Anker 150W Desktop Station.",
                            minWatts >= 100 ? 16 : 15,
                            minWatts >= 100 ? "Anker 747 150W 4-Port GaN Desktop Charger" : "Anker 735 65W GaN Dual USB-C Fast Charger"
                    ));
                }
            }
        }

        // Rule 2: Smartphone Audio Jack vs 3.5mm Wired Earphones
        for (Product phone : smartphones) {
            boolean hasAudioJack = "true".equalsIgnoreCase(phone.getSpec("audio_jack"));
            if (!hasAudioJack) {
                for (Product aud : audio) {
                    String audioType = aud.getSpec("audio_type");
                    if ("wired_3.5mm".equalsIgnoreCase(audioType)) {
                        issues.add(new CompatibilityIssue(
                                "COMPAT_AUDIO_" + phone.getId() + "_" + aud.getId(),
                                "RULE_AUDIO_JACK_MISSING",
                                phone.getId(),
                                phone.getName(),
                                aud.getId(),
                                aud.getName(),
                                "WARNING",
                                "Missing 3.5mm Audio Port on " + phone.getName(),
                                phone.getName() + " does not feature a 3.5mm headphone jack. Connecting " + 
                                aud.getName() + " directly is not possible without an Apple USB-C to 3.5mm DAC adapter.",
                                "Switch to Apple AirPods Pro or add an Apple USB-C to 3.5mm DAC Dongle.",
                                24,
                                "Apple AirPods Pro (2nd Gen, USB-C MagSafe Case)"
                        ));
                    }
                }
            }
        }

        // Rule 3: Case Dimension & Model Matching
        for (Product phone : smartphones) {
            String phoneModel = phone.getSpec("model_family");
            String phoneSize = phone.getSpec("screen_size");

            for (Product c : cases) {
                String targetModel = c.getSpec("target_model");
                String targetSize = c.getSpec("target_screen_size");

                if (targetModel != null && phoneModel != null && !targetModel.equalsIgnoreCase(phoneModel)) {
                    issues.add(new CompatibilityIssue(
                            "COMPAT_CASE_" + phone.getId() + "_" + c.getId(),
                            "RULE_CASE_DIMENSION_MISMATCH",
                            phone.getId(),
                            phone.getName(),
                            c.getId(),
                            c.getName(),
                            "CRITICAL",
                            "Phone Case Physical Size Mismatch",
                            c.getName() + " is engineered for " + targetModel + " (" + targetSize + "\"), but you have selected " + 
                            phone.getName() + " (" + phoneSize + "\"). The case will not fit the phone dimensions or camera cutouts.",
                            "Choose the matching case designed for " + phoneModel + ".",
                            phone.getId() == 5 ? 27 : 29,
                            phone.getId() == 5 ? "Spigen Ultra Hybrid MagFit Case (iPhone 15 Pro, 6.1\")" : "Spigen Liquid Crystal Case (iPhone 15, 6.1\")"
                    ));
                }
            }
        }

        // Rule 4: Phone Sold Without In-Box Charger and No Charger in Cart
        for (Product phone : smartphones) {
            boolean includesCharger = "true".equalsIgnoreCase(phone.getSpec("includes_charger"));
            if (!includesCharger && chargers.isEmpty()) {
                issues.add(new CompatibilityIssue(
                        "COMPAT_NO_CHARGER_" + phone.getId(),
                        "RULE_SMARTPHONE_NO_CHARGER",
                        phone.getId(),
                        phone.getName(),
                        0,
                        "No Charger in Cart",
                        "INFO",
                        "Wall Charger Not Included with " + phone.getName(),
                        phone.getName() + " comes packaged with only a USB-C cable in the box; a wall power adapter is not included.",
                        "Add an Anker 735 65W GaN Dual USB-C Fast Charger to enjoy high-speed charging.",
                        15,
                        "Anker 735 65W GaN Dual USB-C Fast Charger"
                ));
            }
        }

        // Rule 5: Pro Tablet Sold Without Stylus
        for (Product tab : tablets) {
            boolean includesStylus = "true".equalsIgnoreCase(tab.getSpec("includes_stylus"));
            boolean hasStylusInCart = false;
            for (CartItem item : items) {
                if (item.getProduct().getId() == 11) hasStylusInCart = true;
            }
            if (!includesStylus && !hasStylusInCart) {
                issues.add(new CompatibilityIssue(
                        "COMPAT_NO_STYLUS_" + tab.getId(),
                        "RULE_TABLET_NO_STYLUS",
                        tab.getId(),
                        tab.getName(),
                        0,
                        "No Stylus Pen in Cart",
                        "INFO",
                        "Stylus Pen Not Included with " + tab.getName(),
                        tab.getName() + " supports high-precision pressure drawing and notes, but does not bundle a stylus pen in the box.",
                        "Add the Apple Pencil Pro to unlock full productivity.",
                        11,
                        "Apple Pencil Pro (Magnetic Wireless Charging)"
                ));
            }
        }

        return issues;
    }
}
