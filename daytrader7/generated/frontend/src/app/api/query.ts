import { HttpParams } from '@angular/common/http';

/** Query parameters of a call: a value that is null or undefined is left out. */
export function query(
  values: Record<string, string | number | boolean | null | undefined>,
): HttpParams {
  let params = new HttpParams();
  for (const [name, value] of Object.entries(values)) {
    if (value !== null && value !== undefined) {
      params = params.set(name, String(value));
    }
  }
  return params;
}
