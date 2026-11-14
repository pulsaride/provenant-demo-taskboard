// Typed client of the REST endpoints that replace the JSF backing bean `viewTaskAction`.
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ViewTaskActionTaskView } from './models';
import { query } from './query';

@Injectable({ providedIn: 'root' })
export class ViewTaskActionApi {
  private readonly http = inject(HttpClient);

  /** GET /api/viewTaskAction/task */
  getTask(params: { id?: number | null } = {}): Observable<ViewTaskActionTaskView> {
    return this.http.get<ViewTaskActionTaskView>('/api/viewTaskAction/task', { params: query(params) });
  }
}
