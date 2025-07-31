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

  async logout() {
    this.jwt.set(false);
    window.sessionStorage.removeItem('userdetails');
    await this.keycloakService.logout('http://localhost:4200');
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

 getAuthoritiesJwt(): string[] {
  const kc = this.keycloakService.getKeycloakInstance();
  if (kc && kc.token) {
    const payload = this.decodeJwtPayload(kc.token);
    if (!payload) return [];

    const realmRoles = payload.realm_access?.roles || [];
    const clientRoles = payload.resource_access?.['store-security-frontend']?.roles || [];

    const allRoles = [...realmRoles, ...clientRoles];

    const filteredRoles = allRoles.filter(role => 
      !role.startsWith('default-roles') &&
      role !== 'offline_access' &&
      role !== 'uma_authorization'
    );

    return Array.from(new Set(filteredRoles));
  }
  return [];
}



  private decodeJwtPayload(token: string): any | null {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
          })
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (e) {
      console.warn('Invalid JWT token', e);
      return null;
    }
  }
}
