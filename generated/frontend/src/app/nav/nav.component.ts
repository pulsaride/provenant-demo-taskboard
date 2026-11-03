import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <form [formGroup]="form">
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
    </form>
  `,
})
export class NavComponent {
  protected readonly form = this.fb.nonNullable.group({
  });


  constructor(
    private readonly fb: FormBuilder,
    protected readonly auth: AuthService,
  ) {}

  protected logout(): void {
    this.auth.logout().subscribe({
      error: () => { void this.auth.refresh(); },
    });
  }
}
