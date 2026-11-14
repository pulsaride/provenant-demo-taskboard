// Typed client of the REST endpoints that replace the JSF backing bean `quotedata`.
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { QuotedataBuyRequest } from './models';
import { query } from './query';

@Injectable({ providedIn: 'root' })
export class QuotedataApi {
  private readonly http = inject(HttpClient);

  /** POST /api/quotedata/buy */
  buy(body: QuotedataBuyRequest): Observable<void> {
    return this.http.post<void>('/api/quotedata/buy', body);
  }

  /** POST /api/quotedata/getQuotesBySymbols */
  getQuotesBySymbols(body: string): Observable<void> {
    return this.http.post<void>('/api/quotedata/getQuotesBySymbols', body);
  }
}
