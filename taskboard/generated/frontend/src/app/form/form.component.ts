import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { FacesMessages } from '../messages/faces-messages.service';
import { EditTaskActionApi } from '../api/edit-task-action.api';
import { Task } from '../api/models';

@Component({
  selector: 'app-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <form [formGroup]="form" (ngSubmit)="save()" data-legacy-body>
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
          <h1>@if (queryInteger('id') === null) {ADD TASK}@if (queryInteger('id') === null) {EDIT TASK (ID: {{ editTaskAction.task?.id }}, STATUS: {{ editTaskAction.task?.status }})}</h1>
        </div>
        <div class="row">
          <div class="col-md-12">
            <div class="form-group">
              <label for="name" class="text-uppercase">Task Name:</label>
              <input id="name" type="text" formControlName="name" placeholder="Type task name here..." />
              @if (submitAttempted() && form.controls.name.hasError('required')) {
                <small>Task name is required.</small>
              }
              <small class="invalid-feedback"></small>
            </div>
            <div class="form-group">
              <label for="description" class="text-uppercase">Task Description:</label>
              <textarea id="description" formControlName="description" placeholder="Describe the task content here..." rows="8"></textarea>
              @if (submitAttempted() && form.controls.description.hasError('required')) {
                <small>Description is required.</small>
              }
              <small class="invalid-feedback"></small>
            </div>
            <div class="form-group">
              <button type="submit" class="btn btn-lg btn-primary" [disabled]="submitting()">Add Task</button>
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
export class FormComponent implements OnInit {
  protected readonly submitting = signal(false);
  protected readonly submitAttempted = signal(false);

  protected readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required]],
    description: ['', [Validators.required]],
  });


  protected editTaskAction: { task: Task | null } = { task: null };

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    protected readonly auth: AuthService,
    protected readonly facesMessages: FacesMessages,
    private readonly editTaskActionApi: EditTaskActionApi,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  /** Loads what the page reads (the JSF page-load, again after a command). */
  private load(): void {
    this.editTaskActionApi.getTask({ id: this.queryInteger('id') }).subscribe({
      next: (value) => { this.editTaskAction.task = value; if (value) { this.form.patchValue(value as unknown as typeof this.form.value); } },
      error: (error: unknown) => { console.error('editTaskAction.task', error); },
    });
  }

  protected queryInteger(name: string): number | null {
    const value = this.route.snapshot.queryParamMap.get(name);
    if (value === null || !/^-?\d+$/.test(value)) {
      return null;
    }
    const parsed = Number(value);
    return Number.isSafeInteger(parsed) ? parsed : null;
  }

  protected save(): void {
    this.submitAttempted.set(true);
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    const body = { ...this.editTaskAction.task, ...this.form.getRawValue() } as unknown as Task;
    this.editTaskActionApi.save({ id: this.queryInteger('id') }, body).subscribe({
      next: (messages) => {
        this.facesMessages.showAfterNavigation(messages);
        void this.router.navigateByUrl('/tasks');
        this.submitting.set(false);
      },
      error: (error: unknown) => {
        this.facesMessages.clear();
        console.error('save', error);
        this.submitting.set(false);
      },
    });
  }

  protected logout(): void {
    this.auth.logout().subscribe({
      error: () => { void this.auth.refresh(); },
    });
  }
}
