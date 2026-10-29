package com.smartcart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the complete Cart Risk & Value Report before checkout.
 * Includes TCO (Lifetime Cost) and Smart Setup Completion audit.
 */
public class AnalysisReport implements Serializable {
    private static final long serialVersionUID = 1L;

    private int readinessScore = 100; // 0 - 100
    private String statusTitle = "Ready to Purchase";
    private String statusBadgeClass = "badge-success";
    private String executiveSummary = "Your cart items look well-coordinated and within parameters.";
    
    private double cartTotal;
    private double budget;
    private double remainingBudget;
    private double budgetUsagePercentage;
    private boolean budgetExceeded;

    // Feature E: 3-Year Lifetime Cost of Ownership (TCO)
    private double estimatedThreeYearTCO = 0.0;
    private double energyAndMaintenanceTotal = 0.0;

    // Feature F: Setup Completion
    private int setupCompletionScore = 100;
    private List<SetupBundle> recommendedBundles = new ArrayList<>();

    private List<MistakeAlert> mistakeAlerts = new ArrayList<>();
    private List<CompatibilityIssue> compatibilityIssues = new ArrayList<>();
    private List<AlternativeRecommendation> alternativeRecommendations = new ArrayList<>();
    private double totalPotentialSavings = 0.0;

    public AnalysisReport() {}

    public int getReadinessScore() { return readinessScore; }
    public void setReadinessScore(int readinessScore) { 
        this.readinessScore = Math.max(0, Math.min(100, readinessScore)); 
    }

    public String getStatusTitle() { return statusTitle; }
    public void setStatusTitle(String statusTitle) { this.statusTitle = statusTitle; }

    public String getStatusBadgeClass() { return statusBadgeClass; }
    public void setStatusBadgeClass(String statusBadgeClass) { this.statusBadgeClass = statusBadgeClass; }

    public String getScoreColorClass() {
        if (readinessScore >= 85) return "score-green";
        if (readinessScore >= 60) return "score-amber";
        return "score-red";
    }

    public int getAlternativeCount() {
        return alternativeRecommendations != null ? alternativeRecommendations.size() : 0;
    }

    public int getMistakeCount() {
        return mistakeAlerts != null ? mistakeAlerts.size() : 0;
    }

    public String getExecutiveSummary() { return executiveSummary; }
    public void setExecutiveSummary(String executiveSummary) { this.executiveSummary = executiveSummary; }

    public double getCartTotal() { return cartTotal; }
    public void setCartTotal(double cartTotal) { this.cartTotal = cartTotal; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public double getRemainingBudget() { return remainingBudget; }
    public void setRemainingBudget(double remainingBudget) { this.remainingBudget = remainingBudget; }

    public double getBudgetUsagePercentage() { return budgetUsagePercentage; }
    public void setBudgetUsagePercentage(double budgetUsagePercentage) { this.budgetUsagePercentage = budgetUsagePercentage; }

    public boolean isBudgetExceeded() { return budgetExceeded; }
    public void setBudgetExceeded(boolean budgetExceeded) { this.budgetExceeded = budgetExceeded; }

    public double getEstimatedThreeYearTCO() { return estimatedThreeYearTCO; }
    public void setEstimatedThreeYearTCO(double estimatedThreeYearTCO) { this.estimatedThreeYearTCO = estimatedThreeYearTCO; }

    public double getEnergyAndMaintenanceTotal() { return energyAndMaintenanceTotal; }
    public void setEnergyAndMaintenanceTotal(double energyAndMaintenanceTotal) { this.energyAndMaintenanceTotal = energyAndMaintenanceTotal; }

    public int getSetupCompletionScore() { return setupCompletionScore; }
    public void setSetupCompletionScore(int setupCompletionScore) { this.setupCompletionScore = setupCompletionScore; }

    public List<SetupBundle> getRecommendedBundles() { return recommendedBundles; }
    public void setRecommendedBundles(List<SetupBundle> recommendedBundles) { this.recommendedBundles = recommendedBundles; }

    public List<MistakeAlert> getMistakeAlerts() { return mistakeAlerts; }
    public void setMistakeAlerts(List<MistakeAlert> mistakeAlerts) { this.mistakeAlerts = mistakeAlerts; }

    public List<CompatibilityIssue> getCompatibilityIssues() { return compatibilityIssues; }
    public void setCompatibilityIssues(List<CompatibilityIssue> compatibilityIssues) { this.compatibilityIssues = compatibilityIssues; }

    public List<AlternativeRecommendation> getAlternativeRecommendations() { return alternativeRecommendations; }
    public void setAlternativeRecommendations(List<AlternativeRecommendation> alternativeRecommendations) { 
        this.alternativeRecommendations = alternativeRecommendations; 
    }

    public double getTotalPotentialSavings() { return totalPotentialSavings; }
    public void setTotalPotentialSavings(double totalPotentialSavings) { this.totalPotentialSavings = totalPotentialSavings; }

    public String getFormattedTotalPotentialSavings() {
        return String.format("₹%,.2f", totalPotentialSavings);
    }

    public String getFormattedThreeYearTCO() {
        return String.format("₹%,.2f", estimatedThreeYearTCO);
    }

    public String getFormattedEnergyAndMaintenance() {
        return String.format("₹%,.2f", energyAndMaintenanceTotal);
    }

    public int getCriticalIssueCount() {
        int count = 0;
        for (CompatibilityIssue issue : compatibilityIssues) {
            if ("CRITICAL".equalsIgnoreCase(issue.getSeverity())) {
                count++;
            }
        }
        return count;
    }

    public int getWarningCount() {
        int count = mistakeAlerts.size();
        for (CompatibilityIssue issue : compatibilityIssues) {
            if (!"CRITICAL".equalsIgnoreCase(issue.getSeverity())) {
                count++;
            }
        }
        if (budgetExceeded) count++;
        return count;
    }

    public boolean hasAnyRisks() {
        return getCriticalIssueCount() > 0 || getWarningCount() > 0;
    }
}
