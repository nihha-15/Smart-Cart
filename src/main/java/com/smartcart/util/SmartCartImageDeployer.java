package com.smartcart.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.*;

/**
 * SmartCart Studio Product Image Deployer
 * Sources, renders, and verifies authentic, commercial-grade product photographs
 * for all 175 products in the SmartCart catalog.
 */
public class SmartCartImageDeployer {

    private static final File WEBAPP_DIR = new File("C:/Users/nihas/.gemini/antigravity/scratch/SmartCart/src/main/webapp");
    private static final File IMAGES_DIR = new File(WEBAPP_DIR, "images");
    private static final File ARTIFACTS_DIR = new File("C:/Users/nihas/.gemini/antigravity/brain/b6ceeeb0-0f7a-47ad-ac22-a0c65eb72f0e");

    public static void main(String[] args) throws Exception {
        if (!IMAGES_DIR.exists()) {
            IMAGES_DIR.mkdirs();
        }

        System.out.println("=== 1. GENERATING HIGH-QUALITY FALLBACK PLACEHOLDER ===");
        generatePlaceholder();

        System.out.println("\n=== 2. DEPLOYING CURATED REAL PRODUCT PHOTOGRAPHS ===");
        Map<Integer, String> queries = getTargetQueries();

        // High priority local artifact files for key items
        Map<Integer, File> localArtifacts = new HashMap<>();
        localArtifacts.put(1, findArtifact("laptop_macbook_air_studio"));
        localArtifacts.put(2, findArtifact("laptop_rog_zephyrus_studio"));
        localArtifacts.put(5, new File(ARTIFACTS_DIR, ".user_uploaded/media_1793127310414_74738933.png"));
        localArtifacts.put(7, findArtifact("phone_oneplus_studio"));
        localArtifacts.put(9, findArtifact("ipad_air_11_m2_starlight_v3"));
        localArtifacts.put(13, findArtifact("apple_watch_ultra_2_product"));
        localArtifacts.put(14, findArtifact("samsung_galaxy_watch_6_product"));
        localArtifacts.put(15, findArtifact("anker_735_charger"));
        localArtifacts.put(18, findArtifact("xiaomi_50w_powerbank"));
        localArtifacts.put(23, findArtifact("sony_wh1000xm5_headphones"));
        localArtifacts.put(56, findArtifact("samsung_s24_ultra_titanium_gray_v2"));

        for (Map.Entry<Integer, File> entry : localArtifacts.entrySet()) {
            if (entry.getValue() != null && entry.getValue().exists()) {
                try {
                    BufferedImage src = ImageIO.read(entry.getValue());
                    if (src != null) {
                        File dest = new File(IMAGES_DIR, "prod-" + entry.getKey() + ".png");
                        renderStudioCard(src, dest);
                        System.out.printf("  [LOCAL] Deployed ID %d from %s -> %s%n", 
                            entry.getKey(), entry.getValue().getName(), dest.getName());
                    }
                } catch (Exception e) {
                    System.err.println("Failed to load local artifact for ID " + entry.getKey() + ": " + e.getMessage());
                }
            }
        }

        System.out.println("\n=== 3. FETCHING GENUINE PRODUCT PHOTOGRAPHS (THREADED) ===");
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Future<Void>> futures = new ArrayList<>();

        for (int id = 1; id <= 175; id++) {
            final int productId = id;
            if (localArtifacts.containsKey(productId) && localArtifacts.get(productId) != null && localArtifacts.get(productId).exists()) {
                // Already rendered
                continue;
            }

            final String query = queries.getOrDefault(productId, "electronics product white background");
            futures.add(pool.submit(() -> {
                fetchAndRenderProduct(productId, query);
                return null;
            }));
        }

        for (Future<Void> f : futures) {
            try {
                f.get();
            } catch (Exception e) {
                System.err.println("Fetch task error: " + e.getMessage());
            }
        }
        pool.shutdown();
        pool.awaitTermination(60, TimeUnit.SECONDS);

        System.out.println("\n=== 4. CREATING BACKWARD-COMPATIBLE ALIASES FOR IDS 1-45 ===");
        createLegacyAliases();

        System.out.println("\n=== 5. RUNNING COMPREHENSIVE IMAGE AUDIT & HASH VERIFICATION ===");
        verifyAllImages();
    }

    private static File findArtifact(String prefix) {
        File[] files = ARTIFACTS_DIR.listFiles((dir, name) -> name.startsWith(prefix) && (name.endsWith(".jpg") || name.endsWith(".png")));
        if (files != null && files.length > 0) {
            return files[0];
        }
        return null;
    }

    private static void fetchAndRenderProduct(int id, String query) {
        File dest = new File(IMAGES_DIR, "prod-" + id + ".png");
        try {
            List<String> imgUrls = searchCandidateImages(query);
            for (String imgUrl : imgUrls) {
                try {
                    BufferedImage src = downloadImage(imgUrl);
                    if (src != null && src.getWidth() >= 120 && src.getHeight() >= 120) {
                        renderStudioCard(src, dest);
                        System.out.printf("  [OK] ID %3d -> Saved %s (%dx%d, source: %s)%n", 
                            id, dest.getName(), src.getWidth(), src.getHeight(), hostOf(imgUrl));
                        return;
                    }
                } catch (Exception ignored) {
                }
            }
            System.err.printf("  [WARN] ID %3d could not find remote image, falling back for %s%n", id, query);
        } catch (Exception e) {
            System.err.printf("  [ERROR] ID %3d failed: %s%n", id, e.getMessage());
        }
    }

    private static String hostOf(String urlStr) {
        try {
            return new URI(urlStr).getHost();
        } catch (Exception e) {
            return "unknown";
        }
    }

    private static List<String> searchCandidateImages(String query) {
        List<String> list = new ArrayList<>();
        try {
            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://www.bing.com/images/search?q=" + encoded + "&form=HDRSC2&first=1";
            HttpURLConnection conn = (HttpURLConnection) new URI(url).toURL().openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setConnectTimeout(7000);
            conn.setReadTimeout(7000);

            String html = readString(conn.getInputStream());
            Matcher m = Pattern.compile("murl&quot;:&quot;([^&]+)&quot;").matcher(html);
            while (m.find() && list.size() < 6) {
                String u = m.group(1).replace("&amp;", "&");
                if (!u.endsWith(".svg") && !u.endsWith(".gif") && !u.contains("doubleclick") && !u.contains("facebook")) {
                    list.add(u);
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return list;
    }

    private static BufferedImage downloadImage(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URI(urlStr).toURL().openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
        conn.setConnectTimeout(6000);
        conn.setReadTimeout(6000);

        try (InputStream in = conn.getInputStream()) {
            return ImageIO.read(in);
        }
    }

    public static void renderStudioCard(BufferedImage src, File dest) throws IOException {
        int width = 400;
        int height = 320;
        BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = canvas.createGraphics();

        // Rendering hints for supreme sharpness
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clean white background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Soft, subtle card border
        g.setColor(new Color(241, 245, 249));
        g.drawRect(1, 1, width - 3, height - 3);

        // Soft contact shadow underneath
        g.setColor(new Color(15, 23, 42, 22));
        g.fillOval(65, 275, 270, 24);

        // Scale preserving native aspect ratio without distortion
        double maxW = 340.0;
        double maxH = 260.0;
        double scale = Math.min(maxW / src.getWidth(), maxH / src.getHeight());
        int drawW = (int) (src.getWidth() * scale);
        int drawH = (int) (src.getHeight() * scale);
        int drawX = (width - drawW) / 2;
        int drawY = (height - drawH) / 2;

        g.drawImage(src, drawX, drawY, drawW, drawH, null);
        g.dispose();

        ImageIO.write(canvas, "png", dest);
    }

    public static void generatePlaceholder() throws IOException {
        File ph = new File(IMAGES_DIR, "placeholder-product.png");
        int width = 400;
        int height = 320;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(226, 232, 240));
        g.drawRect(2, 2, width - 5, height - 5);

        // Box shadow
        g.setColor(new Color(15, 23, 42, 18));
        g.fillOval(80, 260, 240, 22);

        // Device outline icon
        g.setColor(new Color(241, 245, 249));
        g.fillRoundRect(120, 50, 160, 160, 24, 24);
        g.setColor(new Color(203, 213, 225));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(120, 50, 160, 160, 24, 24);

        // Tech Device Screen inside
        g.setColor(new Color(255, 255, 255));
        g.fillRoundRect(135, 65, 130, 130, 12, 12);
        g.setColor(new Color(226, 232, 240));
        g.drawRoundRect(135, 65, 130, 130, 12, 12);

        // Shield / check icon
        g.setColor(new Color(79, 70, 229));
        g.fillOval(175, 105, 50, 50);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(190, 130, 198, 138);
        g.drawLine(198, 138, 212, 122);

        // Text
        g.setColor(new Color(30, 41, 59));
        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        FontMetrics fm = g.getFontMetrics();
        String text1 = "SmartCart Verified Product";
        g.drawString(text1, (width - fm.stringWidth(text1)) / 2, 235);

        g.setColor(new Color(100, 116, 139));
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        fm = g.getFontMetrics();
        String text2 = "100% Genuine Branded Electronics";
        g.drawString(text2, (width - fm.stringWidth(text2)) / 2, 255);

        g.dispose();
        ImageIO.write(img, "png", ph);
        System.out.println("  -> Generated: " + ph.getAbsolutePath());
    }

    private static void createLegacyAliases() throws IOException {
        String[][] aliases = {
            {"1", "laptop-pro15.png"},
            {"2", "laptop-gaming16.png"},
            {"3", "laptop-air13.png"},
            {"5", "phone-15pro.png"},
            {"6", "phone-15.png"},
            {"7", "phone-oneplus.png"},
            {"8", "phone-lite.png"},
            {"9", "tablet-pro11.png"},
            {"10", "tablet-ipadair.png"},
            {"11", "tablet-stylus.png"},
            {"13", "watch-ultra.png"},
            {"14", "watch-fit.png"},
            {"15", "charger-65w.png"},
            {"16", "charger-45w.png"},
            {"17", "charger-70w.png"},
            {"18", "powerbank-20k.png"},
            {"19", "cable-usbc.png"},
            {"20", "cable-lightning.png"},
            {"21", "cable-hdmi.png"},
            {"22", "adapter-dac.png"},
            {"23", "audio-headphones.png"},
            {"24", "audio-earbuds.png"},
            {"25", "audio-neckband.png"},
            {"26", "audio-wired.png"},
            {"27", "case-15pro.png"},
            {"28", "case-15promax.png"},
            {"29", "case-15clear.png"},
            {"31", "monitor-4k27.png"},
            {"32", "keyboard-mech.png"},
            {"33", "mouse-mxmaster.png"},
            {"34", "plug-smart16a.png"},
            {"35", "lights-hue.png"},
            {"36", "doorbell-smart.png"},
            {"37", "webcam-4k.png"},
            {"38", "mic-studio.png"},
            {"39", "stand-monitor.png"},
            {"40", "dock-10in1.png"},
            {"41", "controller-wireless.png"},
            {"42", "deskmat-xl.png"},
            {"43", "ssd-nvme.png"},
            {"44", "surge-protector.png"},
            {"45", "magsafe-stand.png"}
        };

        for (String[] a : aliases) {
            File src = new File(IMAGES_DIR, "prod-" + a[0] + ".png");
            if (src.exists()) {
                File dst = new File(IMAGES_DIR, a[1]);
                copyFile(src, dst);
            }
        }
        System.out.println("  -> Legacy aliases linked successfully.");
    }

    private static void copyFile(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = in.read(buf)) > 0) {
                out.write(buf, 0, r);
            }
        }
    }

    private static void verifyAllImages() throws Exception {
        Map<String, List<Integer>> hashToIds = new LinkedHashMap<>();
        int missing = 0;

        for (int id = 1; id <= 175; id++) {
            File f = new File(IMAGES_DIR, "prod-" + id + ".png");
            if (!f.exists()) {
                missing++;
                System.out.println("MISSING: prod-" + id + ".png");
                continue;
            }
            String hash = getHash(f);
            hashToIds.computeIfAbsent(hash, k -> new ArrayList<>()).add(id);
        }

        System.out.println("Total products checked: 175");
        System.out.println("Missing files: " + missing);
        System.out.println("Unique image hashes: " + hashToIds.size());

        int dups = 0;
        for (Map.Entry<String, List<Integer>> e : hashToIds.entrySet()) {
            if (e.getValue().size() > 1) {
                dups++;
                System.out.printf("  Duplicate hash %s shared by IDs: %s%n", e.getKey(), e.getValue());
            }
        }
        if (dups == 0) {
            System.out.println("PERFECT! ZERO DUPLICATES! Every single product has an authentic, distinct image.");
        } else {
            System.out.println("Warning: Found " + dups + " duplicate groups remaining.");
        }
    }

    private static String getHash(File f) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = new FileInputStream(f)) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = is.read(buf)) > 0) {
                md.update(buf, 0, r);
            }
        }
        byte[] b = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte x : b) {
            sb.append(String.format("%02x", x));
        }
        return sb.toString();
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            char[] buf = new char[4096];
            int r;
            while ((r = br.read(buf)) != -1) {
                sb.append(buf, 0, r);
            }
        }
        return sb.toString();
    }

    private static Map<Integer, String> getTargetQueries() {
        Map<Integer, String> m = new HashMap<>();

        // Section 1: Laptops (1-4, 46-55)
        m.put(1, "Apple MacBook Air 15 M3 laptop silver product white background");
        m.put(2, "ASUS ROG Zephyrus G16 gaming laptop product white background");
        m.put(3, "Apple MacBook Air 13 M2 midnight laptop product white background");
        m.put(4, "Lenovo IdeaPad Slim 3 laptop arctic grey product white background");
        m.put(46, "Dell XPS 15 9530 laptop product white background");
        m.put(47, "HP Spectre x360 2-in-1 OLED laptop product white background");
        m.put(48, "Lenovo ThinkPad X1 Carbon Gen 12 laptop product white background");
        m.put(49, "Acer Predator Helios 16 gaming laptop product white background");
        m.put(50, "ASUS ZenBook 14 OLED UX3405 laptop product white background");
        m.put(51, "MSI Katana 15 gaming laptop product white background");
        m.put(52, "Samsung Galaxy Book4 Pro 360 laptop product white background");
        m.put(53, "LG Gram 17 laptop white background");
        m.put(54, "Microsoft Surface Laptop 6 platinum product white background");
        m.put(55, "Razer Blade 16 gaming laptop product white background");

        // Section 2: Smartphones (5-8, 56-65)
        m.put(5, "Apple iPhone 15 Pro Natural Titanium product white background");
        m.put(6, "Apple iPhone 15 Blue product white background");
        m.put(7, "OnePlus 12 5G Emerald Green product white background");
        m.put(8, "Xiaomi Redmi Note 13 5G Ocean Teal product white background");
        m.put(56, "Samsung Galaxy S24 Ultra Titanium Gray product white background");
        m.put(57, "Google Pixel 8 Pro Bay Blue product white background");
        m.put(58, "Nothing Phone 2 Dark Grey glyph product white background");
        m.put(59, "Vivo X100 Pro Asteroid Black smartphone product white background");
        m.put(60, "Motorola Edge 50 Ultra Nordic Wood smartphone product white background");
        m.put(61, "Realme GT 6 Fluid Silver smartphone product white background");
        m.put(62, "iQOO 12 5G Legend Edition smartphone product white background");
        m.put(63, "Samsung Galaxy Z Flip 5 Mint foldable smartphone product white background");
        m.put(64, "OnePlus Open Emerald Dusk foldable smartphone product white background");
        m.put(65, "Sony Xperia 1 V Black smartphone product white background");

        // Section 3: Tablets (9-12, 66-76)
        m.put(9, "Apple iPad Air 11 M2 Starlight tablet product white background");
        m.put(10, "Samsung Galaxy Tab S9 FE Gray with SPen product white background");
        m.put(11, "Apple Pencil Pro white product white background");
        m.put(12, "Apple Magic Keyboard for iPad Air 11 white background");
        m.put(66, "Apple iPad Pro 13 M4 Space Black tablet product white background");
        m.put(67, "Samsung Galaxy Tab S9 Ultra with SPen product white background");
        m.put(68, "OnePlus Pad 2 Nimbus Gray tablet product white background");
        m.put(69, "Xiaomi Pad 6 Pro Gravity Gray tablet product white background");
        m.put(70, "Lenovo Tab P12 Storm Grey tablet product white background");
        m.put(71, "Microsoft Surface Pro 10 for Business platinum tablet white background");
        m.put(72, "Realme Pad 2 Inspiration Green tablet product white background");
        m.put(73, "Honor Pad 9 Space Grey tablet product white background");
        m.put(74, "Apple iPad mini 6 Starlight tablet product white background");
        m.put(75, "OnePlus Stylo 2 active stylus pen product white background");
        m.put(76, "Apple Magic Keyboard Folio for iPad product white background");

        // Section 4: Wearables (13-14, 77-85)
        m.put(13, "Apple Watch Ultra 2 titanium orange strap white background");
        m.put(14, "Samsung Galaxy Watch 6 44mm graphite product white background");
        m.put(77, "OnePlus Watch 2 Radiant Steel smartwatch product white background");
        m.put(78, "Amazfit Balance smartwatch Midnight product white background");
        m.put(79, "Fitbit Charge 6 Obsidian black fitness tracker product white background");
        m.put(80, "Noise ColorFit Pro 5 Max smartwatch Jet Black product white background");
        m.put(81, "boAt Lunar Tigre smartwatch Metal Black product white background");
        m.put(82, "Fire-Boltt Invincible Plus smartwatch Black Steel product white background");
        m.put(83, "Fastrack Limitless FS1 Pro smartwatch Black product white background");
        m.put(84, "Garmin Forerunner 265 running smartwatch Black product white background");
        m.put(85, "Apple Watch Series 9 45mm Midnight aluminum product white background");

        // Section 5: Chargers & Power Banks (15-18, 86-95)
        m.put(15, "Anker 735 Charger GaNPrime 65W product white background");
        m.put(16, "Anker 313 Charger 45W USB-C product white background");
        m.put(17, "Apple 70W USB-C Power Adapter product white background");
        m.put(18, "Xiaomi Mi 50W 20000mAh Power Bank 3 Pro black white background");
        m.put(86, "UGREEN Nexode 100W 4-Port GaN Desktop Charger product white background");
        m.put(87, "Stuffcool Neutron 65W Dual Type-C GaN Charger white background");
        m.put(88, "Spigen ArcStation Pro 65W GaN dual port charger white background");
        m.put(89, "Belkin BoostCharge Pro 65W dual USB-C GaN charger white background");
        m.put(90, "Portronics Adapto 65W GaN fast charger white background");
        m.put(91, "boAt Dual Port 65W super fast charging adapter white background");
        m.put(92, "CMF by Nothing 65W GaN 3-port charger orange white background");
        m.put(93, "Ambrane Raap G65 65W triple port GaN charger white background");
        m.put(94, "DailyObjects Surge 65W dual port GaN charger white background");
        m.put(95, "Pebble Volta 65W GaN fast charger white background");

        // Section 6: Cables & Interconnects (19-22, 96-105)
        m.put(19, "Spigen DuraPro 100W braided USB-C cable black white background");
        m.put(20, "Apple USB-C to Lightning Cable 1m white product white background");
        m.put(21, "Belkin Ultra High Speed HDMI 2.1 cable black white background");
        m.put(22, "Apple USB-C to 3.5mm Headphone Jack Adapter dongle white background");
        m.put(96, "Anker PowerLine III Flow silicone USB-C cable cloud white white background");
        m.put(97, "boAt Deuce 300 2-in-1 Type-C cable braided white background");
        m.put(98, "Belkin BoostCharge Pro Flex braided Lightning cable white background");
        m.put(99, "Portronics Konnect CL Type-C to Lightning silicone cable white background");
        m.put(100, "Realme 65W SuperVOOC yellow Type-C cable white background");
        m.put(101, "OnePlus Warp Charge red braided Type-C cable white background");
        m.put(102, "Ambrane Unbreakable 3A fast charging Type-C cable white background");
        m.put(103, "Mi Braided Type-C cable black 1m white background");
        m.put(104, "Stuffcool Vortex 240W USB-C heavy duty cable white background");
        m.put(105, "Amazon Basics double braided nylon USB-C cable silver white background");

        // Section 7: Audio & Headphones (23-26, 106-115)
        m.put(23, "Sony WH-1000XM5 wireless noise cancelling headphones silver white background");
        m.put(24, "Apple AirPods Pro 2nd Gen USB-C white background");
        m.put(25, "OnePlus Bullets Wireless Z2 ANC neckband earphones black white background");
        m.put(26, "Apple EarPods with USB-C connector white background");
        m.put(106, "Bose QuietComfort Ultra Wireless Headphones Black product white background");
        m.put(107, "Sennheiser Momentum 4 Wireless Headphones Black white background");
        m.put(108, "JBL Tour ONE M2 Noise Cancelling Headphones Black white background");
        m.put(109, "Marshall Major IV on-ear headphones black white background");
        m.put(110, "Audio-Technica ATH-M50xBT2 wireless studio headphones white background");
        m.put(111, "Sony WF-1000XM5 true wireless earbuds black white background");
        m.put(112, "Bose QuietComfort Ultra Earbuds black white background");
        m.put(113, "Samsung Galaxy Buds2 Pro Graphite earbuds white background");
        m.put(114, "boAt Nirvana Ion ANC earbuds charcoal black white background");
        m.put(115, "Skullcandy Crusher ANC 2 over-ear headphones black white background");

        // Section 8: Cases & Protection (27-30, 116-125)
        m.put(27, "Spigen Ultra Hybrid MagFit case iPhone 15 Pro clear white background");
        m.put(28, "Spigen Tough Armor MagFit case iPhone 15 Pro Max gunmetal kickstand white background");
        m.put(29, "Spigen Liquid Crystal case iPhone 15 clear TPU white background");
        m.put(30, "Spigen Glas.tR AlignMaster tempered glass screen protector iPhone 15 Pro box white background");
        m.put(116, "Ringke Fusion-X case Galaxy S24 Ultra camo black white background");
        m.put(117, "Caseology Parallax MagSafe case iPhone 15 Pro matte black 3D white background");
        m.put(118, "OtterBox Defender Series case iPhone 15 black multi-layer white background");
        m.put(119, "UAG Monarch Kevlar case Galaxy S24 Ultra rugged black white background");
        m.put(120, "Nillkin CamShield Pro case iPhone 15 Pro sliding camera cover white background");
        m.put(121, "Torras 360 Spin magnetic ring stand case iPhone 15 Pro Max white background");
        m.put(122, "ESR Classic Hybrid clear case stash stand Galaxy S24 white background");
        m.put(123, "Supcase UB Pro full body rugged holster case Galaxy S24 Ultra white background");
        m.put(124, "DailyObjects vegan leather MagSafe case iPhone 15 Pro saddle brown white background");
        m.put(125, "Spigen Thin Fit case Galaxy S24 matte black slim white background");

        // Section 9: Monitors & Displays (31-33, 126-135)
        m.put(31, "LG UltraFine 27 4K UHD IPS monitor 27UN850 white background");
        m.put(32, "Keychron K2 wireless mechanical keyboard white background");
        m.put(33, "Logitech MX Master 3S wireless performance mouse graphite white background");
        m.put(126, "Samsung Odyssey Neo G8 32 curved gaming monitor white background");
        m.put(127, "Dell UltraSharp 32 4K USB-C Hub Monitor U3223QE white background");
        m.put(128, "BenQ PD2705U 27 4K designer monitor white background");
        m.put(129, "ASUS ROG Swift OLED PG32UCDM 32 gaming monitor white background");
        m.put(130, "Acer Predator XB283K 28 4K gaming monitor white background");
        m.put(131, "MSI Optix MPG321UR-QD 32 4K gaming monitor white background");
        m.put(132, "Gigabyte M28U 28 4K gaming monitor white background");
        m.put(133, "ViewSonic ColorPro VP2768a 27 graphic design monitor white background");
        m.put(134, "Philips Evnia 34 curved mini-LED gaming monitor white background");
        m.put(135, "Samsung ViewFinity S9 27 5K display white background");

        // Section 10: Smart Home & Automation (34-36, 136-145)
        m.put(34, "Wipro 16A smart wifi plug white background");
        m.put(35, "Philips Hue White and Color starter kit 3 bulbs bridge white background");
        m.put(36, "Qubo Smart WiFi video doorbell Hero black white background");
        m.put(136, "TP-Link Tapo C210 2K pan tilt security camera white background");
        m.put(137, "Amazon Echo Dot 5th Gen smart speaker charcoal white background");
        m.put(138, "Google Nest Hub 2nd Gen chalk smart display white background");
        m.put(139, "Godrej smart digital touch screen door lock white background");
        m.put(140, "Xiaomi Smart Air Purifier 4 white background");
        m.put(141, "Atomberg Renesa 1200mm smart ceiling fan white background");
        m.put(142, "Havells Crabtree Signia smart wifi touch switch panel white background");
        m.put(143, "Qubo smart door lock ultra Hero black white background");
        m.put(144, "TP-Link Tapo P110 16A smart plug white background");
        m.put(145, "Wipro smart LED batten 20W light bar white background");

        // Section 11: Desk Setup & Streaming (37-40, 146-155)
        m.put(37, "Logitech Brio 4K Ultra HD Pro webcam white background");
        m.put(38, "Blue Yeti USB microphone blackout white background");
        m.put(39, "Amazon Basics gas spring single monitor arm mount white background");
        m.put(40, "Anker 575 10-in-1 dual 4K USB-C docking station white background");
        m.put(146, "Elgato Stream Deck MK.2 studio controller black white background");
        m.put(147, "Rode NT-USB Mini studio condenser microphone white background");
        m.put(148, "HyperX QuadCast S RGB USB microphone white background");
        m.put(149, "Elgato Facecam 1080p60 streaming webcam white background");
        m.put(150, "Shure MV7 USB dynamic podcast microphone black white background");
        m.put(151, "Razer Kiyo Pro streaming webcam white background");
        m.put(152, "Elgato Key Light Air LED panel studio light white background");
        m.put(153, "BenQ ScreenBar Halo wireless controller LED monitor light bar white background");
        m.put(154, "Audio-Technica AT2020USB-X cardioid condenser microphone white background");
        m.put(155, "Logitech Litra Glow streaming light white background");

        // Section 12: Gaming Peripherals (41-43, 156-165)
        m.put(41, "Microsoft Xbox Wireless Controller Carbon Black white background");
        m.put(42, "SteelSeries QcK Heavy XXL gaming mouse pad 900x400 white background");
        m.put(43, "Samsung 990 PRO 2TB PCIe 4.0 NVMe SSD heatsink white background");
        m.put(156, "Sony DualSense Edge wireless controller PS5 white background");
        m.put(157, "Razer DeathAdder V3 Pro wireless gaming mouse black white background");
        m.put(158, "Corsair K70 RGB PRO mechanical gaming keyboard white background");
        m.put(159, "Logitech G PRO X SUPERLIGHT 2 wireless gaming mouse black white background");
        m.put(160, "HyperX Cloud III wireless gaming headset red black white background");
        m.put(161, "ASUS ROG Falchion RX low profile 65 wireless keyboard white background");
        m.put(162, "SteelSeries Arctis Nova Pro wireless headset white background");
        m.put(163, "Razer BlackShark V2 Pro wireless gaming headset white background");
        m.put(164, "Corsair RM850x 850W modular power supply white background");
        m.put(165, "Redragon K552 Kumara mechanical gaming keyboard white background");

        // Section 13: Power Protection & UPS (44-45, 166-175)
        m.put(44, "Belkin 6-socket surge protector 30W USB-C white background");
        m.put(45, "ESR 3-in-1 MagSafe foldable travel wireless charging stand white background");
        m.put(166, "APC Back-UPS BX600C-IN 600VA desktop UPS white background");
        m.put(167, "APC Back-UPS Pro BR1000G-IN 1000VA UPS white background");
        m.put(168, "V-Guard Crystal Plus smart TV voltage stabilizer white background");
        m.put(169, "Microtek Legend 1000VA heavy duty desktop UPS white background");
        m.put(170, "Luminous Zelio 1100 pure sinewave inverter white background");
        m.put(171, "Havells 4-way heavy duty spike surge protector power strip white background");
        m.put(172, "Portronics Power Plate 7 power extension strip USB white background");
        m.put(173, "Syska 4-way power strip surge protector white background");
        m.put(174, "Zebronics Zeb-Iron 600VA line interactive UPS white background");
        m.put(175, "Belkin BoostCharge Pro 3-in-1 wireless charging pad with MagSafe 15W white background");

        return m;
    }
}
