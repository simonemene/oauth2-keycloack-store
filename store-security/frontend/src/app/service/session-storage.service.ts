import { computed, inject, Injectable, signal } from '@angular/core';
import { UserDto } from '../model/UserDto';
import { Router } from '@angular/router';
import { withNoHttpTransferCache } from '@angular/platform-browser';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root',
})
export class SessionStorageService {
  private readonly jwt = signal<boolean | null>(false);

  keycloakService = inject(KeycloakService);

  user: UserDto;

  readonly isAuthenticated = computed(() => {
    const jwt = this.jwt();
    return jwt != null && jwt;
  });

  constructor(private router: Router) {
    this.user = JSON.parse(window.sessionStorage.getItem('userdetails')!);
    if (this.user) {
      try {
        if (this.user.authorization == 'AUTH') {
          this.jwt.set(true);
        }
      } catch (e) {
        console.warn('Error authentication');
      }
    }
  }

  login(user:UserDto) {
    user.authorization = 'AUTH';
    window.sessionStorage.setItem('userdetails',JSON.stringify(user));
    this.jwt.set(true);
  }

  logout() {
    this.jwt.set(false);
    window.sessionStorage.setItem('userdetails', '');
    this.keycloakService.logout();
    this.router.navigate(['/']);
  }

  getJwt(): boolean | null {
    return this.jwt();
  }

  getUsernameJwt(): string {
    let user = new UserDto();
    user = JSON.parse(window.sessionStorage.getItem('userdetails')!);
    if (user) {
      return user.username;
    }
    return '';
  }

  /*getAuthoritiesJwt(): string[] {
    let jwt = window.sessionStorage.getItem('Authorization');
    if (jwt) {
      const payload = this.decodeJwtPayload(jwt);
      const authoritiesString = payload?.authorities;
      return authoritiesString?.split(',') || [];
    }
    return [];
  }*/
}
