import { APP_INITIALIZER, ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { HttpClientModule, HttpClientXsrfModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { KeycloakService } from 'keycloak-angular';

function intializeKeycloack(keycloack:KeycloakService)
{
  return ()=>
    keycloack.init({
      config:{
        url:'http://localhost:8180',
        realm:'store-security',
        clientId:'store-security'
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
  providers: [provideRouter(routes),provideHttpClient(), provideAnimationsAsync(),
    importProvidersFrom(
      HttpClientModule,
      HttpClientXsrfModule.withOptions(
        {
          cookieName:'XSRF-TOKEN',
          headerName:'X-XSRF-TOKEN'
        }
      )
    ),
    KeycloakService,
    {
      provide:APP_INITIALIZER,
      useFactory:intializeKeycloack,
      multi:true,
      deps:[KeycloakService]
    }
  ]
};
