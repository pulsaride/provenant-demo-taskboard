// Typed client of the REST endpoints that replace the JSF backing bean `portfolio`.
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { query } from './query';

@Injectable({ providedIn: 'root' })
export class PortfolioApi {
  private readonly http = inject(HttpClient);

  /** GET /api/portfolio/numberHoldings */
  getNumberHoldings(): Observable<number> {
    return this.http.get<number>('/api/portfolio/numberHoldings');
  }

  /** POST /api/portfolio/sell */
  sell(): Observable<void> {
    return this.http.post<void>('/api/portfolio/sell', null);
  }

  /** GET /api/portfolio/totalBasis */
  getTotalBasis(): Observable<number> {
    return this.http.get<number>('/api/portfolio/totalBasis');
  }

  /** GET /api/portfolio/totalGain */
  getTotalGain(): Observable<number> {
    return this.http.get<number>('/api/portfolio/totalGain');
  }

  /** GET /api/portfolio/totalGainPercentHTML */
  getTotalGainPercentHTML(): Observable<string> {
    return this.http.get<string>('/api/portfolio/totalGainPercentHTML');
  }

  /** GET /api/portfolio/totalValue */
  getTotalValue(): Observable<number> {
    return this.http.get<number>('/api/portfolio/totalValue');
  }
}
