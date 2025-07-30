import { Component, inject, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SessionStorageService } from '../../../service/session-storage.service';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { UserDto } from '../../../model/UserDto';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule,KeycloakAngularModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {

  sessionStorageAuth = inject(SessionStorageService);

  keycloakService = inject(KeycloakService);

  user:UserDto = new UserDto();
  userProfile: KeycloakProfile |null = null;

  loggedIn:boolean = false;

  authenticated = this.sessionStorageAuth.isAuthenticated;

  constructor(){console.log("Ok");
  }

  async ngOnInit() {
  this.loggedIn = await this.keycloakService.isLoggedIn();

  if(this.loggedIn)
  {
    this.user = JSON.parse(window.sessionStorage.getItem('userdetails') || '{}');
    this.sessionStorageAuth.login(this.user);  
  }
}

 async login() {
    try {
      const keycloak = await this.keycloakService.getKeycloakInstance();
      keycloak.login(); // ✅ corretto per v15
    } catch (err) {
      console.error('Login failed:', err);
    }
  }
  

  logout()
  {
    this.sessionStorageAuth.logout();
    this.keycloakService.logout("http://localhost:4200/");
  }

}
