import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { SessionStorageService } from '../../service/session-storage.service';
import { AuthenticationService } from '../../service/authentication.service';

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [],
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.scss',
})
export class LogoutComponent {
  router = inject(Router);
  sessionsStorageAuth = inject(SessionStorageService);
  authService = inject(AuthenticationService);

  constructor() {
    window.sessionStorage.setItem('Authorization', '');
    window.sessionStorage.clear();
    this.sessionsStorageAuth.logout();
  }
}
