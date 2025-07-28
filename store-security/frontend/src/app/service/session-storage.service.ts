import { computed, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class SessionStorageService {
  private readonly authSignal = signal<boolean>(false);

  readonly isAuthenticated = computed(() => this.authSignal());

  constructor(private router: Router) {
    const token = sessionStorage.getItem('Authorization');
    if (token && token.length > 0) {
      this.authSignal.set(true);
    }
  }

  login(token: string) {
    sessionStorage.setItem('Authorization', token);
    this.authSignal.set(true);
  }

  logout() {
    sessionStorage.clear();
    this.authSignal.set(false);
    this.router.navigate(['/login']);
  }

  getUsernameJwt(): string {
    const jwt = sessionStorage.getItem('Authorization');
    if (jwt) {
      const payload = this.decodeJwtPayload(jwt);
      return payload?.username || '';
    }
    return '';
  }

  getAuthoritiesJwt(): string[] {
    const jwt = sessionStorage.getItem('Authorization');
    if (jwt) {
      const payload = this.decodeJwtPayload(jwt);
      return (payload?.authorities || '').split(',');
    }
    return [];
  }

  private decodeJwtPayload(token: string): any {
    try {
      const payload = token.split('.')[1];
      const decoded = atob(payload);
      return JSON.parse(decoded);
    } catch (e) {
      console.error('JWT decoding error', e);
      return null;
    }
  }
}
