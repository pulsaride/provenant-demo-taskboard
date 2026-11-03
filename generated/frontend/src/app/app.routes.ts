import { Routes } from '@angular/router';
import { authGuard } from './auth/auth.guard';
import { DetailsComponent } from './details/details.component';
import { ErrorComponent } from './error/error.component';
import { FormComponent } from './form/form.component';
import { LoginComponent } from './login/login.component';
import { TasksComponent } from './tasks/tasks.component';

export const routes: Routes = [
  { path: '', redirectTo: 'tasks', pathMatch: 'full' },
  { path: 'details', component: DetailsComponent },
  { path: 'error', component: ErrorComponent },
  { path: 'form', component: FormComponent, canActivate: [authGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'tasks', component: TasksComponent },
];
