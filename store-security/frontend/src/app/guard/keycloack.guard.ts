import { inject, Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';
import { UserDto } from '../model/UserDto';
import { SessionStorageService } from '../service/session-storage.service';


@Injectable({
  providedIn: 'root',
})
export class KeycloackGuard extends KeycloakAuthGuard
{
    user = new UserDto();
    public userProfile:KeycloakProfile | null=null;
    sessionStorageService = inject(SessionStorageService);

    constructor(protected override readonly router:Router, protected readonly keycloack:KeycloakService)
    {
        super(router,keycloack);
    }

    public override async isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean | UrlTree> {
    if (!this.authenticated) {
        await this.keycloack.login({
            redirectUri: window.location.origin + state.url,
        });
    } else {
        this.userProfile = await this.keycloack.loadUserProfile();
        this.user.username = this.userProfile.email || "";
        this.sessionStorageService.login(this.user);
    }
    
    const requiredRoles = route.data["roles"];
    if (!(requiredRoles instanceof Array) || requiredRoles.length === 0) {
        return true;
    }
    
    return requiredRoles.some((role) => this.roles.includes(role));
}

}