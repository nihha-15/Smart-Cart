package com.smartcart.dao;

import com.smartcart.model.Product;
import com.smartcart.model.SetupBundle;

import java.util.List;
import java.util.Map;

/**
 * Seeds 130 additional real-world branded products (IDs 46 to 175)
 * across all 13 SmartCart catalog sections (10 distinct brands per section)
 * with authentic specifications, Indian Rupee (INR) prices, and studio photograph paths.
 */
public class ExtendedCatalogueSeeder {

    public static void seed(Map<Integer, Product> products, List<ProductDAO.AlternativeMapping> alternatives, List<SetupBundle> bundles) {
        // =====================================================================
        // SECTION 1: 10 NEW LAPTOPS (46 to 55)
        // =====================================================================
        addProduct(products, 46, "Dell XPS 15 (Core i7-13700H, 16GB, 512GB, RTX 4050)", "laptops",
                "15.6-inch FHD+ InfinityEdge Display, 13th Gen Intel Core i7-13700H, NVIDIA GeForce RTX 4050 6GB GDDR6, 16GB DDR5, 512GB SSD. CNC machined aluminum chassis.",
                174990.00, 189990.00, 10, "images/prod-46.png", 4.7, 74, true, "Executive Pro", 86, 1800.0,
                new String[][]{{"device_type", "laptop"}, {"screen_size", "15.6"}, {"charging_port", "USB-C"}, {"min_charging_watts", "65"}, {"audio_jack", "true"}, {"includes_charger", "true"}});

        addProduct(products, 47, "HP Spectre x360 2-in-1 OLED (Intel Core Ultra 7, 16GB, 1TB)", "laptops",
                "14-inch 2.8K 120Hz OLED Touchscreen 2-in-1 convertible, Intel Core Ultra 7 155H with Intel AI Boost, 16GB LPDDR5X, 1TB SSD. Includes HP MPP 2.0 Tilt Pen.",
                149999.00, 164999.00, 12, "images/prod-47.png", 4.8, 92, true, "2-in-1 OLED", 65, 1500.0,
                new String[][]{{"device_type", "laptop"}, {"screen_size", "14.0"}, {"charging_port", "USB-C"}, {"min_charging_watts", "65"}, {"audio_jack", "true"}, {"includes_charger", "true"}});

        addProduct(products, 48, "Lenovo ThinkPad X1 Carbon Gen 12 (Core Ultra 7, 32GB, 1TB)", "laptops",
                "14-inch 2.8K OLED 120Hz, Intel Core Ultra 7 155U, 32GB LPDDR5X, 1TB PCIe 4.0 SSD, Carbon fiber chassis, legendary ThinkPad keyboard with TrackPoint.",
                159990.00, 179990.00, 8, "images/prod-48.png", 4.9, 65, true, "Business Legend", 65, 1200.0,
                new String[][]{{"device_type", "laptop"}, {"screen_size", "14.0"}, {"charging_port", "USB-C"}, {"min_charging_watts", "65"}, {"audio_jack", "true"}, {"includes_charger", "true"}});

        addProduct(products, 49, "Acer Predator Helios 16 (i9-14900HX, RTX 4070, 16GB, 1TB)", "laptops",
                "16-inch WQXGA 240Hz 500 nits IPS, Intel Core i9-14900HX, NVIDIA GeForce RTX 4070 8GB GDDR6, 16GB DDR5, 1TB SSD. AeroBlade 3D fan cooling system.",
                144999.00, 169999.00, 14, "images/prod-49.png", 4.7, 115, true, "Hardcore Gaming", 140, 2000.0,
                new String[][]{{"device_type", "laptop"}, {"screen_size", "16.0"}, {"charging_port", "USB-C"}, {"min_charging_watts", "100"}, {"audio_jack", "true"}, {"includes_charger", "true"}});

        addProduct(products, 50, "ASUS ZenBook 14 OLED (Intel Core Ultra 5, 16GB, 1TB)", "laptops",
                "14-inch 3K 120Hz OLED NanoEdge Display, Intel Core Ultra 5 125H, 16GB LPDDR5X, 1TB NVMe SSD. Ultra-slim 1.2kg premium all-metal chassis with 75Wh battery.",
                99990.00, 114990.00, 20, "images/prod-50.png", 4.8, 140, true, "Slim & Light", 65, 1000.0,
                new String[][]{{"device_type", "laptop"}, {"screen_size", "14.0"}, {"charging_port", "USB-C"}, {"min_charging_watts", "65"}, {"audio_jack", "true"}, {"includes_charger", "true"}});

        addProduct(products, 51, "MSI Katana 15 Gaming (i7-13620H, RTX 4060, 16GB, 1TB)", "laptops",
                "15.6-inch FHD 144Hz IPS-level Display, Intel Core i7-13620H, NVIDIA GeForce RTX 4060 8GB GDDR6, 16GB DDR5, 1TB SSD. 4-Zone RGB Gaming Keyboard.",
                89990.00, 104990.00, 18, "images/prod-51.png", 4.6, 98, true, "Value Gaming", 105, 1500.0,
                new String[][]{{"device_type", "laptop"}, {"screen_size", "15.6"}, {"charging_port", "USB-C"}, {"min_charging_watts", "65"}, {"audio_jack", "true"}, {"includes_charger", "true"}});

        addProduct(products, 52, "Samsung Galaxy Book4 Pro 360 (Intel Core Ultra 7, 16GB, 512GB)", "laptops",
                "16-inch 3K Dynamic AMOLED 2X Touchscreen 2-in-1, Intel Core Ultra 7 155H, 16GB LPDDR5X, 512GB SSD. Bundled S-Pen and seamless Galaxy ecosystem integration.",
                163990.00, 179990.00, 9, "images/prod-52.png", 4.8, 52, true, "AMOLED 2-in-1", 65, 1400.0,
                new String[][]{{"device_type", "laptop"}, {"screen_size", "16.0"}, {"charging_port", "USB-C"}, {"min_charging_watts", "65"}, {"audio_jack", "true"}, {"includes_charger", "true"}});

        addProduct(products, 53, "LG Gram 17 Ultra-Lightweight (Intel Core Ultra 7, 16GB, 1TB)", "laptops",
                "17-inch WQXGA IPS anti-glare display, Intel Core Ultra 7 155H, 16GB LPDDR5X, 1TB NVMe Gen4 SSD. Featherlight 1.35kg magnesium alloy body with 80Wh battery.",
                129990.00, 149990.00, 11, "images/prod-53.png", 4.7, 63, true, "Featherlight", 65, 1200.0,
                new String[][]{{"device_type", "laptop"}, {"screen_size", "17.0"}, {"charging_port", "USB-C"}, {"min_charging_watts", "65"}, {"audio_jack", "true"}, {"includes_charger", "true"}});

        addProduct(products, 54, "Microsoft Surface Laptop 6 (Intel Core Ultra 5, 16GB, 512GB)", "laptops",
                "13.5-inch PixelSense Touchscreen, Intel Core Ultra 5 135H, 16GB LPDDR5X, 512GB Gen4 SSD. Dual Studio Mics, anti-reflective coating, premium aluminum build.",
                134990.00, 149990.00, 15, "images/prod-54.png", 4.7, 45, true, "Surface Premium", 65, 1200.0,
                new String[][]{{"device_type", "laptop"}, {"screen_size", "13.5"}, {"charging_port", "USB-C"}, {"min_charging_watts", "65"}, {"audio_jack", "true"}, {"includes_charger", "true"}});

        addProduct(products, 55, "Razer Blade 16 (i9-14900HX, RTX 4090, 32GB, 2TB Dual Mini-LED)", "laptops",
                "16-inch Dual-Mode Mini-LED (UHD+ 120Hz / FHD+ 240Hz), Intel Core i9-14900HX, NVIDIA GeForce RTX 4090 16GB GDDR6, 32GB DDR5, 2TB PCIe 4.0 SSD. Anodized CNC aluminum unibody.",
                349990.00, 379990.00, 5, "images/prod-55.png", 4.9, 38, true, "Ultimate Weapon", 175, 3000.0,
                new String[][]{{"device_type", "laptop"}, {"screen_size", "16.0"}, {"charging_port", "USB-C"}, {"min_charging_watts", "100"}, {"audio_jack", "true"}, {"includes_charger", "true"}});

        // =====================================================================
        // SECTION 2: 10 NEW SMARTPHONES (56 to 65)
        // =====================================================================
        addProduct(products, 56, "Samsung Galaxy S24 Ultra (12GB, 256GB, Titanium Gray, S-Pen)", "smartphones",
                "6.8-inch Dynamic AMOLED 2X 120Hz, Snapdragon 8 Gen 3 for Galaxy, 200MP Quad Telephoto Camera, Titanium Frame, Integrated S-Pen with AI generative editing.",
                129999.00, 134999.00, 15, "images/prod-56.png", 4.9, 210, true, "Titanium Flagship", 15, 0.0,
                new String[][]{{"device_type", "phone"}, {"screen_size", "6.8"}, {"charging_port", "USB-C"}, {"min_charging_watts", "45"}, {"audio_jack", "false"}, {"includes_charger", "false"}});

        addProduct(products, 57, "Google Pixel 8 Pro (12GB, 128GB, Bay Blue, Tensor G3)", "smartphones",
                "6.7-inch Super Actua LTPO OLED 120Hz, Google Tensor G3 with Titan M2, 50MP Pro Triple Camera with 5x Telephoto, 7 Years of Android OS updates.",
                99999.00, 106999.00, 12, "images/prod-57.png", 4.8, 165, true, "Pure Google", 15, 0.0,
                new String[][]{{"device_type", "phone"}, {"screen_size", "6.7"}, {"charging_port", "USB-C"}, {"min_charging_watts", "30"}, {"audio_jack", "false"}, {"includes_charger", "false"}});

        addProduct(products, 58, "Nothing Phone (2) (12GB, 256GB, Dark Grey, Glyph Interface)", "smartphones",
                "6.7-inch LTPO OLED 120Hz, Snapdragon 8+ Gen 1, Dual 50MP Sony IMX890 OIS cameras, Iconic transparent back with programmable Glyph Lighting Interface.",
                36999.00, 44999.00, 25, "images/prod-58.png", 4.7, 190, true, "Design Icon", 15, 0.0,
                new String[][]{{"device_type", "phone"}, {"screen_size", "6.7"}, {"charging_port", "USB-C"}, {"min_charging_watts", "45"}, {"audio_jack", "false"}, {"includes_charger", "false"}});

        addProduct(products, 59, "Vivo X100 Pro (16GB, 512GB, Zeiss 1-inch Camera)", "smartphones",
                "6.78-inch 1.5K LTPO AMOLED 120Hz 3000 nits, MediaTek Dimensity 9300, 50MP Sony IMX989 1-inch Sensor with Zeiss APO Telephoto, 100W FlashCharge.",
                89999.00, 96999.00, 14, "images/prod-59.png", 4.8, 88, true, "Zeiss Optics", 15, 0.0,
                new String[][]{{"device_type", "phone"}, {"screen_size", "6.78"}, {"charging_port", "USB-C"}, {"min_charging_watts", "100"}, {"audio_jack", "false"}, {"includes_charger", "true"}});

        addProduct(products, 60, "Motorola Edge 50 Ultra (16GB, 512GB, Real Wood Back)", "smartphones",
                "6.7-inch 1.5K Super HD pOLED 144Hz, Snapdragon 8s Gen 3, Real FSC-certified Nordic Wood back, Pantone-validated cameras, 125W TurboPower charging.",
                54999.00, 64999.00, 16, "images/prod-60.png", 4.7, 72, true, "Real Wood", 15, 0.0,
                new String[][]{{"device_type", "phone"}, {"screen_size", "6.7"}, {"charging_port", "USB-C"}, {"min_charging_watts", "125"}, {"audio_jack", "false"}, {"includes_charger", "true"}});

        addProduct(products, 61, "Realme GT 6 (16GB, 512GB, 6000 nits Ultra Display, 120W)", "smartphones",
                "6.78-inch 8T LTPO 6000 nits AMOLED, Snapdragon 8s Gen 3, Sony LYT-808 OIS Camera, 5500mAh Dual-cell battery with 120W SUPERVOOC fast charge.",
                44999.00, 49999.00, 22, "images/prod-61.png", 4.7, 140, true, "6000 Nits", 15, 0.0,
                new String[][]{{"device_type", "phone"}, {"screen_size", "6.78"}, {"charging_port", "USB-C"}, {"min_charging_watts", "120"}, {"audio_jack", "false"}, {"includes_charger", "true"}});

        addProduct(products, 62, "iQOO 12 5G (16GB, 512GB, Snapdragon 8 Gen 3, Legend Edition)", "smartphones",
                "6.78-inch 1.5K 144Hz AMOLED Display, Snapdragon 8 Gen 3 with dedicated Supercomputing Q1 chip, 50MP 1/1.3\" Astrography Camera, 120W FlashCharge.",
                57999.00, 64999.00, 18, "images/prod-62.png", 4.8, 175, true, "Flagship Beast", 15, 0.0,
                new String[][]{{"device_type", "phone"}, {"screen_size", "6.78"}, {"charging_port", "USB-C"}, {"min_charging_watts", "120"}, {"audio_jack", "false"}, {"includes_charger", "true"}});

        addProduct(products, 63, "Samsung Galaxy Z Flip 5 (8GB, 256GB, Foldable AMOLED)", "smartphones",
                "6.7-inch Dynamic AMOLED 2X 120Hz Inner Folding Screen + 3.4-inch Flex Window Cover Screen, Snapdragon 8 Gen 2 for Galaxy, Armor Aluminum Frame.",
                69999.00, 99999.00, 10, "images/prod-63.png", 4.7, 95, true, "Pocket Foldable", 15, 0.0,
                new String[][]{{"device_type", "phone"}, {"screen_size", "6.7"}, {"charging_port", "USB-C"}, {"min_charging_watts", "25"}, {"audio_jack", "false"}, {"includes_charger", "false"}});

        addProduct(products, 64, "OnePlus Open (16GB, 512GB, Dual ProXDR Displays, Emerald)", "smartphones",
                "7.82-inch 120Hz Flexi-fluid AMOLED Inner Display + 6.31-inch Cover Screen, Snapdragon 8 Gen 2, Hasselblad Camera for Mobile, 67W SUPERVOOC.",
                139999.00, 149999.00, 7, "images/prod-64.png", 4.9, 68, true, "Ultimate Fold", 15, 0.0,
                new String[][]{{"device_type", "phone"}, {"screen_size", "7.82"}, {"charging_port", "USB-C"}, {"min_charging_watts", "67"}, {"audio_jack", "false"}, {"includes_charger", "true"}});

        addProduct(products, 65, "Sony Xperia 1 V (12GB, 256GB, 4K HDR OLED 120Hz, Exmor T)", "smartphones",
                "6.5-inch 21:9 4K HDR OLED 120Hz Display, Next-gen Exmor T for mobile 2-layer transistor pixel sensor, Real optical 85-125mm zoom lens, 3.5mm jack.",
                109999.00, 119999.00, 8, "images/prod-65.png", 4.7, 42, true, "Cinematic 4K", 15, 0.0,
                new String[][]{{"device_type", "phone"}, {"screen_size", "6.5"}, {"charging_port", "USB-C"}, {"min_charging_watts", "30"}, {"audio_jack", "true"}, {"includes_charger", "false"}});

        // =====================================================================
        // SECTION 3: 10 NEW TABLETS (66 to 75)
        // =====================================================================
        addProduct(products, 66, "Apple iPad Pro 13\" M4 (Ultra Retina XDR OLED, 256GB)", "tablets",
                "13-inch Ultra Retina XDR Display with Tandem OLED technology, Apple M4 chip (9-core CPU, 10-core GPU), 256GB Storage, Thunderbolt / USB 4 port.",
                129900.00, 134900.00, 10, "images/prod-66.png", 4.9, 85, true, "Tandem OLED M4", 30, 0.0,
                new String[][]{{"device_type", "tablet"}, {"screen_size", "13.0"}, {"charging_port", "USB-C"}, {"min_charging_watts", "30"}, {"audio_jack", "false"}, {"includes_pen", "false"}});

        addProduct(products, 67, "Samsung Galaxy Tab S9 Ultra (14.6\" Dynamic AMOLED 2X, S-Pen)", "tablets",
                "14.6-inch Dynamic AMOLED 2X 120Hz WQXGA+, Snapdragon 8 Gen 2, 12GB RAM, 256GB Storage, IP68 Water Resistance, Bundled S-Pen included in-box.",
                108999.00, 119999.00, 8, "images/prod-67.png", 4.9, 64, true, "Giant 14.6\" AMOLED", 45, 0.0,
                new String[][]{{"device_type", "tablet"}, {"screen_size", "14.6"}, {"charging_port", "USB-C"}, {"min_charging_watts", "45"}, {"audio_jack", "false"}, {"includes_pen", "true"}});

        addProduct(products, 68, "OnePlus Pad 2 (12.1\" 3K 144Hz, Snapdragon 8 Gen 3, 12GB)", "tablets",
                "12.1-inch 3K 144Hz 7:5 ReadFit Display 900 nits, Snapdragon 8 Gen 3, 12GB RAM, 256GB Storage, 6-Speaker Omnidirectional sound, 67W SUPERVOOC.",
                42999.00, 47999.00, 18, "images/prod-68.png", 4.8, 110, true, "Snapdragon 8 Gen 3", 67, 0.0,
                new String[][]{{"device_type", "tablet"}, {"screen_size", "12.1"}, {"charging_port", "USB-C"}, {"min_charging_watts", "67"}, {"audio_jack", "false"}, {"includes_pen", "false"}});

        addProduct(products, 69, "Xiaomi Pad 6 Pro (11\" 144Hz 2.8K, Snapdragon 8+, 8GB)", "tablets",
                "11-inch 2.8K 144Hz 10-bit Display, Snapdragon 8+ Gen 1, 8GB LPDDR5, 256GB UFS 3.1, Quad stereo speakers with Dolby Atmos, 67W Fast Charging.",
                28999.00, 32999.00, 24, "images/prod-69.png", 4.7, 135, true, "2.8K 144Hz Pro", 67, 0.0,
                new String[][]{{"device_type", "tablet"}, {"screen_size", "11.0"}, {"charging_port", "USB-C"}, {"min_charging_watts", "67"}, {"audio_jack", "false"}, {"includes_pen", "false"}});

        addProduct(products, 70, "Lenovo Tab P12 (12.7\" 3K Display, Quad JBL Speakers, 8GB)", "tablets",
                "12.7-inch 3K LTPS LCD 60Hz, MediaTek Dimensity 7050, 8GB RAM, 256GB Storage, Quad JBL speakers with Dolby Atmos, Includes Lenovo Tab Pen Plus.",
                26999.00, 34999.00, 20, "images/prod-70.png", 4.6, 92, true, "Bundled Pen", 30, 0.0,
                new String[][]{{"device_type", "tablet"}, {"screen_size", "12.7"}, {"charging_port", "USB-C"}, {"min_charging_watts", "30"}, {"audio_jack", "false"}, {"includes_pen", "true"}});

        addProduct(products, 71, "Microsoft Surface Pro 10 (Intel Core Ultra 7, 16GB, 512GB)", "tablets",
                "13-inch PixelSense Flow 120Hz Touchscreen (2880 x 1920), Intel Core Ultra 7 165U with NPU, 16GB LPDDR5X, 512GB SSD, Dual Thunderbolt 4 ports.",
                144990.00, 159990.00, 7, "images/prod-71.png", 4.8, 38, true, "Windows 11 Tablet", 65, 0.0,
                new String[][]{{"device_type", "tablet"}, {"screen_size", "13.0"}, {"charging_port", "USB-C"}, {"min_charging_watts", "65"}, {"audio_jack", "false"}, {"includes_pen", "false"}});

        addProduct(products, 72, "Realme Pad 2 (11.5\" 2K 120Hz, 8GB, 256GB, 33W SUPERVOOC)", "tablets",
                "11.5-inch 2K 120Hz Eye Comfort Display, MediaTek Helio G99 6nm, 8GB RAM + 8GB Dynamic RAM, 256GB Storage, Quad Speakers with Dolby Atmos.",
                17999.00, 21999.00, 30, "images/prod-72.png", 4.5, 160, true, "Budget 2K 120Hz", 33, 0.0,
                new String[][]{{"device_type", "tablet"}, {"screen_size", "11.5"}, {"charging_port", "USB-C"}, {"min_charging_watts", "33"}, {"audio_jack", "false"}, {"includes_pen", "false"}});

        addProduct(products, 73, "Honor Pad 9 (12.1\" 2.5K 120Hz, 8 Speakers, 8GB, 256GB)", "tablets",
                "12.1-inch 2.5K Paper-like Anti-glare Display, Snapdragon 6 Gen 1, 8GB RAM, 256GB ROM, 8-Speaker surround sound system with vocal enhancement.",
                22999.00, 27999.00, 16, "images/prod-73.png", 4.6, 78, true, "8-Speaker Audio", 35, 0.0,
                new String[][]{{"device_type", "tablet"}, {"screen_size", "12.1"}, {"charging_port", "USB-C"}, {"min_charging_watts", "35"}, {"audio_jack", "false"}, {"includes_pen", "false"}});

        addProduct(products, 74, "Apple iPad mini 6 (8.3\" Liquid Retina, A15 Bionic, 64GB)", "tablets",
                "8.3-inch Liquid Retina Display with True Tone and Wide Color P3, Apple A15 Bionic chip with Neural Engine, 12MP Ultra Wide front camera with Center Stage.",
                46900.00, 49900.00, 15, "images/prod-74.png", 4.8, 125, true, "Compact Pocket iPad", 20, 0.0,
                new String[][]{{"device_type", "tablet"}, {"screen_size", "8.3"}, {"charging_port", "USB-C"}, {"min_charging_watts", "20"}, {"audio_jack", "false"}, {"includes_pen", "false"}});

        addProduct(products, 75, "OnePlus Stylo 2 Active Stylus Pen (Ultra-low latency, 4096 levels)", "tablets",
                "Official active stylus pen engineered for OnePlus Pad 2. Features 4096 levels of pressure sensitivity, magnetic charging, and zero-latency haptic feedback.",
                5499.00, 6999.00, 40, "images/prod-75.png", 4.8, 88, true, "Active Stylus", 5, 0.0,
                new String[][]{{"accessory_for", "OnePlus Pad 2"}, {"pressure_levels", "4096"}, {"charging", "Magnetic"}});

        // =====================================================================
        // SECTION 4: 10 NEW WEARABLES (76 to 85)
        // =====================================================================
        addProduct(products, 76, "Garmin Fenix 7 Pro Solar (Multi-band GPS, Sapphire Titanium)", "wearables",
                "1.3-inch Solar charging Power Sapphire lens, Multi-band GPS with SatIQ, Built-in LED flashlight, Endurance score, Up to 22 days of battery life.",
                84990.00, 89990.00, 8, "images/prod-76.png", 4.9, 44, true, "Ultra Expedition", 5, 0.0,
                new String[][]{{"device_type", "wearable"}, {"gps", "Multi-band SatIQ"}, {"battery_days", "22"}, {"sensor", "Elevate Gen 5 Heart Rate & ECG"}});

        addProduct(products, 77, "OnePlus Watch 2 (Dual-Engine Architecture, Wear OS 4)", "wearables",
                "1.43-inch High-res AMOLED 1000 nits, Dual-Engine Architecture (Snapdragon W5 + BES2700), Wear OS 4 by Google with 100-hour Smart Mode battery.",
                21999.00, 24999.00, 25, "images/prod-77.png", 4.8, 130, true, "100hr Battery", 5, 0.0,
                new String[][]{{"device_type", "wearable"}, {"os", "Wear OS 4"}, {"battery_days", "4"}, {"dial_shape", "round"}});

        addProduct(products, 78, "Amazfit Balance (1.5\" AMOLED, Dual-band GPS, Zepp Coach)", "wearables",
                "1.5-inch HD AMOLED 1500 nits, Dual-band Circularly-Polarized GPS, Body Composition measurement, AI Sleep & Fitness Coach, 14-day battery life.",
                19999.00, 24999.00, 20, "images/prod-78.png", 4.7, 95, true, "AI Wellness", 5, 0.0,
                new String[][]{{"device_type", "wearable"}, {"os", "Zepp OS 3.5"}, {"battery_days", "14"}, {"dial_shape", "round"}});

        addProduct(products, 79, "Fitbit Charge 6 (Google Maps, Wallet, ECG, Heart Rate)", "wearables",
                "Advanced fitness and health tracker with built-in Google Maps, Google Wallet, ECG app, YouTube Music controls, and 40+ exercise modes with 7-day battery.",
                13999.00, 14999.00, 22, "images/prod-79.png", 4.6, 112, true, "Google Fitness", 5, 0.0,
                new String[][]{{"device_type", "wearable"}, {"form", "smartband"}, {"battery_days", "7"}, {"sensor", "ECG + SpO2"}});

        addProduct(products, 80, "Noise ColorFit Pro 5 Max (1.96\" AMOLED, Posture Alert)", "wearables",
                "1.96-inch Always-on AMOLED Display, Posture reminder alert, VO2 max tracking, Bluetooth calling with TruSync, Rapid emergency SOS.",
                4499.00, 9999.00, 45, "images/prod-80.png", 4.5, 310, true, "Affordable AMOLED", 5, 0.0,
                new String[][]{{"device_type", "wearable"}, {"display", "1.96 AMOLED"}, {"battery_days", "7"}, {"dial_shape", "square"}});

        addProduct(products, 81, "boAt Lunar Tigre (1.45\" AMOLED, Rugged Metal Casing)", "wearables",
                "1.45-inch Round AMOLED Display, Rugged Zinc alloy metal casing, Functional crown navigation, Advanced Bluetooth Calling, 100+ Sports modes.",
                2999.00, 7999.00, 50, "images/prod-81.png", 4.4, 280, true, "Rugged Metal", 5, 0.0,
                new String[][]{{"device_type", "wearable"}, {"display", "1.45 AMOLED"}, {"battery_days", "5"}, {"dial_shape", "round"}});

        addProduct(products, 82, "Fire-Boltt Invincible Plus (1.43\" AMOLED, 4GB Storage)", "wearables",
                "1.43-inch AMOLED 700 nits, 4GB Local music storage, Connect TWS earbuds directly, 300+ Sports modes, Stainless steel magnetic strap.",
                3999.00, 10999.00, 35, "images/prod-82.png", 4.5, 220, true, "4GB Local Audio", 5, 0.0,
                new String[][]{{"device_type", "wearable"}, {"display", "1.43 AMOLED"}, {"battery_days", "5"}, {"dial_shape", "round"}});

        addProduct(products, 83, "Fastrack Limitless FS1 Pro (1.96\" Super AMOLED Arched)", "wearables",
                "1.96-inch Arched Super AMOLED Display (410x502 resolution), SingleSync Bluetooth calling, NitroFast charging (10 min charge for 1 day).",
                2495.00, 7995.00, 40, "images/prod-83.png", 4.4, 185, true, "Arched AMOLED", 5, 0.0,
                new String[][]{{"device_type", "wearable"}, {"display", "1.96 Curved"}, {"battery_days", "5"}, {"dial_shape", "square"}});

        addProduct(products, 84, "Garmin Forerunner 265 (Running Smartwatch, Touchscreen AMOLED)", "wearables",
                "1.3-inch Colorful AMOLED touchscreen display, Training readiness score, Morning report, Multi-band GNSS with SatIQ, Up to 13 days of battery life.",
                46490.00, 50490.00, 12, "images/prod-84.png", 4.9, 72, true, "Marathon Runner", 5, 0.0,
                new String[][]{{"device_type", "wearable"}, {"gps", "Multi-band GNSS"}, {"battery_days", "13"}, {"dial_shape", "round"}});

        addProduct(products, 85, "Apple Watch Series 9 (45mm GPS, S9 SiP, Double Tap)", "wearables",
                "45mm Always-On Retina 2000 nits display, S9 SiP chip with 4-core Neural Engine, Magic Double Tap gesture, On-device Siri, Precision Finding for iPhone.",
                44900.00, 47900.00, 18, "images/prod-85.png", 4.8, 160, true, "Double Tap S9", 5, 0.0,
                new String[][]{{"device_type", "wearable"}, {"os", "watchOS 10"}, {"battery_days", "1.5"}, {"dial_shape", "square"}});

        // =====================================================================
        // SECTION 5: 10 NEW CHARGERS (86 to 95)
        // =====================================================================
        addProduct(products, 86, "UGREEN Nexode 100W 4-Port GaN Fast Desktop Charger", "chargers",
                "100W Max output across 3x USB-C and 1x USB-A ports. Powered by Navitas GaNFast III chip with Thermal Guard 2.0 temperature protection.",
                5499.00, 7999.00, 30, "images/prod-86.png", 4.8, 145, true, "100W 4-Port GaN", 0, 0.0,
                new String[][]{{"max_wattage", "100"}, {"port_type", "USB-C, USB-A"}, {"port_count", "4"}, {"technology", "GaNFast III"}});

        addProduct(products, 87, "Samsung 45W Power Adapter with 5A Type-C Cable", "chargers",
                "Official Samsung 45W Super Fast Charging 2.0 adapter with USB PD 3.0 PPS. Bundled with 1.8m 5A high-current USB-C to USB-C cable.",
                2999.00, 3499.00, 45, "images/prod-87.png", 4.8, 230, true, "Official 45W PPS", 0, 0.0,
                new String[][]{{"max_wattage", "45"}, {"port_type", "USB-C"}, {"port_count", "1"}, {"cable_included", "true"}});

        addProduct(products, 88, "Belkin BoostCharge Pro 65W Dual USB-C GaN Charger", "chargers",
                "65W total output with intelligent Power Delivery PPS sharing. Charges a MacBook at 45W and an iPhone at 20W simultaneously without overheating.",
                3999.00, 4999.00, 35, "images/prod-88.png", 4.8, 115, true, "Dual USB-C 65W", 0, 0.0,
                new String[][]{{"max_wattage", "65"}, {"port_type", "USB-C"}, {"port_count", "2"}, {"technology", "GaN PPS"}});

        addProduct(products, 89, "Stuffcool Neutron 33W Dual Port Mini GaN Charger", "chargers",
                "Ultra-compact thumb-sized 33W GaN charger with Type-C PD 3.0 and Type-A QC 3.0. Made in India with BIS safety certification.",
                1699.00, 2299.00, 50, "images/prod-89.png", 4.6, 180, true, "Made in India", 0, 0.0,
                new String[][]{{"max_wattage", "33"}, {"port_type", "USB-C, USB-A"}, {"port_count", "2"}, {"technology", "GaN"}});

        addProduct(products, 90, "Portronics Adapto 65W GaN Multi-Port Wall Charger", "chargers",
                "65W high-speed charging brick featuring 2x Type-C and 1x USB-A ports. Compatible with Dell XPS, MacBook Air, HP laptops and iPhones.",
                1999.00, 2999.00, 40, "images/prod-90.png", 4.5, 140, true, "Budget 65W GaN", 0, 0.0,
                new String[][]{{"max_wattage", "65"}, {"port_type", "USB-C, USB-A"}, {"port_count", "3"}, {"technology", "GaN"}});

        addProduct(products, 91, "Baseus Blade 100W Ultra-Slim 20000mAh Power Bank", "chargers",
                "100W high-output ultra-thin laptop power bank (only 18mm thin). Digital status display, dual USB-C ports, dual USB-A ports. Charges MacBook Pro at full speed.",
                5999.00, 7999.00, 22, "images/prod-91.png", 4.7, 98, true, "100W Laptop Bank", 0, 0.0,
                new String[][]{{"max_wattage", "100"}, {"capacity_mah", "20000"}, {"port_count", "4"}, {"thickness_mm", "18"}});

        addProduct(products, 92, "Spigen ArcStation Pro 45W Dual USB-C GaN Wall Charger", "chargers",
                "45W GaN power adapter with dual USB-C ports and QuantumBoost smart allocation. Compact foldable plug design for travel convenience.",
                2499.00, 3299.00, 30, "images/prod-92.png", 4.7, 86, true, "Spigen GaN 45W", 0, 0.0,
                new String[][]{{"max_wattage", "45"}, {"port_type", "USB-C"}, {"port_count", "2"}, {"technology", "GaN"}});

        addProduct(products, 93, "Xiaomi 67W SonicCharge 3.0 Combo Fast Charger", "chargers",
                "67W super-fast adapter with Qualcomm Quick Charge 3.0 support and 6A Type-A to Type-C cable included. 10-layer safety surge protection.",
                1999.00, 2499.00, 35, "images/prod-93.png", 4.6, 210, true, "67W SonicCharge", 0, 0.0,
                new String[][]{{"max_wattage", "67"}, {"port_type", "USB-A"}, {"cable_included", "true"}});

        addProduct(products, 94, "Ambrane PowerLit Ultra 100W 25000mAh Laptop Power Bank", "chargers",
                "Heavy-duty 100W Type-C Power Delivery laptop power bank with massive 25000mAh capacity, metallic unibody casing, and airline-approved rating.",
                3999.00, 5999.00, 28, "images/prod-94.png", 4.6, 175, true, "25000mAh Airline Safe", 0, 0.0,
                new String[][]{{"max_wattage", "100"}, {"capacity_mah", "25000"}, {"port_type", "USB-C, USB-A"}});

        addProduct(products, 95, "UGREEN Nexode 140W 3-Port GaN PD 3.1 Fast Charger", "chargers",
                "140W PD 3.1 next-gen supercharger. Delivers full 140W single-port charging for MacBook Pro 16\" via MagSafe 3. Includes 240W braided Type-C cable.",
                8999.00, 11999.00, 15, "images/prod-95.png", 4.9, 74, true, "140W PD 3.1", 0, 0.0,
                new String[][]{{"max_wattage", "140"}, {"port_type", "USB-C, USB-A"}, {"port_count", "3"}, {"technology", "GaN PD 3.1"}});

        // =====================================================================
        // SECTION 6: 10 NEW CABLES (96 to 105)
        // =====================================================================
        addProduct(products, 96, "Anker PowerLine III Flow Silicone Type-C 100W Cable (1.8m)", "cables",
                "Ultra-soft silicone finish that never tangles. 100W Power Delivery rated with 25,000 bend lifespan. Cloud soft touch texture.",
                1299.00, 1799.00, 60, "images/prod-96.png", 4.8, 310, true, "Silicone Soft", 0, 0.0,
                new String[][]{{"connector_from", "USB-C"}, {"connector_to", "USB-C"}, {"max_wattage", "100"}, {"length_m", "1.8"}});

        addProduct(products, 97, "UGREEN Thunderbolt 4 40Gbps 8K 100W Braided Cable (0.8m)", "cables",
                "Intel Thunderbolt 4 certified cable supporting up to 40Gbps data speed, single 8K@60Hz or dual 4K displays, and 100W fast power delivery.",
                2499.00, 3499.00, 40, "images/prod-97.png", 4.9, 140, true, "TB4 40Gbps 8K", 0, 0.0,
                new String[][]{{"connector_from", "USB-C"}, {"connector_to", "USB-C"}, {"data_speed", "40Gbps"}, {"max_wattage", "100"}});

        addProduct(products, 98, "Belkin BoostCharge Pro Flex Braided Type-C to Lightning (2m)", "cables",
                "Double-braided silicone cable with MFi Apple certification, magnetic cable management clip, and 30,000 bend durability rating.",
                1699.00, 2299.00, 50, "images/prod-98.png", 4.8, 160, true, "MFi Braided 2m", 0, 0.0,
                new String[][]{{"connector_from", "USB-C"}, {"connector_to", "Lightning"}, {"apple_certified", "true"}, {"length_m", "2.0"}});

        addProduct(products, 99, "Baseus 8-in-1 USB-C Hub with 4K HDMI, 100W PD & SD Reader", "cables",
                "Multi-port aluminum hub adapter: 4K@60Hz HDMI, 100W PD charging pass-through, 3x USB 3.0 ports (5Gbps), Gigabit RJ45 Ethernet, and SD/TF card slots.",
                3499.00, 4999.00, 35, "images/prod-99.png", 4.7, 185, true, "8-in-1 4K Hub", 0, 0.0,
                new String[][]{{"hdmi", "4K@60Hz"}, {"power_pass", "100W PD"}, {"ethernet", "Gigabit 1000Mbps"}});

        addProduct(products, 100, "Satechi Aluminum Type-C Multi-Port Adapter 4K Gigabit", "cables",
                "Premium space gray unibody aluminum multiport adapter with 4K HDMI, Gigabit Ethernet, 3x USB 3.0 ports, Type-C pass-through charging.",
                5499.00, 6999.00, 25, "images/prod-100.png", 4.8, 92, true, "Space Gray Pro Hub", 0, 0.0,
                new String[][]{{"hdmi", "4K@60Hz"}, {"material", "Brushed Aluminum"}, {"ethernet", "Gigabit"}});

        addProduct(products, 101, "Amazon Basics DisplayPort 1.4 to 8K Cable (2m Gold Plated)", "cables",
                "High-speed DisplayPort 1.4 cable supporting 8K@60Hz and 4K@144Hz HDR gaming, 32.4 Gbps bandwidth, gold-plated corrosion-resistant connectors.",
                899.00, 1499.00, 55, "images/prod-101.png", 4.6, 240, true, "8K Gaming Cable", 0, 0.0,
                new String[][]{{"connector_from", "DisplayPort"}, {"connector_to", "DisplayPort"}, {"resolution", "8K@60Hz"}});

        addProduct(products, 102, "Portronics Konnect CL 20W Type-C to Lightning Braided Cable", "cables",
                "1.2m nylon braided Type-C to 8-pin Lightning fast charging cable with 20W Power Delivery and 480Mbps data sync speed.",
                499.00, 899.00, 70, "images/prod-102.png", 4.5, 290, true, "Budget Lightning", 0, 0.0,
                new String[][]{{"connector_from", "USB-C"}, {"connector_to", "Lightning"}, {"max_wattage", "20"}});

        addProduct(products, 103, "boAt Rugged v3 Extra Tough Type-C 65W Cable (1.5m)", "cables",
                "Indestructible nylon braided 65W fast charging cable with aluminum alloy housing and tangle-free design. Tested for 10,000+ bends.",
                399.00, 799.00, 80, "images/prod-103.png", 4.4, 380, true, "Rugged 65W", 0, 0.0,
                new String[][]{{"connector_from", "USB-C"}, {"connector_to", "USB-C"}, {"max_wattage", "65"}});

        addProduct(products, 104, "UGREEN 3.5mm Aux Male-to-Male Braided Audio Cable (1m)", "cables",
                "Hi-Fi stereo 3.5mm audio cable with 24K gold-plated connectors, pure silver-plated copper core, and double shielding against RF interference.",
                449.00, 799.00, 60, "images/prod-104.png", 4.7, 210, true, "Hi-Fi Aux", 0, 0.0,
                new String[][]{{"connector_from", "3.5mm"}, {"connector_to", "3.5mm"}, {"plating", "24K Gold"}});

        addProduct(products, 105, "Anker 518 USB-C to 4K HDMI Adapter (60Hz Ultra HD)", "cables",
                "Plug-and-play Type-C to HDMI adapter supporting 4K@60Hz resolution, HDCP 2.2 compliant, premium aluminum finish for laptops and iPads.",
                1799.00, 2499.00, 35, "images/prod-105.png", 4.8, 125, true, "4K 60Hz Adapter", 0, 0.0,
                new String[][]{{"connector_from", "USB-C"}, {"connector_to", "HDMI"}, {"resolution", "4K@60Hz"}});

        // =====================================================================
        // SECTION 7: 10 NEW AUDIO (106 to 115)
        // =====================================================================
        addProduct(products, 106, "Bose QuietComfort Ultra Wireless Noise Cancelling Headphones", "audio",
                "World-class Active Noise Cancellation with Bose Immersive Audio spatial sound, CustomTune acoustic calibration, and up to 24-hour battery life.",
                35900.00, 39900.00, 10, "images/prod-106.png", 4.9, 115, true, "Bose Flagship ANC", 0, 0.0,
                new String[][]{{"audio_type", "wireless"}, {"anc", "true"}, {"battery_hours", "24"}, {"spatial_audio", "Bose Immersive"}});

        addProduct(products, 107, "Sennheiser Momentum 4 Wireless ANC Headphones (60hr Battery)", "audio",
                "Audiophile-grade 42mm transducer system, adaptive noise cancellation, transparency mode, and industry-leading 60-hour marathon battery life.",
                27990.00, 34990.00, 14, "images/prod-107.png", 4.9, 98, true, "60-Hour Battery", 0, 0.0,
                new String[][]{{"audio_type", "wireless"}, {"anc", "true"}, {"battery_hours", "60"}, {"codec", "aptX Adaptive"}});

        addProduct(products, 108, "JBL Tour ONE M2 Adaptive Noise Cancelling Headphones", "audio",
                "True Adaptive Noise Cancelling with 40mm dynamic drivers tuned by JBL Pro Sound, 4-mic crystal clear calls, Smart Ambient, and 50 hours playtime.",
                19999.00, 24999.00, 18, "images/prod-108.png", 4.7, 130, true, "JBL Pro Sound", 0, 0.0,
                new String[][]{{"audio_type", "wireless"}, {"anc", "true"}, {"battery_hours", "50"}, {"driver_mm", "40"}});

        addProduct(products, 109, "Marshall Major IV On-Ear Bluetooth Headphones (80hr Battery)", "audio",
                "Iconic Marshall vintage rock design with custom-tuned 40mm drivers, 80+ hours of wireless playtime, wireless charging capability, multi-directional control knob.",
                12999.00, 14999.00, 20, "images/prod-109.png", 4.8, 215, true, "Vintage Rock 80h", 0, 0.0,
                new String[][]{{"audio_type", "wireless"}, {"anc", "false"}, {"battery_hours", "80"}, {"wireless_charge", "true"}});

        addProduct(products, 110, "Audio-Technica ATH-M50xBT2 Wireless Studio Monitor Headphones", "audio",
                "Legendary M50x studio sound with proprietary 45mm large-aperture drivers, rare earth magnets, AK4331 DAC, LDAC codec support, and 50 hours battery.",
                17990.00, 21990.00, 16, "images/prod-110.png", 4.9, 165, true, "Studio Monitor", 0, 0.0,
                new String[][]{{"audio_type", "wireless"}, {"anc", "false"}, {"battery_hours", "50"}, {"codec", "LDAC Hi-Res"}});

        addProduct(products, 111, "Nothing Ear (a) Hi-Res Wireless Earbuds with Smart ANC", "audio",
                "Transparent acoustic chamber, 11mm custom driver with PMI+TPU diaphragm, 45dB Smart Active Noise Cancellation, LDAC Hi-Res, ChatGPT integration.",
                6999.00, 7999.00, 35, "images/prod-111.png", 4.7, 240, true, "Transparent Hi-Res", 0, 0.0,
                new String[][]{{"audio_type", "wireless"}, {"anc", "true"}, {"battery_hours", "42.5"}, {"codec", "LDAC"}});

        addProduct(products, 112, "Bose QuietComfort Ultra Earbuds with Immersive Audio", "audio",
                "Breakthrough spatialized audio with CustomTune sound personalization, 9 dynamic ANC levels, Snapdragon Sound with aptX Adaptive, IPX4 sweatproof.",
                23900.00, 25900.00, 15, "images/prod-112.png", 4.8, 145, true, "Spatial Earbuds", 0, 0.0,
                new String[][]{{"audio_type", "wireless"}, {"anc", "true"}, {"battery_hours", "24"}, {"spatial_audio", "true"}});

        addProduct(products, 113, "JBL Flip 6 Portable Waterproof Bluetooth Speaker (30W)", "audio",
                "2-way speaker system delivering loud, crystal clear JBL Original Pro Sound with deep bass. IP67 waterproof and dustproof with 12 hours playtime.",
                9999.00, 13999.00, 30, "images/prod-113.png", 4.8, 420, true, "IP67 Waterproof", 0, 0.0,
                new String[][]{{"audio_type", "speaker"}, {"power_watts", "30"}, {"waterproof", "IP67"}, {"battery_hours", "12"}});

        addProduct(products, 114, "boAt Nirvana Ion ANC True Wireless Earbuds (120hr Playtime)", "audio",
                "Crystal Bionic Sound powered by Hifi DSP, 32dB Active Noise Cancellation, Dual EQ modes, Beast Mode ultra-low latency, and massive 120 hours total playback.",
                2499.00, 9990.00, 50, "images/prod-114.png", 4.5, 520, true, "120hr Playtime", 0, 0.0,
                new String[][]{{"audio_type", "wireless"}, {"anc", "true"}, {"battery_hours", "120"}, {"latency", "60ms"}});

        addProduct(products, 115, "Skullcandy Crusher ANC 2 Over-Ear Sensory Bass Headphones", "audio",
                "Patented Adjustable Sensory Bass vibrators, Active Noise Cancelling, Skull-iQ Smart Feature Technology with voice control, and 50 hours battery life.",
                19999.00, 24999.00, 18, "images/prod-115.png", 4.7, 135, true, "Sensory Bass", 0, 0.0,
                new String[][]{{"audio_type", "wireless"}, {"anc", "true"}, {"battery_hours", "50"}, {"feature", "Haptic Sensory Bass"}});

        // =====================================================================
        // SECTION 8: 10 NEW CASES (116 to 125)
        // =====================================================================
        addProduct(products, 116, "Ringke Fusion-X Rugged Bumper Case for Galaxy S24 Ultra", "cases",
                "Semi-transparent rugged back with tough TPU bumper corners, Mil-STD 810G-516.6 drop certification, and dual lanyard strap holes.",
                1499.00, 1999.00, 40, "images/prod-116.png", 4.7, 160, true, "Mil-Grade Drop", 0, 0.0,
                new String[][]{{"case_for", "Samsung Galaxy S24 Ultra"}, {"material", "Polycarbonate + TPU"}, {"magsafe", "false"}});

        addProduct(products, 117, "Caseology Parallax MagSafe Textured Case for iPhone 15 Pro", "cases",
                "Iconic 3D geometric parallax pattern back for enhanced ergonomic grip, integrated MagSafe ring magnets, and dual-layer military grade protection.",
                1799.00, 2499.00, 35, "images/prod-117.png", 4.8, 120, true, "3D Grip MagSafe", 0, 0.0,
                new String[][]{{"case_for", "Apple iPhone 15 Pro"}, {"magsafe", "true"}, {"screen_size", "6.1"}});

        addProduct(products, 118, "OtterBox Defender Series Multi-Layer Heavy Duty Case for iPhone 15", "cases",
                "Legendary rugged protection featuring a solid inner polycarbonate shell and resilient outer slipcover. Port covers block dust and dirt.",
                3999.00, 4999.00, 25, "images/prod-118.png", 4.8, 95, true, "Ultimate Armor", 0, 0.0,
                new String[][]{{"case_for", "Apple iPhone 15"}, {"magsafe", "false"}, {"drop_rating", "4X Military Standard"}});

        addProduct(products, 119, "UAG Monarch Kevlar Rugged Shockproof Case for S24 Ultra", "cases",
                "5-layer handcrafted protection incorporating genuine DuPont Kevlar material, alloy metal hardware, and 25ft drop protection rating.",
                4999.00, 5999.00, 20, "images/prod-119.png", 4.9, 78, true, "DuPont Kevlar 25ft", 0, 0.0,
                new String[][]{{"case_for", "Samsung Galaxy S24 Ultra"}, {"material", "DuPont Kevlar"}, {"drop_feet", "25"}});

        addProduct(products, 120, "Nillkin CamShield Pro Slide Camera Cover Case for iPhone 15 Pro", "cases",
                "Patented sliding camera lens cover protecting your delicate iPhone lenses from scratches and preventing privacy intrusion. Impact resistant airbags.",
                1399.00, 1899.00, 45, "images/prod-120.png", 4.6, 210, true, "Slide Lens Cover", 0, 0.0,
                new String[][]{{"case_for", "Apple iPhone 15 Pro"}, {"magsafe", "false"}, {"feature", "Slide Camera Cover"}});

        addProduct(products, 121, "Torras 360 Spin Magnetic Stand Case for iPhone 15 Pro Max", "cases",
                "360-degree rotatable aerospace-grade aluminum ring stand with strong Halbach N52 magnets for supercharged MagSafe charging and hands-free viewing.",
                2499.00, 3499.00, 30, "images/prod-121.png", 4.8, 140, true, "360 Spin Stand", 0, 0.0,
                new String[][]{{"case_for", "Apple iPhone 15 Pro Max"}, {"magsafe", "true"}, {"stand", "360 Rotatable"}});

        addProduct(products, 122, "ESR Classic Hybrid Clear Case with Stash Stand for S24", "cases",
                "Crystal clear non-yellowing acrylic back with zinc-alloy camera ring kickstand that adjusts up to 85 degrees for video calls.",
                1499.00, 1999.00, 38, "images/prod-122.png", 4.7, 115, true, "Camera Ring Stand", 0, 0.0,
                new String[][]{{"case_for", "Samsung Galaxy S24"}, {"material", "Non-yellowing Acrylic"}, {"kickstand", "true"}});

        addProduct(products, 123, "Dbrand Grip Case with Textured Skin for Google Pixel 8 Pro", "cases",
                "Microscopic grip textures with clicky tactile buttons, chamfered edge swipes, shock absorbing D3O materials, and precision laser fit.",
                3499.00, 4499.00, 22, "images/prod-123.png", 4.9, 85, true, "Max Grip D3O", 0, 0.0,
                new String[][]{{"case_for", "Google Pixel 8 Pro"}, {"material", "D3O Impact Material"}, {"grip", "Microscopic"}});

        addProduct(products, 124, "DailyObjects Vegan Leatherette MagSafe Case for iPhone 15 Pro", "cases",
                "Cruelty-free handcrafted vegan leather with microfiber inner lining, machined aluminum button covers, and strong built-in MagSafe ring.",
                1899.00, 2499.00, 32, "images/prod-124.png", 4.7, 95, true, "Vegan Leather", 0, 0.0,
                new String[][]{{"case_for", "Apple iPhone 15 Pro"}, {"material", "Vegan Leather"}, {"magsafe", "true"}});

        addProduct(products, 125, "Spigen GlasTR EZ Fit Tempered Glass Screen Protector (2-Pack)", "cases",
                "9H hardness premium tempered glass with innovative auto-alignment tray installation tool. Oleophobic coating resists fingerprints and smudges.",
                1299.00, 1699.00, 60, "images/prod-125.png", 4.8, 380, true, "Auto-Align EZ Fit", 0, 0.0,
                new String[][]{{"fits_devices", "iPhone 15 Pro, S24"}, {"hardness", "9H Tempered Glass"}, {"pack_count", "2"}});

        // =====================================================================
        // SECTION 9: 10 NEW MONITORS (126 to 135)
        // =====================================================================
        addProduct(products, 126, "Samsung Odyssey Neo G8 32\" 4K 240Hz Curved Gaming Monitor", "monitors",
                "32-inch 4K UHD (3840x2160) Quantum Mini-LED 1000R Curved Display, 240Hz refresh rate, 1ms response, Quantum HDR 2000, CoreSync ambient lighting.",
                89999.00, 109999.00, 8, "images/prod-126.png", 4.9, 58, true, "Mini-LED 240Hz 4K", 140, 0.0,
                new String[][]{{"screen_size", "32.0"}, {"resolution", "3840x2160 (4K UHD)"}, {"refresh_rate", "240Hz"}, {"panel_type", "Quantum Mini-LED Curved"}});

        addProduct(products, 127, "Dell UltraSharp 27\" 4K USB-C Hub Monitor (U2723QE IPS Black)", "monitors",
                "27-inch 4K UHD with IPS Black technology delivering 2000:1 contrast ratio, 98% DCI-P3, built-in 90W USB-C hub, RJ45 Ethernet, and KVM switch.",
                54990.00, 64990.00, 15, "images/prod-127.png", 4.9, 88, true, "IPS Black 2000:1", 90, 0.0,
                new String[][]{{"screen_size", "27.0"}, {"resolution", "3840x2160 (4K UHD)"}, {"refresh_rate", "60Hz"}, {"usb_c_pd", "90W"}});

        addProduct(products, 128, "BenQ PD2705U 27\" 4K Designer Monitor (sRGB, Type-C 65W KVM)", "monitors",
                "27-inch 4K UHD IPS Designer monitor factory calibrated for 99% sRGB / Rec.709, Calman verified, Pantone validated, Hotkey Puck G2, 65W USB-C.",
                44990.00, 52990.00, 14, "images/prod-128.png", 4.8, 72, true, "Designer Calibrated", 65, 0.0,
                new String[][]{{"screen_size", "27.0"}, {"resolution", "3840x2160 (4K UHD)"}, {"usb_c_pd", "65W"}, {"pantone_validated", "true"}});

        addProduct(products, 129, "ASUS ROG Swift OLED 32\" 4K 240Hz Gaming Monitor (PG32UCDM)", "monitors",
                "32-inch 4K QD-OLED 240Hz panel with 0.03ms response time, custom heatsink, 99% DCI-P3, Dolby Vision, USB-C 90W PD, uniform brightness mode.",
                129999.00, 149999.00, 6, "images/prod-129.png", 4.9, 45, true, "QD-OLED 240Hz 0.03ms", 120, 0.0,
                new String[][]{{"screen_size", "32.0"}, {"resolution", "3840x2160 (4K QD-OLED)"}, {"refresh_rate", "240Hz"}, {"response_time", "0.03ms"}});

        addProduct(products, 130, "Acer Predator XB283K 28\" 4K UHD 144Hz IPS Gaming Monitor", "monitors",
                "28-inch 4K UHD Agile-Splendor IPS 144Hz 1ms, AMD FreeSync Premium, VESA DisplayHDR 400, Dual HDMI 2.1 for PS5 & Xbox Series X 4K 120Hz.",
                37999.00, 44999.00, 18, "images/prod-130.png", 4.7, 102, true, "Console 4K 120Hz", 55, 0.0,
                new String[][]{{"screen_size", "28.0"}, {"resolution", "3840x2160 (4K UHD)"}, {"refresh_rate", "144Hz"}, {"hdmi_version", "HDMI 2.1"}});

        addProduct(products, 131, "MSI Optix MPG321UR-QD 32\" 4K 144Hz Quantum Dot Gaming Monitor", "monitors",
                "32-inch 4K UHD IPS panel enhanced with Quantum Dot technology, 144Hz, 1ms, HDR 600, Smart Gaming OSD, KVM 3.0, and console mode optimization.",
                41999.00, 49999.00, 16, "images/prod-131.png", 4.7, 85, true, "Quantum Dot 4K", 60, 0.0,
                new String[][]{{"screen_size", "32.0"}, {"resolution", "3840x2160 (4K UHD)"}, {"refresh_rate", "144Hz"}, {"hdr", "DisplayHDR 600"}});

        addProduct(products, 132, "Gigabyte M28U 28\" 4K UHD 144Hz Gaming Monitor with KVM", "monitors",
                "28-inch SuperSpeed IPS 4K 144Hz with integrated KVM switch button to control multiple PCs/laptops with one set of keyboard & mouse, 94% DCI-P3.",
                38499.00, 46999.00, 20, "images/prod-132.png", 4.8, 145, true, "KVM Work & Play", 50, 0.0,
                new String[][]{{"screen_size", "28.0"}, {"resolution", "3840x2160 (4K UHD)"}, {"refresh_rate", "144Hz"}, {"kvm", "true"}});

        addProduct(products, 133, "ViewSonic ColorPro VP2768a 27\" 2K Graphic Design Monitor", "monitors",
                "27-inch 2K QHD (2560x1440) IPS panel, Pantone validated, 100% sRGB, Delta E < 2 color accuracy, USB-C 90W PD, daisy chain connectivity.",
                42999.00, 49999.00, 12, "images/prod-133.png", 4.8, 62, true, "ColorPro Pantone", 90, 0.0,
                new String[][]{{"screen_size", "27.0"}, {"resolution", "2560x1440 (2K QHD)"}, {"color_accuracy", "Delta E < 2"}, {"usb_c_pd", "90W"}});

        addProduct(products, 134, "Philips Evnia 34\" WQHD 165Hz Curved Mini-LED Gaming Monitor", "monitors",
                "34-inch UltraWide WQHD (3440x1440) 1800R curved Mini-LED display with 1,152 local dimming zones, DisplayHDR 1400, Ambiglow lighting, 165Hz.",
                64999.00, 79999.00, 9, "images/prod-134.png", 4.8, 48, true, "UltraWide Mini-LED", 110, 0.0,
                new String[][]{{"screen_size", "34.0"}, {"resolution", "3440x1440 (UltraWide WQHD)"}, {"refresh_rate", "165Hz"}, {"dimming_zones", "1152"}});

        addProduct(products, 135, "Samsung ViewFinity S9 27\" 5K Display with 4K SlimFit Camera", "monitors",
                "27-inch 5K (5120x2880) Matte Display 99% DCI-P3, Smart Calibration with smartphone, Detachable 4K SlimFit camera, Thunderbolt 4 90W.",
                99999.00, 119999.00, 7, "images/prod-135.png", 4.9, 39, true, "5K Studio Display", 90, 0.0,
                new String[][]{{"screen_size", "27.0"}, {"resolution", "5120x2880 (5K)"}, {"camera", "4K SlimFit Detachable"}, {"usb_c_pd", "90W Thunderbolt 4"}});

        // =====================================================================
        // SECTION 10: 10 NEW SMART HOME (136 to 145)
        // =====================================================================
        addProduct(products, 136, "TP-Link Tapo C210 2K 3MP Pan/Tilt Smart Security Wi-Fi Camera", "smart-home",
                "Ultra-high definition 2K (3MP) video, 360-degree horizontal rotation, motion tracking with AI person detection, two-way audio, up to 512GB microSD.",
                2299.00, 3999.00, 50, "images/prod-136.png", 4.7, 340, true, "2K 360 Pan/Tilt", 5, 0.0,
                new String[][]{{"resolution", "2K 3MP"}, {"connectivity", "Wi-Fi 2.4GHz"}, {"storage", "MicroSD up to 512GB"}});

        addProduct(products, 137, "Amazon Echo Show 8 (2nd Gen) Smart Display with Alexa", "smart-home",
                "8-inch HD touchscreen, 13MP camera with auto-framing, stereo speakers with spatial audio, hands-free Alexa smart home dashboard control.",
                8999.00, 13999.00, 25, "images/prod-137.png", 4.8, 190, true, "Alexa 8\" Display", 15, 0.0,
                new String[][]{{"display", "8.0 HD Touch"}, {"assistant", "Amazon Alexa"}, {"camera", "13MP Auto-Framing"}});

        addProduct(products, 138, "Google Nest Hub (2nd Gen) Smart Display with Sleep Sensing", "smart-home",
                "7-inch touch display, Soli Radar motion sensor for contactless Sleep Sensing, room-filling bass, Google Assistant, controls thousands of smart lights.",
                6999.00, 9999.00, 22, "images/prod-138.png", 4.7, 140, true, "Soli Sleep Sensing", 15, 0.0,
                new String[][]{{"display", "7.0 Touch"}, {"assistant", "Google Assistant"}, {"sensor", "Soli Radar Sleep Sensing"}});

        addProduct(products, 139, "Philips Wiz Smart 9W B22 Color & Tunable White LED Bulb (3-Pack)", "smart-home",
                "Pack of 3 Wi-Fi LED smart bulbs offering 16 million colors and dynamic lighting modes. Works directly with your Wi-Fi, Alexa, Google Assistant.",
                1899.00, 2999.00, 60, "images/prod-139.png", 4.6, 280, true, "Pack of 3 Bulbs", 27, 0.0,
                new String[][]{{"wattage", "9W each"}, {"colors", "16 Million"}, {"fitting", "B22 Indian Standard"}, {"hub_required", "false"}});

        addProduct(products, 140, "Xiaomi Smart Air Purifier 4 with True HEPA Filter & OLED Display", "smart-home",
                "High-efficiency CADR of up to 400 m3/h, 3-in-1 filtration eliminating 99.97% of particles down to 0.3 microns, real-time OLED AQI display, App control.",
                13999.00, 16999.00, 20, "images/prod-140.png", 4.8, 175, true, "True HEPA OLED", 30, 800.0,
                new String[][]{{"cadr", "400 m3/h"}, {"filter", "True HEPA 99.97%"}, {"app", "Mi Home / Alexa"}});

        addProduct(products, 141, "Atomberg Renesa 1200mm BLDC Smart Ceiling Fan with Remote & App", "smart-home",
                "Super energy efficient BLDC motor consuming only 28W (saves up to 65% on electricity bills). Controlled via smart remote, Atomberg app, and Alexa.",
                3799.00, 5299.00, 35, "images/prod-141.png", 4.7, 310, true, "28W BLDC Energy Save", 28, 0.0,
                new String[][]{{"motor", "BLDC 28W"}, {"blade_sweep", "1200mm"}, {"smart_control", "App + Remote + Voice"}});

        addProduct(products, 142, "Havells Crabtree Signia 4-Node Smart Wi-Fi Touch Switch Panel", "smart-home",
                "Luxury capacitive glass touch switch panel with 4 on/off switches and fan regulator. Direct Wi-Fi connection without external automation gateways.",
                4299.00, 5999.00, 24, "images/prod-142.png", 4.6, 85, true, "Glass Touch Panel", 5, 0.0,
                new String[][]{{"switches", "4 Nodes + Fan"}, {"material", "Toughened Glass"}, {"connectivity", "Wi-Fi"}});

        addProduct(products, 143, "Qubo Smart Door Lock Ultra by Hero Group (5-in-1 Access)", "smart-home",
                "Heavy-duty smart security door lock with Fingerprint recognition (0.3s), Passcode, RFID Card, Emergency Key, and Qubo Mobile App access.",
                12990.00, 19990.00, 18, "images/prod-143.png", 4.8, 120, true, "5-in-1 Smart Lock", 5, 0.0,
                new String[][]{{"access_modes", "Fingerprint, PIN, RFID, App, Key"}, {"battery", "4x AA up to 1 Year"}});

        addProduct(products, 144, "TP-Link Tapo P110 16A Smart Wi-Fi Plug with Energy Monitoring", "smart-home",
                "Heavy-duty 16A smart plug ideal for geysers, air conditioners, and heaters. Real-time power consumption tracking and energy cost calculator.",
                1199.00, 1999.00, 50, "images/prod-144.png", 4.7, 260, true, "16A Heavy Duty", 2, 0.0,
                new String[][]{{"rating", "16A Heavy Load"}, {"energy_monitor", "true"}, {"app", "Tapo App"}});

        addProduct(products, 145, "Wipro Smart LED Batten 20W Color & White Wi-Fi Light Bar", "smart-home",
                "4-foot 20W smart tube light offering 16 million colors plus tunable white light (2700K to 6500K). Syncs with music and schedules automated timers.",
                1299.00, 1990.00, 40, "images/prod-145.png", 4.5, 180, true, "20W Smart Batten", 20, 0.0,
                new String[][]{{"wattage", "20W"}, {"length", "4 Feet"}, {"colors", "16M RGB + Tunable White"}});

        // =====================================================================
        // SECTION 11: 10 NEW DESK SETUP (146 to 155)
        // =====================================================================
        addProduct(products, 146, "Elgato Stream Deck MK.2 Studio Controller (15 LCD Keys)", "desk-setup",
                "15 customizable LCD keys to trigger actions in apps, control audio, launch media, and switch scenes with visual feedback. Detachable magnetic faceplate.",
                13990.00, 16990.00, 20, "images/prod-146.png", 4.9, 175, true, "15 LCD Keys", 5, 0.0,
                new String[][]{{"keys", "15 Customizable LCD"}, {"interface", "USB 2.0"}, {"stand", "Fixed 45-degree"}});

        addProduct(products, 147, "Rode NT-USB Mini Studio Condenser USB Microphone", "desk-setup",
                "Studio-grade condenser capsule with directional cardioid pickup pattern, built-in pop filter, 360-degree swing mount, and zero-latency headphone monitoring.",
                8990.00, 11990.00, 25, "images/prod-147.png", 4.8, 140, true, "Rode Studio Mic", 5, 0.0,
                new String[][]{{"polar_pattern", "Cardioid"}, {"resolution", "24-bit 48kHz"}, {"headphone_jack", "3.5mm Zero Latency"}});

        addProduct(products, 148, "HyperX QuadCast S RGB USB Condenser Gaming Microphone", "desk-setup",
                "Stunning dynamic RGB lighting with customizable software, built-in anti-vibration shock mount, tap-to-mute sensor with LED indicator, 4 selectable polar patterns.",
                13490.00, 16990.00, 22, "images/prod-148.png", 4.8, 210, true, "Dynamic RGB Mic", 5, 0.0,
                new String[][]{{"polar_patterns", "Stereo, Omnidirectional, Cardioid, Bidirectional"}, {"lighting", "Customizable RGB"}});

        addProduct(products, 149, "Elgato Facecam Full HD 1080p60 Uncompressed Streaming Webcam", "desk-setup",
                "Studio-grade f/2.4 24mm all-glass Elgato Prime Lens, Sony STARVIS CMOS sensor, uncompressed 1080p60 video output with zero video artifacting.",
                13990.00, 16990.00, 15, "images/prod-149.png", 4.8, 88, true, "Sony STARVIS 60fps", 5, 0.0,
                new String[][]{{"resolution", "1080p60 Uncompressed"}, {"sensor", "Sony STARVIS CMOS"}, {"lens", "f/2.4 24mm All-Glass"}});

        addProduct(products, 150, "Shure MV7 USB/XLR Dynamic Broadcast Podcast Microphone", "desk-setup",
                "Legendary broadcast dynamic microphone inspired by the iconic SM7B. Dual USB and XLR outputs, Voice Isolation Technology, touch panel controls.",
                22999.00, 26999.00, 12, "images/prod-150.png", 4.9, 130, true, "Broadcast SM7B Pro", 5, 0.0,
                new String[][]{{"capsule", "Dynamic Voice Isolation"}, {"outputs", "USB & XLR"}, {"software", "ShurePlus MOTIV"}});

        addProduct(products, 151, "Razer Kiyo Pro Streaming Webcam (Adaptive Light Sensor, HDR)", "desk-setup",
                "Ultra-sensitive Type 1/2.8 ultra-sensitive CMOS sensor with STARVIS technology, uncompressed Full HD 1080p 60FPS, HDR enabled at 30FPS.",
                9999.00, 14999.00, 20, "images/prod-151.png", 4.7, 115, true, "Adaptive HDR Cam", 5, 0.0,
                new String[][]{{"resolution", "1080p 60FPS / HDR 30FPS"}, {"field_of_view", "Wide / Medium / Narrow"}});

        addProduct(products, 152, "Elgato Key Light Air Professional Studio LED Edge-lit Panel", "desk-setup",
                "1400 lumens of edge-lit diffused glow that eliminates harsh glare on camera. 80 premium OSRAM LEDs, adjustable color temperature (2900 - 7000K).",
                11990.00, 14990.00, 18, "images/prod-152.png", 4.8, 92, true, "1400 Lumens Studio", 25, 0.0,
                new String[][]{{"brightness", "1400 Lumens"}, {"color_temperature", "2900K - 7000K"}, {"control", "Wi-Fi PC/Mac/iOS/Android"}});

        addProduct(products, 153, "BenQ ScreenBar Halo Wireless Controller LED Monitor Light", "desk-setup",
                "Flagship monitor light bar featuring wireless rotary controller dial, patented asymmetrical optical design, dual front and back ambient backlight.",
                14990.00, 17990.00, 16, "images/prod-153.png", 4.9, 105, true, "Halo Wireless Dial", 6, 0.0,
                new String[][]{{"backlight", "Ambient Dual Light"}, {"controller", "2.4GHz Wireless Precision Dial"}});

        addProduct(products, 154, "Audio-Technica AT2020USB-X Cardioid Condenser USB Microphone", "desk-setup",
                "Reimagining of the acclaimed AT2020 studio microphone with high-resolution 24-bit/96 kHz A/D converter, soft-touch capacitive mute button.",
                12490.00, 15990.00, 20, "images/prod-154.png", 4.8, 95, true, "24-bit 96kHz Studio", 5, 0.0,
                new String[][]{{"polar_pattern", "Cardioid"}, {"sample_rate", "24-bit 96kHz"}, {"mute", "Soft-Touch Capacitive"}});

        addProduct(products, 155, "Logitech Litra Glow Premium Streaming Light with TrueSoft", "desk-setup",
                "TrueSoft technology delivers balanced full-spectrum LED light with cinematic color accuracy for natural skin tones. Includes monitor mount.",
                5495.00, 7995.00, 30, "images/prod-155.png", 4.7, 140, true, "TrueSoft Glow", 10, 0.0,
                new String[][]{{"technology", "Logitech TrueSoft"}, {"color_rendering", "93 CRI"}, {"mount", "3-Way Monitor Mount"}});

        // =====================================================================
        // SECTION 12: 10 NEW GAMING (156 to 165)
        // =====================================================================
        addProduct(products, 156, "Sony DualSense Edge Wireless Controller for PS5 & PC", "gaming",
                "Ultra-customizable pro controller with swappable stick modules, remappable back buttons, adjustable trigger stops, and dedicated profile buttons.",
                17990.00, 19990.00, 12, "images/prod-156.png", 4.9, 85, true, "PS5 Pro Controller", 5, 0.0,
                new String[][]{{"compatibility", "PS5, PC, Mac, iOS"}, {"features", "Swappable Stick Modules, Back Buttons"}});

        addProduct(products, 157, "Razer DeathAdder V3 Pro Ultra-lightweight Wireless Gaming Mouse", "gaming",
                "Refined 63g ultra-lightweight ergonomic shape, Focus Pro 30K Optical Sensor, Gen-3 Optical Mouse Switches, up to 90 hours battery life.",
                12999.00, 15999.00, 25, "images/prod-157.png", 4.9, 190, true, "63g 30K Sensor", 0, 0.0,
                new String[][]{{"weight_grams", "63"}, {"sensor", "Focus Pro 30K"}, {"battery_hours", "90"}, {"wireless", "Razer HyperSpeed"}});

        addProduct(products, 158, "Corsair K70 RGB PRO Mechanical Gaming Keyboard (Cherry MX Red)", "gaming",
                "Iconic aircraft-grade aluminum frame, German Cherry MX Red linear switches, 8000Hz hyper-polling AXON technology, tournament mode switch.",
                12499.00, 15999.00, 20, "images/prod-158.png", 4.8, 160, true, "8000Hz Cherry MX", 5, 0.0,
                new String[][]{{"switches", "Cherry MX Red"}, {"polling_rate", "8000Hz AXON"}, {"frame", "Brushed Aluminum"}});

        addProduct(products, 159, "Logitech G PRO X SUPERLIGHT 2 Wireless Gaming Mouse", "gaming",
                "The world's leading esports mouse refined: LIGHTFORCE hybrid optical-mechanical switches, HERO 2 sensor with 32,000 DPI, 60g weight, USB-C.",
                14995.00, 17995.00, 22, "images/prod-159.png", 4.9, 230, true, "60g Hero 2 Sensor", 0, 0.0,
                new String[][]{{"weight_grams", "60"}, {"sensor", "HERO 2 32K DPI"}, {"battery_hours", "95"}, {"switches", "LIGHTFORCE Hybrid"}});

        addProduct(products, 160, "HyperX Cloud III Wireless Gaming Headset (120hr Battery, 53mm)", "gaming",
                "Legendary HyperX memory foam comfort, angled 53mm dynamic drivers tuned for gaming audio, crystal clear 10mm mic, and colossal 120-hour battery life.",
                12990.00, 15990.00, 25, "images/prod-160.png", 4.8, 170, true, "120hr Cloud Comfort", 0, 0.0,
                new String[][]{{"battery_hours", "120"}, {"driver_mm", "53"}, {"microphone", "10mm Detachable with Mesh"}});

        addProduct(products, 161, "ASUS ROG Falchion RX Low Profile 65% Wireless Keyboard", "gaming",
                "Compact 65% gaming keyboard with ROG RX Low-Profile Optical Switches, integrated interactive touch panel, sound dampening silicone foam, tri-mode.",
                9999.00, 12999.00, 18, "images/prod-161.png", 4.7, 95, true, "Low-Profile 65%", 5, 0.0,
                new String[][]{{"switches", "ROG RX Red Optical"}, {"layout", "65%"}, {"battery_hours", "430"}, {"connectivity", "Tri-Mode Wireless"}});

        addProduct(products, 162, "SteelSeries Arctis Nova Pro Wireless Multi-System Headset", "gaming",
                "Hi-Res audio drivers with Active Noise Cancellation, Infinity Power System (2 hot-swappable batteries for limitless gaming), Multi-System Base Station.",
                32999.00, 39999.00, 10, "images/prod-162.png", 4.9, 85, true, "Dual Battery Swappable", 0, 0.0,
                new String[][]{{"anc", "true"}, {"battery", "Infinite Swappable Dual Battery"}, {"hub", "OLED Base Station"}});

        addProduct(products, 163, "Razer BlackShark V2 Pro Wireless Esports Headset (HyperClear)", "gaming",
                "Razer HyperClear Super Wideband Mic, TriForce Titanium 50mm drivers, pro-tuned FPS audio profiles, ultra-soft flowknit memory foam ear cushions.",
                16999.00, 19999.00, 20, "images/prod-163.png", 4.8, 140, true, "Esports Audio Pro", 0, 0.0,
                new String[][]{{"microphone", "HyperClear Super Wideband"}, {"driver_mm", "50 Titanium"}, {"battery_hours", "70"}});

        addProduct(products, 164, "Corsair RM850x 850W 80 Plus Gold Fully Modular ATX Power Supply", "gaming",
                "850 Watts continuous power, 100% all-Japanese 105C capacitors, 135mm magnetic levitation fan with Zero RPM mode, 80 PLUS Gold certified.",
                11999.00, 14999.00, 15, "images/prod-164.png", 4.9, 110, true, "850W Gold Modular", 850, 0.0,
                new String[][]{{"power_watts", "850"}, {"efficiency", "80 PLUS Gold"}, {"modularity", "Fully Modular"}, {"fan", "Magnetic Levitation 135mm"}});

        addProduct(products, 165, "Redragon K552 Kumara Rainbow Backlit Mechanical Gaming Keyboard", "gaming",
                "Tenkeyless 87-key compact mechanical keyboard with custom dustproof blue clicky switches, metal alloy base, rainbow LED backlighting.",
                2799.00, 3999.00, 40, "images/prod-165.png", 4.5, 450, true, "Mechanical Value", 5, 0.0,
                new String[][]{{"switches", "Outemu Blue Clicky"}, {"layout", "Tenkeyless 87 Key"}, {"material", "Metal Plate"}});

        // =====================================================================
        // SECTION 13: 10 NEW POWER (166 to 175)
        // =====================================================================
        addProduct(products, 166, "APC Back-UPS BX600C-IN 600VA / 360W 230V Desktop UPS", "power",
                "Compact desktop line-interactive UPS providing 600VA / 360W battery backup, Automatic Voltage Regulation (AVR), 3 Indian 3-pin output sockets.",
                3299.00, 4299.00, 35, "images/prod-166.png", 4.6, 280, true, "Essential 600VA", 360, 500.0,
                new String[][]{{"va_rating", "600VA"}, {"wattage", "360W"}, {"sockets", "3x Indian 3-pin"}, {"avr", "true"}});

        addProduct(products, 167, "APC Back-UPS Pro BR1000G-IN 1000VA Sine Wave Line-Interactive UPS", "power",
                "Premium 1000VA / 600W battery backup with multifunction LCD status display, Automatic Voltage Regulation, surge protection for gaming rigs.",
                12499.00, 15499.00, 18, "images/prod-167.png", 4.8, 125, true, "1000VA LCD Pro", 600, 1000.0,
                new String[][]{{"va_rating", "1000VA"}, {"wattage", "600W"}, {"display", "Multifunction LCD"}, {"sockets", "6x Outlets"}});

        addProduct(products, 168, "V-Guard Crystal Plus Smart Television Voltage Stabilizer (up to 55\")", "power",
                "Microcontrolled voltage stabilizer specifically engineered to protect Smart TVs up to 55 inches, set-top boxes, and gaming consoles from dangerous surges.",
                2899.00, 3690.00, 30, "images/prod-168.png", 4.7, 195, true, "Smart TV Protection", 250, 0.0,
                new String[][]{{"capacity", "3 Amp"}, {"voltage_range", "90V - 290V"}, {"indicator", "Digital Seven Segment"}});

        addProduct(products, 169, "Microtek Legend 1000VA Heavy-Duty Desktop UPS with AVR", "power",
                "Heavy-duty 1000VA line-interactive UPS featuring twin 12V 7Ah sealed lead-acid batteries, wide input voltage window (140V-300V), and cold start function.",
                5499.00, 6999.00, 24, "images/prod-169.png", 4.6, 140, true, "Dual Battery 1000VA", 600, 800.0,
                new String[][]{{"va_rating", "1000VA"}, {"wattage", "600W"}, {"batteries", "2x 12V 7Ah"}, {"voltage_range", "140V - 300V"}});

        addProduct(products, 170, "Luminous Zelio+ 1100 Pure Sinewave Inverter for Home & Office", "power",
                "India's most intelligent pure sine wave home inverter with 32-bit DSP processor, intuitive LED screen displaying remaining battery backup hours.",
                6799.00, 8499.00, 20, "images/prod-170.png", 4.8, 220, true, "Pure Sine Wave Inverter", 756, 1000.0,
                new String[][]{{"va_rating", "900VA"}, {"wattage", "756W"}, {"wave_type", "Pure Sine Wave"}, {"display", "Hours Remaining LED"}});

        addProduct(products, 171, "Havells 4-Way Heavy Duty Spike & Surge Protector (2m Cord)", "power",
                "4-socket universal surge strip with thermal overload trip shutter, heavy-duty fire retardant plastic casing, high-grade copper contacts, and 2m cable.",
                799.00, 1195.00, 60, "images/prod-171.png", 4.7, 340, true, "Fire Retardant 4-Way", 2500, 0.0,
                new String[][]{{"sockets", "4x Universal"}, {"cord_length_m", "2.0"}, {"max_power", "2500W 10A"}});

        addProduct(products, 172, "Portronics Power Plate 7 with 6 Power Sockets & 4 USB Ports (2500W)", "power",
                "Multi-functional power extension strip equipped with 6 universal AC sockets, 4 USB fast-charging ports (including Type-C), 2500W maximum capacity.",
                1299.00, 1999.00, 45, "images/prod-172.png", 4.6, 210, true, "6 AC + 4 USB 2500W", 2500, 0.0,
                new String[][]{{"sockets", "6 AC Sockets"}, {"usb_ports", "3 USB-A + 1 Type-C"}, {"max_power", "2500W"}});

        addProduct(products, 173, "Syska 4-Way Power Strip Surge Protector with Master Switch", "power",
                "Reliable 4-outlet surge protector with integrated spike suppressor circuit, indicator LED light, reset button, and durable 2-meter cord.",
                649.00, 999.00, 50, "images/prod-173.png", 4.5, 290, true, "Spike Master Switch", 1500, 0.0,
                new String[][]{{"sockets", "4 Sockets"}, {"cord_length_m", "2.0"}, {"surge_protection", "true"}});

        addProduct(products, 174, "Zebronics Zeb-Iron 600VA Line Interactive UPS with Auto Restart", "power",
                "Microcontroller-based 600VA desktop UPS offering automated sleep mode charging, generator compatibility, audible alarms, and auto restart upon AC recovery.",
                2499.00, 3299.00, 35, "images/prod-174.png", 4.4, 180, true, "Auto Restart 600VA", 360, 400.0,
                new String[][]{{"va_rating", "600VA"}, {"wattage", "360W"}, {"auto_restart", "true"}});

        addProduct(products, 175, "Belkin BoostCharge Pro 3-in-1 Wireless Charging Pad with MagSafe 15W", "power",
                "Official Apple Made for MagSafe 15W ultra-fast wireless charging pad for iPhone 15/14, Apple Watch fast charging puck, and AirPods Qi tray.",
                13999.00, 15999.00, 16, "images/prod-175.png", 4.9, 95, true, "Official MagSafe 15W", 25, 0.0,
                new String[][]{{"magsafe_output", "15W Official"}, {"watch_fast_charge", "true"}, {"compatibility", "iPhone 15, Apple Watch, AirPods"}});

        // =====================================================================
        // BUDGET ALTERNATIVES FOR NEW PRODUCTS
        // =====================================================================
        alternatives.add(new ProductDAO.AlternativeMapping(46, 50, 75000.00,
                "Switch from Dell XPS 15 (Rs 1,74,990) to ASUS ZenBook 14 OLED (Rs 99,990). You save Rs 75,000 while enjoying a stunning 3K 120Hz OLED screen and lighter carry weight."));
        alternatives.add(new ProductDAO.AlternativeMapping(55, 49, 204991.00,
                "Switch from Razer Blade 16 (Rs 3,49,990) to Acer Predator Helios 16 (Rs 1,44,999). You save Rs 2,04,991 while retaining extreme i9 RTX gaming horsepower."));
        alternatives.add(new ProductDAO.AlternativeMapping(56, 58, 93000.00,
                "Switch from Samsung Galaxy S24 Ultra (Rs 1,29,999) to Nothing Phone (2) (Rs 36,999). You save Rs 93,000 with clean software, 120Hz OLED, and unique Glyph design."));
        alternatives.add(new ProductDAO.AlternativeMapping(66, 69, 100901.00,
                "Switch from Apple iPad Pro 13\" M4 (Rs 1,29,900) to Xiaomi Pad 6 Pro (Rs 28,999). You save Rs 1,00,901 while enjoying a 2.8K 144Hz screen and Snapdragon 8+ performance."));
        alternatives.add(new ProductDAO.AlternativeMapping(106, 108, 15901.00,
                "Switch from Bose QuietComfort Ultra (Rs 35,900) to JBL Tour ONE M2 (Rs 19,999). You save Rs 15,901 with adaptive noise cancellation and 50-hour battery life."));
        alternatives.add(new ProductDAO.AlternativeMapping(126, 130, 52000.00,
                "Switch from Samsung Odyssey Neo G8 32\" (Rs 89,999) to Acer Predator XB283K 4K (Rs 37,999). You save Rs 52,000 with 4K 144Hz HDMI 2.1 gaming capability."));
    }

    private static void addProduct(Map<Integer, Product> products, int id, String name, String category,
                                   String description, double price, double originalPrice, int stock,
                                   String imageUrl, double rating, int reviewCount, boolean inStock,
                                   String badge, int powerWatts, double annualMaint, String[][] specs) {
        Product p = new Product(id, name, category, description, price, originalPrice, stock, imageUrl, rating, reviewCount, inStock, badge);
        p.setPowerWatts(powerWatts);
        p.setAnnualMaintenanceCost(annualMaint);
        if (specs != null) {
            for (String[] spec : specs) {
                if (spec.length == 2) {
                    p.addSpec(spec[0], spec[1]);
                }
            }
        }
        products.put(id, p);
    }

    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();
        java.util.List<Product> products = dao.getAllProducts();
        try (java.io.PrintWriter out = new java.io.PrintWriter(new java.io.FileWriter("sql/schema.sql", true))) {
            out.println();
            out.println("-- =====================================================================");
            out.println("-- EXTENDED PRODUCTS CATALOGUE (IDs 46 to 175 - 10 New Brands Per Section)");
            out.println("-- =====================================================================");
            for (Product p : products) {
                if (p.getId() >= 46) {
                    String escName = p.getName().replace("'", "''");
                    String escDesc = p.getDescription().replace("'", "''");
                    String badge = p.getBadge() != null ? "'" + p.getBadge().replace("'", "''") + "'" : "NULL";
                    out.printf(java.util.Locale.US, "INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (%d, '%s', '%s', '%s', %.2f, %.2f, %d, '%s', %.1f, %d, %s, %s, %d, %.2f);%n",
                            p.getId(), escName, p.getCategory(), escDesc, p.getPrice(), p.getOriginalPrice(), p.getStock(), p.getImageUrl(), p.getRating(), p.getReviewCount(), p.isFeatured() ? "TRUE" : "FALSE", badge, p.getPowerWatts(), p.getAnnualMaintenanceCost());

                    for (java.util.Map.Entry<String, String> entry : p.getSpecs().entrySet()) {
                        out.printf("INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (%d, '%s', '%s');%n",
                                p.getId(), entry.getKey(), entry.getValue().replace("'", "''"));
                    }
                }
            }
            System.out.println("Appended 130 products (46 to 175) to sql/schema.sql");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
