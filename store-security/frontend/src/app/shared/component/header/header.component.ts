import { Component, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SessionStorageService } from '../../../service/session-storage.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  sessionStorageAuth = inject(SessionStorageService);

  authenticated = this.sessionStorageAuth.isAuthenticated;

}
