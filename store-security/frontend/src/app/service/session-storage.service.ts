import { computed, Injectable, signal } from '@angular/core';
import { UserDto } from '../model/UserDto';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class SessionStorageService {
  private readonly jwt = signal<boolean | null>(null);

  readonly isAuthenticated = computed(() => {
    const jwt = this.jwt();
    return jwt != null && jwt;
  });

  constructor(private router: Router) {
    const authorization = window.sessionStorage.getItem('Authorization');
    if (authorization) {
      try {
        const parsed = JSON.parse(authorization);
        if (parsed) {
          this.jwt.set(parsed);
        }
      } catch (e) {
        console.warn('Error authentication user:');
      }
    }
  }

  login() {
    this.jwt.set(true);
  }

  logout() {
    this.jwt.set(false);
    window.sessionStorage.setItem('Authorization', '');
    this.router.navigate(['/login']);
  }

  getJwt(): boolean | null {
    return this.jwt();
  }

  getUsernameJwt(): string {
    let jwt = window.sessionStorage.getItem('Authorization');
    if (jwt) {
      const payload = this.decodeJwtPayload(jwt);
      return payload?.username;
    }
    return '';
  }

  getAuthoritiesJwt(): string[] {
    let jwt = window.sessionStorage.getItem('Authorization');
    if (jwt) {
      const payload = this.decodeJwtPayload(jwt);
      const authoritiesString = payload?.authorities;
      return authoritiesString?.split(',') || [];
    }
    return [];
  }

  decodeJwtPayload(token: string): any {
    if (!token) return null;

    const payload = token.split('.')[1];
    const decoded = atob(payload);
    return JSON.parse(decoded);
  }
}
