import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Observable, switchMap, tap } from 'rxjs';

/** What this page session knows of the server's login state. */
type SessionState = 'unknown' | 'authenticated' | 'anonymous';

/** Answer of the session endpoint: whether the request has an authenticated principal. */
interface SessionAnswer {
  authenticated: boolean;
}

/** Login state of the server session (its cookie is the real state), read on creation. */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly session = signal<SessionState>('unknown');
  /** Counts the changes of the state: a read that was started before one is out of date. */
  private sequence = 0;
  private readonly firstRead: Promise<void>;

  constructor(private readonly http: HttpClient) {
    this.firstRead = this.refresh();
  }

  get isAuthenticated(): boolean {
    return this.session() === 'authenticated';
  }

  get isAnonymous(): boolean {
    return this.session() === 'anonymous';
  }

  /** Resolves when the first read of the server's state has settled (answered or failed). */
  whenSettled(): Promise<void> {
    return this.firstRead;
  }

  /** Reads the server's state; a failed read leaves it unknown, a read overtaken by a login, logout or 401 is ignored. */
  refresh(): Promise<void> {
    const ticket = ++this.sequence;
    return new Promise((resolve) => {
      this.http.get<SessionAnswer>('/api/auth/session').subscribe({
        next: (answer) => {
          if (ticket === this.sequence) {
            this.session.set(answer.authenticated === true ? 'authenticated' : 'anonymous');
          }
          resolve();
        },
        error: () => {
          if (ticket === this.sequence) {
            this.session.set('unknown');
          }
          resolve();
        },
      });
    });
  }

  /** The server answered 401: it does not know this caller. */
  expire(): void {
    this.sequence++;
    this.session.set('anonymous');
  }

  login(username: string, password: string): Observable<void> {
    // The GET makes the server issue the XSRF-TOKEN cookie; HttpClient sends it back
    // in X-XSRF-TOKEN on the POST, which the server refuses without it.
    return this.http.get<void>('/api/auth/login').pipe(
      switchMap(() => this.http.post<void>('/api/auth/login', { username, password })),
      tap(() => {
        this.sequence++;
        this.session.set('authenticated');
      }),
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>('/api/auth/logout', {}).pipe(
      tap(() => {
        this.sequence++;
        this.session.set('anonymous');
      }),
    );
  }
}
