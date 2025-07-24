import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { SessionStorageService } from '../service/session-storage.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authorization = inject(SessionStorageService);
  const auth = authorization.getAuthoritiesJwt();
  const allowRoles = route.data['roles'] as string[];

  let verify=false;

  if(auth && allowRoles)
  {
    verify = allowRoles.some(role=>auth.includes(role));
  }
  return verify;

};
