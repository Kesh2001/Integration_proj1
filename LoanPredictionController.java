package com.loanprediction.controller;

import com.loanprediction.model.LoanApplication;
import com.loanprediction.model.LoanPrediction;
import com.loanprediction.service.LoanPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan")
public class LoanPredictionController {
    
    @Autowired
    private LoanPredictionService predictionService;
    
    @PostMapping("/predict")
    public LoanPrediction predict(@RequestBody LoanApplication application) {
        return predictionService.predict(application);
    }
}