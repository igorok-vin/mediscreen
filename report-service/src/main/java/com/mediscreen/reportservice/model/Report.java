package com.mediscreen.reportservice.model;

public class Report {
    String riskLevel;
    String finalResult;

    public Report(String riskLevel, String finalResult) {
        this.riskLevel = riskLevel;
        this.finalResult = finalResult;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getFinalResult() {
        return finalResult;
    }

    @Override
    public String toString() {
        return "Report{" +
                "riskLevel='" + riskLevel + '\'' +
                ", finalResult='" + finalResult + '\'' +
                '}';
    }
}
