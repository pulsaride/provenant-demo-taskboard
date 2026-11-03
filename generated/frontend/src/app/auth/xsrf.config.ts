import { withXsrfConfiguration } from '@angular/common/http';

/** The CSRF cookie and header of the server; pass to provideHttpClient(..). */
export const xsrfConfiguration = withXsrfConfiguration({
  cookieName: 'XSRF-TOKEN',
  headerName: 'X-XSRF-TOKEN',
});
