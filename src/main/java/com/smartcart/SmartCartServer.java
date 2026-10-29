package com.smartcart;

import com.smartcart.controller.*;
import com.smartcart.dao.DBUtil;
import com.smartcart.model.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Embedded HTTP Server & Servlet Container for SmartCart.
 * Enables zero-setup, one-click execution directly in the VS Code terminal.
 * Bridges standard JDK HttpServer to Jakarta Servlets and JSPs.
 */
public class SmartCartServer {

    private static final int PORT = 8080;
    private static final Map<String, SimpleHttpSession> SESSIONS = new ConcurrentHashMap<>();
    private static File webappDir;

    // Servlets instances
    private static HomeServlet homeServlet;
    private static ProductServlet productServlet;
    private static CartServlet cartServlet;
    private static AnalysisServlet analysisServlet;
    private static CheckoutServlet checkoutServlet;
    private static OrderConfirmationServlet orderConfirmationServlet;
    private static AuthServlet authServlet;
    private static OrderHistoryServlet orderHistoryServlet;
    private static WishlistServlet wishlistServlet;

    public static void main(String[] args) throws Exception {
        int port = PORT;
        if (args.length > 0) {
            try { port = Integer.parseInt(args[0]); } catch (Exception ignored) {}
        }

        // Determine webapp directory
        webappDir = new File("src/main/webapp");
        if (!webappDir.exists()) {
            webappDir = new File("SmartCart/src/main/webapp");
        }
        if (!webappDir.exists()) {
            webappDir = new File("../src/main/webapp");
        }

        // Initialize servlets
        homeServlet = new HomeServlet();
        homeServlet.init();
        productServlet = new ProductServlet();
        productServlet.init();
        cartServlet = new CartServlet();
        cartServlet.init();
        analysisServlet = new AnalysisServlet();
        analysisServlet.init();
        checkoutServlet = new CheckoutServlet();
        checkoutServlet.init();
        orderConfirmationServlet = new OrderConfirmationServlet();
        orderConfirmationServlet.init();
        authServlet = new AuthServlet();
        authServlet.init();
        orderHistoryServlet = new OrderHistoryServlet();
        orderHistoryServlet.init();
        wishlistServlet = new WishlistServlet();
        wishlistServlet.init();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MasterHttpHandler());
        server.setExecutor(null); // default executor
        server.start();

        System.out.println("==================================================================");
        System.out.println("   SmartCart — Intelligent E-Commerce Cart System is RUNNING!");
        System.out.println("   Local URL: http://localhost:" + port + "/");
        System.out.println("   Active DB: " + (DBUtil.isMySqlAvailable() ? "MySQL (smartcart_db)" : "In-Memory Seeded Store"));
        System.out.println("   Webapp Dir: " + webappDir.getAbsolutePath());
        System.out.println("   Press Ctrl+C to stop the server");
        System.out.println("==================================================================");
    }

    static class MasterHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path == null || path.isEmpty()) path = "/";

            // Static files handling
            if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
                handleStaticFile(exchange, path);
                return;
            }

            // Session lookup
            String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
            String sessionId = extractCookie(cookieHeader, "JSESSIONID");
            SimpleHttpSession session;
            boolean isNewSession = false;
            if (sessionId == null || !SESSIONS.containsKey(sessionId)) {
                sessionId = UUID.randomUUID().toString();
                session = new SimpleHttpSession(sessionId);
                SESSIONS.put(sessionId, session);
                isNewSession = true;
            } else {
                session = SESSIONS.get(sessionId);
            }

            // Parse request body for POST parameters
            Map<String, List<String>> params = new HashMap<>();
            parseQueryString(exchange.getRequestURI().getRawQuery(), params);
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                parseRequestBody(exchange, params);
            }

            // Wrap in Servlet Request & Response
            SimpleServletRequest req = new SimpleServletRequest(exchange, path, params, session);
            ByteArrayOutputStream respBuffer = new ByteArrayOutputStream();
            SimpleServletResponse resp = new SimpleServletResponse(exchange, respBuffer);

            if (isNewSession) {
                exchange.getResponseHeaders().add("Set-Cookie", "JSESSIONID=" + sessionId + "; Path=/; HttpOnly");
            }

            try {
                // Dispatch to corresponding Servlet
                HttpServlet targetServlet = null;
                if ("/".equals(path) || "/home".equals(path) || "/index".equals(path)) {
                    targetServlet = homeServlet;
                } else if ("/products".equals(path) || "/product".equals(path)) {
                    targetServlet = productServlet;
                } else if (path.startsWith("/cart/analysis")) {
                    targetServlet = analysisServlet;
                } else if (path.startsWith("/cart")) {
                    targetServlet = cartServlet;
                } else if ("/checkout".equals(path) || "/order/place".equals(path)) {
                    targetServlet = checkoutServlet;
                } else if ("/order/confirmation".equals(path)) {
                    targetServlet = orderConfirmationServlet;
                } else if ("/login".equals(path) || "/register".equals(path) || "/logout".equals(path) || "/profile".equals(path)) {
                    targetServlet = authServlet;
                } else if ("/orders".equals(path) || "/order/history".equals(path)) {
                    targetServlet = orderHistoryServlet;
                } else if ("/wishlist".equals(path) || path.startsWith("/wishlist")) {
                    targetServlet = wishlistServlet;
                }

                if (targetServlet != null) {
                    targetServlet.service(req, resp);
                } else {
                    // Try to serve static file from root
                    File f = new File(webappDir, path.substring(1));
                    if (f.exists() && f.isFile()) {
                        handleStaticFile(exchange, path);
                        return;
                    } else {
                        // Default to home
                        homeServlet.service(req, resp);
                    }
                }

                if (!resp.isCommitted()) {
                    resp.getWriter().flush();
                    byte[] bytes = respBuffer.toByteArray();
                    exchange.getResponseHeaders().set("Content-Type", resp.getContentType() != null ? resp.getContentType() : "text/html; charset=UTF-8");
                    exchange.sendResponseHeaders(resp.getStatus(), bytes.length > 0 ? bytes.length : -1);
                    if (bytes.length > 0) {
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(bytes);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                String err = "<html><body><h1>500 Server Error</h1><pre>" + e.getMessage() + "</pre></body></html>";
                byte[] b = err.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(500, b.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(b);
                }
            }
        }

        private void handleStaticFile(HttpExchange exchange, String path) throws IOException {
            File file = new File(webappDir, path.substring(1));
            if (!file.exists() || file.isDirectory()) {
                String msg = "404 Not Found";
                exchange.sendResponseHeaders(404, msg.length());
                try (OutputStream os = exchange.getResponseBody()) { os.write(msg.getBytes()); }
                return;
            }

            String mime = "application/octet-stream";
            if (path.endsWith(".css")) mime = "text/css; charset=UTF-8";
            else if (path.endsWith(".js")) mime = "application/javascript; charset=UTF-8";
            else if (path.endsWith(".svg")) mime = "image/svg+xml";
            else if (path.endsWith(".png")) mime = "image/png";
            else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) mime = "image/jpeg";
            else if (path.endsWith(".html")) mime = "text/html; charset=UTF-8";

            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().set("Content-Type", mime);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private String extractCookie(String header, String name) {
            if (header == null) return null;
            for (String part : header.split(";")) {
                String[] kv = part.trim().split("=", 2);
                if (kv.length == 2 && kv[0].trim().equals(name)) {
                    return kv[1].trim();
                }
            }
            return null;
        }

        private void parseQueryString(String query, Map<String, List<String>> params) {
            if (query == null || query.isEmpty()) return;
            for (String pair : query.split("&")) {
                String[] kv = pair.split("=", 2);
                String k = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                String v = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
                params.computeIfAbsent(k, x -> new ArrayList<>()).add(v);
            }
        }

        private void parseRequestBody(HttpExchange exchange, Map<String, List<String>> params) throws IOException {
            InputStream is = exchange.getRequestBody();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int r;
            while ((r = is.read(buf)) != -1) {
                baos.write(buf, 0, r);
            }
            String body = baos.toString(StandardCharsets.UTF_8);
            parseQueryString(body, params);
        }
    }

    // =========================================================================
    // JSP Template Engine
    // =========================================================================
    public static void renderJsp(String jspPath, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (jspPath.startsWith("/")) jspPath = jspPath.substring(1);
        File jspFile = new File(webappDir, jspPath);
        if (!jspFile.exists()) {
            resp.getWriter().println("<h1>JSP Not Found: " + jspPath + "</h1>");
            return;
        }

        String raw = Files.readString(jspFile.toPath(), StandardCharsets.UTF_8);

        // Process includes first
        raw = processIncludes(raw);

        // Process c:if and c:forEach and EL expressions
        String rendered = processTemplate(raw, req);

        // Flash message cleanup: once rendered, clear one-time toast/error messages from session
        try {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.removeAttribute("toastMessage");
                session.removeAttribute("errorMessage");
            }
        } catch (Exception ignored) {}

        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().write(rendered);
    }

    private static String processIncludes(String template) throws IOException {
        Pattern p = Pattern.compile("<%@\\s*include\\s+file=[\"']([^\"']+)[\"']\\s*%>|<jsp:include\\s+page=[\"']([^\"']+)[\"']\\s*/>");
        Matcher m = p.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String incFile = m.group(1) != null ? m.group(1) : m.group(2);
            if (incFile.startsWith("/")) incFile = incFile.substring(1);
            File f = new File(webappDir, incFile);
            String incContent = f.exists() ? Files.readString(f.toPath(), StandardCharsets.UTF_8) : "";
            m.appendReplacement(sb, Matcher.quoteReplacement(processIncludes(incContent)));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String processTemplate(String template, HttpServletRequest req) {
        // Remove JSP comments <%-- ... --%>
        template = template.replaceAll("<%--[\\s\\S]*?--%>", "");

        // Remove JSP directives like <%@ page ... %> and taglib directives
        template = template.replaceAll("<%@\\s*page[^%>]*%>", "");
        template = template.replaceAll("<%@\\s*taglib[^%>]*%>", "");

        // Process <c:forEach items="${...}" var="...">...</c:forEach> (supporting nested loops!)
        template = processForEachTags(template, req, null);

        // Process top-level <c:if test="...">...</c:if>
        template = processIfTags(template, req, null);

        // Process ${...} EL expressions
        template = processExpressions(template, req, null);

        // Strip any remaining Java scriptlets <% ... %> so raw code never leaks to the browser
        template = template.replaceAll("<%[\\s\\S]*?%>", "");

        return template;
    }

    private static String processForEachTags(String template, HttpServletRequest req, Map<String, Object> parentScope) {
        int startIdx = template.indexOf("<c:forEach");
        if (startIdx == -1) return template;

        StringBuilder sb = new StringBuilder();
        int cur = 0;

        while (startIdx != -1) {
            sb.append(template, cur, startIdx);

            int tagEnd = template.indexOf('>', startIdx);
            if (tagEnd == -1) break;

            String openTag = template.substring(startIdx, tagEnd + 1);
            Matcher mItems = Pattern.compile("items=[\"']\\$\\{([^}]+)\\}[\"']").matcher(openTag);
            Matcher mVar = Pattern.compile("var=[\"']([^\"']+)[\"']").matcher(openTag);

            if (!mItems.find() || !mVar.find()) {
                sb.append(openTag);
                cur = tagEnd + 1;
                startIdx = template.indexOf("<c:forEach", cur);
                continue;
            }

            String expr = mItems.group(1);
            String varName = mVar.group(1);

            // Find matching </c:forEach> by tracking nesting depth
            int depth = 1;
            int searchPos = tagEnd + 1;
            int closeTagIdx = -1;

            while (depth > 0 && searchPos < template.length()) {
                int nextOpen = template.indexOf("<c:forEach", searchPos);
                int nextClose = template.indexOf("</c:forEach>", searchPos);

                if (nextClose == -1) break;

                if (nextOpen != -1 && nextOpen < nextClose) {
                    depth++;
                    searchPos = nextOpen + 10;
                } else {
                    depth--;
                    if (depth == 0) {
                        closeTagIdx = nextClose;
                        break;
                    }
                    searchPos = nextClose + 12;
                }
            }

            if (closeTagIdx == -1) {
                sb.append(openTag);
                cur = tagEnd + 1;
                startIdx = template.indexOf("<c:forEach", cur);
                continue;
            }

            String body = template.substring(tagEnd + 1, closeTagIdx);

            Object collectionObj = evaluateExpression(expr, req, parentScope);
            if (collectionObj instanceof Iterable<?>) {
                for (Object item : (Iterable<?>) collectionObj) {
                    Map<String, Object> loopScope = new HashMap<>();
                    if (parentScope != null) loopScope.putAll(parentScope);
                    loopScope.put(varName, item);

                    String itemBody = processForEachTags(body, req, loopScope);
                    itemBody = processIfTags(itemBody, req, loopScope);
                    itemBody = processExpressions(itemBody, req, loopScope);
                    sb.append(itemBody);
                }
            } else if (collectionObj != null && collectionObj.getClass().isArray()) {
                for (Object item : (Object[]) collectionObj) {
                    Map<String, Object> loopScope = new HashMap<>();
                    if (parentScope != null) loopScope.putAll(parentScope);
                    loopScope.put(varName, item);

                    String itemBody = processForEachTags(body, req, loopScope);
                    itemBody = processIfTags(itemBody, req, loopScope);
                    itemBody = processExpressions(itemBody, req, loopScope);
                    sb.append(itemBody);
                }
            }

            cur = closeTagIdx + "</c:forEach>".length();
            startIdx = template.indexOf("<c:forEach", cur);
        }

        sb.append(template.substring(cur));
        return sb.toString();
    }

    private static String processIfTags(String template, HttpServletRequest req, Map<String, Object> localScope) {
        Pattern pattern = Pattern.compile("<c:if\\s+test=[\"']\\$\\{([^}]+)\\}[\"']>(.*?)</c:if>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String condition = matcher.group(1).trim();
            String body = matcher.group(2);

            boolean pass = evaluateCondition(condition, req, localScope);
            if (pass) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(body));
            } else {
                matcher.appendReplacement(sb, "");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static boolean evaluateCondition(String cond, HttpServletRequest req, Map<String, Object> localScope) {
        cond = cond.trim();

        // Handle OR logic
        if (cond.contains(" || ") || cond.contains(" or ")) {
            String[] subConds = cond.split(" \\|\\| | or ");
            for (String sub : subConds) {
                if (evaluateCondition(sub.trim(), req, localScope)) return true;
            }
            return false;
        }

        // Handle AND logic
        if (cond.contains(" && ") || cond.contains(" and ")) {
            String[] subConds = cond.split(" && | and ");
            for (String sub : subConds) {
                if (!evaluateCondition(sub.trim(), req, localScope)) return false;
            }
            return true;
        }

        boolean negate = false;
        if (cond.startsWith("not ") || cond.startsWith("!")) {
            negate = true;
            cond = cond.replaceFirst("^(not\\s+|!)", "").trim();
        }

        if (cond.startsWith("empty ")) {
            String target = cond.substring(6).trim();
            Object val = evaluateExpression(target, req, localScope);
            boolean isEmpty = (val == null || 
                               (val instanceof Collection && ((Collection<?>) val).isEmpty()) || 
                               (val instanceof Cart && ((Cart) val).isEmpty()) ||
                               (val instanceof Wishlist && ((Wishlist) val).isEmpty()) ||
                               (val instanceof String && ((String) val).isEmpty()));
            return negate ? !isEmpty : isEmpty;
        }

        // Comparison: a == b or a != b
        if (cond.contains(" == ")) {
            String[] parts = cond.split(" == ", 2);
            Object v1 = evaluateExpression(parts[0].trim(), req, localScope);
            String s2 = unquote(parts[1].trim());
            boolean eq = Objects.equals(String.valueOf(v1), s2);
            return negate ? !eq : eq;
        }
        if (cond.contains(" != ")) {
            String[] parts = cond.split(" != ", 2);
            Object v1 = evaluateExpression(parts[0].trim(), req, localScope);
            String s2 = unquote(parts[1].trim());
            boolean neq = !Objects.equals(String.valueOf(v1), s2);
            return negate ? !neq : neq;
        }
        if (cond.contains(" >= ")) {
            String[] parts = cond.split(" >= ", 2);
            double d1 = parseDouble(evaluateExpression(parts[0].trim(), req, localScope));
            double d2 = parseDouble(parts[1].trim());
            boolean res = d1 >= d2;
            return negate ? !res : res;
        }
        if (cond.contains(" <= ")) {
            String[] parts = cond.split(" <= ", 2);
            double d1 = parseDouble(evaluateExpression(parts[0].trim(), req, localScope));
            double d2 = parseDouble(parts[1].trim());
            boolean res = d1 <= d2;
            return negate ? !res : res;
        }
        if (cond.contains(" > ")) {
            String[] parts = cond.split(" > ", 2);
            double d1 = parseDouble(evaluateExpression(parts[0].trim(), req, localScope));
            double d2 = parseDouble(parts[1].trim());
            boolean res = d1 > d2;
            return negate ? !res : res;
        }
        if (cond.contains(" < ")) {
            String[] parts = cond.split(" < ", 2);
            double d1 = parseDouble(evaluateExpression(parts[0].trim(), req, localScope));
            double d2 = parseDouble(parts[1].trim());
            boolean res = d1 < d2;
            return negate ? !res : res;
        }

        Object obj = evaluateExpression(cond, req, localScope);
        boolean truthy = false;
        if (obj instanceof Boolean) {
            truthy = (Boolean) obj;
        } else if (obj instanceof Number) {
            truthy = ((Number) obj).doubleValue() > 0;
        } else if (obj != null) {
            truthy = true;
        }
        return negate ? !truthy : truthy;
    }

    private static double parseDouble(Object obj) {
        if (obj == null) return 0.0;
        try {
            return Double.parseDouble(String.valueOf(obj).replaceAll("[^0-9.-]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static String unquote(String s) {
        if ((s.startsWith("'") && s.endsWith("'")) || (s.startsWith("\"") && s.endsWith("\""))) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private static String processExpressions(String template, HttpServletRequest req, Map<String, Object> localScope) {
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String expr = matcher.group(1).trim();
            Object val = evaluateExpression(expr, req, localScope);
            String replacement = val != null ? String.valueOf(val) : "";
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static Object evaluateExpression(String expr, HttpServletRequest req, Map<String, Object> localScope) {
        if (expr == null || expr.isEmpty()) return null;
        if (expr.equals("pageContext.request.contextPath")) return req.getContextPath();

        String[] parts = expr.split("\\.");
        String rootName = parts[0];
        Object current = null;
        int startIndex = 1;

        if ("sessionScope".equals(rootName)) {
            if (req.getSession(false) != null && parts.length > 1) {
                current = req.getSession(false).getAttribute(parts[1]);
                startIndex = 2;
            }
        } else if ("requestScope".equals(rootName)) {
            if (parts.length > 1) {
                current = req.getAttribute(parts[1]);
                startIndex = 2;
            }
        } else if (localScope != null && localScope.containsKey(rootName)) {
            current = localScope.get(rootName);
        } else if (req.getAttribute(rootName) != null) {
            current = req.getAttribute(rootName);
        } else if (req.getSession(false) != null && req.getSession(false).getAttribute(rootName) != null) {
            current = req.getSession(false).getAttribute(rootName);
        }

        if (current == null) {
            if ("true".equalsIgnoreCase(rootName)) return Boolean.TRUE;
            if ("false".equalsIgnoreCase(rootName)) return Boolean.FALSE;
            return null;
        }

        for (int i = startIndex; i < parts.length; i++) {
            if (current == null) return null;
            String prop = parts[i];
            current = getProperty(current, prop);
        }
        return current;
    }

    private static Object getProperty(Object obj, String propName) {
        if (obj == null) return null;
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).get(propName);
        }
        if (propName.endsWith("()")) {
            propName = propName.substring(0, propName.length() - 2);
        }
        String capitalized = Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
        String[] methodNames = new String[]{"get" + capitalized, "is" + capitalized, "has" + capitalized, propName};
        for (String mName : methodNames) {
            try {
                Method m = obj.getClass().getMethod(mName);
                m.setAccessible(true);
                return m.invoke(obj);
            } catch (Exception ignored) {}
        }
        return null;
    }

    // =========================================================================
    // Lightweight Servlet API Implementations
    // =========================================================================

    static class SimpleHttpSession implements HttpSession {
        private final String id;
        private final long creationTime = System.currentTimeMillis();
        private final Map<String, Object> attributes = new ConcurrentHashMap<>();

        public SimpleHttpSession(String id) { this.id = id; }
        @Override public String getId() { return id; }
        @Override public long getCreationTime() { return creationTime; }
        @Override public long getLastAccessedTime() { return creationTime; }
        @Override public ServletContext getServletContext() { return null; }
        @Override public void setMaxInactiveInterval(int interval) {}
        @Override public int getMaxInactiveInterval() { return 1800; }
        @Override public Object getAttribute(String name) { return attributes.get(name); }
        @Override public Enumeration<String> getAttributeNames() { return Collections.enumeration(attributes.keySet()); }
        @Override public void setAttribute(String name, Object value) {
            if (value == null) attributes.remove(name);
            else attributes.put(name, value);
        }
        @Override public void removeAttribute(String name) { attributes.remove(name); }
        @Override public void invalidate() { attributes.clear(); }
        @Override public boolean isNew() { return false; }
    }

    static class SimpleServletRequest implements HttpServletRequest {
        private final HttpExchange exchange;
        private final String servletPath;
        private final Map<String, List<String>> params;
        private final SimpleHttpSession session;
        private final Map<String, Object> attributes = new HashMap<>();

        public SimpleServletRequest(HttpExchange exchange, String servletPath, 
                                    Map<String, List<String>> params, SimpleHttpSession session) {
            this.exchange = exchange;
            this.servletPath = servletPath;
            this.params = params;
            this.session = session;
        }

        @Override public String getParameter(String name) {
            List<String> list = params.get(name);
            return (list != null && !list.isEmpty()) ? list.get(0) : null;
        }
        @Override public Enumeration<String> getParameterNames() { return Collections.enumeration(params.keySet()); }
        @Override public String[] getParameterValues(String name) {
            List<String> list = params.get(name);
            return list != null ? list.toArray(new String[0]) : null;
        }
        @Override public Map<String, String[]> getParameterMap() {
            Map<String, String[]> map = new HashMap<>();
            params.forEach((k, v) -> map.put(k, v.toArray(new String[0])));
            return map;
        }
        @Override public String getMethod() { return exchange.getRequestMethod(); }
        @Override public String getRequestURI() { return exchange.getRequestURI().getPath(); }
        @Override public StringBuffer getRequestURL() { return new StringBuffer("http://localhost:8080" + getRequestURI()); }
        @Override public String getServletPath() { return servletPath; }
        @Override public String getContextPath() { return ""; }
        @Override public String getPathInfo() { return null; }
        @Override public String getQueryString() { return exchange.getRequestURI().getRawQuery(); }
        @Override public HttpSession getSession(boolean create) { return session; }
        @Override public HttpSession getSession() { return session; }
        @Override public Object getAttribute(String name) { return attributes.get(name); }
        @Override public Enumeration<String> getAttributeNames() { return Collections.enumeration(attributes.keySet()); }
        @Override public void setAttribute(String name, Object o) { attributes.put(name, o); }
        @Override public void removeAttribute(String name) { attributes.remove(name); }
        @Override public RequestDispatcher getRequestDispatcher(String path) {
            return new RequestDispatcher() {
                @Override
                public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
                    renderJsp(path, (HttpServletRequest) request, (HttpServletResponse) response);
                }
                @Override public void include(ServletRequest request, ServletResponse response) {}
            };
        }
        @Override public String getHeader(String name) { return exchange.getRequestHeaders().getFirst(name); }
        @Override public Enumeration<String> getHeaders(String name) {
            List<String> h = exchange.getRequestHeaders().get(name);
            return Collections.enumeration(h != null ? h : Collections.emptyList());
        }
        @Override public Enumeration<String> getHeaderNames() { return Collections.enumeration(exchange.getRequestHeaders().keySet()); }
        @Override public Cookie[] getCookies() { return new Cookie[0]; }
        @Override public String getProtocol() { return exchange.getProtocol(); }
        @Override public String getScheme() { return "http"; }
        @Override public String getServerName() { return "localhost"; }
        @Override public int getServerPort() { return 8080; }
        @Override public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
        }
        @Override public ServletInputStream getInputStream() { return null; }
        @Override public String getContentType() { return exchange.getRequestHeaders().getFirst("Content-Type"); }
        @Override public int getContentLength() { return 0; }
        @Override public long getContentLengthLong() { return 0; }
        @Override public String getCharacterEncoding() { return "UTF-8"; }
        @Override public void setCharacterEncoding(String env) {}
        @Override public Locale getLocale() { return Locale.getDefault(); }
        @Override public Enumeration<Locale> getLocales() { return Collections.enumeration(List.of(Locale.getDefault())); }
        @Override public boolean isSecure() { return false; }
        @Override public String getRemoteAddr() { return exchange.getRemoteAddress().getAddress().getHostAddress(); }
        @Override public String getRemoteHost() { return exchange.getRemoteAddress().getHostName(); }
        @Override public int getRemotePort() { return exchange.getRemoteAddress().getPort(); }
        @Override public String getLocalAddr() { return "127.0.0.1"; }
        @Override public String getLocalName() { return "localhost"; }
        @Override public int getLocalPort() { return 8080; }
        @Override public ServletContext getServletContext() { return null; }
        @Override public AsyncContext startAsync() { return null; }
        @Override public AsyncContext startAsync(ServletRequest req, ServletResponse res) { return null; }
        @Override public boolean isAsyncStarted() { return false; }
        @Override public boolean isAsyncSupported() { return false; }
        @Override public AsyncContext getAsyncContext() { return null; }
        @Override public DispatcherType getDispatcherType() { return DispatcherType.REQUEST; }
        @Override public String getRequestId() { return UUID.randomUUID().toString(); }
        @Override public String getProtocolRequestId() { return ""; }
        @Override public ServletConnection getServletConnection() { return null; }
        @Override public String getAuthType() { return null; }
        @Override public long getDateHeader(String name) { return -1; }
        @Override public int getIntHeader(String name) { return -1; }
        @Override public String getPathTranslated() { return null; }
        @Override public String getRemoteUser() { return null; }
        @Override public boolean isUserInRole(String role) { return false; }
        @Override public Principal getUserPrincipal() { return null; }
        @Override public String getRequestedSessionId() { return session.getId(); }
        @Override public boolean isRequestedSessionIdValid() { return true; }
        @Override public boolean isRequestedSessionIdFromCookie() { return true; }
        @Override public boolean isRequestedSessionIdFromURL() { return false; }
        @Override public boolean authenticate(HttpServletResponse response) { return true; }
        @Override public void login(String username, String password) {}
        @Override public void logout() {}
        @Override public Collection<Part> getParts() { return Collections.emptyList(); }
        @Override public Part getPart(String name) { return null; }
        @Override public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass) { return null; }
        @Override public String changeSessionId() { return session.getId(); }
    }

    static class SimpleServletResponse implements HttpServletResponse {
        private final HttpExchange exchange;
        private final ByteArrayOutputStream buffer;
        private PrintWriter writer;
        private int status = 200;
        private String contentType = "text/html; charset=UTF-8";
        private boolean committed = false;

        public SimpleServletResponse(HttpExchange exchange, ByteArrayOutputStream buffer) {
            this.exchange = exchange;
            this.buffer = buffer;
            this.writer = new PrintWriter(new OutputStreamWriter(buffer, StandardCharsets.UTF_8), true);
        }

        @Override public void setContentType(String type) { this.contentType = type; }
        @Override public String getContentType() { return contentType; }
        @Override public PrintWriter getWriter() { return writer; }
        @Override public ServletOutputStream getOutputStream() {
            return new ServletOutputStream() {
                @Override public boolean isReady() { return true; }
                @Override public void setWriteListener(WriteListener writeListener) {}
                @Override public void write(int b) { buffer.write(b); }
            };
        }
        @Override public void setStatus(int sc) { this.status = sc; }
        @Override public int getStatus() { return status; }
        @Override public void sendRedirect(String location) throws IOException {
            exchange.getResponseHeaders().set("Location", location);
            exchange.sendResponseHeaders(302, -1);
            committed = true;
        }
        public void sendRedirect(String location, int sc, boolean clearBuffer) throws IOException {
            exchange.getResponseHeaders().set("Location", location);
            exchange.sendResponseHeaders(sc, -1);
            committed = true;
        }
        @Override public void setHeader(String name, String value) { exchange.getResponseHeaders().set(name, value); }
        @Override public void addHeader(String name, String value) { exchange.getResponseHeaders().add(name, value); }
        @Override public void setIntHeader(String name, int value) { setHeader(name, String.valueOf(value)); }
        @Override public void addIntHeader(String name, int value) { addHeader(name, String.valueOf(value)); }
        @Override public void setDateHeader(String name, long date) { setHeader(name, String.valueOf(date)); }
        @Override public void addDateHeader(String name, long date) { addHeader(name, String.valueOf(date)); }
        @Override public boolean containsHeader(String name) { return exchange.getResponseHeaders().containsKey(name); }
        @Override public String getHeader(String name) { return exchange.getResponseHeaders().getFirst(name); }
        @Override public Collection<String> getHeaders(String name) { return exchange.getRequestHeaders().get(name); }
        @Override public Collection<String> getHeaderNames() { return exchange.getResponseHeaders().keySet(); }
        @Override public void addCookie(Cookie cookie) {
            exchange.getResponseHeaders().add("Set-Cookie", cookie.getName() + "=" + cookie.getValue() + "; Path=/");
        }
        @Override public void sendError(int sc, String msg) throws IOException {
            this.status = sc;
            writer.write("<h1>" + sc + " " + msg + "</h1>");
        }
        @Override public void sendError(int sc) throws IOException { sendError(sc, "Error"); }
        @Override public void setContentLength(int len) {}
        @Override public void setContentLengthLong(long len) {}
        @Override public void setBufferSize(int size) {}
        @Override public int getBufferSize() { return 8192; }
        @Override public void flushBuffer() { writer.flush(); }
        @Override public void resetBuffer() { buffer.reset(); }
        @Override public boolean isCommitted() { return committed; }
        @Override public void reset() { buffer.reset(); }
        @Override public void setLocale(Locale loc) {}
        @Override public Locale getLocale() { return Locale.getDefault(); }
        @Override public String getCharacterEncoding() { return "UTF-8"; }
        @Override public void setCharacterEncoding(String charset) {}
        @Override public String encodeURL(String url) { return url; }
        @Override public String encodeRedirectURL(String url) { return url; }
    }
}
