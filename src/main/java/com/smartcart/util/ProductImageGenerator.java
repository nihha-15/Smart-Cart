package com.smartcart.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * High-fidelity Studio Product Image Generator.
 * Uses Java 2D Graphics with anti-aliasing, linear & radial gradients,
 * realistic device geometry, contact shadows, display glows, and brand badges
 * to render crisp 400x320 PNG studio photographs for all 175 SmartCart products.
 */
public class ProductImageGenerator {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 320;
    private static final File OUTPUT_DIR = new File("src/main/webapp/images");

    public static void main(String[] args) {
        if (!OUTPUT_DIR.exists()) {
            OUTPUT_DIR.mkdirs();
        }
        System.out.println("Starting high-resolution Studio Image generation for SmartCart...");
        long start = System.currentTimeMillis();

        generateAll175ProductImages();

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Successfully generated all product images in " + elapsed + " ms to " + OUTPUT_DIR.getAbsolutePath());
    }

    private static Graphics2D setupCanvas(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // Studio lighting gradient background
        RadialGradientPaint bgGrad = new RadialGradientPaint(
                new Point2D.Float(WIDTH / 2.0f, HEIGHT / 2.0f - 20),
                220.0f,
                new float[]{0.0f, 0.7f, 1.0f},
                new Color[]{new Color(255, 255, 255), new Color(248, 250, 252), new Color(241, 245, 249)}
        );
        g.setPaint(bgGrad);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Subtle studio border
        g.setColor(new Color(226, 232, 240));
        g.drawRoundRect(2, 2, WIDTH - 5, HEIGHT - 5, 16, 16);

        // Floor contact shadow
        g.setPaint(new RadialGradientPaint(
                new Point2D.Float(WIDTH / 2.0f, HEIGHT - 45),
                160.0f,
                new float[]{0.0f, 0.6f, 1.0f},
                new Color[]{new Color(15, 23, 42, 60), new Color(30, 41, 59, 25), new Color(255, 255, 255, 0)}
        ));
        g.fillOval(40, HEIGHT - 65, WIDTH - 80, 40);

        return g;
    }

    private static void drawBrandBadge(Graphics2D g, String brand, String tag) {
        // Brand pill (top left)
        int badgeW = Math.max(90, brand.length() * 10 + 24);
        g.setPaint(new Color(255, 255, 255, 240));
        g.fillRoundRect(16, 16, badgeW, 28, 8, 8);
        g.setColor(new Color(203, 213, 225));
        g.drawRoundRect(16, 16, badgeW, 28, 8, 8);

        g.setColor(new Color(15, 23, 42));
        g.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g.drawString(brand.toUpperCase(), 26, 35);

        // Spec / category tag (top right)
        if (tag != null && !tag.isEmpty()) {
            int tagW = tag.length() * 7 + 20;
            int tagX = WIDTH - tagW - 16;
            g.setPaint(new Color(241, 245, 249, 230));
            g.fillRoundRect(tagX, 16, tagW, 26, 6, 6);
            g.setColor(new Color(203, 213, 225));
            g.drawRoundRect(tagX, 16, tagW, 26, 6, 6);

            g.setColor(new Color(71, 85, 105));
            g.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g.drawString(tag, tagX + 10, 33);
        }
    }

    private static void saveImage(BufferedImage img, String filename) {
        try {
            File file = new File(OUTPUT_DIR, filename);
            ImageIO.write(img, "PNG", file);
        } catch (IOException e) {
            System.err.println("Failed to write image " + filename + ": " + e.getMessage());
        }
    }

    // -------------------------------------------------------------
    // CATEGORY RENDERERS
    // -------------------------------------------------------------

    public static void renderLaptop(String filename, String brand, String model, Color bodyColor, Color screenAccent, boolean gaming) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        // Screen Lid
        GradientPaint lidGrad = new GradientPaint(100, 40, bodyColor, 300, 40, bodyColor.darker());
        g.setPaint(lidGrad);
        g.fillRoundRect(85, 45, 230, 155, 10, 10);
        g.setColor(new Color(71, 85, 105));
        g.drawRoundRect(85, 45, 230, 155, 10, 10);

        // Screen Bezel & Display
        g.setColor(new Color(15, 23, 42));
        g.fillRoundRect(93, 53, 214, 139, 4, 4);

        // Wallpaper / Glow
        GradientPaint displayGrad = new GradientPaint(93, 53, screenAccent, 307, 192, new Color(15, 23, 42));
        g.setPaint(displayGrad);
        g.fillRoundRect(95, 55, 210, 135, 4, 4);

        // Display highlights / UI
        g.setColor(new Color(255, 255, 255, 120));
        g.fillRoundRect(180, 175, 40, 5, 2, 2); // Dock bar
        g.setColor(new Color(255, 255, 255, 160));
        g.drawString(model, 105, 75);

        // WebCam
        g.setColor(new Color(203, 213, 225));
        g.fillOval(198, 48, 4, 4);

        // Keyboard Base (Isometric Perspective)
        Polygon base = new Polygon();
        base.addPoint(50, 235);
        base.addPoint(350, 235);
        base.addPoint(315, 200);
        base.addPoint(85, 200);

        GradientPaint baseGrad = new GradientPaint(50, 200, bodyColor, 50, 235, bodyColor.darker());
        g.setPaint(baseGrad);
        g.fillPolygon(base);
        g.setColor(new Color(100, 116, 139));
        g.drawPolygon(base);

        // Keyboard Well
        Polygon kb = new Polygon();
        kb.addPoint(95, 218);
        kb.addPoint(305, 218);
        kb.addPoint(290, 203);
        kb.addPoint(110, 203);
        g.setColor(gaming ? new Color(239, 68, 68, 180) : new Color(30, 41, 59));
        g.fillPolygon(kb);

        // Trackpad
        g.setColor(new Color(255, 255, 255, 40));
        g.fillRoundRect(170, 221, 60, 12, 3, 3);
        g.setColor(new Color(148, 163, 184));
        g.drawRoundRect(170, 221, 60, 12, 3, 3);

        // Base front lip
        g.setColor(bodyColor.darker().darker());
        g.fillRect(50, 235, 300, 6);

        drawBrandBadge(g, brand, gaming ? "GAMING" : "OLED / PRO");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderSmartphone(String filename, String brand, String model, Color frameColor, String camStyle) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        // Phone Body (Centered Portrait)
        int x = 140, y = 42, w = 120, h = 225, r = 24;

        // Frame / Chamfer
        g.setColor(frameColor.darker());
        g.fillRoundRect(x - 2, y - 2, w + 4, h + 4, r + 4, r + 4);

        GradientPaint frameGrad = new GradientPaint(x, y, frameColor, x + w, y + h, frameColor.darker());
        g.setPaint(frameGrad);
        g.fillRoundRect(x, y, w, h, r, r);

        // Screen bezel & display
        g.setColor(new Color(15, 23, 42));
        g.fillRoundRect(x + 5, y + 5, w - 10, h - 10, r - 4, r - 4);

        // Wallpaper
        GradientPaint screenGrad = new GradientPaint(x + 5, y + 5, new Color(59, 130, 246), x + w - 5, y + h - 5, new Color(147, 51, 234));
        g.setPaint(screenGrad);
        g.fillRoundRect(x + 6, y + 6, w - 12, h - 12, r - 5, r - 5);

        // Glass glare diagonal
        g.setPaint(new GradientPaint(x + 6, y + 6, new Color(255, 255, 255, 70), x + 80, y + 120, new Color(255, 255, 255, 0)));
        Polygon glare = new Polygon();
        glare.addPoint(x + 6, y + 6);
        glare.addPoint(x + 70, y + 6);
        glare.addPoint(x + 6, y + 100);
        g.fillPolygon(glare);

        // Punch hole / Dynamic Island
        if ("pill".equals(camStyle)) {
            g.setColor(new Color(15, 23, 42));
            g.fillRoundRect(x + w / 2 - 16, y + 12, 32, 10, 5, 5);
        } else {
            g.setColor(new Color(15, 23, 42));
            g.fillOval(x + w / 2 - 4, y + 12, 8, 8);
        }

        // Camera Bump (reflected ghost on left side to show back design)
        g.setColor(new Color(15, 23, 42, 220));
        g.fillRoundRect(65, 80, 55, 85, 14, 14);
        g.setColor(new Color(148, 163, 184));
        g.drawRoundRect(65, 80, 55, 85, 14, 14);
        // Triple Lenses
        g.setColor(new Color(30, 41, 59));
        g.fillOval(75, 90, 20, 20);
        g.fillOval(75, 115, 20, 20);
        g.fillOval(95, 102, 16, 16);
        g.setColor(new Color(56, 189, 248));
        g.fillOval(80, 95, 10, 10);
        g.fillOval(80, 120, 10, 10);
        g.fillOval(98, 105, 8, 8);

        // Connecting indicator
        g.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g.setColor(new Color(100, 116, 139));
        g.drawString("Rear Optics", 63, 180);

        drawBrandBadge(g, brand, "5G FLAGSHIP");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderTablet(String filename, String brand, String model, Color bodyColor, boolean hasPen) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        int x = 85, y = 45, w = 220, h = 175, r = 16;

        // Tablet Frame
        g.setColor(bodyColor.darker());
        g.fillRoundRect(x, y, w, h, r, r);
        g.setColor(new Color(203, 213, 225));
        g.drawRoundRect(x, y, w, h, r, r);

        // Screen bezel
        g.setColor(new Color(15, 23, 42));
        g.fillRoundRect(x + 8, y + 8, w - 16, h - 16, 8, 8);

        // Display
        GradientPaint disp = new GradientPaint(x + 8, y + 8, new Color(99, 102, 241), x + w, y + h, new Color(236, 72, 153));
        g.setPaint(disp);
        g.fillRoundRect(x + 10, y + 10, w - 20, h - 20, 6, 6);

        // Front camera
        g.setColor(new Color(15, 23, 42));
        g.fillOval(x + w / 2 - 3, y + 3, 6, 6);

        // Stylus Pen
        if (hasPen) {
            g.setColor(new Color(241, 245, 249));
            g.fillRoundRect(x + 20, y + h + 18, 180, 8, 4, 4);
            g.setColor(new Color(100, 116, 139));
            g.drawRoundRect(x + 20, y + h + 18, 180, 8, 4, 4);
            // Pen tip
            Polygon tip = new Polygon();
            tip.addPoint(x + 15, y + h + 22);
            tip.addPoint(x + 20, y + h + 18);
            tip.addPoint(x + 20, y + h + 26);
            g.setColor(new Color(30, 41, 59));
            g.fillPolygon(tip);
            // Label
            g.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g.setColor(new Color(71, 85, 105));
            g.drawString("Precision Stylus Included", x + 50, y + h + 38);
        }

        drawBrandBadge(g, brand, "TABLET");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderWearable(String filename, String brand, String model, String shape, Color strapColor) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        int cx = WIDTH / 2;
        int cy = HEIGHT / 2;

        // Straps (Top & Bottom)
        g.setColor(strapColor);
        g.fillRoundRect(cx - 30, 35, 60, 240, 12, 12);
        g.setColor(strapColor.darker());
        g.drawRoundRect(cx - 30, 35, 60, 240, 12, 12);

        // Buckle lines
        for (int i = 50; i < 90; i += 8) {
            g.setColor(strapColor.darker());
            g.drawLine(cx - 15, i, cx + 15, i);
        }
        for (int i = 215; i < 255; i += 8) {
            g.setColor(strapColor.darker());
            g.drawLine(cx - 15, i, cx + 15, i);
        }

        // Watch Case
        if ("square".equalsIgnoreCase(shape)) {
            g.setColor(new Color(51, 65, 85));
            g.fillRoundRect(cx - 55, cy - 65, 110, 130, 32, 32);
            g.setColor(new Color(203, 213, 225));
            g.drawRoundRect(cx - 55, cy - 65, 110, 130, 32, 32);

            // Digital Crown
            g.setColor(new Color(239, 68, 68));
            g.fillRoundRect(cx + 53, cy - 35, 8, 26, 4, 4);

            // Display
            g.setColor(new Color(15, 23, 42));
            g.fillRoundRect(cx - 48, cy - 58, 96, 116, 26, 26);
        } else {
            g.setColor(new Color(51, 65, 85));
            g.fillOval(cx - 65, cy - 65, 130, 130);
            g.setColor(new Color(203, 213, 225));
            g.drawOval(cx - 65, cy - 65, 130, 130);

            // Bezel tick marks
            for (int a = 0; a < 360; a += 30) {
                double rad = Math.toRadians(a);
                int x1 = (int) (cx + 58 * Math.cos(rad));
                int y1 = (int) (cy + 58 * Math.sin(rad));
                int x2 = (int) (cx + 63 * Math.cos(rad));
                int y2 = (int) (cy + 63 * Math.sin(rad));
                g.setColor(new Color(148, 163, 184));
                g.drawLine(x1, y1, x2, y2);
            }

            // Display
            g.setColor(new Color(15, 23, 42));
            g.fillOval(cx - 54, cy - 54, 108, 108);
        }

        // Watch face graphics (Clock & Heart Rate)
        g.setColor(new Color(16, 185, 129));
        g.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g.drawString("10:42", cx - 30, cy + 5);

        g.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g.setColor(new Color(239, 68, 68));
        g.drawString("? 72 BPM", cx - 25, cy + 24);

        g.setColor(new Color(56, 189, 248));
        g.drawString("8,420 STEPS", cx - 32, cy - 15);

        drawBrandBadge(g, brand, "SMARTWATCH");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderCharger(String filename, String brand, String model, String watts, Color bodyColor) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        int cx = WIDTH / 2;
        int cy = HEIGHT / 2;

        // Charger Block Body
        g.setPaint(new GradientPaint(cx - 60, cy - 65, bodyColor, cx + 60, cy + 65, bodyColor.darker()));
        g.fillRoundRect(cx - 65, cy - 65, 130, 130, 18, 18);
        g.setColor(new Color(203, 213, 225));
        g.drawRoundRect(cx - 65, cy - 65, 130, 130, 18, 18);

        // USB-C Ports (3 Ports)
        for (int i = -30; i <= 30; i += 30) {
            g.setColor(new Color(30, 41, 59));
            g.fillRoundRect(cx - 24, cy + i - 6, 48, 12, 6, 6);
            g.setColor(new Color(16, 185, 129));
            g.fillRoundRect(cx - 16, cy + i - 2, 32, 4, 2, 2);
        }

        // Wattage label on side
        g.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g.setColor(bodyColor.equals(new Color(248, 250, 252)) ? new Color(15, 23, 42) : new Color(255, 255, 255));
        g.drawString(watts, cx - 35, cy - 40);

        // GaN fast charging badge
        g.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g.setColor(new Color(16, 185, 129));
        g.drawString("GaN FAST POWER", cx - 42, cy + 50);

        drawBrandBadge(g, brand, watts + " GaN");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderCable(String filename, String brand, String model, String plugFrom, String plugTo) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        // Coiled Braided Cable
        g.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(71, 85, 105));
        g.drawArc(90, 80, 220, 130, 0, 360);
        g.drawArc(110, 95, 180, 100, 45, 270);

        // Braiding highlight pattern
        g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4, 4}, 0));
        g.setColor(new Color(148, 163, 184));
        g.drawArc(90, 80, 220, 130, 0, 360);

        // Left Connector
        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(15, 23, 42));
        g.fillRoundRect(70, 140, 45, 22, 6, 6);
        g.setColor(new Color(203, 213, 225));
        g.fillRoundRect(50, 144, 20, 14, 4, 4); // Metal head
        g.setColor(new Color(16, 185, 129));
        g.setFont(new Font("Segoe UI", Font.BOLD, 9));
        g.drawString(plugFrom, 73, 155);

        // Right Connector
        g.setColor(new Color(15, 23, 42));
        g.fillRoundRect(285, 140, 45, 22, 6, 6);
        g.setColor(new Color(203, 213, 225));
        g.fillRoundRect(330, 144, 20, 14, 4, 4); // Metal head
        g.setColor(new Color(16, 185, 129));
        g.drawString(plugTo, 288, 155);

        drawBrandBadge(g, brand, "100W / 40Gbps");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderAudio(String filename, String brand, String model, String type, Color bodyColor) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        int cx = WIDTH / 2;
        int cy = HEIGHT / 2;

        if ("headphones".equalsIgnoreCase(type)) {
            // Headband
            g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(bodyColor);
            g.drawArc(cx - 75, cy - 85, 150, 130, 0, 180);

            // Earcups
            g.setStroke(new BasicStroke(1));
            // Left Cup
            g.setColor(bodyColor.darker());
            g.fillRoundRect(cx - 85, cy - 25, 34, 75, 16, 16);
            g.setColor(new Color(203, 213, 225));
            g.drawRoundRect(cx - 85, cy - 25, 34, 75, 16, 16);
            // Left Cushion
            g.setColor(new Color(30, 41, 59));
            g.fillRoundRect(cx - 68, cy - 20, 14, 65, 8, 8);

            // Right Cup
            g.setColor(bodyColor.darker());
            g.fillRoundRect(cx + 51, cy - 25, 34, 75, 16, 16);
            g.setColor(new Color(203, 213, 225));
            g.drawRoundRect(cx + 51, cy - 25, 34, 75, 16, 16);
            // Right Cushion
            g.setColor(new Color(30, 41, 59));
            g.fillRoundRect(cx + 54, cy - 20, 14, 65, 8, 8);
        } else if ("earbuds".equalsIgnoreCase(type)) {
            // Charging Case
            g.setColor(bodyColor);
            g.fillRoundRect(cx - 50, cy - 20, 100, 75, 26, 26);
            g.setColor(new Color(203, 213, 225));
            g.drawRoundRect(cx - 50, cy - 20, 100, 75, 26, 26);
            // Case Lid Cut
            g.setColor(new Color(148, 163, 184));
            g.drawLine(cx - 50, cy + 5, cx + 50, cy + 5);
            // LED
            g.setColor(new Color(16, 185, 129));
            g.fillOval(cx - 3, cy + 25, 6, 6);

            // Left Earbud Outside
            g.setColor(bodyColor);
            g.fillOval(cx - 80, cy - 45, 26, 26);
            g.fillRoundRect(cx - 75, cy - 30, 8, 30, 4, 4);

            // Right Earbud Outside
            g.setColor(bodyColor);
            g.fillOval(cx + 54, cy - 45, 26, 26);
            g.fillRoundRect(cx + 67, cy - 30, 8, 30, 4, 4);
        } else { // Speaker
            g.setColor(bodyColor);
            g.fillRoundRect(cx - 75, cy - 35, 150, 70, 24, 24);
            g.setColor(new Color(203, 213, 225));
            g.drawRoundRect(cx - 75, cy - 35, 150, 70, 24, 24);

            // Grill texture
            g.setColor(new Color(255, 255, 255, 40));
            for (int x = cx - 55; x < cx + 55; x += 10) {
                g.drawLine(x, cy - 25, x, cy + 25);
            }
            // Brand badge on speaker
            g.setColor(new Color(239, 68, 68));
            g.fillRoundRect(cx - 20, cy - 10, 40, 20, 4, 4);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g.drawString(brand, cx - 14, cy + 4);
        }

        drawBrandBadge(g, brand, "HI-RES AUDIO");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderCase(String filename, String brand, String model, Color caseColor, boolean magsafe) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        int cx = WIDTH / 2;
        int cy = HEIGHT / 2;
        int w = 115, h = 215, r = 24;

        // Case Outer
        g.setColor(caseColor);
        g.fillRoundRect(cx - w / 2, cy - h / 2, w, h, r, r);
        g.setColor(new Color(203, 213, 225));
        g.drawRoundRect(cx - w / 2, cy - h / 2, w, h, r, r);

        // Camera Cutout (Top Left)
        g.setColor(new Color(15, 23, 42));
        g.fillRoundRect(cx - w / 2 + 10, cy - h / 2 + 10, 42, 45, 12, 12);
        g.setColor(new Color(148, 163, 184));
        g.drawRoundRect(cx - w / 2 + 10, cy - h / 2 + 10, 42, 45, 12, 12);

        // MagSafe Ring
        if (magsafe) {
            g.setStroke(new BasicStroke(3));
            g.setColor(new Color(255, 255, 255, 120));
            g.drawOval(cx - 30, cy - 15, 60, 60);
            g.drawRoundRect(cx - 3, cy + 48, 6, 16, 3, 3);
            g.setStroke(new BasicStroke(1));
        }

        // Texture / Carbon pattern or clear sheen
        g.setColor(new Color(255, 255, 255, 25));
        g.fillRoundRect(cx - w / 2 + 6, cy - h / 2 + 6, w - 12, h - 12, r - 4, r - 4);

        drawBrandBadge(g, brand, "PROTECTIVE");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderMonitor(String filename, String brand, String model, boolean curved) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        int cx = WIDTH / 2;

        // Stand Base
        g.setColor(new Color(71, 85, 105));
        Polygon base = new Polygon();
        base.addPoint(cx - 65, 255);
        base.addPoint(cx + 65, 255);
        base.addPoint(cx + 50, 240);
        base.addPoint(cx - 50, 240);
        g.fillPolygon(base);

        // Stand Neck
        g.fillRect(cx - 8, 175, 16, 68);
        g.setColor(new Color(148, 163, 184));
        g.drawRect(cx - 8, 175, 16, 68);

        // Screen Frame
        g.setColor(new Color(15, 23, 42));
        g.fillRoundRect(55, 48, 290, 145, curved ? 16 : 6, curved ? 16 : 6);
        g.setColor(new Color(100, 116, 139));
        g.drawRoundRect(55, 48, 290, 145, curved ? 16 : 6, curved ? 16 : 6);

        // Display Area
        GradientPaint screenGlow = new GradientPaint(58, 51, new Color(14, 165, 233), 342, 188, new Color(99, 102, 241));
        g.setPaint(screenGlow);
        g.fillRoundRect(58, 51, 284, 139, 4, 4);

        // Monitor UI
        g.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g.setColor(Color.WHITE);
        g.drawString(model, 75, 80);

        g.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g.drawString(curved ? "1000R CURVED | 240Hz" : "4K UHD | 144Hz IPS", 75, 100);

        drawBrandBadge(g, brand, curved ? "CURVED 240Hz" : "4K PRO");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderSmartHome(String filename, String brand, String model, String type) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        int cx = WIDTH / 2;
        int cy = HEIGHT / 2;

        if ("cam".equalsIgnoreCase(type)) {
            // Security Camera (Dome / Stand)
            g.setColor(new Color(241, 245, 249));
            g.fillRoundRect(cx - 30, cy + 10, 60, 45, 10, 10);
            g.fillOval(cx - 45, cy - 50, 90, 90);
            g.setColor(new Color(203, 213, 225));
            g.drawOval(cx - 45, cy - 50, 90, 90);

            // Lens Orb
            g.setColor(new Color(15, 23, 42));
            g.fillOval(cx - 32, cy - 38, 64, 64);
            // Glass aperture
            g.setColor(new Color(56, 189, 248));
            g.fillOval(cx - 14, cy - 20, 28, 28);
            // Status LED
            g.setColor(new Color(16, 185, 129));
            g.fillOval(cx - 3, cy - 30, 6, 6);
        } else if ("display".equalsIgnoreCase(type)) {
            // Smart Display (Nest Hub / Echo Show)
            g.setColor(new Color(71, 85, 105));
            g.fillRoundRect(cx - 70, cy - 45, 140, 95, 14, 14);
            // Screen
            g.setColor(new Color(15, 23, 42));
            g.fillRoundRect(cx - 62, cy - 37, 124, 79, 8, 8);
            // UI
            g.setColor(new Color(56, 189, 248));
            g.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g.drawString("24?C Clear", cx - 45, cy - 5);
            g.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g.setColor(new Color(203, 213, 225));
            g.drawString("Living Room Hub", cx - 45, cy + 18);
        } else if ("bulb".equalsIgnoreCase(type)) {
            // Smart Bulb
            g.setPaint(new RadialGradientPaint(new Point2D.Float(cx, cy - 20), 50.0f, new float[]{0f, 1f}, new Color[]{new Color(254, 240, 138), new Color(245, 158, 11)}));
            g.fillOval(cx - 40, cy - 60, 80, 80);
            // Base
            g.setColor(new Color(203, 213, 225));
            g.fillRect(cx - 20, cy + 15, 40, 25);
            for (int y = cy + 18; y <= cy + 36; y += 6) {
                g.setColor(new Color(148, 163, 184));
                g.drawLine(cx - 20, y, cx + 20, y);
            }
        } else { // Appliance / Purifier / Lock
            g.setColor(new Color(248, 250, 252));
            g.fillRoundRect(cx - 45, cy - 65, 90, 130, 16, 16);
            g.setColor(new Color(203, 213, 225));
            g.drawRoundRect(cx - 45, cy - 65, 90, 130, 16, 16);
            // Digital OLED ring
            g.setColor(new Color(15, 23, 42));
            g.fillOval(cx - 20, cy - 45, 40, 40);
            g.setColor(new Color(16, 185, 129));
            g.drawOval(cx - 18, cy - 43, 36, 36);
            g.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g.drawString("AQI 15", cx - 14, cy - 21);
            // Air intake slats
            for (int y = cy - 5; y < cy + 50; y += 8) {
                g.setColor(new Color(203, 213, 225));
                g.drawLine(cx - 35, y, cx + 35, y);
            }
        }

        drawBrandBadge(g, brand, "SMART HOME");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderDeskSetup(String filename, String brand, String model, String type) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        int cx = WIDTH / 2;
        int cy = HEIGHT / 2;

        if ("streamdeck".equalsIgnoreCase(type)) {
            // Stream Deck Controller
            g.setColor(new Color(30, 41, 59));
            g.fillRoundRect(cx - 75, cy - 55, 150, 110, 16, 16);
            g.setColor(new Color(100, 116, 139));
            g.drawRoundRect(cx - 75, cy - 55, 150, 110, 16, 16);

            // 15 LCD Keys Grid (3 x 5)
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 5; c++) {
                    int kx = cx - 65 + c * 26;
                    int ky = cy - 45 + r * 28;
                    g.setColor(new Color(15, 23, 42));
                    g.fillRoundRect(kx, ky, 22, 22, 4, 4);
                    // Glowing icon color
                    Color keyCol = ((r + c) % 2 == 0) ? new Color(16, 185, 129) : new Color(56, 189, 248);
                    g.setColor(keyCol);
                    g.drawRoundRect(kx + 3, ky + 3, 16, 16, 3, 3);
                }
            }
        } else if ("mic".equalsIgnoreCase(type)) {
            // Broadcast Microphone
            // Stand
            g.setColor(new Color(71, 85, 105));
            g.fillOval(cx - 40, cy + 45, 80, 20);
            g.fillRect(cx - 5, cy + 10, 10, 40);

            // Mic Body
            g.setColor(new Color(15, 23, 42));
            g.fillRoundRect(cx - 26, cy - 65, 52, 90, 22, 22);
            g.setColor(new Color(148, 163, 184));
            g.drawRoundRect(cx - 26, cy - 65, 52, 90, 22, 22);

            // Mesh grill
            g.setColor(new Color(239, 68, 68)); // RGB ring
            g.fillRoundRect(cx - 22, cy - 60, 44, 38, 12, 12);
        } else if ("light".equalsIgnoreCase(type)) {
            // LED Light Bar / Panel
            g.setColor(new Color(15, 23, 42));
            g.fillRoundRect(cx - 90, cy - 25, 180, 35, 8, 8);
            // Glowing LED strip
            g.setColor(new Color(254, 240, 138));
            g.fillRoundRect(cx - 85, cy - 18, 170, 21, 4, 4);
        } else { // Webcam
            g.setColor(new Color(30, 41, 59));
            g.fillOval(cx - 35, cy - 45, 70, 70);
            g.setColor(new Color(15, 23, 42));
            g.fillOval(cx - 24, cy - 34, 48, 48);
            g.setColor(new Color(56, 189, 248));
            g.fillOval(cx - 10, cy - 20, 20, 20);
            // Stand mount
            g.setColor(new Color(71, 85, 105));
            g.fillRoundRect(cx - 20, cy + 25, 40, 25, 4, 4);
        }

        drawBrandBadge(g, brand, "STUDIO PRO");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderGaming(String filename, String brand, String model, String type) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        int cx = WIDTH / 2;
        int cy = HEIGHT / 2;

        if ("controller".equalsIgnoreCase(type)) {
            // PS5 / Xbox Controller Form
            g.setColor(new Color(248, 250, 252));
            Polygon ctrl = new Polygon();
            ctrl.addPoint(cx - 75, cy - 35);
            ctrl.addPoint(cx + 75, cy - 35);
            ctrl.addPoint(cx + 90, cy + 45);
            ctrl.addPoint(cx + 55, cy + 60);
            ctrl.addPoint(cx + 25, cy + 15);
            ctrl.addPoint(cx - 25, cy + 15);
            ctrl.addPoint(cx - 55, cy + 60);
            ctrl.addPoint(cx - 90, cy + 45);
            g.fillPolygon(ctrl);
            g.setColor(new Color(203, 213, 225));
            g.drawPolygon(ctrl);

            // Center Touchpad / Logo
            g.setColor(new Color(30, 41, 59));
            g.fillRoundRect(cx - 28, cy - 30, 56, 32, 6, 6);
            // Thumbsticks
            g.setColor(new Color(51, 65, 85));
            g.fillOval(cx - 38, cy + 5, 24, 24);
            g.fillOval(cx + 14, cy + 5, 24, 24);
            // Action buttons
            g.setColor(new Color(56, 189, 248));
            g.fillOval(cx + 48, cy - 22, 8, 8);
            g.fillOval(cx + 60, cy - 12, 8, 8);
            g.fillOval(cx + 48, cy - 2, 8, 8);
            g.fillOval(cx + 36, cy - 12, 8, 8);
        } else if ("mouse".equalsIgnoreCase(type)) {
            // Esports Mouse
            g.setColor(new Color(15, 23, 42));
            g.fillRoundRect(cx - 35, cy - 65, 70, 130, 32, 32);
            g.setColor(new Color(71, 85, 105));
            g.drawRoundRect(cx - 35, cy - 65, 70, 130, 32, 32);

            // Scroll Wheel
            g.setColor(new Color(56, 189, 248));
            g.fillRoundRect(cx - 5, cy - 45, 10, 24, 4, 4);

            // RGB Line
            g.setColor(new Color(239, 68, 68));
            g.drawLine(cx, cy - 20, cx, cy + 20);
        } else if ("keyboard".equalsIgnoreCase(type)) {
            // Mechanical Keyboard
            g.setColor(new Color(30, 41, 59));
            g.fillRoundRect(cx - 95, cy - 45, 190, 85, 10, 10);
            g.setColor(new Color(100, 116, 139));
            g.drawRoundRect(cx - 95, cy - 45, 190, 85, 10, 10);

            // RGB key rows
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 12; c++) {
                    int kx = cx - 88 + c * 15;
                    int ky = cy - 38 + r * 18;
                    g.setColor(new Color(15, 23, 42));
                    g.fillRoundRect(kx, ky, 12, 14, 2, 2);
                    Color rgb = ((r + c) % 3 == 0) ? new Color(239, 68, 68) : (((r + c) % 3 == 1) ? new Color(16, 185, 129) : new Color(56, 189, 248));
                    g.setColor(rgb);
                    g.fillRect(kx + 2, ky + 11, 8, 2);
                }
            }
        } else { // Power Supply
            g.setColor(new Color(15, 23, 42));
            g.fillRoundRect(cx - 65, cy - 55, 130, 110, 10, 10);
            g.setColor(new Color(71, 85, 105));
            g.drawRoundRect(cx - 65, cy - 55, 130, 110, 10, 10);
            // Fan grill
            g.setColor(new Color(245, 158, 11));
            g.drawOval(cx - 40, cy - 35, 80, 80);
            g.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g.drawString("850W GOLD", cx - 35, cy + 8);
        }

        drawBrandBadge(g, brand, "ESPORTS READY");
        g.dispose();
        saveImage(img, filename);
    }

    public static void renderPower(String filename, String brand, String model, String type) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = setupCanvas(img);

        int cx = WIDTH / 2;
        int cy = HEIGHT / 2;

        if ("ups".equalsIgnoreCase(type)) {
            // Tower UPS
            g.setColor(new Color(15, 23, 42));
            g.fillRoundRect(cx - 45, cy - 75, 90, 150, 12, 12);
            g.setColor(new Color(71, 85, 105));
            g.drawRoundRect(cx - 45, cy - 75, 90, 150, 12, 12);

            // LCD Status Display
            g.setColor(new Color(8, 47, 73));
            g.fillRoundRect(cx - 32, cy - 60, 64, 40, 6, 6);
            g.setColor(new Color(14, 165, 233));
            g.drawRoundRect(cx - 32, cy - 60, 64, 40, 6, 6);

            g.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g.setColor(new Color(56, 189, 248));
            g.drawString("230V IN", cx - 22, cy - 45);
            g.setColor(new Color(16, 185, 129));
            g.drawString("100% BAT", cx - 24, cy - 30);

            // Power button with green halo
            g.setColor(new Color(30, 41, 59));
            g.fillOval(cx - 12, cy - 5, 24, 24);
            g.setColor(new Color(16, 185, 129));
            g.drawOval(cx - 12, cy - 5, 24, 24);

            // Cooling vents
            for (int y = cy + 30; y < cy + 65; y += 7) {
                g.setColor(new Color(51, 65, 85));
                g.drawLine(cx - 30, y, cx + 30, y);
            }
        } else if ("stabilizer".equalsIgnoreCase(type)) {
            // Stabilizer
            g.setColor(new Color(241, 245, 249));
            g.fillRoundRect(cx - 60, cy - 50, 120, 100, 12, 12);
            g.setColor(new Color(203, 213, 225));
            g.drawRoundRect(cx - 60, cy - 50, 120, 100, 12, 12);

            // Red LED voltage meter
            g.setColor(new Color(15, 23, 42));
            g.fillRoundRect(cx - 30, cy - 35, 60, 30, 4, 4);
            g.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g.setColor(new Color(239, 68, 68));
            g.drawString("220V", cx - 18, cy - 14);

            // Green OK Indicator
            g.setColor(new Color(16, 185, 129));
            g.fillOval(cx - 6, cy + 12, 12, 12);
        } else if ("strip".equalsIgnoreCase(type)) {
            // Multi-Plug Spike Protector
            g.setColor(new Color(248, 250, 252));
            g.fillRoundRect(cx - 85, cy - 28, 170, 56, 10, 10);
            g.setColor(new Color(203, 213, 225));
            g.drawRoundRect(cx - 85, cy - 28, 170, 56, 10, 10);

            // Master switch
            g.setColor(new Color(239, 68, 68));
            g.fillRect(cx - 75, cy - 14, 14, 28);

            // Sockets
            for (int x = cx - 45; x <= cx + 55; x += 32) {
                g.setColor(new Color(226, 232, 240));
                g.fillOval(x - 10, cy - 10, 20, 20);
                g.setColor(new Color(15, 23, 42));
                g.fillOval(x - 5, cy - 6, 3, 3);
                g.fillOval(x + 2, cy - 6, 3, 3);
                g.fillOval(x - 2, cy + 1, 4, 4);
            }
        } else { // Pad
            g.setColor(new Color(241, 245, 249));
            g.fillOval(cx - 55, cy - 55, 110, 110);
            g.setColor(new Color(203, 213, 225));
            g.drawOval(cx - 55, cy - 55, 110, 110);
            // MagSafe magnetic ring
            g.setStroke(new BasicStroke(3));
            g.setColor(new Color(148, 163, 184));
            g.drawOval(cx - 30, cy - 30, 60, 60);
            g.setStroke(new BasicStroke(1));
        }

        drawBrandBadge(g, brand, "POWER SURGE PRO");
        g.dispose();
        saveImage(img, filename);
    }

    // -------------------------------------------------------------
    // GENERATE ALL 175 REAL PRODUCT IMAGES
    // -------------------------------------------------------------

    public static void generateAll175ProductImages() {
        // --- Existing Products 1 to 45 ---
        // Laptops (1-4)
        renderLaptop("laptop-pro15.png", "Apple", "MacBook Air 15 M3", new Color(203, 213, 225), new Color(14, 165, 233), false);
        renderLaptop("laptop-gaming16.png", "ASUS ROG", "Zephyrus G16", new Color(15, 23, 42), new Color(239, 68, 68), true);
        renderLaptop("laptop-air13.png", "Apple", "MacBook Air 13 M2", new Color(241, 245, 249), new Color(59, 130, 246), false);
        renderLaptop("laptop-legion.png", "Lenovo", "Legion Pro 5i", new Color(30, 41, 59), new Color(16, 185, 129), true);

        // Phones (5-8)
        renderSmartphone("phone-15pro.png", "Apple", "iPhone 15 Pro", new Color(71, 85, 105), "pill");
        renderSmartphone("phone-15.png", "Apple", "iPhone 15", new Color(147, 197, 253), "pill");
        renderSmartphone("phone-s24.png", "Samsung", "Galaxy S24", new Color(254, 240, 138), "dot");
        renderSmartphone("phone-oneplus.png", "OnePlus", "OnePlus 12", new Color(6, 78, 59), "dot");

        // Tablets (9-12)
        renderTablet("tablet-ipadair.png", "Apple", "iPad Air 11 M2", new Color(199, 210, 254), true);
        renderTablet("tablet-tabs9fe.png", "Samsung", "Galaxy Tab S9 FE", new Color(226, 232, 240), true);
        renderTablet("tablet-ipad10.png", "Apple", "iPad 10th Gen", new Color(254, 202, 202), true);
        renderTablet("tablet-xiaomi.png", "Xiaomi", "Pad 6", new Color(203, 213, 225), false);

        // Wearables (13-14)
        renderWearable("watch-ultra2.png", "Apple", "Watch Ultra 2", "square", new Color(249, 115, 22));
        renderWearable("watch-galaxy6.png", "Samsung", "Galaxy Watch 6 Classic", "round", new Color(30, 41, 59));

        // Chargers (15-18)
        renderCharger("charger-65w.png", "Anker", "735 GaN 65W", "65W", new Color(15, 23, 42));
        renderCharger("charger-100w.png", "Anker", "747 GaN 150W", "150W", new Color(15, 23, 42));
        renderCharger("charger-20w.png", "Apple", "20W USB-C Adapter", "20W", new Color(248, 250, 252));
        renderCharger("charger-30w.png", "Belkin", "BoostCharge 30W", "30W", new Color(248, 250, 252));

        // Cables (19-22)
        renderCable("cable-usbc.png", "Anker", "543 USB-C 100W", "Type-C", "Type-C");
        renderCable("cable-tb4.png", "Apple", "Thunderbolt 4 Pro", "TB4", "TB4");
        renderCable("cable-lightning.png", "Belkin", "USB-C to Lightning", "Type-C", "Lightning");
        renderCable("cable-hdmi.png", "Belkin", "Ultra High Speed HDMI 2.1", "HDMI", "HDMI");

        // Audio (23-26)
        renderAudio("audio-headphones.png", "Sony", "WH-1000XM5", "headphones", new Color(30, 41, 59));
        renderAudio("audio-earbuds.png", "Apple", "AirPods Pro 2", "earbuds", new Color(248, 250, 252));
        renderAudio("audio-wired.png", "Sennheiser", "IE 200 Monitors", "headphones", new Color(15, 23, 42));
        renderAudio("adapter-dac.png", "Apple", "USB-C to 3.5mm DAC", "earbuds", new Color(248, 250, 252));

        // Cases (27-30)
        renderCase("case-15pro.png", "Spigen", "Ultra Hybrid MagFit", new Color(15, 23, 42), true);
        renderCase("case-15promax.png", "Spigen", "Liquid Air Armor", new Color(30, 41, 59), false);
        renderCase("case-15clear.png", "ESR", "Classic Hybrid Clear", new Color(241, 245, 249), true);
        renderCase("case-s24.png", "Spigen", "Tough Armor S24", new Color(15, 23, 42), false);

        // Monitors (31-33)
        renderMonitor("monitor-lg.png", "LG", "UltraGear 27 4K 144Hz", false);
        renderMonitor("monitor-dell.png", "Dell", "UltraSharp 32 4K USB-C", false);
        renderMonitor("monitor-asus.png", "ASUS ProArt", "27 1440p Creator", false);

        // Smart Home (34-36)
        renderSmartHome("doorbell-smart.png", "Google Nest", "Cam Battery Security", "cam");
        renderSmartHome("speaker-echo.png", "Amazon", "Echo Dot 5th Gen", "display");
        renderSmartHome("light-hue.png", "Philips Hue", "Smart Wi-Fi Starter Kit", "bulb");

        // Desk Setup (37-40)
        renderDeskSetup("mouse-mxmaster.png", "Logitech", "MX Master 3S", "mouse");
        renderDeskSetup("keyboard-mech.png", "Keychron", "Q1 Pro Wireless", "streamdeck");
        renderDeskSetup("light-bar.png", "BenQ", "ScreenBar Plus Monitor Light", "light");
        renderDeskSetup("deskmat-xl.png", "Grovemade", "Felt Large Desk Mat", "light");

        // Gaming (41-43)
        renderGaming("controller-wireless.png", "Sony", "DualSense PS5 Wireless", "controller");
        renderGaming("keyboard-gaming.png", "SteelSeries", "Apex Pro TKL", "keyboard");
        renderGaming("mouse-gaming.png", "Logitech G", "G502 X PLUS Wireless", "mouse");

        // Power (44-45)
        renderPower("ups-1500va.png", "APC", "Back-UPS Pro 1500VA Sine Wave", "ups");
        renderPower("magsafe-stand.png", "ESR", "3-in-1 Foldable MagSafe Stand", "pad");

        // ---------------------------------------------------------
        // SECTION 1: 10 NEW LAPTOPS (46 to 55)
        // ---------------------------------------------------------
        renderLaptop("prod-46.png", "Dell", "XPS 15 (i7, 16GB, RTX 4050)", new Color(226, 232, 240), new Color(14, 165, 233), false);
        renderLaptop("prod-47.png", "HP", "Spectre x360 2-in-1 OLED", new Color(15, 23, 42), new Color(245, 158, 11), false);
        renderLaptop("prod-48.png", "Lenovo", "ThinkPad X1 Carbon Gen 12", new Color(15, 23, 42), new Color(239, 68, 68), false);
        renderLaptop("prod-49.png", "Acer", "Predator Helios 16 RTX 4070", new Color(30, 41, 59), new Color(59, 130, 246), true);
        renderLaptop("prod-50.png", "ASUS", "ZenBook 14 OLED Ultra 5", new Color(71, 85, 105), new Color(147, 51, 234), false);
        renderLaptop("prod-51.png", "MSI", "Katana 15 Gaming RTX 4060", new Color(15, 23, 42), new Color(239, 68, 68), true);
        renderLaptop("prod-52.png", "Samsung", "Galaxy Book4 Pro 360", new Color(203, 213, 225), new Color(16, 185, 129), false);
        renderLaptop("prod-53.png", "LG", "Gram 17 Ultra-Lightweight", new Color(241, 245, 249), new Color(56, 189, 248), false);
        renderLaptop("prod-54.png", "Microsoft", "Surface Laptop 6 Platinum", new Color(226, 232, 240), new Color(99, 102, 241), false);
        renderLaptop("prod-55.png", "Razer", "Blade 16 Dual Mini-LED RTX 4090", new Color(15, 23, 42), new Color(34, 197, 94), true);

        // ---------------------------------------------------------
        // SECTION 2: 10 NEW SMARTPHONES (56 to 65)
        // ---------------------------------------------------------
        renderSmartphone("prod-56.png", "Samsung", "Galaxy S24 Ultra Titanium", new Color(71, 85, 105), "dot");
        renderSmartphone("prod-57.png", "Google", "Pixel 8 Pro Bay Blue", new Color(147, 197, 253), "pill");
        renderSmartphone("prod-58.png", "Nothing", "Phone (2) Dark Grey Glyph", new Color(30, 41, 59), "dot");
        renderSmartphone("prod-59.png", "Vivo", "X100 Pro Zeiss 1-inch", new Color(15, 23, 42), "dot");
        renderSmartphone("prod-60.png", "Motorola", "Edge 50 Ultra Wood Back", new Color(180, 83, 9), "dot");
        renderSmartphone("prod-61.png", "Realme", "GT 6 6000nits 120W", new Color(203, 213, 225), "dot");
        renderSmartphone("prod-62.png", "iQOO", "12 5G Legend Edition", new Color(248, 250, 252), "dot");
        renderSmartphone("prod-63.png", "Samsung", "Galaxy Z Flip 5 Foldable", new Color(233, 213, 255), "dot");
        renderSmartphone("prod-64.png", "OnePlus", "Open Emerald Dusk Foldable", new Color(6, 78, 59), "dot");
        renderSmartphone("prod-65.png", "Sony", "Xperia 1 V 4K OLED", new Color(15, 23, 42), "dot");

        // ---------------------------------------------------------
        // SECTION 3: 10 NEW TABLETS (66 to 75)
        // ---------------------------------------------------------
        renderTablet("prod-66.png", "Apple", "iPad Pro 13 M4 Ultra Retina", new Color(15, 23, 42), true);
        renderTablet("prod-67.png", "Samsung", "Galaxy Tab S9 Ultra 14.6", new Color(51, 65, 85), true);
        renderTablet("prod-68.png", "OnePlus", "Pad 2 3K 144Hz", new Color(71, 85, 105), true);
        renderTablet("prod-69.png", "Xiaomi", "Pad 6 Pro Snapdragon 8+", new Color(203, 213, 225), true);
        renderTablet("prod-70.png", "Lenovo", "Tab P12 3K Quad JBL", new Color(100, 116, 139), true);
        renderTablet("prod-71.png", "Microsoft", "Surface Pro 10 Platinum", new Color(226, 232, 240), true);
        renderTablet("prod-72.png", "Realme", "Pad 2 11.5 2K 120Hz", new Color(203, 213, 225), false);
        renderTablet("prod-73.png", "Honor", "Pad 9 12.1 8 Speakers", new Color(241, 245, 249), false);
        renderTablet("prod-74.png", "Apple", "iPad mini 6 A15 Bionic", new Color(254, 202, 202), true);
        renderTablet("prod-75.png", "OnePlus", "Stylo 2 Active Stylus", new Color(15, 23, 42), true);

        // ---------------------------------------------------------
        // SECTION 4: 10 NEW WEARABLES (76 to 85)
        // ---------------------------------------------------------
        renderWearable("prod-76.png", "Garmin", "Fenix 7 Pro Solar Sapphire", "round", new Color(30, 41, 59));
        renderWearable("prod-77.png", "OnePlus", "Watch 2 Wear OS 4", "round", new Color(71, 85, 105));
        renderWearable("prod-78.png", "Amazfit", "Balance AI AMOLED", "round", new Color(148, 163, 184));
        renderWearable("prod-79.png", "Fitbit", "Charge 6 Google ECG", "square", new Color(239, 68, 68));
        renderWearable("prod-80.png", "Noise", "ColorFit Pro 5 Max 1.96", "square", new Color(15, 23, 42));
        renderWearable("prod-81.png", "boAt", "Lunar Tigre Rugged Metal", "round", new Color(249, 115, 22));
        renderWearable("prod-82.png", "Fire-Boltt", "Invincible Plus 4GB AMOLED", "round", new Color(51, 65, 85));
        renderWearable("prod-83.png", "Fastrack", "Limitless FS1 Pro Arched", "square", new Color(59, 130, 246));
        renderWearable("prod-84.png", "Garmin", "Forerunner 265 Running", "round", new Color(14, 165, 233));
        renderWearable("prod-85.png", "Apple", "Watch Series 9 45mm", "square", new Color(239, 68, 68));

        // ---------------------------------------------------------
        // SECTION 5: 10 NEW CHARGERS (86 to 95)
        // ---------------------------------------------------------
        renderCharger("prod-86.png", "UGREEN", "Nexode 100W 4-Port GaN", "100W", new Color(15, 23, 42));
        renderCharger("prod-87.png", "Samsung", "45W Fast Power Adapter", "45W", new Color(248, 250, 252));
        renderCharger("prod-88.png", "Belkin", "BoostCharge Pro 65W GaN", "65W", new Color(248, 250, 252));
        renderCharger("prod-89.png", "Stuffcool", "Neutron 33W Mini GaN", "33W", new Color(15, 23, 42));
        renderCharger("prod-90.png", "Portronics", "Adapto 65W Multi-Port", "65W", new Color(248, 250, 252));
        renderCharger("prod-91.png", "Baseus", "Blade 100W Ultra-Slim Bank", "100W", new Color(15, 23, 42));
        renderCharger("prod-92.png", "Spigen", "ArcStation Pro 45W Dual", "45W", new Color(30, 41, 59));
        renderCharger("prod-93.png", "Xiaomi", "67W SonicCharge 3.0", "67W", new Color(248, 250, 252));
        renderCharger("prod-94.png", "Ambrane", "PowerLit Ultra 100W 25000", "100W", new Color(15, 23, 42));
        renderCharger("prod-95.png", "UGREEN", "Nexode 140W PD 3.1 GaN", "140W", new Color(30, 41, 59));

        // ---------------------------------------------------------
        // SECTION 6: 10 NEW CABLES (96 to 105)
        // ---------------------------------------------------------
        renderCable("prod-96.png", "Anker", "PowerLine III Flow 100W", "Type-C", "Type-C");
        renderCable("prod-97.png", "UGREEN", "Thunderbolt 4 40Gbps 8K", "TB4", "TB4");
        renderCable("prod-98.png", "Belkin", "BoostCharge Flex Braided", "Type-C", "Lightning");
        renderCable("prod-99.png", "Baseus", "8-in-1 Hub 4K HDMI 100W", "Type-C", "Hub");
        renderCable("prod-100.png", "Satechi", "Type-C Multi-Port 4K Gigabit", "Type-C", "Hub");
        renderCable("prod-101.png", "Amazon Basics", "DisplayPort 1.4 8K Cable", "DP", "DP");
        renderCable("prod-102.png", "Portronics", "Konnect CL 20W Fast Cable", "Type-C", "Lightning");
        renderCable("prod-103.png", "boAt", "Rugged v3 65W Tough Cable", "Type-C", "Type-C");
        renderCable("prod-104.png", "UGREEN", "3.5mm Aux Braided Audio", "3.5mm", "3.5mm");
        renderCable("prod-105.png", "Anker", "518 USB-C to 4K HDMI", "Type-C", "HDMI");

        // ---------------------------------------------------------
        // SECTION 7: 10 NEW AUDIO (106 to 115)
        // ---------------------------------------------------------
        renderAudio("prod-106.png", "Bose", "QuietComfort Ultra ANC", "headphones", new Color(15, 23, 42));
        renderAudio("prod-107.png", "Sennheiser", "Momentum 4 Wireless 60h", "headphones", new Color(30, 41, 59));
        renderAudio("prod-108.png", "JBL", "Tour ONE M2 Adaptive ANC", "headphones", new Color(15, 23, 42));
        renderAudio("prod-109.png", "Marshall", "Major IV On-Ear 80h", "headphones", new Color(20, 20, 20));
        renderAudio("prod-110.png", "Audio-Technica", "ATH-M50xBT2 Studio", "headphones", new Color(15, 23, 42));
        renderAudio("prod-111.png", "Nothing", "Ear (a) Hi-Res Smart ANC", "earbuds", new Color(254, 240, 138));
        renderAudio("prod-112.png", "Bose", "QC Ultra Immersive Earbuds", "earbuds", new Color(15, 23, 42));
        renderAudio("prod-113.png", "JBL", "Flip 6 Waterproof 30W", "speaker", new Color(239, 68, 68));
        renderAudio("prod-114.png", "boAt", "Nirvana Ion ANC 120h", "earbuds", new Color(15, 23, 42));
        renderAudio("prod-115.png", "Skullcandy", "Crusher ANC 2 Sensory Bass", "headphones", new Color(15, 23, 42));

        // ---------------------------------------------------------
        // SECTION 8: 10 NEW CASES (116 to 125)
        // ---------------------------------------------------------
        renderCase("prod-116.png", "Ringke", "Fusion-X S24 Ultra Bumper", new Color(30, 41, 59), false);
        renderCase("prod-117.png", "Caseology", "Parallax MagSafe 15 Pro", new Color(51, 65, 85), true);
        renderCase("prod-118.png", "OtterBox", "Defender Heavy Duty iPhone 15", new Color(15, 23, 42), false);
        renderCase("prod-119.png", "UAG", "Monarch Kevlar S24 Ultra", new Color(20, 20, 20), true);
        renderCase("prod-120.png", "Nillkin", "CamShield Pro Slide 15 Pro", new Color(30, 41, 59), false);
        renderCase("prod-121.png", "Torras", "360 Spin Stand MagSafe", new Color(15, 23, 42), true);
        renderCase("prod-122.png", "ESR", "Classic Hybrid Stash Stand", new Color(241, 245, 249), true);
        renderCase("prod-123.png", "Dbrand", "Grip Case Pixel 8 Pro", new Color(15, 23, 42), false);
        renderCase("prod-124.png", "DailyObjects", "Vegan Leatherette MagSafe", new Color(180, 83, 9), true);
        renderCase("prod-125.png", "Spigen", "GlasTR EZ Fit Screen Glass", new Color(241, 245, 249), false);

        // ---------------------------------------------------------
        // SECTION 9: 10 NEW MONITORS (126 to 135)
        // ---------------------------------------------------------
        renderMonitor("prod-126.png", "Samsung", "Odyssey Neo G8 32 4K 240Hz", true);
        renderMonitor("prod-127.png", "Dell", "UltraSharp 27 4K U2723QE", false);
        renderMonitor("prod-128.png", "BenQ", "PD2705U 27 4K Designer KVM", false);
        renderMonitor("prod-129.png", "ASUS ROG", "Swift OLED 32 4K 240Hz", true);
        renderMonitor("prod-130.png", "Acer", "Predator XB283K 28 4K 144Hz", false);
        renderMonitor("prod-131.png", "MSI", "Optix 32 4K Quantum Dot", false);
        renderMonitor("prod-132.png", "Gigabyte", "M28U 28 4K 144Hz KVM", false);
        renderMonitor("prod-133.png", "ViewSonic", "ColorPro VP2768a 27 2K", false);
        renderMonitor("prod-134.png", "Philips", "Evnia 34 WQHD 165Hz Curved", true);
        renderMonitor("prod-135.png", "Samsung", "ViewFinity S9 27 5K SlimFit", false);

        // ---------------------------------------------------------
        // SECTION 10: 10 NEW SMART HOME (136 to 145)
        // ---------------------------------------------------------
        renderSmartHome("prod-136.png", "TP-Link", "Tapo C210 2K Pan/Tilt Cam", "cam");
        renderSmartHome("prod-137.png", "Amazon", "Echo Show 8 Smart Display", "display");
        renderSmartHome("prod-138.png", "Google", "Nest Hub 2nd Gen Sleep", "display");
        renderSmartHome("prod-139.png", "Philips Wiz", "Smart 9W LED 3-Pack", "bulb");
        renderSmartHome("prod-140.png", "Xiaomi", "Smart Air Purifier 4 OLED", "purifier");
        renderSmartHome("prod-141.png", "Atomberg", "Renesa BLDC Smart Fan", "purifier");
        renderSmartHome("prod-142.png", "Havells", "Crabtree Signia Smart Touch", "display");
        renderSmartHome("prod-143.png", "Qubo", "Smart Door Lock Ultra 5in1", "purifier");
        renderSmartHome("prod-144.png", "TP-Link", "Tapo P110 16A Smart Plug", "purifier");
        renderSmartHome("prod-145.png", "Wipro", "Smart 20W LED Batten Bar", "bulb");

        // ---------------------------------------------------------
        // SECTION 11: 10 NEW DESK SETUP (146 to 155)
        // ---------------------------------------------------------
        renderDeskSetup("prod-146.png", "Elgato", "Stream Deck MK.2 15 Keys", "streamdeck");
        renderDeskSetup("prod-147.png", "Rode", "NT-USB Mini Studio Mic", "mic");
        renderDeskSetup("prod-148.png", "HyperX", "QuadCast S RGB Condenser", "mic");
        renderDeskSetup("prod-149.png", "Elgato", "Facecam 1080p60 Uncompressed", "cam");
        renderDeskSetup("prod-150.png", "Shure", "MV7 USB/XLR Broadcast Mic", "mic");
        renderDeskSetup("prod-151.png", "Razer", "Kiyo Pro HDR Sensor Cam", "cam");
        renderDeskSetup("prod-152.png", "Elgato", "Key Light Air LED Panel", "light");
        renderDeskSetup("prod-153.png", "BenQ", "ScreenBar Halo Wireless LED", "light");
        renderDeskSetup("prod-154.png", "Audio-Technica", "AT2020USB-X Cardioid Mic", "mic");
        renderDeskSetup("prod-155.png", "Logitech", "Litra Glow Streaming Light", "light");

        // ---------------------------------------------------------
        // SECTION 12: 10 NEW GAMING (156 to 165)
        // ---------------------------------------------------------
        renderGaming("prod-156.png", "Sony", "DualSense Edge PS5 & PC", "controller");
        renderGaming("prod-157.png", "Razer", "DeathAdder V3 Pro Mouse", "mouse");
        renderGaming("prod-158.png", "Corsair", "K70 RGB PRO Cherry MX", "keyboard");
        renderGaming("prod-159.png", "Logitech G", "PRO X SUPERLIGHT 2 Mouse", "mouse");
        renderGaming("prod-160.png", "HyperX", "Cloud III Wireless 120h", "controller");
        renderGaming("prod-161.png", "ASUS ROG", "Falchion RX 65% Wireless", "keyboard");
        renderGaming("prod-162.png", "SteelSeries", "Arctis Nova Pro Wireless", "controller");
        renderGaming("prod-163.png", "Razer", "BlackShark V2 Pro Esports", "controller");
        renderGaming("prod-164.png", "Corsair", "RM850x 80 Plus Gold PSU", "psu");
        renderGaming("prod-165.png", "Redragon", "K552 Kumara RGB Keyboard", "keyboard");

        // ---------------------------------------------------------
        // SECTION 13: 10 NEW POWER (166 to 175)
        // ---------------------------------------------------------
        renderPower("prod-166.png", "APC", "Back-UPS BX600C 600VA Desktop", "ups");
        renderPower("prod-167.png", "APC", "Back-UPS Pro BR1000G Sine Wave", "ups");
        renderPower("prod-168.png", "V-Guard", "Crystal Plus TV Stabilizer", "stabilizer");
        renderPower("prod-169.png", "Microtek", "Legend 1000VA Heavy-Duty UPS", "ups");
        renderPower("prod-170.png", "Luminous", "Zelio+ 1100 Pure Sinewave", "ups");
        renderPower("prod-171.png", "Havells", "4-Way Heavy Duty Surge Strip", "strip");
        renderPower("prod-172.png", "Portronics", "Power Plate 7 6-Socket 4-USB", "strip");
        renderPower("prod-173.png", "Syska", "4-Way Power Strip Surge Master", "strip");
        renderPower("prod-174.png", "Zebronics", "Zeb-Iron 600VA Auto Restart", "ups");
        renderPower("prod-175.png", "Belkin", "BoostCharge Pro 3-in-1 MagSafe", "pad");
    }
}
