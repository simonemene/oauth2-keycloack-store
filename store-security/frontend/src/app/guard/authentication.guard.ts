import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SessionStorageService } from '../service/session-storage.service';

export const authenticationGuard: CanActivateFn = (route, state) => {
  const autheticated = inject(SessionStorageService);
  const router = inject(Router);
  if(autheticated.isAuthenticated())
  {
    return true;
  }
  else
  {
    router.navigate(['/login']);
    return false;
  }
};
