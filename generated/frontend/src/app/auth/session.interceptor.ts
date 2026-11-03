import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn, withInterceptors } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

/** A 401 of any call: the server session is gone or never existed. */
const markExpired: HttpInterceptorFn = (request, next) =>
  next(request).pipe(
    catchError((error: unknown) => {
      if (error instanceof HttpErrorResponse && error.status === 401) {
        inject(AuthService).expire();
      }
      return throwError(() => error);
    }),
  );

/** Pass to provideHttpClient(..). */
export const sessionInterceptor = withInterceptors([markExpired]);
