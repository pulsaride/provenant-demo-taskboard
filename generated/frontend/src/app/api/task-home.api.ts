// Typed client of the REST endpoints that replace the JSF backing bean `taskHome`.
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FacesGlobalMessage } from '../messages/faces-messages.service';
import { TaskHomeDoingtasksView, TaskHomeDonetasksView, TaskHomeTodotasksView } from './models';
import { query } from './query';

@Injectable({ providedIn: 'root' })
export class TaskHomeApi {
  private readonly http = inject(HttpClient);

  /** POST /api/taskHome/deleteTask */
  deleteTask(params: { id: number }): Observable<FacesGlobalMessage[]> {
    return this.http.post<FacesGlobalMessage[]>('/api/taskHome/deleteTask', null, { params: query(params) });
  }

  /** GET /api/taskHome/doingtasks */
  getDoingtasks(): Observable<TaskHomeDoingtasksView[]> {
    return this.http.get<TaskHomeDoingtasksView[]>('/api/taskHome/doingtasks');
  }

  /** GET /api/taskHome/donetasks */
  getDonetasks(): Observable<TaskHomeDonetasksView[]> {
    return this.http.get<TaskHomeDonetasksView[]>('/api/taskHome/donetasks');
  }

  /** POST /api/taskHome/markTaskDoing */
  markTaskDoing(params: { id: number }): Observable<void> {
    return this.http.post<void>('/api/taskHome/markTaskDoing', null, { params: query(params) });
  }

  /** POST /api/taskHome/markTaskDone */
  markTaskDone(params: { id: number }): Observable<void> {
    return this.http.post<void>('/api/taskHome/markTaskDone', null, { params: query(params) });
  }

  /** GET /api/taskHome/todotasks */
  getTodotasks(): Observable<TaskHomeTodotasksView[]> {
    return this.http.get<TaskHomeTodotasksView[]>('/api/taskHome/todotasks');
  }
}
