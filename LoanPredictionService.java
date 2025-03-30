package com.loanprediction.service;

import com.loanprediction.model.LoanApplication;
import com.loanprediction.model.LoanPrediction;
import org.python.util.PythonInterpreter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
public class LoanPredictionService {
    private PythonInterpreter pyInterpreter;
    
    @PostConstruct
    public void init() throws IOException {
        pyInterpreter = new PythonInterpreter();
        
        // Load Python script with prediction logic
        InputStream scriptStream = new ClassPathResource("python/prediction.py").getInputStream();
        pyInterpreter.execfile(scriptStream);
        scriptStream.close();
        
        // Load models
        loadModel("best_classifier.pkl");
        loadModel("best_regression_limit.pkl");
        loadModel("best_regression_interest.pkl");
    }
    
    private void loadModel(String modelName) throws IOException {
        InputStream modelStream = new ClassPathResource("models/" + modelName).getInputStream();
        pyInterpreter.set("model_path", modelStream);
        pyInterpreter.exec("import pickle\n" +
                          "with open(model_path, 'rb') as f:\n" +
                          "    " + modelName.replace(".pkl", "") + " = pickle.load(f)");
        modelStream.close();
    }
    
    public LoanPrediction predict(LoanApplication application) {
        // Convert Java object to Python dictionary
        pyInterpreter.set("app_data", application);
        
        // Execute prediction
        pyInterpreter.exec("import numpy as np\n" +
                          "from sklearn.preprocessing import StandardScaler, OneHotEncoder\n" +
                          "from sklearn.compose import ColumnTransformer\n" +
                          "import pandas as pd\n" +
                          "\n" +
                          "# Define preprocessing (must match training)\n" +
                          "numeric_cols = ['selfReportedExpenses', 'annualIncome', 'selfReportedDebt', 'requestedAmount', 'age', 'monthsEmployed', 'creditScore', 'creditUtilization', 'numOpenAccounts', 'numCreditInquiries', 'estimatedDebt', 'dti']\n" +
                          "cat_cols = ['province', 'employmentStatus', 'paymentHistory']\n" +
                          "\n" +
                          "# Create DataFrame from input\n" +
                          "input_df = pd.DataFrame([app_data])\n" +
                          "\n" +
                          "# Preprocessing pipeline\n" +
                          "preprocessor = ColumnTransformer([\n" +
                          "    ('num', StandardScaler(), numeric_cols),\n" +
                          "    ('cat', OneHotEncoder(drop='first', handle_unknown='ignore'), cat_cols)\n" +
                          "])\n" +
                          "\n" +
                          "# Transform input\n" +
                          "processed_input = preprocessor.fit_transform(input_df)\n" +
                          "\n" +
                          "# Make predictions\n" +
                          "approved = best_classifier.predict(processed_input)[0]\n" +
                          "amount = best_regression_limit.predict(processed_input)[0] if approved else 0\n" +
                          "rate = best_regression_interest.predict(processed_input)[0] if approved else 0\n" +
                          "\n" +
                          "result = {'approved': bool(approved), 'amount': float(amount), 'rate': float(rate)}");
        
        // Get results back from Python
        Boolean approved = pyInterpreter.get("result", Boolean.class);
        Double amount = pyInterpreter.get("amount", Double.class);
        Double rate = pyInterpreter.get("rate", Double.class);
        
        return new LoanPrediction(approved, amount, rate);
    }
}