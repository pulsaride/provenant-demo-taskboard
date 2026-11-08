import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { FacesMessages } from '../messages/faces-messages.service';

@Component({
  selector: 'app-error',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <form [formGroup]="form" data-legacy-body>
      <header>
        <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark" role="navigation">
          <div class="container">
            <a [routerLink]="['/tasks']" class="navbar-brand"><span class="text-uppercase">Java EE 8 TaskBoard</span></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
              <ul class="navbar-nav mr-auto">
                <!-- TODO(ui_text): dynamic_class_not_carried:request_uri_contains -->
                <li class="nav-item">
                  <a [routerLink]="['/tasks']" class="nav-link text-uppercase">All Tasks <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                  <a [routerLink]="['/form']" class="nav-link text-uppercase"><span class="fa fa-plus-circle" aria-hidden="true"></span> New Task</a>
                </li>
              </ul>
              <div class="form-inline mt-2 mt-md-0">
                @if (auth.isAuthenticated) {
                  <button type="button" (click)="logout()" class="btn btn-danger btn-lg my-2 my-sm-0">Logout</button>
                }
                @if (auth.isAnonymous) {
                  <button type="button" [routerLink]="['/login']" class="btn btn-outline-success btn-lg my-2 my-sm-0">Login</button>
                }
              </div>
            </div>
          </div>
        </nav>
      </header>
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
        <div class="page-header">
          <h1> Psst...something was wrong</h1>
        </div>
        <div class="row">
          <div class="col-md-12">
            <div class="panel panel-danger">
              <div class="panel-heading"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> Error Details</div>
              <div class="panel-body">
                <p><!-- TODO(ui_text): text_expression_not_carried --></p>
              </div>
            </div>
          </div>
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
export class ErrorComponent {
  protected readonly form = this.fb.nonNullable.group({
  });


  constructor(
    private readonly fb: FormBuilder,
    protected readonly auth: AuthService,
    protected readonly facesMessages: FacesMessages,
  ) {}

  protected logout(): void {
    this.auth.logout().subscribe({
      error: () => { void this.auth.refresh(); },
    });
  }
}
