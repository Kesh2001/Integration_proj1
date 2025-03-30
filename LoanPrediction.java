package com.loanprediction.model;

import lombok.Data;

@Data
public class LoanPrediction {
    private Boolean approved;
    private Double approvedAmount;
    private Double interestRate;
    
    public LoanPrediction(Boolean approved, Double approvedAmount, Double interestRate) {
        this.approved = approved;
        this.approvedAmount = approvedAmount != null ? Math.round(approvedAmount * 100.0) / 100.0 : null;
        this.interestRate = interestRate != null ? Math.round(interestRate * 100.0) / 100.0 : null;
    }
}