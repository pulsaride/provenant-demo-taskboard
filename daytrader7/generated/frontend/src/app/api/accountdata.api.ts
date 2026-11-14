// Typed client of the REST endpoints that replace the JSF backing bean `accountdata`.
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { query } from './query';

@Injectable({ providedIn: 'root' })
export class AccountdataApi {
  private readonly http = inject(HttpClient);

  /** GET /api/accountdata/accountID */
  getAccountID(): Observable<number> {
    return this.http.get<number>('/api/accountdata/accountID');
  }

  /** GET /api/accountdata/balance */
  getBalance(): Observable<number> {
    return this.http.get<number>('/api/accountdata/balance');
  }

  /** GET /api/accountdata/creationDate */
  getCreationDate(): Observable<string> {
    return this.http.get<string>('/api/accountdata/creationDate');
  }

  /** GET /api/accountdata/currentTime */
  getCurrentTime(): Observable<string> {
    return this.http.get<string>('/api/accountdata/currentTime');
  }

  /** GET /api/accountdata/gainHTML */
  getGainHTML(): Observable<string> {
    return this.http.get<string>('/api/accountdata/gainHTML');
  }

  /** GET /api/accountdata/gainPercentHTML */
  getGainPercentHTML(): Observable<string> {
    return this.http.get<string>('/api/accountdata/gainPercentHTML');
  }

  /** GET /api/accountdata/holdingsTotal */
  getHoldingsTotal(): Observable<number> {
    return this.http.get<number>('/api/accountdata/holdingsTotal');
  }

  /** GET /api/accountdata/lastLogin */
  getLastLogin(): Observable<string> {
    return this.http.get<string>('/api/accountdata/lastLogin');
  }

  /** GET /api/accountdata/loginCount */
  getLoginCount(): Observable<number> {
    return this.http.get<number>('/api/accountdata/loginCount');
  }

  /** GET /api/accountdata/logoutCount */
  getLogoutCount(): Observable<number> {
    return this.http.get<number>('/api/accountdata/logoutCount');
  }

  /** GET /api/accountdata/numberHoldings */
  getNumberHoldings(): Observable<number> {
    return this.http.get<number>('/api/accountdata/numberHoldings');
  }

  /** GET /api/accountdata/openBalance */
  getOpenBalance(): Observable<number> {
    return this.http.get<number>('/api/accountdata/openBalance');
  }

  /** GET /api/accountdata/profileID */
  getProfileID(): Observable<string> {
    return this.http.get<string>('/api/accountdata/profileID');
  }

  /** GET /api/accountdata/sessionCreationDate */
  getSessionCreationDate(): Observable<string> {
    return this.http.get<string>('/api/accountdata/sessionCreationDate');
  }

  /** GET /api/accountdata/sumOfCashHoldings */
  getSumOfCashHoldings(): Observable<number> {
    return this.http.get<number>('/api/accountdata/sumOfCashHoldings');
  }

  /** POST /api/accountdata/toggleShowAllRows */
  toggleShowAllRows(): Observable<void> {
    return this.http.post<void>('/api/accountdata/toggleShowAllRows', null);
  }
}
