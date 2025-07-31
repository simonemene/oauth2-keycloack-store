import {
  HttpErrorResponse,
  HttpHeaders,
  HttpInterceptorFn,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { SessionStorageService } from '../service/session-storage.service';
import { catchError, switchMap, throwError, of, from } from 'rxjs';

export const httpInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const sessionStorageService = inject(SessionStorageService);
  const keycloakService = inject(KeycloakService);

  return from(keycloakService.getToken()).pipe(
    switchMap((token) => {
      let headers = req.headers
        .set('X-Requested-With', 'XMLHttpRequest');

      if (token) {
        headers = headers.set('Authorization', `Bearer ${token}`);
      }

      const clonedRequest = req.clone({ headers });

      return next(clonedRequest);
    }),
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401) {
        sessionStorageService.logout();
        router.navigate(['/login']);
      }
      return throwError(() => err);
    })
  );
};
