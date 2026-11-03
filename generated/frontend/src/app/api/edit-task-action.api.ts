// Typed client of the REST endpoints that replace the JSF backing bean `editTaskAction`.
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FacesGlobalMessage } from '../messages/faces-messages.service';
import { Task } from './models';
import { query } from './query';

@Injectable({ providedIn: 'root' })
export class EditTaskActionApi {
  private readonly http = inject(HttpClient);

  /** POST /api/editTaskAction/save */
  save(params: { id?: number | null }, body: Task): Observable<FacesGlobalMessage[]> {
    return this.http.post<FacesGlobalMessage[]>('/api/editTaskAction/save', body, { params: query(params) });
  }

  /** GET /api/editTaskAction/task */
  getTask(params: { id?: number | null } = {}): Observable<Task> {
    return this.http.get<Task>('/api/editTaskAction/task', { params: query(params) });
  }
}
