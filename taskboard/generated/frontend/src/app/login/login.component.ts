import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { FacesMessages } from '../messages/faces-messages.service';

@Component({
  selector: 'app-login',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="submit()" data-legacy-body>
      <!-- TODO(ui_text): remember_me_checkbox_not_rendered -->
      <main role="main" class="container">
        <!-- TODO(ui_text): framework_messages_not_carried -->
        @if (facesMessages.list().length > 0) {
          <p></p>
          <ul id="messages" class="list-unstyled">
            @for (message of facesMessages.list(); track $index) {
              <li [attr.class]="message.severity === 'INFO' ? 'alert alert-info' : message.severity === 'WARN' ? 'alert alert-warning' : 'alert alert-error'">{{ message.summary }}</li>
            }
          </ul>
          <p></p>
        }
        <div class="form-signin">
          <div class="text-center">
            <h2 class="form-signin-heading my-2">JAVA EE 8 TASKBOARD</h2>
            <img src="legacy/images/tb.png">
          </div>
          <label for="username" class="sr-only">Username</label>
          <input id="username" type="text" formControlName="username" class="form-control" placeholder="Username" />
          <label for="password" class="sr-only">Password</label>
          <input id="password" type="password" formControlName="password" class="form-control" placeholder="Password" />
          <button type="submit" class="btn btn-lg btn-primary btn-block" [disabled]="submitting()">Login</button>
        </div>
      </main>
      <footer id="footer">
        <div class="container">
          <p class="text-muted">(c) Hantsy Bai hantsy at gmail.com </p>
        </div>
      </footer>
    </form>
  `,
})
export class LoginComponent {
  protected readonly submitting = signal(false);
  protected failed = false;
  protected readonly form = this.fb.nonNullable.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly auth: AuthService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    protected readonly facesMessages: FacesMessages,
  ) {}

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { username: username, password: password } = this.form.getRawValue();
    this.submitting.set(true);
    this.auth.login(username, password).subscribe({
      next: () => {
        this.submitting.set(false);
        this.failed = false;
        this.facesMessages.clear();
        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
        void this.router.navigateByUrl(returnUrl && returnUrl.startsWith('/') && !returnUrl.startsWith('//') ? returnUrl : '/');
      },
      error: () => {
        this.submitting.set(false);
        this.failed = true;
        this.facesMessages.show([{ severity: 'ERROR', summary: 'Authentication failed' }]);
      },
    });
  }
}
