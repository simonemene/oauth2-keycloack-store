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
export class AppComponent{
   sessionStorageService = inject(SessionStorageService);
  title = 'frontend';

  constructor()
  {
    console.log("inizio");
    
  }

   @HostListener('window:beforeunload', ['$event'])
  unloadNotification(event: any) {
    navigator.sendBeacon('/logout'); 
  }

}




