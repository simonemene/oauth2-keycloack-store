import { Component, inject, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SessionStorageService } from '../../../service/session-storage.service';
import { KeycloakService } from 'keycloak-angular';
import { UserDto } from '../../../model/UserDto';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {

  sessionStorageAuth = inject(SessionStorageService);

  keycloackService = inject(KeycloakService);

  user:UserDto = new UserDto();
  userProfile: KeycloakProfile |null = null;

  loggedIn:boolean = false;

  authenticated = this.sessionStorageAuth.isAuthenticated;

  async ngOnInit() {
  this.loggedIn = await this.keycloackService.isLoggedIn();

  if(this.loggedIn)
  {
    const token = await this.keycloackService.getToken();
    this.sessionStorageAuth.login(token || '');

    this.userProfile = await this.keycloackService.loadUserProfile();
    this.user.username = this.userProfile?.email || "";
    window.sessionStorage.setItem('userdetails', JSON.stringify(this.user));
  }
}


  login()
  {
    this.keycloackService.login();
  }

  logout()
  {
    this.sessionStorageAuth.logout();
    this.keycloackService.logout("http://localhost:4200/login");
  }

}
