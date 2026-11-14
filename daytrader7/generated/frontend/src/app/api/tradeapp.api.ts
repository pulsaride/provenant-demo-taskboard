// Typed client of the REST endpoints that replace the JSF backing bean `tradeapp`.
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TradeappLoginRequest, TradeappLogoutRequest, TradeappUpdateProfileRequest } from './models';
import { query } from './query';

@Injectable({ providedIn: 'root' })
export class TradeappApi {
  private readonly http = inject(HttpClient);

  /** POST /api/tradeapp/login */
  login(body: TradeappLoginRequest): Observable<void> {
    return this.http.post<void>('/api/tradeapp/login', body);
  }

  /** POST /api/tradeapp/logout */
  logout(body: TradeappLogoutRequest): Observable<void> {
    return this.http.post<void>('/api/tradeapp/logout', body);
  }

  /** GET /api/tradeapp/results */
  getResults(): Observable<string> {
    return this.http.get<string>('/api/tradeapp/results');
  }

  /** POST /api/tradeapp/updateProfile */
  updateProfile(body: TradeappUpdateProfileRequest): Observable<void> {
    return this.http.post<void>('/api/tradeapp/updateProfile', body);
  }
}
