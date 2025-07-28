import { APP_INITIALIZER, ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { HttpClientModule, HttpClientXsrfModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';

function intializeKeycloak(keycloack:KeycloakService)
{
  return ()=>
    keycloack.init({
      config:{
        url:'http://localhost:8180/',
        realm:'store-security',
        clientId:'store-security-frontend'  
      },
      initOptions:
      {
        pkceMethod:'S256',
        redirectUri:'http://localhost:4200/welcome'
      },
      loadUserProfileAtStartUp:false
    });
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    provideAnimationsAsync(),
    importProvidersFrom(KeycloakAngularModule),
    {
      provide: APP_INITIALIZER,
      useFactory: intializeKeycloak,
      multi: true,
      deps: [KeycloakService],
    },
  ],
};
