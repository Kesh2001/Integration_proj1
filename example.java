import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface LoanApplication {
  selfReportedExpenses: number;
  annualIncome: number;
  // ... all other fields
}

interface LoanPrediction {
  approved: boolean;
  approvedAmount: number | null;
  interestRate: number | null;
}

@Injectable({
  providedIn: 'root'
})
export class LoanPredictionService {
  private apiUrl = '/api/loan/predict';

  constructor(private http: HttpClient) { }

  predictLoan(application: LoanApplication): Observable<LoanPrediction> {
    return this.http.post<LoanPrediction>(this.apiUrl, application);
  }
}