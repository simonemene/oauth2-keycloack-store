import { Component, HostListener, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './shared/component/header/header.component';
import { FooterComponent } from './shared/component/footer/footer.component';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { SessionStorageService } from './service/session-storage.service';
import { UserDto } from './model/UserDto';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HeaderComponent,
    FooterComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'frontend';


  @HostListener('window:beforeunload', ['$event'])
  unloadNotification(event: any) {
    navigator.sendBeacon('/logout');
  }
}
