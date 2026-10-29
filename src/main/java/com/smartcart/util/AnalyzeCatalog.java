package com.smartcart.util;

import com.smartcart.dao.ProductDAO;
import com.smartcart.model.Product;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.MessageDigest;
import java.util.*;

public class AnalyzeCatalog {
    public static void main(String[] args) throws Exception {
        File webappDir = new File("C:/Users/nihas/.gemini/antigravity/scratch/SmartCart/src/main/webapp");
        ProductDAO dao = new ProductDAO();
        List<Product> products = dao.getAllProducts();

        System.out.println("Total products in DAO: " + products.size());

        Map<String, List<Product>> hashToProducts = new LinkedHashMap<>();
        int missingCount = 0;

        for (Product p : products) {
            String imgPath = p.getImageUrl();
            File imgFile = new File(webappDir, imgPath);
            if (!imgFile.exists()) {
                missingCount++;
                System.out.printf("MISSING: ID=%d Name='%s' ImageUrl='%s'%n", p.getId(), p.getName(), imgPath);
                continue;
            }

            String hash = getFileHash(imgFile);
            hashToProducts.computeIfAbsent(hash, k -> new ArrayList<>()).add(p);
        }

        System.out.println("Missing images count: " + missingCount);
        System.out.println("Unique image hashes across catalog: " + hashToProducts.size());

        System.out.println("\n--- DUPLICATE IMAGE GROUPS ---");
        int dupGroup = 0;
        for (Map.Entry<String, List<Product>> entry : hashToProducts.entrySet()) {
            if (entry.getValue().size() > 1) {
                dupGroup++;
                System.out.printf("Group %d (Hash: %s, Count: %d, File: %s):%n", 
                    dupGroup, entry.getKey(), entry.getValue().size(), entry.getValue().get(0).getImageUrl());
                for (Product p : entry.getValue()) {
                    System.out.printf("   - ID=%d [%s] %s (%s)%n", p.getId(), p.getCategory(), p.getName(), p.getImageUrl());
                }
            }
        }
    }

    private static String getFileHash(File f) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = new FileInputStream(f)) {
            byte[] buf = new byte[8192];
            int read;
            while ((read = is.read(buf)) > 0) {
                md.update(buf, 0, read);
            }
        }
        byte[] d = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : d) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
