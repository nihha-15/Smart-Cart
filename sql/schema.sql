-- =====================================================================
-- SmartCart: An Intelligent E-Commerce Cart System (INR ₹ Edition)
-- Database: MySQL
-- Database Schema & 45 Real-World Products across 13 Categories
-- =====================================================================

CREATE DATABASE IF NOT EXISTS smartcart_db;
USE smartcart_db;

DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS product_alternatives;
DROP TABLE IF EXISTS compatibility_rules;
DROP TABLE IF EXISTS product_specs;
DROP TABLE IF EXISTS products;

-- 0. Users Table (Customer Authentication)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(25),
    address TEXT,
    saved_budget DECIMAL(10, 2) DEFAULT 75000.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 1. Products Table (Includes 3-Year TCO power/maintenance attributes)
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    original_price DECIMAL(10, 2),
    stock INT NOT NULL DEFAULT 10,
    image_url VARCHAR(255),
    rating DECIMAL(2, 1) DEFAULT 4.5,
    review_count INT DEFAULT 42,
    featured BOOLEAN DEFAULT FALSE,
    badge VARCHAR(50) DEFAULT NULL,
    power_watts INT DEFAULT 0,
    annual_maintenance DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Product Specifications (Key-Value attributes for rule matching)
CREATE TABLE product_specs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    spec_key VARCHAR(50) NOT NULL,
    spec_value VARCHAR(100) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_prod_spec (product_id, spec_key)
);

-- 3. Compatibility Rules (Declarative rule engine configuration)
CREATE TABLE compatibility_rules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rule_code VARCHAR(50) NOT NULL UNIQUE,
    primary_category VARCHAR(50) NOT NULL,
    accessory_category VARCHAR(50) NOT NULL,
    spec_key VARCHAR(50) NOT NULL,
    expected_spec_value VARCHAR(100),
    severity VARCHAR(20) DEFAULT 'WARNING',
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    suggestion_product_id INT DEFAULT NULL
);

-- 4. Product Alternatives (Cheaper substitutes for Budget Guardian)
CREATE TABLE product_alternatives (
    id INT AUTO_INCREMENT PRIMARY KEY,
    original_product_id INT NOT NULL,
    alternative_product_id INT NOT NULL,
    savings_amount DECIMAL(10, 2) NOT NULL,
    reason TEXT NOT NULL,
    FOREIGN KEY (original_product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (alternative_product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- 5. Orders Table (With 18% Indian GST Breakdown: 9% CGST + 9% SGST)
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT DEFAULT NULL,
    order_number VARCHAR(30) NOT NULL UNIQUE,
    customer_name VARCHAR(100) NOT NULL,
    customer_email VARCHAR(120) NOT NULL,
    customer_phone VARCHAR(25) NOT NULL,
    shipping_address TEXT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    budget_limit DECIMAL(10, 2),
    subtotal DECIMAL(10, 2) NOT NULL,
    cgst DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    sgst DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    tax DECIMAL(10, 2) NOT NULL,
    shipping_cost DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    discount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10, 2) NOT NULL,
    readiness_score INT DEFAULT 100,
    gstin VARCHAR(30) DEFAULT '29AABCU9603R1ZM',
    order_status VARCHAR(30) DEFAULT 'CONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Order Items Table
CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(150) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    item_total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- =====================================================================
-- SEED DATA: Customers, Real-World Products (45 in INR ₹), Specs & Rules
-- =====================================================================

-- Demo Customer Accounts
INSERT INTO users (id, name, email, password, phone, address, saved_budget) VALUES
(1, 'Aditya Sharma', 'aditya@smartcart.in', 'smartcart123', '+91 98765 43210', 'Flat 402, Prestige Tech Park, Outer Ring Road, Kadubeesanahalli, Bengaluru, Karnataka 560103', 85000.00),
(2, 'Alex Morgan', 'alex@college.edu', 'password123', '+91 91234 56789', 'Hostel Block B, Pune, Maharashtra', 50000.00);

-- Category 1: Laptops
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(1, 'Apple MacBook Air 15" M3 (16GB, 512GB SSD)', 'laptops', '15.3-inch Liquid Retina Display with True Tone, Apple M3 chip (8-core CPU, 10-core GPU), 16GB Unified Memory, 512GB SSD. MagSafe 3 and Dual Thunderbolt / USB 4 ports. Requires min 65W/70W fast charging.', 134900.00, 144900.00, 15, 'images/laptop-pro15.svg', 4.9, 142, TRUE, 'Best Seller', 65, 1500.00),
(2, 'ASUS ROG Zephyrus G16 (RTX 4080, i9-14900HX)', 'laptops', '16-inch 2.5K 240Hz OLED ROG Nebula Display, Intel Core i9-14900HX, NVIDIA GeForce RTX 4080 12GB GDDR6, 32GB LPDDR5X, 1TB PCIe 4.0 SSD. High-draw gaming requiring min 100W PD charging.', 169990.00, 189990.00, 8, 'images/laptop-gaming16.svg', 4.9, 88, TRUE, 'Extreme Performance', 180, 2500.00),
(3, 'Apple MacBook Air 13" M2 (8GB, 256GB SSD)', 'laptops', '13.6-inch Liquid Retina Display, Apple M2 chip, 8GB Unified Memory, 256GB SSD, 1080p FaceTime HD camera, fanless silent thermal architecture, all-day 18hr battery.', 89900.00, 99900.00, 20, 'images/laptop-air13.svg', 4.8, 65, FALSE, 'Ultra Portable', 45, 1000.00),
(4, 'Lenovo IdeaPad Slim 3 (Ryzen 5 7520U, 16GB, 512GB)', 'laptops', '15.6-inch FHD Anti-Glare display, AMD Ryzen 5 7520U, 16GB LPDDR5, 512GB NVMe SSD, Dolby Audio, Rapid Charge Boost (2hr runtime in 15min). Student-friendly workhorse.', 44990.00, 51990.00, 25, 'images/laptop-air13.svg', 4.5, 95, FALSE, 'Student Favorite', 45, 600.00);

-- Category 2: Smartphones
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(5, 'Apple iPhone 15 Pro (128GB, Natural Titanium)', 'smartphones', '6.1-inch Super Retina XDR OLED with ProMotion 120Hz, A17 Pro Chip, Grade 5 Aerospace Titanium build, 48MP Pro camera system, USB-C 3.2 Gen 2 (10Gbps). Retail box contains cable only; NO wall adapter or 3.5mm jack.', 119900.00, 134900.00, 30, 'images/phone-15pro.svg', 4.9, 320, TRUE, 'Top Flagship', 20, 1500.00),
(6, 'Apple iPhone 15 (128GB, Blue)', 'smartphones', '6.1-inch Super Retina XDR OLED, Dynamic Island, A16 Bionic chip, 48MP main camera with 2x Telephoto, USB-C. Special SmartCart promotional bundle includes an official 20W charger in-box.', 69900.00, 79900.00, 40, 'images/phone-15.svg', 4.7, 210, FALSE, 'Value Flagship', 20, 1000.00),
(7, 'OnePlus 12 5G (16GB RAM, 512GB Storage)', 'smartphones', 'Snapdragon 8 Gen 3, 6.82-inch 2K 120Hz ProXDR LTPO AMOLED, 4th Gen Hasselblad Camera, 5400mAh battery. Includes official 100W SuperVOOC charger in the retail box.', 64999.00, 69999.00, 35, 'images/phone-15.svg', 4.8, 175, TRUE, 'Value King', 100, 700.00),
(8, 'Xiaomi Redmi Note 13 5G (8GB RAM, 256GB)', 'smartphones', '6.67-inch 120Hz AMOLED, MediaTek Dimensity 6080, 108MP 3x in-sensor zoom camera, dedicated 3.5mm headphone jack, 5000mAh battery with 33W fast charger included in-box.', 16999.00, 19999.00, 50, 'images/phone-lite.svg', 4.5, 98, FALSE, 'Budget Pick', 33, 300.00);

-- Category 3: Tablets & Stylus
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(9, 'Apple iPad Air 11" M2 (128GB, WiFi)', 'tablets', '11-inch Liquid Retina Display with P3 wide color, Apple M2 chip, landscape 12MP Ultra Wide front camera with Center Stage, USB-C Thunderbolt. Apple Pencil NOT included in retail box.', 59900.00, 64900.00, 18, 'images/tablet-pro11.svg', 4.9, 112, TRUE, 'Pro Tablet', 30, 800.00),
(10, 'Samsung Galaxy Tab S9 FE (128GB, S-Pen Included)', 'tablets', '10.9-inch 90Hz display, Exynos 1380, IP68 water & dust resistance, dual speakers tuned by AKG. Official Samsung S-Pen included free inside the retail box.', 34999.00, 39999.00, 22, 'images/tablet-pro11.svg', 4.7, 85, FALSE, 'Stylus Included', 25, 600.00),
(11, 'Apple Pencil Pro (Magnetic Wireless Charging)', 'tablets', 'Barrel roll gyroscope, squeeze gesture sensor, haptic feedback, pixel-perfect precision, tilt sensitivity, magnetic wireless pairing and charging on iPad Air M2 & iPad Pro.', 11900.00, 12900.00, 40, 'images/tablet-stylus.svg', 4.9, 92, FALSE, 'Must-Have Stylus', 0, 0.00),
(12, 'Apple Magic Keyboard for iPad Air 11"', 'tablets', 'Floating cantilever magnetic design, full-size backlit scissor keys with 1mm travel, built-in trackpad for iPadOS gestures, and USB-C pass-through charging.', 14900.00, 17900.00, 25, 'images/tablet-stylus.svg', 4.7, 54, FALSE, 'Productivity', 0, 0.00);

-- Category 4: Wearables / Smartwatches
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(13, 'Apple Watch Ultra 2 (49mm Titanium, GPS + Cellular)', 'wearables', '49mm Grade 5 Aerospace Titanium case, 3000-nit sapphire crystal OLED, S9 SiP with Double Tap gesture, precision dual-frequency GPS, 36hr normal / 72hr low-power battery, 100m water resistance.', 89900.00, 94900.00, 20, 'images/watch-ultra.svg', 4.9, 130, TRUE, 'Rugged Pro', 5, 500.00),
(14, 'Samsung Galaxy Watch 6 (44mm Bluetooth)', 'wearables', '1.5-inch Super AMOLED Sapphire Crystal, Advanced Sleep Coaching, ECG & Blood Pressure monitoring, Body Composition BIA sensor, Wear OS powered by Samsung.', 19999.00, 23999.00, 60, 'images/watch-fit.svg', 4.6, 210, FALSE, 'Fitness Best', 3, 200.00);

-- Category 5: Fast Chargers
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(15, 'Anker 735 65W GaN Dual USB-C Fast Charger', 'chargers', 'Anker GaNPrime technology with PowerIQ 4.0 dynamic power distribution: 2x USB-C + 1x USB-A (65W max). Charges laptops, iPhones, and tablets safely with ActiveShield 2.0 temperature protection.', 3499.00, 4499.00, 80, 'images/charger-65w.svg', 4.8, 240, TRUE, 'Recommended', 0, 0.00),
(16, 'Anker 747 150W 4-Port GaN Desktop Charger', 'chargers', 'GaNPrime 150W desktop charger with 3x USB-C + 1x USB-A. Capable of powering demanding RTX 4080 gaming laptops, MacBooks, and high-performance workstation docks simultaneously.', 7999.00, 9999.00, 45, 'images/charger-100w.svg', 4.9, 110, FALSE, 'Heavy Duty', 0, 0.00),
(17, 'Apple 20W USB-C Power Adapter', 'chargers', 'Official Apple 20W Power Delivery wall adapter for iPhone, iPad, and AirPods. Compact and energy-efficient. Note: NOT sufficient to charge laptops or high-wattage gaming machines.', 1900.00, 2100.00, 100, 'images/charger-20w.svg', 4.6, 280, FALSE, 'Phone Charger', 0, 0.00),
(18, 'Mi 50W 20,000mAh Power Bank 3 Pro', 'chargers', '20,000mAh lithium-polymer capacity with 50W MAX Type-C two-way fast charging. Triple port output capable of powering MacBook Air, Nintendo Switch, and all smartphones on the move.', 3499.00, 4499.00, 50, 'images/powerbank-20k.svg', 4.8, 145, TRUE, 'Travel Essential', 0, 0.00);

-- Category 6: Cables & Adapters
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(19, 'Spigen DuraPro 100W Braided USB-C Cable (2m)', 'cables', 'Heavy-duty double-braided nylon exterior with E-Marker smart chip supporting 100W 5A Power Delivery and 480Mbps data transfer. Tested for 30,000+ bends.', 899.00, 1299.00, 150, 'images/cable-usbc.svg', 4.8, 310, FALSE, 'Best Cable', 0, 0.00),
(20, 'Apple USB-C to Lightning Cable (1m)', 'cables', 'Original Apple MFi-certified cable connecting your iPhone, iPad, or iPod with Lightning connector to your USB-C enabled Mac or iPad for syncing and fast charging.', 1900.00, 2100.00, 75, 'images/cable-lightning.svg', 4.7, 120, FALSE, 'Legacy Lightning', 0, 0.00),
(21, 'Belkin Ultra HD High Speed 8K HDMI 2.1 Cable (2m)', 'cables', 'Official Ultra High Speed HDMI certified, 48Gbps bandwidth supporting 8K@60Hz, 4K@120Hz, eARC, Dolby Vision HDR, and VRR for high-end gaming consoles and monitors.', 1499.00, 1999.00, 60, 'images/cable-hdmi.svg', 4.8, 140, FALSE, '4K Gaming', 0, 0.00),
(22, 'Apple USB-C to 3.5mm Headphone Jack Adapter', 'cables', 'Genuine Apple digital-to-analog converter (DAC) dongle allowing standard 3.5mm audio headphones and AUX cables to connect directly to USB-C iPhones, iPads, and MacBooks.', 900.00, 1100.00, 90, 'images/adapter-dac.svg', 4.7, 180, TRUE, 'DAC Adapter', 0, 0.00);

-- Category 7: Audio
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(23, 'Sony WH-1000XM5 Wireless Noise Cancelling Headphones', 'audio', 'Industry-leading noise cancellation with 8 microphones and Auto NC Optimizer, 30mm carbon-fiber driver unit, LDAC Hi-Res Wireless Audio, 30-hour battery life with 3-minute quick charge for 3 hours playback.', 29990.00, 34990.00, 25, 'images/audio-headphones.svg', 4.9, 230, TRUE, 'Audiophile', 4, 400.00),
(24, 'Apple AirPods Pro (2nd Gen, USB-C MagSafe Case)', 'audio', 'Apple H2 headphone chip, up to 2x more Active Noise Cancellation, Adaptive Audio, Transparency mode, Personalized Spatial Audio with dynamic head tracking, dust/sweat/water resistant (IP54).', 24900.00, 26900.00, 70, 'images/audio-earbuds.svg', 4.9, 280, TRUE, 'Top Value TWS', 2, 200.00),
(25, 'OnePlus Bullets Wireless Z2 ANC Bluetooth Neckband', 'audio', '45dB Hybrid Active Noise Cancellation, 12.4mm large dynamic drivers with anti-distortion audio technology, 28 hours battery life, 10-minute Warp Charge gives 20 hours playback.', 1999.00, 2499.00, 85, 'images/audio-earbuds.svg', 4.5, 320, FALSE, 'Sports Neckband', 2, 150.00),
(26, 'Sony MDR-XB55AP 3.5mm Extra Bass Wired Earphones', 'audio', '12mm neodymium driver units delivering punchy club-like bass, inline remote microphone for hands-free calls, tangle-free serrated flat cable with standard gold-plated 3.5mm audio jack. Requires 3.5mm port or DAC dongle.', 1490.00, 1990.00, 45, 'images/audio-wired.svg', 4.4, 85, FALSE, 'Zero Latency', 0, 0.00);

-- Category 8: Protective Cases
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(27, 'Spigen Ultra Hybrid MagFit Case (iPhone 15 Pro, 6.1")', 'cases', 'Clear shock-absorbent TPU bumper with durable PC back, Air Cushion Technology on all corners, integrated strong MagSafe magnet ring designed exclusively for the 6.1-inch iPhone 15 Pro.', 1699.00, 2199.00, 60, 'images/case-15pro.svg', 4.8, 140, FALSE, 'Shockproof', 0, 0.00),
(28, 'Spigen Tough Armor MagFit Case (iPhone 15 Pro Max, 6.7")', 'cases', 'Heavy-duty dual-layer kickstand case with extreme impact foam protection and MagSafe compatibility, engineered specifically for the larger 6.7-inch iPhone 15 Pro Max.', 1999.00, 2499.00, 40, 'images/case-15promax.svg', 4.7, 95, FALSE, 'Oversized 6.7"', 0, 0.00),
(29, 'Spigen Liquid Crystal Case (iPhone 15, 6.1")', 'cases', 'Lightweight flexible TPU case with anti-watermark microdot pattern and reinforced raised bezels around camera and screen, precision tailored for the standard 6.1-inch iPhone 15 dual-camera layout.', 1199.00, 1499.00, 70, 'images/case-15clear.svg', 4.6, 110, FALSE, 'Crystal Clear', 0, 0.00),
(30, 'Spigen Glas.tR AlignMaster Tempered Glass for iPhone 15 Pro (2-Pack)', 'cases', '9H surface hardness oleophobic tempered glass screen protector with auto-alignment installation frame tray for bubble-free application on iPhone 15 Pro.', 999.00, 1399.00, 95, 'images/case-15clear.svg', 4.8, 160, FALSE, 'Screen Shield', 0, 0.00);

-- Category 9: Monitors & Workstation Peripherals
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(31, 'LG UltraGear 27" 4K UHD 144Hz Nano IPS Gaming Monitor (27GR93U)', 'monitors', '27-inch 4K UHD (3840x2160) Nano IPS display, 144Hz refresh rate, 1ms GtG response time, VESA DisplayHDR 400, AMD FreeSync Premium & NVIDIA G-Sync Compatible, HDMI 2.1 x2, DP 1.4. Certified high-speed HDMI cable included in-box.', 39999.00, 49999.00, 15, 'images/monitor-4k.svg', 4.8, 88, TRUE, '4K 144Hz', 48, 500.00),
(32, 'Keychron K2 V2 Wireless Mechanical Keyboard (RGB Hot-Swap)', 'monitors', '75% compact 84-key wireless mechanical keyboard, Gateron G Pro Brown switches, hot-swappable PCB, Mac/Windows layout switchable, 4000mAh battery, connects up to 3 devices via Bluetooth 5.1 + Type-C wired mode.', 7999.00, 9999.00, 35, 'images/keyboard-mech.svg', 4.8, 125, FALSE, 'Mechanical', 0, 0.00),
(33, 'Logitech MX Master 3S Wireless Performance Mouse', 'monitors', 'Quiet clicks with 90% noise reduction, 8000 DPI Darkfield sensor tracks on glass, MagSpeed electromagnetic scroll wheel (1000 lines/sec), ergonomic sculpted thumb rest, USB-C quick charging.', 8995.00, 10995.00, 50, 'images/mouse-ergo.svg', 4.9, 92, FALSE, 'Ergonomic', 0, 0.00);

-- Category 10: Smart Home & IoT
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(34, 'Wipro 16A Smart WiFi Plug with Energy Monitoring', 'smart-home', 'Heavy-duty 16A smart plug for geysers, ACs, and microwaves. Real-time energy consumption telemetry, voice control via Alexa & Google Assistant, programmable timer schedules via Wipro Smart App.', 999.00, 1499.00, 60, 'images/plug-smart.svg', 4.6, 140, TRUE, 'Smart Home', 1, 0.00),
(35, 'Philips Hue Play Smart RGB Light Bar with Power Supply', 'smart-home', 'Versatile smart light bar delivering 16 million colors and tunable white light. Mounts behind TV/monitor for immersive gaming & entertainment sync. Controllable via Hue Bridge, Bluetooth, or voice.', 4999.00, 5999.00, 45, 'images/light-rgb.svg', 4.7, 95, FALSE, 'RGB Ambience', 18, 100.00),
(36, 'Qubo Smart WiFi Video Doorbell (Hero Electronix)', 'smart-home', '2K QHD video resolution, AI Person Detection with instant intruder siren alarm, crystal-clear 2-way talk, full-color night vision, IP65 weatherproof, 6-month rechargeable battery life.', 5490.00, 6990.00, 25, 'images/doorbell-smart.svg', 4.7, 78, TRUE, 'Home Security', 4, 300.00);

-- Category 11: Desk Setup & Streaming Gear
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(37, 'Logitech Brio 4K Ultra HD Pro Streaming Webcam', 'desk-setup', 'Ultra 4K HD video at 30 fps or 1080p at 60 fps, RightLight 3 with HDR auto-exposure, dual omnidirectional noise-cancelling mics, 65°/78°/90° adjustable field of view, Windows Hello facial recognition.', 16995.00, 19995.00, 30, 'images/webcam-4k.svg', 4.8, 110, TRUE, 'Pro Streamer', 5, 0.00),
(38, 'Blue Yeti USB Condenser Microphone for Streaming', 'desk-setup', 'Legendary tri-capsule array with 4 pickup patterns (Cardioid, Omnidirectional, Bidirectional, Stereo). Built-in headphone output with volume control, mic gain control, and instant mute button.', 9999.00, 12999.00, 40, 'images/mic-studio.svg', 4.8, 130, FALSE, 'Hi-Res Audio', 3, 0.00),
(39, 'Amazon Basics Gas Spring Single Monitor Arm Mount', 'desk-setup', 'Premium aircraft-grade aluminum gas-spring arm holding monitors from 13 to 32 inches up to 9kg. Full motion 360° rotation, +75°/-45° tilt, 180° swivel with integrated cable management channels.', 2499.00, 3499.00, 20, 'images/stand-monitor.svg', 4.7, 85, TRUE, 'Ergonomic Desk', 0, 0.00),
(40, 'Anker 575 10-in-1 Dual 4K USB-C Docking Station (100W PD)', 'desk-setup', 'Equipped with 2x HDMI 4K@60Hz, 1x DisplayPort, 1x USB-C 10Gbps data, 1x USB-A 10Gbps, 2x USB-A 480Mbps, 100W USB-C Power Delivery passthrough, Ethernet 1Gbps, and SD/TF card slots.', 8499.00, 10999.00, 35, 'images/dock-10in1.svg', 4.8, 92, TRUE, 'Workstation Hub', 15, 0.00);

-- Category 12: High-Performance Gaming Gear
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(41, 'Microsoft Xbox Wireless Controller (Carbon Black)', 'gaming', 'Sculpted surfaces and refined geometry, hybrid D-pad, textured grip on triggers and bumpers, dedicated Share button, 3.5mm audio jack, AA battery power for up to 40 hours of gameplay.', 5390.00, 5990.00, 50, 'images/controller-wireless.svg', 4.8, 160, TRUE, 'RGB Controller', 3, 100.00),
(42, 'SteelSeries QcK Heavy XXL Gaming Mouse Pad (900x400mm)', 'gaming', 'Extra thick 4mm non-slip rubber base eliminates unwanted movement, exclusive micro-woven QcK cloth optimized for low and high DPI mouse tracking movements.', 2499.00, 3199.00, 80, 'images/deskmat-xl.svg', 4.9, 220, FALSE, 'Waterproof', 0, 0.00),
(43, 'Samsung 990 PRO 2TB PCIe 4.0 NVMe SSD with Heatsink', 'gaming', 'Blistering sequential read/write speeds up to 7,450 / 6,900 MB/s, integrated slim heatsink designed to fit PlayStation 5 and gaming PCs for sustained peak thermal control.', 16999.00, 19999.00, 40, 'images/ssd-nvme.svg', 4.9, 145, TRUE, 'Ultra Fast', 6, 0.00);

-- Category 13: Power Management & Protection
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES
(44, 'Belkin 6-Socket Surge Protector with 30W USB-C PD Fast Port', 'power', 'Heavy-duty 6 universal AC sockets with 650 Joules surge protection, 30W USB-C Power Delivery fast charging port, 1x USB-A port, 2m heavy-duty cord, and connected equipment warranty.', 2299.00, 2999.00, 55, 'images/surge-protector.svg', 4.7, 185, TRUE, 'Surge Protection', 2, 0.00),
(45, 'ESR 3-in-1 MagSafe Foldable Travel Wireless Charging Stand', 'power', 'Officially certified Apple 15W Made for MagSafe fast wireless charging for iPhone 15/14, 5W Apple Watch fast charger, and 5W AirPods magnetic charging pad in an ultra-compact folding aluminum travel form.', 4499.00, 5999.00, 45, 'images/magsafe-stand.svg', 4.8, 120, TRUE, 'MagSafe 15W', 15, 0.00);

-- =====================================================================
-- PRODUCT SPECIFICATIONS
-- =====================================================================
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES
(1, 'device_type', 'laptop'), (1, 'screen_size', '15.3'), (1, 'charging_port', 'USB-C'), (1, 'min_charging_watts', '65'), (1, 'audio_jack', 'true'), (1, 'includes_charger', 'true'),
(2, 'device_type', 'laptop'), (2, 'screen_size', '16.0'), (2, 'charging_port', 'USB-C'), (2, 'min_charging_watts', '100'), (2, 'audio_jack', 'true'), (2, 'includes_charger', 'true'),
(3, 'device_type', 'laptop'), (3, 'screen_size', '13.6'), (3, 'charging_port', 'USB-C'), (3, 'min_charging_watts', '45'), (3, 'audio_jack', 'true'), (3, 'includes_charger', 'true'),
(4, 'device_type', 'laptop'), (4, 'screen_size', '15.6'), (4, 'charging_port', 'USB-C'), (4, 'min_charging_watts', '45'), (4, 'audio_jack', 'true'), (4, 'includes_charger', 'true'),
(5, 'device_type', 'smartphone'), (5, 'model_family', 'iPhone 15 Pro'), (5, 'screen_size', '6.1'), (5, 'charging_port', 'USB-C'), (5, 'min_charging_watts', '20'), (5, 'audio_jack', 'false'), (5, 'includes_charger', 'false'),
(6, 'device_type', 'smartphone'), (6, 'model_family', 'iPhone 15'), (6, 'screen_size', '6.1'), (6, 'charging_port', 'USB-C'), (6, 'min_charging_watts', '20'), (6, 'audio_jack', 'false'), (6, 'includes_charger', 'true'),
(7, 'device_type', 'smartphone'), (7, 'model_family', 'OnePlus 12'), (7, 'screen_size', '6.82'), (7, 'charging_port', 'USB-C'), (7, 'min_charging_watts', '100'), (7, 'audio_jack', 'false'), (7, 'includes_charger', 'true'),
(8, 'device_type', 'smartphone'), (8, 'model_family', 'Redmi Note 13'), (8, 'screen_size', '6.67'), (8, 'charging_port', 'USB-C'), (8, 'min_charging_watts', '33'), (8, 'audio_jack', 'true'), (8, 'includes_charger', 'true'),
(9, 'device_type', 'tablet'), (9, 'screen_size', '11.0'), (9, 'charging_port', 'USB-C'), (9, 'min_charging_watts', '30'), (9, 'includes_stylus', 'false'),
(10, 'device_type', 'tablet'), (10, 'screen_size', '10.9'), (10, 'charging_port', 'USB-C'), (10, 'min_charging_watts', '25'), (10, 'includes_stylus', 'true'),
(11, 'accessory_type', 'stylus'), (11, 'target_device', 'Apple iPad Air 11" M2'),
(12, 'accessory_type', 'keyboard_case'), (12, 'target_device', 'Apple iPad Air 11" M2'),
(13, 'device_type', 'smartwatch'), (13, 'charging_type', 'magnetic_puck'),
(14, 'device_type', 'smartwatch'), (14, 'charging_type', 'magnetic_puck'),
(15, 'output_watts', '65'), (15, 'connector_type', 'USB-C'), (15, 'device_compatibility', 'laptop,smartphone,tablet'),
(16, 'output_watts', '150'), (16, 'connector_type', 'USB-C'), (16, 'device_compatibility', 'laptop,smartphone,tablet'),
(17, 'output_watts', '20'), (17, 'connector_type', 'USB-C'), (17, 'device_compatibility', 'smartphone,earbuds'),
(18, 'output_watts', '50'), (18, 'connector_type', 'USB-C'), (18, 'device_compatibility', 'laptop,smartphone,tablet'),
(19, 'cable_from', 'USB-C'), (19, 'cable_to', 'USB-C'), (19, 'max_watts', '100'),
(20, 'cable_from', 'USB-C'), (20, 'cable_to', 'Lightning'), (20, 'max_watts', '27'),
(21, 'cable_from', 'HDMI'), (21, 'cable_to', 'HDMI'),
(22, 'cable_from', 'USB-C'), (22, 'cable_to', '3.5mm'), (22, 'dac_enabled', 'true'),
(23, 'audio_type', 'wireless_bluetooth'), (23, 'charging_port', 'USB-C'),
(24, 'audio_type', 'wireless_bluetooth'), (24, 'charging_port', 'USB-C'),
(25, 'audio_type', 'wireless_bluetooth'), (25, 'charging_port', 'USB-C'),
(26, 'audio_type', 'wired_3.5mm'), (26, 'connector_type', '3.5mm'),
(27, 'target_model', 'iPhone 15 Pro'), (27, 'target_screen_size', '6.1'),
(28, 'target_model', 'iPhone 15 Pro Max'), (28, 'target_screen_size', '6.7'),
(29, 'target_model', 'iPhone 15'), (29, 'target_screen_size', '6.1'),
(30, 'target_model', 'iPhone 15 Pro'), (30, 'accessory_type', 'screen_protector'),
(31, 'device_type', 'monitor'), (31, 'screen_size', '27.0'), (31, 'resolution', '4K UHD'), (31, 'video_port', 'HDMI,DisplayPort,USB-C'), (31, 'includes_hdmi', 'true'),
(32, 'device_type', 'keyboard'), (32, 'connector_type', 'USB-C'),
(33, 'device_type', 'mouse'), (33, 'connector_type', 'USB-C'),
(34, 'device_type', 'smart_plug'), (34, 'max_amps', '16'), (34, 'voltage', '240V'),
(35, 'device_type', 'light_bar'), (35, 'connector_type', 'USB-C'),
(36, 'device_type', 'video_doorbell'), (36, 'resolution', '2K QHD'),
(37, 'device_type', 'webcam'), (37, 'resolution', '4K UHD'), (37, 'connector_type', 'USB-C'),
(38, 'device_type', 'microphone'), (38, 'polar_pattern', 'multi_pattern'), (38, 'connector_type', 'USB'),
(39, 'device_type', 'monitor_arm'), (39, 'vesa_mount', '75x75,100x100'),
(40, 'device_type', 'docking_station'), (40, 'connector_type', 'USB-C'), (40, 'max_power_passthrough', '100W'),
(41, 'device_type', 'controller'), (41, 'connection', 'wireless_bluetooth'),
(42, 'device_type', 'desk_mat'), (42, 'material', 'hydrophobic_cloth'),
(43, 'device_type', 'nvme_ssd'), (43, 'interface', 'PCIe 4.0 NVMe'), (43, 'capacity', '2TB'),
(44, 'device_type', 'surge_protector'), (44, 'sockets', '6'), (44, 'joules', '650'),
(45, 'device_type', 'wireless_charger'), (45, 'output_watts', '15'), (45, 'charging_type', 'magsafe');

-- =====================================================================
-- COMPATIBILITY RULES
-- =====================================================================
INSERT INTO compatibility_rules (rule_code, primary_category, accessory_category, spec_key, expected_spec_value, severity, title, description, suggestion_product_id) VALUES
('RULE_CHARGER_WATTAGE', 'laptops', 'chargers', 'output_watts', 'min_charging_watts', 'CRITICAL', 'Insufficient Charger Wattage', 'The selected charger does not supply enough power for your laptop. Charging will fail or drain while working.', 15),
('RULE_AUDIO_JACK_MISSING', 'smartphones', 'audio', 'audio_jack', 'true', 'WARNING', 'Missing 3.5mm Headphone Jack', 'The smartphone you selected lacks a 3.5mm analog headphone jack. Wired 3.5mm earphones will require a USB-C DAC adapter to function.', 24),
('RULE_CASE_DIMENSION_MISMATCH', 'smartphones', 'cases', 'target_model', 'model_family', 'CRITICAL', 'Phone Case Model Mismatch', 'The case selected is physically incompatible with your smartphone dimensions (e.g. 6.7-inch Pro Max case for a 6.1-inch phone).', 27),
('RULE_SMARTPHONE_NO_CHARGER', 'smartphones', 'chargers', 'includes_charger', 'false', 'INFO', 'No Wall Charger In Box', 'This smartphone does not come with a power adapter in the box. Consider adding a compatible 65W or 20W USB-C charger.', 15),
('RULE_TABLET_NO_STYLUS', 'tablets', 'tablets', 'includes_stylus', 'false', 'INFO', 'Stylus Pen Not Included', 'This pro tablet supports pressure-sensitive stylus drawing and writing but does not include one in the box.', 11);

-- =====================================================================
-- PRODUCT ALTERNATIVES (Budget Guardian Data in INR ₹)
-- =====================================================================
INSERT INTO product_alternatives (original_product_id, alternative_product_id, savings_amount, reason) VALUES
(2, 3, 80090.00, 'Switch from ASUS ROG Zephyrus G16 (₹1,69,990) to Apple MacBook Air 13" M2 (₹89,900). You save ₹80,090 while gaining 18-hour silent battery life and exceptional build quality.'),
(5, 6, 50000.00, 'Switch from Apple iPhone 15 Pro (₹1,19,900) to Apple iPhone 15 (₹69,900). You save ₹50,000 and receive an in-box 20W charger with the same Dynamic Island experience.'),
(9, 10, 24901.00, 'Switch from Apple iPad Air 11" M2 (₹59,900) to Samsung Galaxy Tab S9 FE (₹34,999). You save ₹24,901 and get the official S-Pen included free in-box.'),
(23, 24, 5090.00, 'Switch from Sony WH-1000XM5 (₹29,990) to Apple AirPods Pro (₹24,900). You save ₹5,090 with pocketable active noise cancellation and spatial audio.'),
(16, 15, 4500.00, 'Switch from Anker 747 150W Charger (₹7,999) to Anker 735 65W GaN Charger (₹3,499). You save ₹4,500 while still charging laptops and phones rapidly.');

-- =====================================================================
-- EXTENDED PRODUCTS CATALOGUE (IDs 46 to 175 - 10 New Brands Per Section)
-- =====================================================================
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (46, 'Dell XPS 15 (Core i7-13700H, 16GB, 512GB, RTX 4050)', 'laptops', '15.6-inch FHD+ InfinityEdge Display, 13th Gen Intel Core i7-13700H, NVIDIA GeForce RTX 4050 6GB GDDR6, 16GB DDR5, 512GB SSD. CNC machined aluminum chassis.', 174990.00, 189990.00, 10, 'images/prod-46.png', 4.7, 74, TRUE, 'Executive Pro', 86, 1800.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (46, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (46, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (46, 'device_type', 'laptop');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (46, 'screen_size', '15.6');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (46, 'min_charging_watts', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (46, 'audio_jack', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (47, 'HP Spectre x360 2-in-1 OLED (Intel Core Ultra 7, 16GB, 1TB)', 'laptops', '14-inch 2.8K 120Hz OLED Touchscreen 2-in-1 convertible, Intel Core Ultra 7 155H with Intel AI Boost, 16GB LPDDR5X, 1TB SSD. Includes HP MPP 2.0 Tilt Pen.', 149999.00, 164999.00, 12, 'images/prod-47.png', 4.8, 92, TRUE, '2-in-1 OLED', 65, 1500.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (47, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (47, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (47, 'device_type', 'laptop');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (47, 'screen_size', '14.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (47, 'min_charging_watts', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (47, 'audio_jack', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (48, 'Lenovo ThinkPad X1 Carbon Gen 12 (Core Ultra 7, 32GB, 1TB)', 'laptops', '14-inch 2.8K OLED 120Hz, Intel Core Ultra 7 155U, 32GB LPDDR5X, 1TB PCIe 4.0 SSD, Carbon fiber chassis, legendary ThinkPad keyboard with TrackPoint.', 159990.00, 179990.00, 8, 'images/prod-48.png', 4.9, 65, TRUE, 'Business Legend', 65, 1200.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (48, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (48, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (48, 'device_type', 'laptop');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (48, 'screen_size', '14.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (48, 'min_charging_watts', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (48, 'audio_jack', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (49, 'Acer Predator Helios 16 (i9-14900HX, RTX 4070, 16GB, 1TB)', 'laptops', '16-inch WQXGA 240Hz 500 nits IPS, Intel Core i9-14900HX, NVIDIA GeForce RTX 4070 8GB GDDR6, 16GB DDR5, 1TB SSD. AeroBlade 3D fan cooling system.', 144999.00, 169999.00, 14, 'images/prod-49.png', 4.7, 115, TRUE, 'Hardcore Gaming', 140, 2000.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (49, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (49, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (49, 'device_type', 'laptop');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (49, 'screen_size', '16.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (49, 'min_charging_watts', '100');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (49, 'audio_jack', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (50, 'ASUS ZenBook 14 OLED (Intel Core Ultra 5, 16GB, 1TB)', 'laptops', '14-inch 3K 120Hz OLED NanoEdge Display, Intel Core Ultra 5 125H, 16GB LPDDR5X, 1TB NVMe SSD. Ultra-slim 1.2kg premium all-metal chassis with 75Wh battery.', 99990.00, 114990.00, 20, 'images/prod-50.png', 4.8, 140, TRUE, 'Slim & Light', 65, 1000.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (50, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (50, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (50, 'device_type', 'laptop');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (50, 'screen_size', '14.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (50, 'min_charging_watts', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (50, 'audio_jack', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (51, 'MSI Katana 15 Gaming (i7-13620H, RTX 4060, 16GB, 1TB)', 'laptops', '15.6-inch FHD 144Hz IPS-level Display, Intel Core i7-13620H, NVIDIA GeForce RTX 4060 8GB GDDR6, 16GB DDR5, 1TB SSD. 4-Zone RGB Gaming Keyboard.', 89990.00, 104990.00, 18, 'images/prod-51.png', 4.6, 98, TRUE, 'Value Gaming', 105, 1500.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (51, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (51, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (51, 'device_type', 'laptop');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (51, 'screen_size', '15.6');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (51, 'min_charging_watts', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (51, 'audio_jack', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (52, 'Samsung Galaxy Book4 Pro 360 (Intel Core Ultra 7, 16GB, 512GB)', 'laptops', '16-inch 3K Dynamic AMOLED 2X Touchscreen 2-in-1, Intel Core Ultra 7 155H, 16GB LPDDR5X, 512GB SSD. Bundled S-Pen and seamless Galaxy ecosystem integration.', 163990.00, 179990.00, 9, 'images/prod-52.png', 4.8, 52, TRUE, 'AMOLED 2-in-1', 65, 1400.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (52, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (52, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (52, 'device_type', 'laptop');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (52, 'screen_size', '16.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (52, 'min_charging_watts', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (52, 'audio_jack', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (53, 'LG Gram 17 Ultra-Lightweight (Intel Core Ultra 7, 16GB, 1TB)', 'laptops', '17-inch WQXGA IPS anti-glare display, Intel Core Ultra 7 155H, 16GB LPDDR5X, 1TB NVMe Gen4 SSD. Featherlight 1.35kg magnesium alloy body with 80Wh battery.', 129990.00, 149990.00, 11, 'images/prod-53.png', 4.7, 63, TRUE, 'Featherlight', 65, 1200.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (53, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (53, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (53, 'device_type', 'laptop');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (53, 'screen_size', '17.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (53, 'min_charging_watts', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (53, 'audio_jack', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (54, 'Microsoft Surface Laptop 6 (Intel Core Ultra 5, 16GB, 512GB)', 'laptops', '13.5-inch PixelSense Touchscreen, Intel Core Ultra 5 135H, 16GB LPDDR5X, 512GB Gen4 SSD. Dual Studio Mics, anti-reflective coating, premium aluminum build.', 134990.00, 149990.00, 15, 'images/prod-54.png', 4.7, 45, TRUE, 'Surface Premium', 65, 1200.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (54, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (54, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (54, 'device_type', 'laptop');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (54, 'screen_size', '13.5');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (54, 'min_charging_watts', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (54, 'audio_jack', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (55, 'Razer Blade 16 (i9-14900HX, RTX 4090, 32GB, 2TB Dual Mini-LED)', 'laptops', '16-inch Dual-Mode Mini-LED (UHD+ 120Hz / FHD+ 240Hz), Intel Core i9-14900HX, NVIDIA GeForce RTX 4090 16GB GDDR6, 32GB DDR5, 2TB PCIe 4.0 SSD. Anodized CNC aluminum unibody.', 349990.00, 379990.00, 5, 'images/prod-55.png', 4.9, 38, TRUE, 'Ultimate Weapon', 175, 3000.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (55, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (55, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (55, 'device_type', 'laptop');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (55, 'screen_size', '16.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (55, 'min_charging_watts', '100');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (55, 'audio_jack', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (56, 'Samsung Galaxy S24 Ultra (12GB, 256GB, Titanium Gray, S-Pen)', 'smartphones', '6.8-inch Dynamic AMOLED 2X 120Hz, Snapdragon 8 Gen 3 for Galaxy, 200MP Quad Telephoto Camera, Titanium Frame, Integrated S-Pen with AI generative editing.', 129999.00, 134999.00, 15, 'images/prod-56.png', 4.9, 210, TRUE, 'Titanium Flagship', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (56, 'includes_charger', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (56, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (56, 'device_type', 'phone');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (56, 'screen_size', '6.8');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (56, 'min_charging_watts', '45');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (56, 'audio_jack', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (57, 'Google Pixel 8 Pro (12GB, 128GB, Bay Blue, Tensor G3)', 'smartphones', '6.7-inch Super Actua LTPO OLED 120Hz, Google Tensor G3 with Titan M2, 50MP Pro Triple Camera with 5x Telephoto, 7 Years of Android OS updates.', 99999.00, 106999.00, 12, 'images/prod-57.png', 4.8, 165, TRUE, 'Pure Google', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (57, 'includes_charger', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (57, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (57, 'device_type', 'phone');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (57, 'screen_size', '6.7');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (57, 'min_charging_watts', '30');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (57, 'audio_jack', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (58, 'Nothing Phone (2) (12GB, 256GB, Dark Grey, Glyph Interface)', 'smartphones', '6.7-inch LTPO OLED 120Hz, Snapdragon 8+ Gen 1, Dual 50MP Sony IMX890 OIS cameras, Iconic transparent back with programmable Glyph Lighting Interface.', 36999.00, 44999.00, 25, 'images/prod-58.png', 4.7, 190, TRUE, 'Design Icon', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (58, 'includes_charger', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (58, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (58, 'device_type', 'phone');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (58, 'screen_size', '6.7');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (58, 'min_charging_watts', '45');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (58, 'audio_jack', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (59, 'Vivo X100 Pro (16GB, 512GB, Zeiss 1-inch Camera)', 'smartphones', '6.78-inch 1.5K LTPO AMOLED 120Hz 3000 nits, MediaTek Dimensity 9300, 50MP Sony IMX989 1-inch Sensor with Zeiss APO Telephoto, 100W FlashCharge.', 89999.00, 96999.00, 14, 'images/prod-59.png', 4.8, 88, TRUE, 'Zeiss Optics', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (59, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (59, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (59, 'device_type', 'phone');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (59, 'screen_size', '6.78');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (59, 'min_charging_watts', '100');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (59, 'audio_jack', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (60, 'Motorola Edge 50 Ultra (16GB, 512GB, Real Wood Back)', 'smartphones', '6.7-inch 1.5K Super HD pOLED 144Hz, Snapdragon 8s Gen 3, Real FSC-certified Nordic Wood back, Pantone-validated cameras, 125W TurboPower charging.', 54999.00, 64999.00, 16, 'images/prod-60.png', 4.7, 72, TRUE, 'Real Wood', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (60, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (60, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (60, 'device_type', 'phone');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (60, 'screen_size', '6.7');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (60, 'min_charging_watts', '125');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (60, 'audio_jack', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (61, 'Realme GT 6 (16GB, 512GB, 6000 nits Ultra Display, 120W)', 'smartphones', '6.78-inch 8T LTPO 6000 nits AMOLED, Snapdragon 8s Gen 3, Sony LYT-808 OIS Camera, 5500mAh Dual-cell battery with 120W SUPERVOOC fast charge.', 44999.00, 49999.00, 22, 'images/prod-61.png', 4.7, 140, TRUE, '6000 Nits', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (61, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (61, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (61, 'device_type', 'phone');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (61, 'screen_size', '6.78');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (61, 'min_charging_watts', '120');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (61, 'audio_jack', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (62, 'iQOO 12 5G (16GB, 512GB, Snapdragon 8 Gen 3, Legend Edition)', 'smartphones', '6.78-inch 1.5K 144Hz AMOLED Display, Snapdragon 8 Gen 3 with dedicated Supercomputing Q1 chip, 50MP 1/1.3" Astrography Camera, 120W FlashCharge.', 57999.00, 64999.00, 18, 'images/prod-62.png', 4.8, 175, TRUE, 'Flagship Beast', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (62, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (62, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (62, 'device_type', 'phone');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (62, 'screen_size', '6.78');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (62, 'min_charging_watts', '120');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (62, 'audio_jack', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (63, 'Samsung Galaxy Z Flip 5 (8GB, 256GB, Foldable AMOLED)', 'smartphones', '6.7-inch Dynamic AMOLED 2X 120Hz Inner Folding Screen + 3.4-inch Flex Window Cover Screen, Snapdragon 8 Gen 2 for Galaxy, Armor Aluminum Frame.', 69999.00, 99999.00, 10, 'images/prod-63.png', 4.7, 95, TRUE, 'Pocket Foldable', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (63, 'includes_charger', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (63, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (63, 'device_type', 'phone');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (63, 'screen_size', '6.7');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (63, 'min_charging_watts', '25');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (63, 'audio_jack', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (64, 'OnePlus Open (16GB, 512GB, Dual ProXDR Displays, Emerald)', 'smartphones', '7.82-inch 120Hz Flexi-fluid AMOLED Inner Display + 6.31-inch Cover Screen, Snapdragon 8 Gen 2, Hasselblad Camera for Mobile, 67W SUPERVOOC.', 139999.00, 149999.00, 7, 'images/prod-64.png', 4.9, 68, TRUE, 'Ultimate Fold', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (64, 'includes_charger', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (64, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (64, 'device_type', 'phone');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (64, 'screen_size', '7.82');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (64, 'min_charging_watts', '67');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (64, 'audio_jack', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (65, 'Sony Xperia 1 V (12GB, 256GB, 4K HDR OLED 120Hz, Exmor T)', 'smartphones', '6.5-inch 21:9 4K HDR OLED 120Hz Display, Next-gen Exmor T for mobile 2-layer transistor pixel sensor, Real optical 85-125mm zoom lens, 3.5mm jack.', 109999.00, 119999.00, 8, 'images/prod-65.png', 4.7, 42, TRUE, 'Cinematic 4K', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (65, 'includes_charger', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (65, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (65, 'device_type', 'phone');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (65, 'screen_size', '6.5');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (65, 'min_charging_watts', '30');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (65, 'audio_jack', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (66, 'Apple iPad Pro 13" M4 (Ultra Retina XDR OLED, 256GB)', 'tablets', '13-inch Ultra Retina XDR Display with Tandem OLED technology, Apple M4 chip (9-core CPU, 10-core GPU), 256GB Storage, Thunderbolt / USB 4 port.', 129900.00, 134900.00, 10, 'images/prod-66.png', 4.9, 85, TRUE, 'Tandem OLED M4', 30, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (66, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (66, 'device_type', 'tablet');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (66, 'screen_size', '13.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (66, 'min_charging_watts', '30');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (66, 'audio_jack', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (66, 'includes_pen', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (67, 'Samsung Galaxy Tab S9 Ultra (14.6" Dynamic AMOLED 2X, S-Pen)', 'tablets', '14.6-inch Dynamic AMOLED 2X 120Hz WQXGA+, Snapdragon 8 Gen 2, 12GB RAM, 256GB Storage, IP68 Water Resistance, Bundled S-Pen included in-box.', 108999.00, 119999.00, 8, 'images/prod-67.png', 4.9, 64, TRUE, 'Giant 14.6" AMOLED', 45, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (67, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (67, 'device_type', 'tablet');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (67, 'screen_size', '14.6');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (67, 'min_charging_watts', '45');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (67, 'audio_jack', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (67, 'includes_pen', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (68, 'OnePlus Pad 2 (12.1" 3K 144Hz, Snapdragon 8 Gen 3, 12GB)', 'tablets', '12.1-inch 3K 144Hz 7:5 ReadFit Display 900 nits, Snapdragon 8 Gen 3, 12GB RAM, 256GB Storage, 6-Speaker Omnidirectional sound, 67W SUPERVOOC.', 42999.00, 47999.00, 18, 'images/prod-68.png', 4.8, 110, TRUE, 'Snapdragon 8 Gen 3', 67, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (68, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (68, 'device_type', 'tablet');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (68, 'screen_size', '12.1');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (68, 'min_charging_watts', '67');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (68, 'audio_jack', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (68, 'includes_pen', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (69, 'Xiaomi Pad 6 Pro (11" 144Hz 2.8K, Snapdragon 8+, 8GB)', 'tablets', '11-inch 2.8K 144Hz 10-bit Display, Snapdragon 8+ Gen 1, 8GB LPDDR5, 256GB UFS 3.1, Quad stereo speakers with Dolby Atmos, 67W Fast Charging.', 28999.00, 32999.00, 24, 'images/prod-69.png', 4.7, 135, TRUE, '2.8K 144Hz Pro', 67, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (69, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (69, 'device_type', 'tablet');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (69, 'screen_size', '11.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (69, 'min_charging_watts', '67');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (69, 'audio_jack', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (69, 'includes_pen', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (70, 'Lenovo Tab P12 (12.7" 3K Display, Quad JBL Speakers, 8GB)', 'tablets', '12.7-inch 3K LTPS LCD 60Hz, MediaTek Dimensity 7050, 8GB RAM, 256GB Storage, Quad JBL speakers with Dolby Atmos, Includes Lenovo Tab Pen Plus.', 26999.00, 34999.00, 20, 'images/prod-70.png', 4.6, 92, TRUE, 'Bundled Pen', 30, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (70, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (70, 'device_type', 'tablet');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (70, 'screen_size', '12.7');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (70, 'min_charging_watts', '30');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (70, 'audio_jack', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (70, 'includes_pen', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (71, 'Microsoft Surface Pro 10 (Intel Core Ultra 7, 16GB, 512GB)', 'tablets', '13-inch PixelSense Flow 120Hz Touchscreen (2880 x 1920), Intel Core Ultra 7 165U with NPU, 16GB LPDDR5X, 512GB SSD, Dual Thunderbolt 4 ports.', 144990.00, 159990.00, 7, 'images/prod-71.png', 4.8, 38, TRUE, 'Windows 11 Tablet', 65, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (71, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (71, 'device_type', 'tablet');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (71, 'screen_size', '13.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (71, 'min_charging_watts', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (71, 'audio_jack', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (71, 'includes_pen', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (72, 'Realme Pad 2 (11.5" 2K 120Hz, 8GB, 256GB, 33W SUPERVOOC)', 'tablets', '11.5-inch 2K 120Hz Eye Comfort Display, MediaTek Helio G99 6nm, 8GB RAM + 8GB Dynamic RAM, 256GB Storage, Quad Speakers with Dolby Atmos.', 17999.00, 21999.00, 30, 'images/prod-72.png', 4.5, 160, TRUE, 'Budget 2K 120Hz', 33, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (72, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (72, 'device_type', 'tablet');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (72, 'screen_size', '11.5');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (72, 'min_charging_watts', '33');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (72, 'audio_jack', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (72, 'includes_pen', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (73, 'Honor Pad 9 (12.1" 2.5K 120Hz, 8 Speakers, 8GB, 256GB)', 'tablets', '12.1-inch 2.5K Paper-like Anti-glare Display, Snapdragon 6 Gen 1, 8GB RAM, 256GB ROM, 8-Speaker surround sound system with vocal enhancement.', 22999.00, 27999.00, 16, 'images/prod-73.png', 4.6, 78, TRUE, '8-Speaker Audio', 35, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (73, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (73, 'device_type', 'tablet');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (73, 'screen_size', '12.1');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (73, 'min_charging_watts', '35');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (73, 'audio_jack', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (73, 'includes_pen', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (74, 'Apple iPad mini 6 (8.3" Liquid Retina, A15 Bionic, 64GB)', 'tablets', '8.3-inch Liquid Retina Display with True Tone and Wide Color P3, Apple A15 Bionic chip with Neural Engine, 12MP Ultra Wide front camera with Center Stage.', 46900.00, 49900.00, 15, 'images/prod-74.png', 4.8, 125, TRUE, 'Compact Pocket iPad', 20, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (74, 'charging_port', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (74, 'device_type', 'tablet');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (74, 'screen_size', '8.3');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (74, 'min_charging_watts', '20');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (74, 'audio_jack', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (74, 'includes_pen', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (75, 'OnePlus Stylo 2 Active Stylus Pen (Ultra-low latency, 4096 levels)', 'tablets', 'Official active stylus pen engineered for OnePlus Pad 2. Features 4096 levels of pressure sensitivity, magnetic charging, and zero-latency haptic feedback.', 5499.00, 6999.00, 40, 'images/prod-75.png', 4.8, 88, TRUE, 'Active Stylus', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (75, 'accessory_for', 'OnePlus Pad 2');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (75, 'charging', 'Magnetic');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (75, 'pressure_levels', '4096');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (76, 'Garmin Fenix 7 Pro Solar (Multi-band GPS, Sapphire Titanium)', 'wearables', '1.3-inch Solar charging Power Sapphire lens, Multi-band GPS with SatIQ, Built-in LED flashlight, Endurance score, Up to 22 days of battery life.', 84990.00, 89990.00, 8, 'images/prod-76.png', 4.9, 44, TRUE, 'Ultra Expedition', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (76, 'device_type', 'wearable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (76, 'battery_days', '22');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (76, 'sensor', 'Elevate Gen 5 Heart Rate & ECG');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (76, 'gps', 'Multi-band SatIQ');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (77, 'OnePlus Watch 2 (Dual-Engine Architecture, Wear OS 4)', 'wearables', '1.43-inch High-res AMOLED 1000 nits, Dual-Engine Architecture (Snapdragon W5 + BES2700), Wear OS 4 by Google with 100-hour Smart Mode battery.', 21999.00, 24999.00, 25, 'images/prod-77.png', 4.8, 130, TRUE, '100hr Battery', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (77, 'os', 'Wear OS 4');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (77, 'dial_shape', 'round');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (77, 'device_type', 'wearable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (77, 'battery_days', '4');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (78, 'Amazfit Balance (1.5" AMOLED, Dual-band GPS, Zepp Coach)', 'wearables', '1.5-inch HD AMOLED 1500 nits, Dual-band Circularly-Polarized GPS, Body Composition measurement, AI Sleep & Fitness Coach, 14-day battery life.', 19999.00, 24999.00, 20, 'images/prod-78.png', 4.7, 95, TRUE, 'AI Wellness', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (78, 'os', 'Zepp OS 3.5');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (78, 'dial_shape', 'round');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (78, 'device_type', 'wearable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (78, 'battery_days', '14');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (79, 'Fitbit Charge 6 (Google Maps, Wallet, ECG, Heart Rate)', 'wearables', 'Advanced fitness and health tracker with built-in Google Maps, Google Wallet, ECG app, YouTube Music controls, and 40+ exercise modes with 7-day battery.', 13999.00, 14999.00, 22, 'images/prod-79.png', 4.6, 112, TRUE, 'Google Fitness', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (79, 'form', 'smartband');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (79, 'device_type', 'wearable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (79, 'battery_days', '7');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (79, 'sensor', 'ECG + SpO2');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (80, 'Noise ColorFit Pro 5 Max (1.96" AMOLED, Posture Alert)', 'wearables', '1.96-inch Always-on AMOLED Display, Posture reminder alert, VO2 max tracking, Bluetooth calling with TruSync, Rapid emergency SOS.', 4499.00, 9999.00, 45, 'images/prod-80.png', 4.5, 310, TRUE, 'Affordable AMOLED', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (80, 'dial_shape', 'square');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (80, 'display', '1.96 AMOLED');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (80, 'device_type', 'wearable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (80, 'battery_days', '7');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (81, 'boAt Lunar Tigre (1.45" AMOLED, Rugged Metal Casing)', 'wearables', '1.45-inch Round AMOLED Display, Rugged Zinc alloy metal casing, Functional crown navigation, Advanced Bluetooth Calling, 100+ Sports modes.', 2999.00, 7999.00, 50, 'images/prod-81.png', 4.4, 280, TRUE, 'Rugged Metal', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (81, 'dial_shape', 'round');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (81, 'display', '1.45 AMOLED');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (81, 'device_type', 'wearable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (81, 'battery_days', '5');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (82, 'Fire-Boltt Invincible Plus (1.43" AMOLED, 4GB Storage)', 'wearables', '1.43-inch AMOLED 700 nits, 4GB Local music storage, Connect TWS earbuds directly, 300+ Sports modes, Stainless steel magnetic strap.', 3999.00, 10999.00, 35, 'images/prod-82.png', 4.5, 220, TRUE, '4GB Local Audio', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (82, 'dial_shape', 'round');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (82, 'display', '1.43 AMOLED');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (82, 'device_type', 'wearable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (82, 'battery_days', '5');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (83, 'Fastrack Limitless FS1 Pro (1.96" Super AMOLED Arched)', 'wearables', '1.96-inch Arched Super AMOLED Display (410x502 resolution), SingleSync Bluetooth calling, NitroFast charging (10 min charge for 1 day).', 2495.00, 7995.00, 40, 'images/prod-83.png', 4.4, 185, TRUE, 'Arched AMOLED', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (83, 'dial_shape', 'square');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (83, 'display', '1.96 Curved');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (83, 'device_type', 'wearable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (83, 'battery_days', '5');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (84, 'Garmin Forerunner 265 (Running Smartwatch, Touchscreen AMOLED)', 'wearables', '1.3-inch Colorful AMOLED touchscreen display, Training readiness score, Morning report, Multi-band GNSS with SatIQ, Up to 13 days of battery life.', 46490.00, 50490.00, 12, 'images/prod-84.png', 4.9, 72, TRUE, 'Marathon Runner', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (84, 'dial_shape', 'round');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (84, 'device_type', 'wearable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (84, 'battery_days', '13');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (84, 'gps', 'Multi-band GNSS');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (85, 'Apple Watch Series 9 (45mm GPS, S9 SiP, Double Tap)', 'wearables', '45mm Always-On Retina 2000 nits display, S9 SiP chip with 4-core Neural Engine, Magic Double Tap gesture, On-device Siri, Precision Finding for iPhone.', 44900.00, 47900.00, 18, 'images/prod-85.png', 4.8, 160, TRUE, 'Double Tap S9', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (85, 'os', 'watchOS 10');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (85, 'dial_shape', 'square');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (85, 'device_type', 'wearable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (85, 'battery_days', '1.5');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (86, 'UGREEN Nexode 100W 4-Port GaN Fast Desktop Charger', 'chargers', '100W Max output across 3x USB-C and 1x USB-A ports. Powered by Navitas GaNFast III chip with Thermal Guard 2.0 temperature protection.', 5499.00, 7999.00, 30, 'images/prod-86.png', 4.8, 145, TRUE, '100W 4-Port GaN', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (86, 'port_type', 'USB-C, USB-A');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (86, 'max_wattage', '100');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (86, 'port_count', '4');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (86, 'technology', 'GaNFast III');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (87, 'Samsung 45W Power Adapter with 5A Type-C Cable', 'chargers', 'Official Samsung 45W Super Fast Charging 2.0 adapter with USB PD 3.0 PPS. Bundled with 1.8m 5A high-current USB-C to USB-C cable.', 2999.00, 3499.00, 45, 'images/prod-87.png', 4.8, 230, TRUE, 'Official 45W PPS', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (87, 'port_type', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (87, 'max_wattage', '45');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (87, 'cable_included', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (87, 'port_count', '1');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (88, 'Belkin BoostCharge Pro 65W Dual USB-C GaN Charger', 'chargers', '65W total output with intelligent Power Delivery PPS sharing. Charges a MacBook at 45W and an iPhone at 20W simultaneously without overheating.', 3999.00, 4999.00, 35, 'images/prod-88.png', 4.8, 115, TRUE, 'Dual USB-C 65W', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (88, 'port_type', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (88, 'max_wattage', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (88, 'port_count', '2');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (88, 'technology', 'GaN PPS');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (89, 'Stuffcool Neutron 33W Dual Port Mini GaN Charger', 'chargers', 'Ultra-compact thumb-sized 33W GaN charger with Type-C PD 3.0 and Type-A QC 3.0. Made in India with BIS safety certification.', 1699.00, 2299.00, 50, 'images/prod-89.png', 4.6, 180, TRUE, 'Made in India', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (89, 'port_type', 'USB-C, USB-A');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (89, 'max_wattage', '33');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (89, 'port_count', '2');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (89, 'technology', 'GaN');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (90, 'Portronics Adapto 65W GaN Multi-Port Wall Charger', 'chargers', '65W high-speed charging brick featuring 2x Type-C and 1x USB-A ports. Compatible with Dell XPS, MacBook Air, HP laptops and iPhones.', 1999.00, 2999.00, 40, 'images/prod-90.png', 4.5, 140, TRUE, 'Budget 65W GaN', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (90, 'port_type', 'USB-C, USB-A');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (90, 'max_wattage', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (90, 'port_count', '3');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (90, 'technology', 'GaN');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (91, 'Baseus Blade 100W Ultra-Slim 20000mAh Power Bank', 'chargers', '100W high-output ultra-thin laptop power bank (only 18mm thin). Digital status display, dual USB-C ports, dual USB-A ports. Charges MacBook Pro at full speed.', 5999.00, 7999.00, 22, 'images/prod-91.png', 4.7, 98, TRUE, '100W Laptop Bank', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (91, 'max_wattage', '100');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (91, 'thickness_mm', '18');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (91, 'capacity_mah', '20000');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (91, 'port_count', '4');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (92, 'Spigen ArcStation Pro 45W Dual USB-C GaN Wall Charger', 'chargers', '45W GaN power adapter with dual USB-C ports and QuantumBoost smart allocation. Compact foldable plug design for travel convenience.', 2499.00, 3299.00, 30, 'images/prod-92.png', 4.7, 86, TRUE, 'Spigen GaN 45W', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (92, 'port_type', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (92, 'max_wattage', '45');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (92, 'port_count', '2');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (92, 'technology', 'GaN');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (93, 'Xiaomi 67W SonicCharge 3.0 Combo Fast Charger', 'chargers', '67W super-fast adapter with Qualcomm Quick Charge 3.0 support and 6A Type-A to Type-C cable included. 10-layer safety surge protection.', 1999.00, 2499.00, 35, 'images/prod-93.png', 4.6, 210, TRUE, '67W SonicCharge', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (93, 'port_type', 'USB-A');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (93, 'max_wattage', '67');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (93, 'cable_included', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (94, 'Ambrane PowerLit Ultra 100W 25000mAh Laptop Power Bank', 'chargers', 'Heavy-duty 100W Type-C Power Delivery laptop power bank with massive 25000mAh capacity, metallic unibody casing, and airline-approved rating.', 3999.00, 5999.00, 28, 'images/prod-94.png', 4.6, 175, TRUE, '25000mAh Airline Safe', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (94, 'port_type', 'USB-C, USB-A');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (94, 'max_wattage', '100');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (94, 'capacity_mah', '25000');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (95, 'UGREEN Nexode 140W 3-Port GaN PD 3.1 Fast Charger', 'chargers', '140W PD 3.1 next-gen supercharger. Delivers full 140W single-port charging for MacBook Pro 16" via MagSafe 3. Includes 240W braided Type-C cable.', 8999.00, 11999.00, 15, 'images/prod-95.png', 4.9, 74, TRUE, '140W PD 3.1', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (95, 'port_type', 'USB-C, USB-A');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (95, 'max_wattage', '140');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (95, 'port_count', '3');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (95, 'technology', 'GaN PD 3.1');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (96, 'Anker PowerLine III Flow Silicone Type-C 100W Cable (1.8m)', 'cables', 'Ultra-soft silicone finish that never tangles. 100W Power Delivery rated with 25,000 bend lifespan. Cloud soft touch texture.', 1299.00, 1799.00, 60, 'images/prod-96.png', 4.8, 310, TRUE, 'Silicone Soft', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (96, 'connector_from', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (96, 'max_wattage', '100');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (96, 'connector_to', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (96, 'length_m', '1.8');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (97, 'UGREEN Thunderbolt 4 40Gbps 8K 100W Braided Cable (0.8m)', 'cables', 'Intel Thunderbolt 4 certified cable supporting up to 40Gbps data speed, single 8K@60Hz or dual 4K displays, and 100W fast power delivery.', 2499.00, 3499.00, 40, 'images/prod-97.png', 4.9, 140, TRUE, 'TB4 40Gbps 8K', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (97, 'connector_from', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (97, 'max_wattage', '100');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (97, 'connector_to', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (97, 'data_speed', '40Gbps');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (98, 'Belkin BoostCharge Pro Flex Braided Type-C to Lightning (2m)', 'cables', 'Double-braided silicone cable with MFi Apple certification, magnetic cable management clip, and 30,000 bend durability rating.', 1699.00, 2299.00, 50, 'images/prod-98.png', 4.8, 160, TRUE, 'MFi Braided 2m', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (98, 'connector_from', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (98, 'connector_to', 'Lightning');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (98, 'apple_certified', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (98, 'length_m', '2.0');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (99, 'Baseus 8-in-1 USB-C Hub with 4K HDMI, 100W PD & SD Reader', 'cables', 'Multi-port aluminum hub adapter: 4K@60Hz HDMI, 100W PD charging pass-through, 3x USB 3.0 ports (5Gbps), Gigabit RJ45 Ethernet, and SD/TF card slots.', 3499.00, 4999.00, 35, 'images/prod-99.png', 4.7, 185, TRUE, '8-in-1 4K Hub', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (99, 'ethernet', 'Gigabit 1000Mbps');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (99, 'hdmi', '4K@60Hz');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (99, 'power_pass', '100W PD');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (100, 'Satechi Aluminum Type-C Multi-Port Adapter 4K Gigabit', 'cables', 'Premium space gray unibody aluminum multiport adapter with 4K HDMI, Gigabit Ethernet, 3x USB 3.0 ports, Type-C pass-through charging.', 5499.00, 6999.00, 25, 'images/prod-100.png', 4.8, 92, TRUE, 'Space Gray Pro Hub', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (100, 'ethernet', 'Gigabit');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (100, 'material', 'Brushed Aluminum');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (100, 'hdmi', '4K@60Hz');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (101, 'Amazon Basics DisplayPort 1.4 to 8K Cable (2m Gold Plated)', 'cables', 'High-speed DisplayPort 1.4 cable supporting 8K@60Hz and 4K@144Hz HDR gaming, 32.4 Gbps bandwidth, gold-plated corrosion-resistant connectors.', 899.00, 1499.00, 55, 'images/prod-101.png', 4.6, 240, TRUE, '8K Gaming Cable', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (101, 'connector_from', 'DisplayPort');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (101, 'connector_to', 'DisplayPort');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (101, 'resolution', '8K@60Hz');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (102, 'Portronics Konnect CL 20W Type-C to Lightning Braided Cable', 'cables', '1.2m nylon braided Type-C to 8-pin Lightning fast charging cable with 20W Power Delivery and 480Mbps data sync speed.', 499.00, 899.00, 70, 'images/prod-102.png', 4.5, 290, TRUE, 'Budget Lightning', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (102, 'connector_from', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (102, 'max_wattage', '20');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (102, 'connector_to', 'Lightning');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (103, 'boAt Rugged v3 Extra Tough Type-C 65W Cable (1.5m)', 'cables', 'Indestructible nylon braided 65W fast charging cable with aluminum alloy housing and tangle-free design. Tested for 10,000+ bends.', 399.00, 799.00, 80, 'images/prod-103.png', 4.4, 380, TRUE, 'Rugged 65W', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (103, 'connector_from', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (103, 'max_wattage', '65');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (103, 'connector_to', 'USB-C');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (104, 'UGREEN 3.5mm Aux Male-to-Male Braided Audio Cable (1m)', 'cables', 'Hi-Fi stereo 3.5mm audio cable with 24K gold-plated connectors, pure silver-plated copper core, and double shielding against RF interference.', 449.00, 799.00, 60, 'images/prod-104.png', 4.7, 210, TRUE, 'Hi-Fi Aux', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (104, 'plating', '24K Gold');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (104, 'connector_from', '3.5mm');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (104, 'connector_to', '3.5mm');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (105, 'Anker 518 USB-C to 4K HDMI Adapter (60Hz Ultra HD)', 'cables', 'Plug-and-play Type-C to HDMI adapter supporting 4K@60Hz resolution, HDCP 2.2 compliant, premium aluminum finish for laptops and iPads.', 1799.00, 2499.00, 35, 'images/prod-105.png', 4.8, 125, TRUE, '4K 60Hz Adapter', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (105, 'connector_from', 'USB-C');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (105, 'connector_to', 'HDMI');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (105, 'resolution', '4K@60Hz');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (106, 'Bose QuietComfort Ultra Wireless Noise Cancelling Headphones', 'audio', 'World-class Active Noise Cancellation with Bose Immersive Audio spatial sound, CustomTune acoustic calibration, and up to 24-hour battery life.', 35900.00, 39900.00, 10, 'images/prod-106.png', 4.9, 115, TRUE, 'Bose Flagship ANC', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (106, 'anc', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (106, 'audio_type', 'wireless');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (106, 'spatial_audio', 'Bose Immersive');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (106, 'battery_hours', '24');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (107, 'Sennheiser Momentum 4 Wireless ANC Headphones (60hr Battery)', 'audio', 'Audiophile-grade 42mm transducer system, adaptive noise cancellation, transparency mode, and industry-leading 60-hour marathon battery life.', 27990.00, 34990.00, 14, 'images/prod-107.png', 4.9, 98, TRUE, '60-Hour Battery', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (107, 'codec', 'aptX Adaptive');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (107, 'anc', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (107, 'audio_type', 'wireless');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (107, 'battery_hours', '60');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (108, 'JBL Tour ONE M2 Adaptive Noise Cancelling Headphones', 'audio', 'True Adaptive Noise Cancelling with 40mm dynamic drivers tuned by JBL Pro Sound, 4-mic crystal clear calls, Smart Ambient, and 50 hours playtime.', 19999.00, 24999.00, 18, 'images/prod-108.png', 4.7, 130, TRUE, 'JBL Pro Sound', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (108, 'anc', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (108, 'driver_mm', '40');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (108, 'audio_type', 'wireless');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (108, 'battery_hours', '50');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (109, 'Marshall Major IV On-Ear Bluetooth Headphones (80hr Battery)', 'audio', 'Iconic Marshall vintage rock design with custom-tuned 40mm drivers, 80+ hours of wireless playtime, wireless charging capability, multi-directional control knob.', 12999.00, 14999.00, 20, 'images/prod-109.png', 4.8, 215, TRUE, 'Vintage Rock 80h', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (109, 'wireless_charge', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (109, 'anc', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (109, 'audio_type', 'wireless');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (109, 'battery_hours', '80');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (110, 'Audio-Technica ATH-M50xBT2 Wireless Studio Monitor Headphones', 'audio', 'Legendary M50x studio sound with proprietary 45mm large-aperture drivers, rare earth magnets, AK4331 DAC, LDAC codec support, and 50 hours battery.', 17990.00, 21990.00, 16, 'images/prod-110.png', 4.9, 165, TRUE, 'Studio Monitor', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (110, 'codec', 'LDAC Hi-Res');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (110, 'anc', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (110, 'audio_type', 'wireless');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (110, 'battery_hours', '50');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (111, 'Nothing Ear (a) Hi-Res Wireless Earbuds with Smart ANC', 'audio', 'Transparent acoustic chamber, 11mm custom driver with PMI+TPU diaphragm, 45dB Smart Active Noise Cancellation, LDAC Hi-Res, ChatGPT integration.', 6999.00, 7999.00, 35, 'images/prod-111.png', 4.7, 240, TRUE, 'Transparent Hi-Res', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (111, 'codec', 'LDAC');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (111, 'anc', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (111, 'audio_type', 'wireless');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (111, 'battery_hours', '42.5');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (112, 'Bose QuietComfort Ultra Earbuds with Immersive Audio', 'audio', 'Breakthrough spatialized audio with CustomTune sound personalization, 9 dynamic ANC levels, Snapdragon Sound with aptX Adaptive, IPX4 sweatproof.', 23900.00, 25900.00, 15, 'images/prod-112.png', 4.8, 145, TRUE, 'Spatial Earbuds', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (112, 'anc', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (112, 'audio_type', 'wireless');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (112, 'spatial_audio', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (112, 'battery_hours', '24');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (113, 'JBL Flip 6 Portable Waterproof Bluetooth Speaker (30W)', 'audio', '2-way speaker system delivering loud, crystal clear JBL Original Pro Sound with deep bass. IP67 waterproof and dustproof with 12 hours playtime.', 9999.00, 13999.00, 30, 'images/prod-113.png', 4.8, 420, TRUE, 'IP67 Waterproof', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (113, 'power_watts', '30');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (113, 'audio_type', 'speaker');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (113, 'waterproof', 'IP67');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (113, 'battery_hours', '12');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (114, 'boAt Nirvana Ion ANC True Wireless Earbuds (120hr Playtime)', 'audio', 'Crystal Bionic Sound powered by Hifi DSP, 32dB Active Noise Cancellation, Dual EQ modes, Beast Mode ultra-low latency, and massive 120 hours total playback.', 2499.00, 9990.00, 50, 'images/prod-114.png', 4.5, 520, TRUE, '120hr Playtime', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (114, 'anc', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (114, 'latency', '60ms');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (114, 'audio_type', 'wireless');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (114, 'battery_hours', '120');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (115, 'Skullcandy Crusher ANC 2 Over-Ear Sensory Bass Headphones', 'audio', 'Patented Adjustable Sensory Bass vibrators, Active Noise Cancelling, Skull-iQ Smart Feature Technology with voice control, and 50 hours battery life.', 19999.00, 24999.00, 18, 'images/prod-115.png', 4.7, 135, TRUE, 'Sensory Bass', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (115, 'feature', 'Haptic Sensory Bass');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (115, 'anc', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (115, 'audio_type', 'wireless');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (115, 'battery_hours', '50');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (116, 'Ringke Fusion-X Rugged Bumper Case for Galaxy S24 Ultra', 'cases', 'Semi-transparent rugged back with tough TPU bumper corners, Mil-STD 810G-516.6 drop certification, and dual lanyard strap holes.', 1499.00, 1999.00, 40, 'images/prod-116.png', 4.7, 160, TRUE, 'Mil-Grade Drop', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (116, 'case_for', 'Samsung Galaxy S24 Ultra');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (116, 'material', 'Polycarbonate + TPU');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (116, 'magsafe', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (117, 'Caseology Parallax MagSafe Textured Case for iPhone 15 Pro', 'cases', 'Iconic 3D geometric parallax pattern back for enhanced ergonomic grip, integrated MagSafe ring magnets, and dual-layer military grade protection.', 1799.00, 2499.00, 35, 'images/prod-117.png', 4.8, 120, TRUE, '3D Grip MagSafe', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (117, 'case_for', 'Apple iPhone 15 Pro');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (117, 'screen_size', '6.1');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (117, 'magsafe', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (118, 'OtterBox Defender Series Multi-Layer Heavy Duty Case for iPhone 15', 'cases', 'Legendary rugged protection featuring a solid inner polycarbonate shell and resilient outer slipcover. Port covers block dust and dirt.', 3999.00, 4999.00, 25, 'images/prod-118.png', 4.8, 95, TRUE, 'Ultimate Armor', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (118, 'case_for', 'Apple iPhone 15');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (118, 'drop_rating', '4X Military Standard');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (118, 'magsafe', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (119, 'UAG Monarch Kevlar Rugged Shockproof Case for S24 Ultra', 'cases', '5-layer handcrafted protection incorporating genuine DuPont Kevlar material, alloy metal hardware, and 25ft drop protection rating.', 4999.00, 5999.00, 20, 'images/prod-119.png', 4.9, 78, TRUE, 'DuPont Kevlar 25ft', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (119, 'case_for', 'Samsung Galaxy S24 Ultra');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (119, 'drop_feet', '25');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (119, 'material', 'DuPont Kevlar');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (120, 'Nillkin CamShield Pro Slide Camera Cover Case for iPhone 15 Pro', 'cases', 'Patented sliding camera lens cover protecting your delicate iPhone lenses from scratches and preventing privacy intrusion. Impact resistant airbags.', 1399.00, 1899.00, 45, 'images/prod-120.png', 4.6, 210, TRUE, 'Slide Lens Cover', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (120, 'case_for', 'Apple iPhone 15 Pro');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (120, 'feature', 'Slide Camera Cover');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (120, 'magsafe', 'false');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (121, 'Torras 360 Spin Magnetic Stand Case for iPhone 15 Pro Max', 'cases', '360-degree rotatable aerospace-grade aluminum ring stand with strong Halbach N52 magnets for supercharged MagSafe charging and hands-free viewing.', 2499.00, 3499.00, 30, 'images/prod-121.png', 4.8, 140, TRUE, '360 Spin Stand', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (121, 'case_for', 'Apple iPhone 15 Pro Max');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (121, 'stand', '360 Rotatable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (121, 'magsafe', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (122, 'ESR Classic Hybrid Clear Case with Stash Stand for S24', 'cases', 'Crystal clear non-yellowing acrylic back with zinc-alloy camera ring kickstand that adjusts up to 85 degrees for video calls.', 1499.00, 1999.00, 38, 'images/prod-122.png', 4.7, 115, TRUE, 'Camera Ring Stand', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (122, 'case_for', 'Samsung Galaxy S24');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (122, 'kickstand', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (122, 'material', 'Non-yellowing Acrylic');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (123, 'Dbrand Grip Case with Textured Skin for Google Pixel 8 Pro', 'cases', 'Microscopic grip textures with clicky tactile buttons, chamfered edge swipes, shock absorbing D3O materials, and precision laser fit.', 3499.00, 4499.00, 22, 'images/prod-123.png', 4.9, 85, TRUE, 'Max Grip D3O', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (123, 'case_for', 'Google Pixel 8 Pro');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (123, 'grip', 'Microscopic');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (123, 'material', 'D3O Impact Material');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (124, 'DailyObjects Vegan Leatherette MagSafe Case for iPhone 15 Pro', 'cases', 'Cruelty-free handcrafted vegan leather with microfiber inner lining, machined aluminum button covers, and strong built-in MagSafe ring.', 1899.00, 2499.00, 32, 'images/prod-124.png', 4.7, 95, TRUE, 'Vegan Leather', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (124, 'case_for', 'Apple iPhone 15 Pro');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (124, 'material', 'Vegan Leather');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (124, 'magsafe', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (125, 'Spigen GlasTR EZ Fit Tempered Glass Screen Protector (2-Pack)', 'cases', '9H hardness premium tempered glass with innovative auto-alignment tray installation tool. Oleophobic coating resists fingerprints and smudges.', 1299.00, 1699.00, 60, 'images/prod-125.png', 4.8, 380, TRUE, 'Auto-Align EZ Fit', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (125, 'hardness', '9H Tempered Glass');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (125, 'fits_devices', 'iPhone 15 Pro, S24');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (125, 'pack_count', '2');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (126, 'Samsung Odyssey Neo G8 32" 4K 240Hz Curved Gaming Monitor', 'monitors', '32-inch 4K UHD (3840x2160) Quantum Mini-LED 1000R Curved Display, 240Hz refresh rate, 1ms response, Quantum HDR 2000, CoreSync ambient lighting.', 89999.00, 109999.00, 8, 'images/prod-126.png', 4.9, 58, TRUE, 'Mini-LED 240Hz 4K', 140, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (126, 'panel_type', 'Quantum Mini-LED Curved');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (126, 'screen_size', '32.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (126, 'refresh_rate', '240Hz');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (126, 'resolution', '3840x2160 (4K UHD)');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (127, 'Dell UltraSharp 27" 4K USB-C Hub Monitor (U2723QE IPS Black)', 'monitors', '27-inch 4K UHD with IPS Black technology delivering 2000:1 contrast ratio, 98% DCI-P3, built-in 90W USB-C hub, RJ45 Ethernet, and KVM switch.', 54990.00, 64990.00, 15, 'images/prod-127.png', 4.9, 88, TRUE, 'IPS Black 2000:1', 90, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (127, 'usb_c_pd', '90W');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (127, 'screen_size', '27.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (127, 'refresh_rate', '60Hz');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (127, 'resolution', '3840x2160 (4K UHD)');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (128, 'BenQ PD2705U 27" 4K Designer Monitor (sRGB, Type-C 65W KVM)', 'monitors', '27-inch 4K UHD IPS Designer monitor factory calibrated for 99% sRGB / Rec.709, Calman verified, Pantone validated, Hotkey Puck G2, 65W USB-C.', 44990.00, 52990.00, 14, 'images/prod-128.png', 4.8, 72, TRUE, 'Designer Calibrated', 65, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (128, 'usb_c_pd', '65W');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (128, 'pantone_validated', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (128, 'screen_size', '27.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (128, 'resolution', '3840x2160 (4K UHD)');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (129, 'ASUS ROG Swift OLED 32" 4K 240Hz Gaming Monitor (PG32UCDM)', 'monitors', '32-inch 4K QD-OLED 240Hz panel with 0.03ms response time, custom heatsink, 99% DCI-P3, Dolby Vision, USB-C 90W PD, uniform brightness mode.', 129999.00, 149999.00, 6, 'images/prod-129.png', 4.9, 45, TRUE, 'QD-OLED 240Hz 0.03ms', 120, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (129, 'screen_size', '32.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (129, 'refresh_rate', '240Hz');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (129, 'response_time', '0.03ms');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (129, 'resolution', '3840x2160 (4K QD-OLED)');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (130, 'Acer Predator XB283K 28" 4K UHD 144Hz IPS Gaming Monitor', 'monitors', '28-inch 4K UHD Agile-Splendor IPS 144Hz 1ms, AMD FreeSync Premium, VESA DisplayHDR 400, Dual HDMI 2.1 for PS5 & Xbox Series X 4K 120Hz.', 37999.00, 44999.00, 18, 'images/prod-130.png', 4.7, 102, TRUE, 'Console 4K 120Hz', 55, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (130, 'screen_size', '28.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (130, 'refresh_rate', '144Hz');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (130, 'resolution', '3840x2160 (4K UHD)');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (130, 'hdmi_version', 'HDMI 2.1');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (131, 'MSI Optix MPG321UR-QD 32" 4K 144Hz Quantum Dot Gaming Monitor', 'monitors', '32-inch 4K UHD IPS panel enhanced with Quantum Dot technology, 144Hz, 1ms, HDR 600, Smart Gaming OSD, KVM 3.0, and console mode optimization.', 41999.00, 49999.00, 16, 'images/prod-131.png', 4.7, 85, TRUE, 'Quantum Dot 4K', 60, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (131, 'hdr', 'DisplayHDR 600');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (131, 'screen_size', '32.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (131, 'refresh_rate', '144Hz');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (131, 'resolution', '3840x2160 (4K UHD)');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (132, 'Gigabyte M28U 28" 4K UHD 144Hz Gaming Monitor with KVM', 'monitors', '28-inch SuperSpeed IPS 4K 144Hz with integrated KVM switch button to control multiple PCs/laptops with one set of keyboard & mouse, 94% DCI-P3.', 38499.00, 46999.00, 20, 'images/prod-132.png', 4.8, 145, TRUE, 'KVM Work & Play', 50, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (132, 'kvm', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (132, 'screen_size', '28.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (132, 'refresh_rate', '144Hz');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (132, 'resolution', '3840x2160 (4K UHD)');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (133, 'ViewSonic ColorPro VP2768a 27" 2K Graphic Design Monitor', 'monitors', '27-inch 2K QHD (2560x1440) IPS panel, Pantone validated, 100% sRGB, Delta E < 2 color accuracy, USB-C 90W PD, daisy chain connectivity.', 42999.00, 49999.00, 12, 'images/prod-133.png', 4.8, 62, TRUE, 'ColorPro Pantone', 90, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (133, 'usb_c_pd', '90W');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (133, 'screen_size', '27.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (133, 'resolution', '2560x1440 (2K QHD)');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (133, 'color_accuracy', 'Delta E < 2');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (134, 'Philips Evnia 34" WQHD 165Hz Curved Mini-LED Gaming Monitor', 'monitors', '34-inch UltraWide WQHD (3440x1440) 1800R curved Mini-LED display with 1,152 local dimming zones, DisplayHDR 1400, Ambiglow lighting, 165Hz.', 64999.00, 79999.00, 9, 'images/prod-134.png', 4.8, 48, TRUE, 'UltraWide Mini-LED', 110, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (134, 'dimming_zones', '1152');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (134, 'screen_size', '34.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (134, 'refresh_rate', '165Hz');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (134, 'resolution', '3440x1440 (UltraWide WQHD)');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (135, 'Samsung ViewFinity S9 27" 5K Display with 4K SlimFit Camera', 'monitors', '27-inch 5K (5120x2880) Matte Display 99% DCI-P3, Smart Calibration with smartphone, Detachable 4K SlimFit camera, Thunderbolt 4 90W.', 99999.00, 119999.00, 7, 'images/prod-135.png', 4.9, 39, TRUE, '5K Studio Display', 90, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (135, 'usb_c_pd', '90W Thunderbolt 4');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (135, 'screen_size', '27.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (135, 'camera', '4K SlimFit Detachable');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (135, 'resolution', '5120x2880 (5K)');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (136, 'TP-Link Tapo C210 2K 3MP Pan/Tilt Smart Security Wi-Fi Camera', 'smart-home', 'Ultra-high definition 2K (3MP) video, 360-degree horizontal rotation, motion tracking with AI person detection, two-way audio, up to 512GB microSD.', 2299.00, 3999.00, 50, 'images/prod-136.png', 4.7, 340, TRUE, '2K 360 Pan/Tilt', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (136, 'connectivity', 'Wi-Fi 2.4GHz');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (136, 'storage', 'MicroSD up to 512GB');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (136, 'resolution', '2K 3MP');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (137, 'Amazon Echo Show 8 (2nd Gen) Smart Display with Alexa', 'smart-home', '8-inch HD touchscreen, 13MP camera with auto-framing, stereo speakers with spatial audio, hands-free Alexa smart home dashboard control.', 8999.00, 13999.00, 25, 'images/prod-137.png', 4.8, 190, TRUE, 'Alexa 8" Display', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (137, 'display', '8.0 HD Touch');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (137, 'assistant', 'Amazon Alexa');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (137, 'camera', '13MP Auto-Framing');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (138, 'Google Nest Hub (2nd Gen) Smart Display with Sleep Sensing', 'smart-home', '7-inch touch display, Soli Radar motion sensor for contactless Sleep Sensing, room-filling bass, Google Assistant, controls thousands of smart lights.', 6999.00, 9999.00, 22, 'images/prod-138.png', 4.7, 140, TRUE, 'Soli Sleep Sensing', 15, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (138, 'display', '7.0 Touch');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (138, 'assistant', 'Google Assistant');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (138, 'sensor', 'Soli Radar Sleep Sensing');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (139, 'Philips Wiz Smart 9W B22 Color & Tunable White LED Bulb (3-Pack)', 'smart-home', 'Pack of 3 Wi-Fi LED smart bulbs offering 16 million colors and dynamic lighting modes. Works directly with your Wi-Fi, Alexa, Google Assistant.', 1899.00, 2999.00, 60, 'images/prod-139.png', 4.6, 280, TRUE, 'Pack of 3 Bulbs', 27, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (139, 'wattage', '9W each');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (139, 'hub_required', 'false');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (139, 'colors', '16 Million');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (139, 'fitting', 'B22 Indian Standard');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (140, 'Xiaomi Smart Air Purifier 4 with True HEPA Filter & OLED Display', 'smart-home', 'High-efficiency CADR of up to 400 m3/h, 3-in-1 filtration eliminating 99.97% of particles down to 0.3 microns, real-time OLED AQI display, App control.', 13999.00, 16999.00, 20, 'images/prod-140.png', 4.8, 175, TRUE, 'True HEPA OLED', 30, 800.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (140, 'filter', 'True HEPA 99.97%');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (140, 'app', 'Mi Home / Alexa');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (140, 'cadr', '400 m3/h');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (141, 'Atomberg Renesa 1200mm BLDC Smart Ceiling Fan with Remote & App', 'smart-home', 'Super energy efficient BLDC motor consuming only 28W (saves up to 65% on electricity bills). Controlled via smart remote, Atomberg app, and Alexa.', 3799.00, 5299.00, 35, 'images/prod-141.png', 4.7, 310, TRUE, '28W BLDC Energy Save', 28, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (141, 'motor', 'BLDC 28W');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (141, 'smart_control', 'App + Remote + Voice');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (141, 'blade_sweep', '1200mm');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (142, 'Havells Crabtree Signia 4-Node Smart Wi-Fi Touch Switch Panel', 'smart-home', 'Luxury capacitive glass touch switch panel with 4 on/off switches and fan regulator. Direct Wi-Fi connection without external automation gateways.', 4299.00, 5999.00, 24, 'images/prod-142.png', 4.6, 85, TRUE, 'Glass Touch Panel', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (142, 'material', 'Toughened Glass');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (142, 'connectivity', 'Wi-Fi');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (142, 'switches', '4 Nodes + Fan');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (143, 'Qubo Smart Door Lock Ultra by Hero Group (5-in-1 Access)', 'smart-home', 'Heavy-duty smart security door lock with Fingerprint recognition (0.3s), Passcode, RFID Card, Emergency Key, and Qubo Mobile App access.', 12990.00, 19990.00, 18, 'images/prod-143.png', 4.8, 120, TRUE, '5-in-1 Smart Lock', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (143, 'battery', '4x AA up to 1 Year');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (143, 'access_modes', 'Fingerprint, PIN, RFID, App, Key');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (144, 'TP-Link Tapo P110 16A Smart Wi-Fi Plug with Energy Monitoring', 'smart-home', 'Heavy-duty 16A smart plug ideal for geysers, air conditioners, and heaters. Real-time power consumption tracking and energy cost calculator.', 1199.00, 1999.00, 50, 'images/prod-144.png', 4.7, 260, TRUE, '16A Heavy Duty', 2, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (144, 'app', 'Tapo App');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (144, 'energy_monitor', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (144, 'rating', '16A Heavy Load');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (145, 'Wipro Smart LED Batten 20W Color & White Wi-Fi Light Bar', 'smart-home', '4-foot 20W smart tube light offering 16 million colors plus tunable white light (2700K to 6500K). Syncs with music and schedules automated timers.', 1299.00, 1990.00, 40, 'images/prod-145.png', 4.5, 180, TRUE, '20W Smart Batten', 20, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (145, 'wattage', '20W');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (145, 'length', '4 Feet');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (145, 'colors', '16M RGB + Tunable White');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (146, 'Elgato Stream Deck MK.2 Studio Controller (15 LCD Keys)', 'desk-setup', '15 customizable LCD keys to trigger actions in apps, control audio, launch media, and switch scenes with visual feedback. Detachable magnetic faceplate.', 13990.00, 16990.00, 20, 'images/prod-146.png', 4.9, 175, TRUE, '15 LCD Keys', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (146, 'keys', '15 Customizable LCD');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (146, 'interface', 'USB 2.0');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (146, 'stand', 'Fixed 45-degree');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (147, 'Rode NT-USB Mini Studio Condenser USB Microphone', 'desk-setup', 'Studio-grade condenser capsule with directional cardioid pickup pattern, built-in pop filter, 360-degree swing mount, and zero-latency headphone monitoring.', 8990.00, 11990.00, 25, 'images/prod-147.png', 4.8, 140, TRUE, 'Rode Studio Mic', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (147, 'polar_pattern', 'Cardioid');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (147, 'headphone_jack', '3.5mm Zero Latency');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (147, 'resolution', '24-bit 48kHz');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (148, 'HyperX QuadCast S RGB USB Condenser Gaming Microphone', 'desk-setup', 'Stunning dynamic RGB lighting with customizable software, built-in anti-vibration shock mount, tap-to-mute sensor with LED indicator, 4 selectable polar patterns.', 13490.00, 16990.00, 22, 'images/prod-148.png', 4.8, 210, TRUE, 'Dynamic RGB Mic', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (148, 'polar_patterns', 'Stereo, Omnidirectional, Cardioid, Bidirectional');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (148, 'lighting', 'Customizable RGB');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (149, 'Elgato Facecam Full HD 1080p60 Uncompressed Streaming Webcam', 'desk-setup', 'Studio-grade f/2.4 24mm all-glass Elgato Prime Lens, Sony STARVIS CMOS sensor, uncompressed 1080p60 video output with zero video artifacting.', 13990.00, 16990.00, 15, 'images/prod-149.png', 4.8, 88, TRUE, 'Sony STARVIS 60fps', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (149, 'sensor', 'Sony STARVIS CMOS');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (149, 'lens', 'f/2.4 24mm All-Glass');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (149, 'resolution', '1080p60 Uncompressed');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (150, 'Shure MV7 USB/XLR Dynamic Broadcast Podcast Microphone', 'desk-setup', 'Legendary broadcast dynamic microphone inspired by the iconic SM7B. Dual USB and XLR outputs, Voice Isolation Technology, touch panel controls.', 22999.00, 26999.00, 12, 'images/prod-150.png', 4.9, 130, TRUE, 'Broadcast SM7B Pro', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (150, 'outputs', 'USB & XLR');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (150, 'capsule', 'Dynamic Voice Isolation');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (150, 'software', 'ShurePlus MOTIV');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (151, 'Razer Kiyo Pro Streaming Webcam (Adaptive Light Sensor, HDR)', 'desk-setup', 'Ultra-sensitive Type 1/2.8 ultra-sensitive CMOS sensor with STARVIS technology, uncompressed Full HD 1080p 60FPS, HDR enabled at 30FPS.', 9999.00, 14999.00, 20, 'images/prod-151.png', 4.7, 115, TRUE, 'Adaptive HDR Cam', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (151, 'resolution', '1080p 60FPS / HDR 30FPS');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (151, 'field_of_view', 'Wide / Medium / Narrow');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (152, 'Elgato Key Light Air Professional Studio LED Edge-lit Panel', 'desk-setup', '1400 lumens of edge-lit diffused glow that eliminates harsh glare on camera. 80 premium OSRAM LEDs, adjustable color temperature (2900 - 7000K).', 11990.00, 14990.00, 18, 'images/prod-152.png', 4.8, 92, TRUE, '1400 Lumens Studio', 25, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (152, 'brightness', '1400 Lumens');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (152, 'color_temperature', '2900K - 7000K');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (152, 'control', 'Wi-Fi PC/Mac/iOS/Android');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (153, 'BenQ ScreenBar Halo Wireless Controller LED Monitor Light', 'desk-setup', 'Flagship monitor light bar featuring wireless rotary controller dial, patented asymmetrical optical design, dual front and back ambient backlight.', 14990.00, 17990.00, 16, 'images/prod-153.png', 4.9, 105, TRUE, 'Halo Wireless Dial', 6, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (153, 'controller', '2.4GHz Wireless Precision Dial');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (153, 'backlight', 'Ambient Dual Light');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (154, 'Audio-Technica AT2020USB-X Cardioid Condenser USB Microphone', 'desk-setup', 'Reimagining of the acclaimed AT2020 studio microphone with high-resolution 24-bit/96 kHz A/D converter, soft-touch capacitive mute button.', 12490.00, 15990.00, 20, 'images/prod-154.png', 4.8, 95, TRUE, '24-bit 96kHz Studio', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (154, 'sample_rate', '24-bit 96kHz');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (154, 'polar_pattern', 'Cardioid');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (154, 'mute', 'Soft-Touch Capacitive');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (155, 'Logitech Litra Glow Premium Streaming Light with TrueSoft', 'desk-setup', 'TrueSoft technology delivers balanced full-spectrum LED light with cinematic color accuracy for natural skin tones. Includes monitor mount.', 5495.00, 7995.00, 30, 'images/prod-155.png', 4.7, 140, TRUE, 'TrueSoft Glow', 10, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (155, 'color_rendering', '93 CRI');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (155, 'technology', 'Logitech TrueSoft');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (155, 'mount', '3-Way Monitor Mount');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (156, 'Sony DualSense Edge Wireless Controller for PS5 & PC', 'gaming', 'Ultra-customizable pro controller with swappable stick modules, remappable back buttons, adjustable trigger stops, and dedicated profile buttons.', 17990.00, 19990.00, 12, 'images/prod-156.png', 4.9, 85, TRUE, 'PS5 Pro Controller', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (156, 'features', 'Swappable Stick Modules, Back Buttons');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (156, 'compatibility', 'PS5, PC, Mac, iOS');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (157, 'Razer DeathAdder V3 Pro Ultra-lightweight Wireless Gaming Mouse', 'gaming', 'Refined 63g ultra-lightweight ergonomic shape, Focus Pro 30K Optical Sensor, Gen-3 Optical Mouse Switches, up to 90 hours battery life.', 12999.00, 15999.00, 25, 'images/prod-157.png', 4.9, 190, TRUE, '63g 30K Sensor', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (157, 'weight_grams', '63');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (157, 'sensor', 'Focus Pro 30K');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (157, 'wireless', 'Razer HyperSpeed');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (157, 'battery_hours', '90');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (158, 'Corsair K70 RGB PRO Mechanical Gaming Keyboard (Cherry MX Red)', 'gaming', 'Iconic aircraft-grade aluminum frame, German Cherry MX Red linear switches, 8000Hz hyper-polling AXON technology, tournament mode switch.', 12499.00, 15999.00, 20, 'images/prod-158.png', 4.8, 160, TRUE, '8000Hz Cherry MX', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (158, 'polling_rate', '8000Hz AXON');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (158, 'switches', 'Cherry MX Red');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (158, 'frame', 'Brushed Aluminum');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (159, 'Logitech G PRO X SUPERLIGHT 2 Wireless Gaming Mouse', 'gaming', 'The world''s leading esports mouse refined: LIGHTFORCE hybrid optical-mechanical switches, HERO 2 sensor with 32,000 DPI, 60g weight, USB-C.', 14995.00, 17995.00, 22, 'images/prod-159.png', 4.9, 230, TRUE, '60g Hero 2 Sensor', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (159, 'weight_grams', '60');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (159, 'switches', 'LIGHTFORCE Hybrid');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (159, 'sensor', 'HERO 2 32K DPI');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (159, 'battery_hours', '95');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (160, 'HyperX Cloud III Wireless Gaming Headset (120hr Battery, 53mm)', 'gaming', 'Legendary HyperX memory foam comfort, angled 53mm dynamic drivers tuned for gaming audio, crystal clear 10mm mic, and colossal 120-hour battery life.', 12990.00, 15990.00, 25, 'images/prod-160.png', 4.8, 170, TRUE, '120hr Cloud Comfort', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (160, 'driver_mm', '53');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (160, 'microphone', '10mm Detachable with Mesh');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (160, 'battery_hours', '120');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (161, 'ASUS ROG Falchion RX Low Profile 65% Wireless Keyboard', 'gaming', 'Compact 65% gaming keyboard with ROG RX Low-Profile Optical Switches, integrated interactive touch panel, sound dampening silicone foam, tri-mode.', 9999.00, 12999.00, 18, 'images/prod-161.png', 4.7, 95, TRUE, 'Low-Profile 65%', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (161, 'layout', '65%');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (161, 'connectivity', 'Tri-Mode Wireless');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (161, 'switches', 'ROG RX Red Optical');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (161, 'battery_hours', '430');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (162, 'SteelSeries Arctis Nova Pro Wireless Multi-System Headset', 'gaming', 'Hi-Res audio drivers with Active Noise Cancellation, Infinity Power System (2 hot-swappable batteries for limitless gaming), Multi-System Base Station.', 32999.00, 39999.00, 10, 'images/prod-162.png', 4.9, 85, TRUE, 'Dual Battery Swappable', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (162, 'hub', 'OLED Base Station');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (162, 'anc', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (162, 'battery', 'Infinite Swappable Dual Battery');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (163, 'Razer BlackShark V2 Pro Wireless Esports Headset (HyperClear)', 'gaming', 'Razer HyperClear Super Wideband Mic, TriForce Titanium 50mm drivers, pro-tuned FPS audio profiles, ultra-soft flowknit memory foam ear cushions.', 16999.00, 19999.00, 20, 'images/prod-163.png', 4.8, 140, TRUE, 'Esports Audio Pro', 0, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (163, 'driver_mm', '50 Titanium');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (163, 'microphone', 'HyperClear Super Wideband');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (163, 'battery_hours', '70');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (164, 'Corsair RM850x 850W 80 Plus Gold Fully Modular ATX Power Supply', 'gaming', '850 Watts continuous power, 100% all-Japanese 105C capacitors, 135mm magnetic levitation fan with Zero RPM mode, 80 PLUS Gold certified.', 11999.00, 14999.00, 15, 'images/prod-164.png', 4.9, 110, TRUE, '850W Gold Modular', 850, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (164, 'efficiency', '80 PLUS Gold');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (164, 'fan', 'Magnetic Levitation 135mm');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (164, 'modularity', 'Fully Modular');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (164, 'power_watts', '850');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (165, 'Redragon K552 Kumara Rainbow Backlit Mechanical Gaming Keyboard', 'gaming', 'Tenkeyless 87-key compact mechanical keyboard with custom dustproof blue clicky switches, metal alloy base, rainbow LED backlighting.', 2799.00, 3999.00, 40, 'images/prod-165.png', 4.5, 450, TRUE, 'Mechanical Value', 5, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (165, 'layout', 'Tenkeyless 87 Key');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (165, 'material', 'Metal Plate');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (165, 'switches', 'Outemu Blue Clicky');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (166, 'APC Back-UPS BX600C-IN 600VA / 360W 230V Desktop UPS', 'power', 'Compact desktop line-interactive UPS providing 600VA / 360W battery backup, Automatic Voltage Regulation (AVR), 3 Indian 3-pin output sockets.', 3299.00, 4299.00, 35, 'images/prod-166.png', 4.6, 280, TRUE, 'Essential 600VA', 360, 500.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (166, 'wattage', '360W');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (166, 'va_rating', '600VA');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (166, 'sockets', '3x Indian 3-pin');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (166, 'avr', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (167, 'APC Back-UPS Pro BR1000G-IN 1000VA Sine Wave Line-Interactive UPS', 'power', 'Premium 1000VA / 600W battery backup with multifunction LCD status display, Automatic Voltage Regulation, surge protection for gaming rigs.', 12499.00, 15499.00, 18, 'images/prod-167.png', 4.8, 125, TRUE, '1000VA LCD Pro', 600, 1000.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (167, 'wattage', '600W');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (167, 'va_rating', '1000VA');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (167, 'display', 'Multifunction LCD');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (167, 'sockets', '6x Outlets');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (168, 'V-Guard Crystal Plus Smart Television Voltage Stabilizer (up to 55")', 'power', 'Microcontrolled voltage stabilizer specifically engineered to protect Smart TVs up to 55 inches, set-top boxes, and gaming consoles from dangerous surges.', 2899.00, 3690.00, 30, 'images/prod-168.png', 4.7, 195, TRUE, 'Smart TV Protection', 250, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (168, 'indicator', 'Digital Seven Segment');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (168, 'voltage_range', '90V - 290V');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (168, 'capacity', '3 Amp');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (169, 'Microtek Legend 1000VA Heavy-Duty Desktop UPS with AVR', 'power', 'Heavy-duty 1000VA line-interactive UPS featuring twin 12V 7Ah sealed lead-acid batteries, wide input voltage window (140V-300V), and cold start function.', 5499.00, 6999.00, 24, 'images/prod-169.png', 4.6, 140, TRUE, 'Dual Battery 1000VA', 600, 800.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (169, 'wattage', '600W');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (169, 'va_rating', '1000VA');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (169, 'batteries', '2x 12V 7Ah');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (169, 'voltage_range', '140V - 300V');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (170, 'Luminous Zelio+ 1100 Pure Sinewave Inverter for Home & Office', 'power', 'India''s most intelligent pure sine wave home inverter with 32-bit DSP processor, intuitive LED screen displaying remaining battery backup hours.', 6799.00, 8499.00, 20, 'images/prod-170.png', 4.8, 220, TRUE, 'Pure Sine Wave Inverter', 756, 1000.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (170, 'wattage', '756W');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (170, 'va_rating', '900VA');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (170, 'display', 'Hours Remaining LED');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (170, 'wave_type', 'Pure Sine Wave');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (171, 'Havells 4-Way Heavy Duty Spike & Surge Protector (2m Cord)', 'power', '4-socket universal surge strip with thermal overload trip shutter, heavy-duty fire retardant plastic casing, high-grade copper contacts, and 2m cable.', 799.00, 1195.00, 60, 'images/prod-171.png', 4.7, 340, TRUE, 'Fire Retardant 4-Way', 2500, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (171, 'max_power', '2500W 10A');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (171, 'sockets', '4x Universal');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (171, 'cord_length_m', '2.0');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (172, 'Portronics Power Plate 7 with 6 Power Sockets & 4 USB Ports (2500W)', 'power', 'Multi-functional power extension strip equipped with 6 universal AC sockets, 4 USB fast-charging ports (including Type-C), 2500W maximum capacity.', 1299.00, 1999.00, 45, 'images/prod-172.png', 4.6, 210, TRUE, '6 AC + 4 USB 2500W', 2500, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (172, 'max_power', '2500W');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (172, 'sockets', '6 AC Sockets');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (172, 'usb_ports', '3 USB-A + 1 Type-C');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (173, 'Syska 4-Way Power Strip Surge Protector with Master Switch', 'power', 'Reliable 4-outlet surge protector with integrated spike suppressor circuit, indicator LED light, reset button, and durable 2-meter cord.', 649.00, 999.00, 50, 'images/prod-173.png', 4.5, 290, TRUE, 'Spike Master Switch', 1500, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (173, 'sockets', '4 Sockets');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (173, 'surge_protection', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (173, 'cord_length_m', '2.0');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (174, 'Zebronics Zeb-Iron 600VA Line Interactive UPS with Auto Restart', 'power', 'Microcontroller-based 600VA desktop UPS offering automated sleep mode charging, generator compatibility, audible alarms, and auto restart upon AC recovery.', 2499.00, 3299.00, 35, 'images/prod-174.png', 4.4, 180, TRUE, 'Auto Restart 600VA', 360, 400.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (174, 'wattage', '360W');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (174, 'va_rating', '600VA');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (174, 'auto_restart', 'true');
INSERT INTO products (id, name, category, description, price, original_price, stock, image_url, rating, review_count, featured, badge, power_watts, annual_maintenance) VALUES (175, 'Belkin BoostCharge Pro 3-in-1 Wireless Charging Pad with MagSafe 15W', 'power', 'Official Apple Made for MagSafe 15W ultra-fast wireless charging pad for iPhone 15/14, Apple Watch fast charging puck, and AirPods Qi tray.', 13999.00, 15999.00, 16, 'images/prod-175.png', 4.9, 95, TRUE, 'Official MagSafe 15W', 25, 0.00);
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (175, 'watch_fast_charge', 'true');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (175, 'magsafe_output', '15W Official');
INSERT INTO product_specs (product_id, spec_key, spec_value) VALUES (175, 'compatibility', 'iPhone 15, Apple Watch, AirPods');
