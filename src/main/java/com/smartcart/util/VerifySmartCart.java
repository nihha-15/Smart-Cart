package com.smartcart.util;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class VerifySmartCart {
    private static final String BASE_URL = "http://localhost:8080";

    public static void main(String[] args) throws Exception {
        System.out.println("=== 1. VERIFYING MAIN PAGES ===");
        checkEndpoint("/", 200, "SmartCart");
        checkEndpoint("/products", 200, "All Products");

        System.out.println("\n=== 2. VERIFYING ALL 13 CATEGORY VIEWS ===");
        String[] categories = {
            "laptops", "smartphones", "tablets", "wearables", "chargers",
            "cables", "audio", "cases", "monitors", "smart-home",
            "desk-setup", "gaming", "power"
        };
        for (String cat : categories) {
            checkEndpoint("/products?category=" + cat, 200, "category");
        }

        System.out.println("\n=== 3. VERIFYING PRODUCT DETAILS PAGES ===");
        checkEndpoint("/product?id=1", 200, "MacBook Air");
        checkEndpoint("/product?id=5", 200, "iPhone 15 Pro");
        checkEndpoint("/product?id=46", 200, "Dell XPS 15");
        checkEndpoint("/product?id=56", 200, "Galaxy S24 Ultra");
        checkEndpoint("/product?id=126", 200, "Samsung Odyssey Neo");

        System.out.println("\n=== 4. VERIFYING ALL 175 PRODUCT IMAGE HTTP STATUSES ===");
        int okCount = 0;
        int failCount = 0;
        for (int i = 1; i <= 175; i++) {
            String imgUrl = "/images/prod-" + i + ".png";
            int code = getStatusCode(imgUrl);
            if (code == 200) {
                okCount++;
            } else {
                failCount++;
                System.err.printf("Image failed: %s (Status: %d)%n", imgUrl, code);
            }
        }
        System.out.printf("Image Check Summary: %d OK (HTTP 200), %d FAILED%n", okCount, failCount);

        System.out.println("\n=== 5. VERIFYING PLACEHOLDER IMAGE ===");
        checkEndpoint("/images/placeholder-product.png", 200, null);

        if (failCount == 0) {
            System.out.println("\n=======================================================");
            System.out.println("   ALL 175 PRODUCT IMAGES VERIFIED ON LIVE HTTP SERVER!");
            System.out.println("=======================================================");
        }
    }

    private static void checkEndpoint(String path, int expectedCode, String expectedText) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URI(BASE_URL + path).toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            int code = conn.getResponseCode();
            if (code != expectedCode) {
                System.out.printf("  [FAIL] %s -> Expected %d, got %d%n", path, expectedCode, code);
                return;
            }
            if (expectedText != null) {
                String body = readBody(conn.getInputStream());
                if (!body.contains(expectedText)) {
                    System.out.printf("  [WARN] %s -> Missing expected text '%s'%n", path, expectedText);
                    return;
                }
            }
            System.out.printf("  [OK] %-35s -> HTTP %d%n", path, code);
        } catch (Exception e) {
            System.out.printf("  [ERROR] %s -> %s%n", path, e.getMessage());
        }
    }

    private static int getStatusCode(String path) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URI(BASE_URL + path).toURL().openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            return conn.getResponseCode();
        } catch (Exception e) {
            return -1;
        }
    }

    private static String readBody(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            char[] buf = new char[4096];
            int r;
            while ((r = br.read(buf)) != -1) {
                sb.append(buf, 0, r);
            }
        }
        return sb.toString();
    }
}
