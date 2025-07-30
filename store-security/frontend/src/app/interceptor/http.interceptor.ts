import {
  HttpErrorResponse,
  HttpHeaders,
  HttpInterceptorFn,
} from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular'; // importa il servizio keycloak
import { SessionStorageService } from '../service/session-storage.service';

export const httpInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const sessionStorageService = inject(SessionStorageService);
  const keycloakService = inject(KeycloakService); 
  let httpHeaders = new HttpHeaders();

  keycloakService.getToken().then(token => {
  console.log("Token reale:", token);

  let httpHeaders = new HttpHeaders();
  if (token) {
    httpHeaders = httpHeaders.append('Authorization', `Bearer ${token}`);
  }

  });


  httpHeaders = httpHeaders.append('X-Requested-With', 'XMLHttpRequest');

  const handleHeader = req.clone({
    headers: httpHeaders,
  });

  return next(handleHeader).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401) {
        sessionStorageService.logout();
        router.navigate(['/login']);
      }
      return throwError(() => err);
    })
  );
};
