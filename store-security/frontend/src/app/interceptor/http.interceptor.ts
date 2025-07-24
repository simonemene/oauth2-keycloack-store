import {
  HttpErrorResponse,
  HttpHeaders,
  HttpInterceptorFn,
} from '@angular/common/http';
import { UserDto } from '../model/UserDto';
import { catchError, tap, throwError } from 'rxjs';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { SessionStorageService } from '../service/session-storage.service';

export const httpInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const sessionStorageService = inject(SessionStorageService);

  let httpHeaders = new HttpHeaders();

  let authorization = sessionStorage.getItem('Authorization');
  if (authorization) {
    httpHeaders = httpHeaders.append('Authorization', authorization);
  }

  httpHeaders = httpHeaders.append('X-Requested-With', 'XMLHttpRequest');

  const handleHeader = req.clone({
    headers: httpHeaders,
  });

  return next(handleHeader).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status !== 401) {
        sessionStorageService.logout();
        router.navigate(['/login']);
      }
      return throwError(() => err);
    })
  );
};
