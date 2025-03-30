package com.loanprediction.model;

import lombok.Data;

@Data
public class LoanApplication {
    private Double selfReportedExpenses;
    private Double annualIncome;
    private Double selfReportedDebt;
    private Double requestedAmount;
    private Integer age;
    private Integer monthsEmployed;
    private Integer creditScore;
    private Double creditUtilization;
    private Integer numOpenAccounts;
    private Integer numCreditInquiries;
    private Double estimatedDebt;
    private Double dti;
    private String province;
    private String employmentStatus;
    private String paymentHistory;
}