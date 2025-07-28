import { Component, HostListener, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './shared/component/header/header.component';
import { FooterComponent } from './shared/component/footer/footer.component';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { SessionStorageService } from './service/session-storage.service';



@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,HeaderComponent,FooterComponent,KeycloakAngularModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
    keycloak = inject(KeycloakService);
   sessionStorageService = inject(SessionStorageService);
  title = 'frontend';

   @HostListener('window:beforeunload', ['$event'])
  unloadNotification(event: any) {
    navigator.sendBeacon('/logout'); 
  }

  async ngOnInit() {
  const loggedIn = await this.keycloak.isLoggedIn();
  if (loggedIn) {
    const profile = await this.keycloak.loadUserProfile();
    const token = await this.keycloak.getToken();

    sessionStorage.setItem('username', profile.email || '');
    sessionStorage.setItem('authorities', JSON.stringify(this.keycloak.getUserRoles()));

    this.sessionStorageService.login(token!);
  } else {
    this.sessionStorageService.logout();
  }
}



}
