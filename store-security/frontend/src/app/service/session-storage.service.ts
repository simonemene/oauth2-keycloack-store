import { computed, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { UserDto } from '../model/UserDto';

@Injectable({
  providedIn: 'root',
})
export class SessionStorageService {
  private readonly authSignal = signal<boolean>(false);

  readonly isAuthenticated = computed(() => this.authSignal());

  constructor(private router: Router) {
    const user = sessionStorage.getItem('userdetails');
    if (user && user.length > 0) {
      this.authSignal.set(true);
    }
  }

  login(user: UserDto) {
    sessionStorage.setItem('userdetails', JSON.stringify(user));
    this.authSignal.set(true);
  }

  logout() {
    sessionStorage.clear();
    this.authSignal.set(false);
    this.router.navigate(['/login']);
  }
}
