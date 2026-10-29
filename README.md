# SmartCart — An Intelligent E-Commerce Cart System

> **Java College Microproject / Academic Capstone**  
> Built with: **Java Servlets**, **JavaServer Pages (JSP)**, **JDBC**, **MySQL**, **HTML5/CSS3/JavaScript**, and **Maven**.

---

## 1. Project Overview

Traditional e-commerce shopping carts merely act as digital containers—they add items, update quantities, and calculate subtotals. They fail to assist consumers in making **informed, compatible, and cost-effective purchasing decisions**.

**SmartCart** bridges this decision gap by adding a pre-checkout **Intelligent Decision Engine** layer. Before payment, SmartCart evaluates the cart contents against:
1. **Planned Budget Limits** (Feature A)
2. **Accidental Purchase Mistakes** (Feature B)
3. **Hardware Specification Incompatibilities** (Feature C)
4. **Overall Purchase Risk & Value Score** (Feature D)

---

## 2. Technology Stack & Architecture

- **Backend:** Java 17+ (Servlets & JSP)
- **Database:** MySQL with JDBC Prepared Statements
- **Build & Dependency Management:** Apache Maven (`pom.xml`)
- **Server:** Apache Tomcat (Tomcat 10 / 11 compliant with Jakarta EE) + Built-in Java HTTP server (`SmartCartServer.java`) for zero-setup execution in VS Code terminal.
- **Frontend:** Responsive HTML5, CSS3, Modern JavaScript with real-time budget synchronization.
- **Visual Design:** Clean white/light-neutral background (`#ffffff` / `#f8fafc`), deep navy typography (`#0f172a`), and emerald green accents (`#10b981` / `#059669`).

### Architecture Pattern (MVC + DAO)
```
Browser Client (JSP / HTML5 / CSS3 / JavaScript)
      │
      ▼ HTTP Requests (GET / POST)
Controller Layer (Jakarta Servlets: Home, Product, Cart, Analysis, Checkout)
      │
      ├───────────────────────┬────────────────────────┐
      ▼                       ▼                        ▼
Service Layer            Domain Models             DAO Layer
(SmartAnalysisService)   (Product, CartItem,      (ProductDAO, OrderDAO)
                         Cart, Order, Alerts)          │
                                                       ▼ JDBC
                                               MySQL (smartcart_db)
                                          (with resilient memory fallback)
```

---

## 3. The Four Distinctive SmartCart Features

### Feature A: Budget Guardian
- **Interactive Budget Input:** Users can set and adjust a target shopping budget (default: $1,200.00).
- **Live Progress Bar:** Dynamically color-coded:
  - 🟢 **Green (Safe):** Spending < 70% of planned budget.
  - 🟡 **Amber (Warning):** Spending between 70% and 99% of budget ("Approaching budget limit").
  - 🔴 **Red (Danger):** Spending $\ge$ 100% of budget ("Budget Exceeded by $X").
- **Alternative Recommendations:** If the cart approaches or exceeds the budget limit, the engine automatically suggests lower-cost alternatives (e.g., swapping a $1,499 gaming laptop for a $999 ultrabook to save $500).

### Feature B: Cart Mistake Detector
- **Unusually High Quantities:** Flags accidental bulk quantities (e.g., adding $\ge$ 3 laptops/phones or $\ge$ 5 accessories) to protect against accidental double-clicks.
- **Unnecessary Redundant Purchases:** Detects when a customer purchases an accessory that is already included in the retail box (e.g., buying a separate 20W charger when purchasing the AeroPhone 15 which already includes a 20W charger in-box).
- **Duplicate Accessories:** Flags multiple phone cases in a single order intended for personal use.
- **User Control:** Warnings can be reviewed and dismissed with a single click without removing items.

### Feature C: Compatibility Checker
A rule-based specification engine analyzing device-to-accessory relationships:
- **Charger Wattage Inadequacy:** Flags when a laptop requiring 65W or 100W USB-C PD is paired with an underpowered 20W smartphone charger. Explains that the laptop will fail to charge or lose battery under load.
- **Missing Audio Port:** Flags when a smartphone without a 3.5mm jack (e.g., AeroPhone 15 Pro) is paired with standard 3.5mm analog gaming earphones without a USB-C DAC adapter.
- **Physical Case Size Mismatch:** Identifies when a 6.7-inch phone case is selected for a 6.1-inch smartphone model.
- **Missing In-Box Charger:** Informs the buyer when a smartphone is sold without a wall power adapter in the retail box.

### Feature D: Cart Risk & Value Report
The central differentiator of SmartCart—a comprehensive pre-checkout diagnostic report:
- **Purchase Readiness Score (0 to 100):**
  - Deducts points for critical hardware incompatibilities (-25 pts each), budget overruns (-20 pts), and mistake alerts (-10 pts each).
  - Categorizes purchase as:
    - **Safe to Purchase (85–100):** All specifications compatible and within budget.
    - **Review Recommended (60–84):** Non-critical warnings or potential cost savings identified.
    - **Critical Issues Detected (0–59):** Severe incompatibilities or major budget violations.
- **One-Click Alternative Swap:** Enables swapping costly items with cheaper alternatives directly from the report to instantly restore budget health.

---

## 4. Project Directory Structure

```
SmartCart/
├── pom.xml                               # Maven dependency & WAR packaging configuration
├── run.bat                               # Windows Command Prompt 1-click execution script
├── run.ps1                               # PowerShell execution script for VS Code terminal
├── lib/
│   └── jakarta.servlet-api-6.1.0.jar     # Local Jakarta Servlet library
├── sql/
│   └── schema.sql                        # MySQL DDL & Seed Data script
├── src/
│   ├── main/
│   │   ├── java/com/smartcart/
│   │   │   ├── SmartCartServer.java      # Embedded HTTP Server & Servlet Container
│   │   │   ├── model/
│   │   │   │   ├── Product.java          # Product entity with specifications map
│   │   │   │   ├── CartItem.java         # Cart item with quantity & subtotal
│   │   │   │   ├── Cart.java             # Session cart, financial math & budget state
│   │   │   │   ├── Order.java            # Order persistence model
│   │   │   │   ├── OrderItem.java        # Order line item model
│   │   │   │   ├── MistakeAlert.java     # Alert model for Mistake Detector
│   │   │   │   ├── CompatibilityIssue.java # Issue model for Compatibility Checker
│   │   │   │   ├── AlternativeRecommendation.java # Budget alternative model
│   │   │   │   └── AnalysisReport.java   # Executive Risk & Value Report
│   │   │   ├── dao/
│   │   │   │   ├── DBUtil.java           # JDBC manager (MySQL + in-memory fallback)
│   │   │   │   ├── ProductDAO.java       # Product catalogue & specifications DAO
│   │   │   │   └── OrderDAO.java         # Transactional order persistence DAO
│   │   │   ├── service/
│   │   │   │   └── SmartAnalysisService.java # Core Decision Engine (Features A, B, C, D)
│   │   │   └── controller/
│   │   │       ├── HomeServlet.java      # Serves featured products on homepage
│   │   │       ├── ProductServlet.java   # Catalogue, search & product details
│   │   │       ├── CartServlet.java      # Cart mutations & Budget Guardian updates
│   │   │       ├── AnalysisServlet.java  # Pre-checkout report & alert dismissal
│   │   │       ├── CheckoutServlet.java  # Delivery validation & bill calculation
│   │   │       └── OrderConfirmationServlet.java # Receipt presentation
│   │   ├── resources/
│   │   │   └── db.properties             # Database credentials configuration
│   │   └── webapp/
│   │       ├── WEB-INF/web.xml           # Servlet mapping descriptor for Tomcat
│   │       ├── header.jsp                # Common navigation bar & search component
│   │       ├── footer.jsp                # Common footer with project info
│   │       ├── index.jsp                 # Landing page & feature showcase
│   │       ├── products.jsp              # Catalogue listing with category filters
│   │       ├── product-details.jsp       # Specifications table & alternatives
│   │       ├── cart.jsp                  # Cart with live Budget Guardian bar
│   │       ├── analysis.jsp              # Flagship Risk & Value report dashboard
│   │       ├── checkout.jsp              # Delivery details & simulated payment
│   │       ├── order-confirmation.jsp    # Order receipt & readiness badge
│   │       ├── css/style.css             # Modern clean white/emerald theme
│   │       ├── js/main.js                # Live budget synchronization & toasts
│   │       └── images/                   # High-res SVG product illustrations
│   └── test/java/
│       └── SmartCartTest.java            # Automated verification test suite (33 tests)
```

---

## 5. How to Run the Project

### Option A: Directly in VS Code Terminal (Recommended, Zero Setup!)

You do not need to install an external Tomcat or Maven to test and demonstrate the project. A built-in runner is included:

1. Open a terminal in the `SmartCart` directory in VS Code:
   ```powershell
   cd SmartCart
   ```
2. Run the PowerShell execution script:
   ```powershell
   .\run.ps1
   ```
   *(Or on Windows Command Prompt: `run.bat`)*
3. The script will automatically compile all Java classes and start the server:
   ```
   ==================================================================
      SmartCart — Intelligent E-Commerce Cart System is RUNNING!
      Local URL: http://localhost:8080/
      Active DB: In-Memory Seeded Store (or MySQL if connected)
   ==================================================================
   ```
4. Open your browser and navigate to: **`http://localhost:8080`**

---

### Option B: Running with Apache Tomcat in Eclipse / IntelliJ / NetBeans

1. **Import as Maven Project:**
   - In IntelliJ: `File -> Open -> Select SmartCart/pom.xml`.
   - In Eclipse: `File -> Import -> Maven -> Existing Maven Projects`.
2. **Add Tomcat Server:**
   - Configure **Apache Tomcat 10.x** or **11.x** (compatible with Jakarta Servlet 6.0).
   - Add `SmartCart` to Tomcat's deployment artifacts (`SmartCart:war exploded`).
   - Set context path to `/SmartCart` or `/`.
3. **Start Tomcat:**
   - Access the application at: `http://localhost:8080/SmartCart/`

---

## 6. MySQL Database Setup (Optional for Full Persistence)

The project includes an intelligent dual-mode database layer:
- **Offline / Zero-Config Mode:** If MySQL is not running, `DBUtil` automatically falls back to an in-memory repository seeded with all products and specs.
- **MySQL Mode:** When a MySQL server is active, it runs live JDBC `PreparedStatement` queries.

To connect your local MySQL server:
1. Open MySQL Workbench or your MySQL terminal:
   ```bash
   mysql -u root -p
   ```
2. Execute the included schema script:
   ```sql
   source C:/path/to/SmartCart/sql/schema.sql;
   ```
3. Update `src/main/resources/db.properties` with your database password:
   ```properties
   db.url=jdbc:mysql://localhost:3306/smartcart_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   db.user=root
   db.password=YOUR_PASSWORD_HERE
   ```

---

## 7. Step-by-Step Test Scenarios for Viva & Demonstrations

Follow these test workflows to demonstrate each smart feature:

### Scenario 1: Demonstrating Feature A (Budget Guardian)
1. Go to `http://localhost:8080/products`.
2. Add **Titan Gaming Laptop 16** ($1,499.00) to your cart.
3. Open **My Cart**.
4. Observe the **Budget Guardian** progress bar:
   - Budget limit: $1,200.00.
   - Cart Total: $1,618.92 (Subtotal $1,499 + 8% Tax $119.92).
   - The bar turns **Red**, displaying: `🚨 Budget Exceeded! Your cart exceeds your target budget by $418.92`.
5. Enter a higher budget (e.g. `2000`) and click **Set Budget**; observe the bar turn **Green** and remaining budget update instantly.

### Scenario 2: Demonstrating Feature B (Mistake Detector)
1. Go to `http://localhost:8080/products`.
2. Add **AeroPhone 15** ($799.00) to your cart.
3. Add **20W Compact USB-C Charger** ($19.00).
4. Open **My Cart**.
5. Observe the **Mistake Detector Banner**:
   - `⚠️ Potentially Redundant Charger Purchase`
   - Explanation: *"The AeroPhone 15 retail box already includes an official 20W fast wall charger. You may not need to spend an extra $19.00 on a second one."*
6. Click **Dismiss Warning** to verify that the user can review and acknowledge without removing the item.

### Scenario 3: Demonstrating Feature C (Compatibility Checker)
1. Clear the cart and add **UltraBook Pro 15** ($999.00).
2. Add **20W Compact USB-C Charger** ($19.00).
3. Open **My Cart** or click **📊 Smart Analysis**.
4. Observe the **Critical Incompatibility Warning**:
   - `⛔ Insufficient Charger Power (20W vs 65W Required)`
   - Explanation: *"20W Compact USB-C Charger only outputs 20W, but UltraBook Pro 15 requires at least 65W. The laptop will charge very slowly, fail to charge, or lose power while running."*
   - Resolution button provided: `+ Add 65W Fast GaN Wall Charger`.

### Scenario 4: Demonstrating Feature D (Cart Risk & Value Report)
1. With items in your cart, click **📊 View SmartCart Analysis Report**.
2. Inspect the **Executive Readiness Score Dial**:
   - Shows numerical score (e.g., `70/100`) and status: *"Critical Issues Detected"*.
3. Scroll down to **Budget Guardian Value Recommendations**:
   - If an expensive item is in the cart, observe the suggested alternative (e.g., *"Titan Gaming Laptop -> UltraBook Pro 15, Save $500.00"*).
4. Click **⚡ Apply One-Click Swap**:
   - The system automatically replaces the expensive model with the value alternative and updates the cart total.
5. Click **Proceed to Checkout**, fill in delivery details, and click **Place Order**.
6. View the final **Order Confirmation Receipt** with the generated order number (e.g., `SC-20261026-8734`), archived readiness audit, and itemized bill.

---

## 8. Automated Unit Test Verification

To execute the automated Java test suite:
```powershell
& "C:\Program Files\Java\jdk-26.0.1\bin\javac.exe" -d target\classes -cp "target\classes;lib\jakarta.servlet-api-6.1.0.jar" src\test\java\SmartCartTest.java
& "C:\Program Files\Java\jdk-26.0.1\bin\java.exe" -cp "target\classes;lib\jakarta.servlet-api-6.1.0.jar" SmartCartTest
```
All **33 automated tests** validate calculations, mistake detection, compatibility rules, and order transactions.
