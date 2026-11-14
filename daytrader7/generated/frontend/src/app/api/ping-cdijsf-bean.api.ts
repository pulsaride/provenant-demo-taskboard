// Typed client of the REST endpoints that replace the JSF backing bean `pingCDIJSFBean`.
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { query } from './query';

@Injectable({ providedIn: 'root' })
export class PingCDIJSFBeanApi {
  private readonly http = inject(HttpClient);

  /** GET /api/pingCDIJSFBean/hitCount */
  getHitCount(): Observable<number> {
    return this.http.get<number>('/api/pingCDIJSFBean/hitCount');
  }
}
