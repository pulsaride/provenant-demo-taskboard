import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

/** Waits for the server's login state, then sends an anonymous user to the login screen, remembering where they were going. */
export const authGuard: CanActivateFn = async (_route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  await auth.whenSettled();
  if (auth.isAuthenticated) {
    return true;
  }
  return router.createUrlTree(['/login'], {
    queryParams: { continued: 'true', returnUrl: state.url },
  });
};
