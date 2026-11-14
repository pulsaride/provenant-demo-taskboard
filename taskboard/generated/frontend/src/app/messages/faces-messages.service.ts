import { Injectable, inject, signal } from '@angular/core';
import { NavigationEnd, NavigationStart, Router } from '@angular/router';
import { filter } from 'rxjs';

/** A JSF global message a page command queued: the severity and its literal summary. */
export interface FacesGlobalMessage {
  severity: 'INFO' | 'WARN' | 'ERROR';
  summary: string;
}

/** The messages of the last command of the page; any navigation clears them. */
@Injectable({ providedIn: 'root' })
export class FacesMessages {
  private readonly messages = signal<readonly FacesGlobalMessage[]>([]);
  readonly list = this.messages.asReadonly();

  private pending: readonly FacesGlobalMessage[] = [];

  constructor() {
    const events = inject(Router).events;
    events.pipe(filter((event) => event instanceof NavigationStart)).subscribe(() => {
      this.messages.set([]);
    });
    events.pipe(filter((event) => event instanceof NavigationEnd)).subscribe(() => {
      if (this.pending.length > 0) {
        this.messages.set(this.pending);
        this.pending = [];
      }
    });
  }

  /** The answer of a command: replaces the messages. */
  show(messages: readonly FacesGlobalMessage[]): void {
    this.messages.set(messages);
  }

  /**
   * The answer of a command that navigates to a redirect target: the legacy keeps
   * the messages in the session until the next render, which is the target's, so
   * they are shown once the navigation has ended (shown once, then gone).
   */
  showAfterNavigation(messages: readonly FacesGlobalMessage[]): void {
    this.pending = messages;
  }

  /** A command that answered with no message (or failed): nothing is pending either. */
  clear(): void {
    this.messages.set([]);
    this.pending = [];
  }
}
