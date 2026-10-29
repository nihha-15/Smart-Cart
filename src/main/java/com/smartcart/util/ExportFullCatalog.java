package com.smartcart.util;

import com.smartcart.dao.ProductDAO;
import com.smartcart.model.Product;

import java.io.*;
import java.util.*;

public class ExportFullCatalog {
    public static void main(String[] args) throws Exception {
        ProductDAO dao = new ProductDAO();
        List<Product> products = dao.getAllProducts();

        Map<String, List<Product>> byCategory = new LinkedHashMap<>();
        for (Product p : products) {
            byCategory.computeIfAbsent(p.getCategory(), k -> new ArrayList<>()).add(p);
        }

        System.out.println("CATEGORIES FOUND: " + byCategory.size());
        for (Map.Entry<String, List<Product>> entry : byCategory.entrySet()) {
            System.out.println("\n=== CATEGORY: " + entry.getKey() + " (" + entry.getValue().size() + " products) ===");
            for (Product p : entry.getValue()) {
                System.out.printf("  [%3d] %-12s | %-55s | %s%n", 
                    p.getId(), p.getBrand(), p.getName(), p.getImageUrl());
            }
        }
    }
}
