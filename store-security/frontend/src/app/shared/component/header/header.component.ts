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
export class HeaderComponent implements OnInit
 {

  sessionStorageAuth = inject(SessionStorageService);


  authenticated = this.sessionStorageAuth.isAuthenticated;

  user:UserDto = new UserDto();
  public userProfile: KeycloakProfile | null = null;

  constructor(private readonly keycloak:KeycloakService)
  {
  }

  public async ngOnInit() {
    if(await this.keycloak.isLoggedIn())
    {
      this.userProfile = await this.keycloak.loadUserProfile();
      this.user.username = this.userProfile.email || "";
      this.sessionStorageAuth.login(this.user);
      window.sessionStorage.setItem("userdetails",JSON.stringify(this.user));
    }
  }

 login(): void {
  this.keycloak.login();
}

logout()
{
  this.sessionStorageAuth.logout();
}



}
