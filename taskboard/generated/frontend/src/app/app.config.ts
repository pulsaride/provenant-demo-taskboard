import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { sessionInterceptor } from './auth/session.interceptor';
import { xsrfConfiguration } from './auth/xsrf.config';

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection(), provideRouter(routes), provideHttpClient(xsrfConfiguration, sessionInterceptor)],
};
