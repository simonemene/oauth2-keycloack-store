import { inject, Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { KeycloakAngularModule, KeycloakAuthGuard, KeycloakService } from "keycloak-angular";
import { SessionStorageService } from "../service/session-storage.service";
import { initZone } from "zone.js/lib/zone-impl";
import { KeycloakProfile } from 'keycloak-js';
import { UserDto } from "../model/UserDto";

@Injectable({
  providedIn: 'root',
})
export class KeycloakGuard extends KeycloakAuthGuard
{
    sessionService = inject(SessionStorageService);
    public userProfile: KeycloakProfile | null = null;
    user: UserDto = new UserDto();

    constructor(protected override readonly router:Router,protected readonly keycloak:KeycloakService)
    {
       super(router,keycloak);
    }

    public override async isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean | UrlTree> {
        if(!this.authenticated)
        {
            await this.keycloak.login(
                {
                    redirectUri: window.location.origin + state.url
                }
            );
        }else
        {
            this.userProfile = await this.keycloak.loadUserProfile();
            this.user.authorization = 'AUTH';
            this.user.username = this.userProfile.email || "";
            this.sessionService.login(this.user);
        }

        const roles = route.data['roles'];
        if(!(roles instanceof Array) || roles.length === 0)
        {
            return true;
        }

        return roles.some(auth=>this.roles.includes(auth));
    }
}