package com.mediscreen.uiservice.model;

public class Report {
    String riskLevel;
    String finalResult;

    public Report() {
    }

    public Report(String riskLevel, String finalResult) {
        this.riskLevel = riskLevel;
        this.finalResult = finalResult;
    }

    public String getRiskLevel() {
        return riskLevel;
    }
}
