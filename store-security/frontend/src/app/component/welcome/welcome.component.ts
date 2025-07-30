import { Component, effect, inject, OnInit } from '@angular/core';
import { AuthenticationService } from '../../service/authentication.service';
import { SessionStorageService } from '../../service/session-storage.service';
import { UserDto } from '../../model/UserDto';
import { HttpClientModule } from '@angular/common/http';
import { StockService } from '../../service/stock.service';
import { StockDto } from '../../model/StockDto';
import { ROLE } from '../../constant/role.constants';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../service/user.service';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.scss',
})
export class WelcomeComponent implements OnInit {
  username: UserDto = new UserDto();
  sessionStorageAuth = inject(SessionStorageService);
  userService = inject(UserService);

  constructor(private router: Router) {
    effect(() => {
      if (this.userService.reloadUsers()) {
        this.userService.setRealoadUser(false);
        router.navigate(['/logout']);
      }
    });
  }

  ngOnInit(): void {
    if (this.sessionStorageAuth.isAuthenticated()) {
      let jwt = window.sessionStorage.getItem('Authorization');
      this.username.username = this.sessionStorageAuth.getUsernameJwt();
      this.username.authoritiesList = this.sessionStorageAuth.getAuthoritiesJwt();
    }
  }

  isAdmin(): boolean {
    return this.username.authoritiesList.includes(ROLE.ADMIN);
  }

  isUser(): boolean {
    return this.username.authoritiesList.includes(ROLE.USER);
  }

  isTrack(): boolean {
    return this.username.authoritiesList.includes(ROLE.TRACK);
  }


}
