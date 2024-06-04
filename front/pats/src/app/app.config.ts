import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideAnimations } from '@angular/platform-browser/animations';
// http configs
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptorInterceptor } from './core/interceptor/auth/auth-interceptor.interceptor';


import {
  provideRouter,

} from '@angular/router';

import { DropdownModule, SidebarModule } from '@coreui/angular-pro';
import { IconSetService } from '@coreui/icons-angular';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    //http providers
    provideHttpClient(withInterceptors([authInterceptorInterceptor])),


    //end http providers
    provideRouter(routes,
  
    ),
    importProvidersFrom(SidebarModule, DropdownModule),
    IconSetService,
    provideAnimations()
  ]
};
