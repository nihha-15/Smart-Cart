package com.smartcart.model;

import java.io.Serializable;

/**
 * Represents a hardware/spec incompatibility issue between items in the cart.
 */
public class CompatibilityIssue implements Serializable {
    private static final long serialVersionUID = 1L;

    private String issueId;
    private String ruleCode;
    private int primaryProductId;
    private String primaryProductName;
    private int accessoryProductId;
    private String accessoryProductName;
    private String severity; // "CRITICAL", "WARNING", "INFO"
    private String title;
    private String reason;
    private String suggestedAction;
    private int recommendedProductId;
    private String recommendedProductName;

    public CompatibilityIssue() {}

    public CompatibilityIssue(String issueId, String ruleCode, int primaryProductId, 
                              String primaryProductName, int accessoryProductId, 
                              String accessoryProductName, String severity, String title, 
                              String reason, String suggestedAction, int recommendedProductId, 
                              String recommendedProductName) {
        this.issueId = issueId;
        this.ruleCode = ruleCode;
        this.primaryProductId = primaryProductId;
        this.primaryProductName = primaryProductName;
        this.accessoryProductId = accessoryProductId;
        this.accessoryProductName = accessoryProductName;
        this.severity = severity;
        this.title = title;
        this.reason = reason;
        this.suggestedAction = suggestedAction;
        this.recommendedProductId = recommendedProductId;
        this.recommendedProductName = recommendedProductName;
    }

    public String getIssueId() { return issueId; }
    public void setIssueId(String issueId) { this.issueId = issueId; }

    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }

    public int getPrimaryProductId() { return primaryProductId; }
    public void setPrimaryProductId(int primaryProductId) { this.primaryProductId = primaryProductId; }

    public String getPrimaryProductName() { return primaryProductName; }
    public void setPrimaryProductName(String primaryProductName) { this.primaryProductName = primaryProductName; }

    public int getAccessoryProductId() { return accessoryProductId; }
    public void setAccessoryProductId(int accessoryProductId) { this.accessoryProductId = accessoryProductId; }

    public String getAccessoryProductName() { return accessoryProductName; }
    public void setAccessoryProductName(String accessoryProductName) { this.accessoryProductName = accessoryProductName; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getSuggestedAction() { return suggestedAction; }
    public void setSuggestedAction(String suggestedAction) { this.suggestedAction = suggestedAction; }

    public int getRecommendedProductId() { return recommendedProductId; }
    public void setRecommendedProductId(int recommendedProductId) { this.recommendedProductId = recommendedProductId; }

    public String getRecommendedProductName() { return recommendedProductName; }
    public void setRecommendedProductName(String recommendedProductName) { this.recommendedProductName = recommendedProductName; }
}
