import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { FacesMessages } from '../messages/faces-messages.service';
import { TaskHomeApi } from '../api/task-home.api';
import { TaskHomeDoingtasksView, TaskHomeDonetasksView, TaskHomeTodotasksView } from '../api/models';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <form [formGroup]="form">
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
          <h1> TASK LIST</h1>
        </div>
        <div class="row">
          <div class="col-md-4 col-xs-12">
            <div class="card">
              <div class="card-header"><span class="fa fa-list-alt" aria-hidden="true"></span> TODO </div>
              <div class="card-body">
                <p>Tasks newly added in the backlog.</p>
              </div>
              <ul id="todotasks" class="list-group">
                @for (task of taskHome.todotasks; track $index) {
                  <li class="list-group-item">
                    <h4>
                      <span>#{{ task.id }} {{ task.name }}</span>
                      &ngsp;
                      <span class="pull-right">
                        <a [routerLink]="['/details']" [queryParams]="{ id: task.id }"><span class="fa fa-file-text-o" aria-hidden="true"></span></a>
                        <!-- TODO(nav): link to /details does not reproduce: query_parameter_precision:id -->
                        &ngsp;
                        <a [routerLink]="['/form']" [queryParams]="{ id: task.id }"><span class="fa fa-pencil" aria-hidden="true"></span></a>
                        <!-- TODO(nav): link to /form does not reproduce: query_parameter_precision:id -->
                      </span>
                    </h4>
                    <p>{{ task.description }}</p>
                    <p>
                      <button type="button" class="btn btn-sm btn-success" (click)="markTaskDoing(task.id)"><span class="fa fa-play" aria-hidden="true"></span>START </button>
                    </p>
                  </li>
                }
              </ul>
            </div>
          </div>
          <div id="doingtasks" class="col-md-4 col-xs-12">
            <div class="card">
              <div class="card-header"><span class="fa fa-hourglass-start" aria-hidden="true"></span> WORK IN PROGRESS </div>
              <div class="card-body">
                <p>Tasks had been assigned and started.</p>
              </div>
              <ul id="doingtasks" class="list-group">
                @for (task of taskHome.doingtasks; track $index) {
                  <li class="list-group-item">
                    <h4>#{{ task.id }} {{ task.name }}</h4>
                    <p>{{ task.description }}</p>
                    <p>
                      <button type="button" class="btn btn-sm btn-info" (click)="markTaskDone(task.id)"><span class="fa fa-check" aria-hidden="true"></span> DONE </button>
                    </p>
                  </li>
                }
              </ul>
            </div>
          </div>
          <div id="donetasks" class="col-md-4 col-xs-12">
            <div class="card">
              <div class="card-header"><span class="fa fa-check-circle-o" aria-hidden="true"></span> DONE </div>
              <div class="card-body">
                <p>Tasks had been done successfully.</p>
              </div>
              <ul id="donetasks" class="list-group">
                @for (task of taskHome.donetasks; track $index) {
                  <li class="list-group-item">
                    <h4>#{{ task.id }} {{ task.name }}</h4>
                    <p>{{ task.description }}</p>
                    <p>
                      <button type="button" class="btn btn-sm btn-danger" (click)="deleteTask(task.id)"><span class="fa fa-trash" aria-hidden="true"></span> DELETE </button>
                    </p>
                  </li>
                }
              </ul>
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
export class TasksComponent implements OnInit {
  protected readonly form = this.fb.nonNullable.group({
  });


  protected taskHome: { todotasks: TaskHomeTodotasksView[]; doingtasks: TaskHomeDoingtasksView[]; donetasks: TaskHomeDonetasksView[] } = { todotasks: [], doingtasks: [], donetasks: [] };

  constructor(
    private readonly fb: FormBuilder,
    protected readonly auth: AuthService,
    protected readonly facesMessages: FacesMessages,
    private readonly taskHomeApi: TaskHomeApi,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  /** Loads what the page reads (the JSF page-load, again after a command). */
  private load(): void {
    this.taskHomeApi.getTodotasks().subscribe({
      next: (value) => { this.taskHome.todotasks = value;  },
      error: (error: unknown) => { console.error('taskHome.todotasks', error); },
    });
    this.taskHomeApi.getDoingtasks().subscribe({
      next: (value) => { this.taskHome.doingtasks = value;  },
      error: (error: unknown) => { console.error('taskHome.doingtasks', error); },
    });
    this.taskHomeApi.getDonetasks().subscribe({
      next: (value) => { this.taskHome.donetasks = value;  },
      error: (error: unknown) => { console.error('taskHome.donetasks', error); },
    });
  }

  protected markTaskDoing(id: number | null): void {
    if (id === null) {
      console.error('markTaskDoing: a required request parameter is null');
      return;
    }
    this.taskHomeApi.markTaskDoing({ id }).subscribe({
      next: () => { this.facesMessages.clear(); this.load(); },
      error: (error: unknown) => { this.facesMessages.clear(); console.error('markTaskDoing', error); },
    });
  }

  protected markTaskDone(id: number | null): void {
    if (id === null) {
      console.error('markTaskDone: a required request parameter is null');
      return;
    }
    this.taskHomeApi.markTaskDone({ id }).subscribe({
      next: () => { this.facesMessages.clear(); this.load(); },
      error: (error: unknown) => { this.facesMessages.clear(); console.error('markTaskDone', error); },
    });
  }

  protected deleteTask(id: number | null): void {
    if (id === null) {
      console.error('deleteTask: a required request parameter is null');
      return;
    }
    this.taskHomeApi.deleteTask({ id }).subscribe({
      next: (messages) => { this.facesMessages.show(messages); this.load(); },
      error: (error: unknown) => { this.facesMessages.clear(); console.error('deleteTask', error); },
    });
  }

  protected logout(): void {
    this.auth.logout().subscribe({
      error: () => { void this.auth.refresh(); },
    });
  }
}
